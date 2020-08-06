package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DeezerSearchedArtist extends AppCompatActivity {

    URL artistUrl;
    URL searchedUrl;
    String tracklist;
    Bitmap albumCoverImage;
    String albumCoverUrl;
    String songDuration;
    String songTitle;
    String albumName;
    String iconName;
    Intent fromDeezerActivity;

    /**
     * file existance is intended to confirm a file exists before redownloading it
     *
     */
    public boolean fileExistance(String fname){
        File file = getBaseContext().getFileStreamPath(fname);
        return file.exists();   }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deezer_searched_artist);

        fromDeezerActivity = getIntent();
        String savedUrl = fromDeezerActivity.getStringExtra("Url");

        SecondQuery query = new SecondQuery();
        query.execute(savedUrl);


    }

    /**
     * secondQuery is intended to use the url and title passed from the DeezerActivity class to collect the remaining information to present the user
     */

    private class SecondQuery extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... args) {
            try {

                URL searchedUrl = new URL(args [0]);

                HttpURLConnection urlConnection = (HttpURLConnection) searchedUrl.openConnection();

                InputStream response = urlConnection.getInputStream();

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput( response  , "UTF-8");


                int eventType = xpp.getEventType(); //The parser is currently at START_DOCUMENT

                while(eventType != XmlPullParser.END_DOCUMENT && tracklist == null)
                {

                    if(eventType == XmlPullParser.START_TAG)
                    {
                        if(xpp.getName().equals("tracklist")) {
                            xpp.next();
                            tracklist = xpp.getText();
                            SecondQuery artistQuery = new SecondQuery();

                            try {
                                artistUrl = new URL(tracklist);

                                urlConnection = (HttpURLConnection) artistUrl.openConnection();

                                //wait for data:
                                response = urlConnection.getInputStream();

                                //JSON reading:   Look at slide 26
                                //Build the entire string response:
                                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                                StringBuilder sb = new StringBuilder();

                                String line = null;
                                while ((line = reader.readLine()) != null)
                                {
                                    sb.append(line + "\n");
                                }
                                String result = sb.toString(); //result is the whole string


                                // convert string to JSON: Look at slide 27:
                                JSONObject artistInfo = new JSONObject(result);

                                JSONArray artistData = artistInfo.getJSONArray("data");


                                for(int i = 0; i < artistData.length(); i++) {

                                    if(artistData.getJSONObject(i).getString("title").equals(fromDeezerActivity.getStringExtra("title"))) {

                                        songTitle = artistData.getJSONObject(i).getString("title");
                                        songDuration = artistData.getJSONObject(i).getString("duration");
                                        albumName = artistData.getJSONObject(i).getJSONObject("album").getString("title");
                                        albumCoverUrl = artistData.getJSONObject(i).getJSONObject("album").getString("cover");
                                    }
                                }
                                Log.i("MainActivity", "The variables are: " + songTitle + " " + songDuration + " " + songTitle + " " + albumCoverUrl) ;

                            } catch (Exception e){

                            }
                            String imageFileName = albumCoverUrl;
                            try{
                                if(!fileExistance(imageFileName)) {
                                    albumCoverImage = null;

                                    URL url = new URL(albumCoverUrl);

                                    urlConnection = (HttpURLConnection) url.openConnection();
                                    urlConnection.connect();
                                    int responseCode = urlConnection.getResponseCode();
                                    if (responseCode == 200) {
                                        albumCoverImage = BitmapFactory.decodeStream(urlConnection.getInputStream());
                                        publishProgress(100);
                                    }
                                    FileOutputStream outputStream = openFileOutput(imageFileName, Context.MODE_PRIVATE);
                                    albumCoverImage.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                                    outputStream.flush();
                                    outputStream.close();
                                    Log.i("DeezerActivity", "Image downloaded");
                                } else{
                                    FileInputStream fis = null;
                                    try {    fis = openFileInput(imageFileName);   }
                                    catch (FileNotFoundException e) {    e.printStackTrace();  }
                                    albumCoverImage = BitmapFactory.decodeStream(fis);
                                    Log.i("DeezerActivity", "Image used from " + iconName + ".png, already downloaded");

                                }

                            } catch(Exception e){

                            }
                        }
                    }
                    eventType = xpp.next(); //move to the next xml event and store it in a variable
                }

                Log.i("DeezerActivity", "Current Variables: " + tracklist);
            }

            catch (Exception e)
            {

            }
            return "done";

        }



        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            TextView songDurationView = findViewById(R.id.durationTextField);
            TextView songTitleView = findViewById(R.id.artistTitleText);
            TextView albumNameView = findViewById(R.id.albumNameTextField);
            ImageView albumCoverView = findViewById(R.id.albumArtField);

            songTitleView.setText(songTitle);
            songDurationView.setText(songDuration);
            albumNameView.setText(albumName);
            albumCoverView.setImageBitmap(albumCoverImage);
        }
    }
}