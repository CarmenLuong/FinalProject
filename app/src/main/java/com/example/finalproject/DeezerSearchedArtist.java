package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DeezerSearchedArtist extends AppCompatActivity {

    URL artistUrl;
    URL searchedUrl;
    String tracklist;
    String searchedString;
    Bitmap albumCoverImage;
    String albumCoverUrl;
    String songDuration;
    String songTitle;
    String albumName;
    String iconName;
    Intent fromDeezerActivity;
    ListView deezerList;
    ArrayList<DeezerArtistClass> savedSongList = new ArrayList<>();
    ListAdapter myListAdapter;

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

        deezerList = findViewById(R.id.deezerArrayList);

        fromDeezerActivity = getIntent();

//        Bundle extra = fromDeezerActivity.getBundleExtra("extra");

        String savedUrl = fromDeezerActivity.getStringExtra("Url");
        searchedString = fromDeezerActivity.getStringExtra("searchedString");
//        savedSongList = (ArrayList<DeezerArtistClass>) extra.getSerializable("artistArrayList");

        DeezerSearchedArtist.DeezerQuery query = new DeezerSearchedArtist.DeezerQuery();
        query.execute("https://api.deezer.com/search/artist/?q=" + searchedString + "&output=xml");

        Log.e("DeezerSearchedArtist", "i started the class");

        for (DeezerArtistClass item: savedSongList){
            Log.i("message", "Title is: " + item.getTitle());
        }

        myListAdapter = new ListAdapter();
        deezerList.setAdapter(myListAdapter);



//        SecondQuery query = new SecondQuery();
//        query.execute(savedUrl);


    }

    /**
     * DeezerQuery is intended to search the api and collect information releveant to the users search
     */

    private class DeezerQuery extends AsyncTask<String, Integer, String>{

        @Override
        protected String doInBackground(String... args) {
            try {

                searchedUrl = new URL(args [0]);

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
                            DeezerSearchedArtist.DeezerQuery artistQuery = new DeezerSearchedArtist.DeezerQuery();

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
//                                JSONObject artistInfo = new JSONObject(result);
//


                                Log.e("DeezerActivity", "opened "+ tracklist +" url");
                                Log.e("DeezerActivity", "result string is " + result);

                                JSONObject artistInfo = new JSONObject(result);
                                JSONArray artistData = artistInfo.getJSONArray("data");

                                for(int i = 0; i < artistData.length(); i++) {
                                    JSONObject jsonObject = artistData.getJSONObject(i);


                                    songTitle = jsonObject.getString("title");
//                                    songDuration = String.valueOf((String) artistInfo.getString("duration"));
//                                    albumName = String.valueOf((String) artistInfo.getString("album"));
//                                    albumCoverUrl = String.valueOf((String) artistInfo.getString("cover"));
                                    savedSongList.add(new DeezerArtistClass(songTitle));
                                    Log.i("DeezerActivity", "The song titles are: " + songTitle) ;

                                }

//                                for (int i = 0 ; i < artistData.length() ; i++) {
//                                    Log.e("DeezerActivity", "Made it in the first loop");
//                                    JSONObject jsonObject = artistInfo.getJSONObject(i);
//                                    JSONArray artistData = jsonObject.getJSONArray("data");
//                                    for (int l = 0; l < artistData.length(); l++) {
//                                        Log.e("DeezerActivity", "Made it in the second loop");
//                                        jsonObject = artistData.getJSONObject(l);
//                                        songTitle = jsonObject.getString("title");
//                                        Log.i("DeezerActivity", "The song titles are: " + songTitle) ;
//                                    }
//                                }

                                Log.i("DeezerActivity", "The array of titles is" + savedSongList.toString());
                                // Log.i("DeezerActivity", "The variables are: " + songTitle + " " + songDuration + " " + songTitle + " " + albumCoverUrl + " " + tracklist) ;

                            } catch (Exception e){
                                e.printStackTrace();
                                Log.e("DeezerActivity", "fallen into exception in DeezerSearchedArtist AsyncTask");
                            }
//                            String imageFileName = albumCoverUrl;
//                            try{
//                                if(!fileExistance(imageFileName)) {
//                                    albumCoverImage = null;
//
//                                    URL url = new URL(albumCoverUrl);
//
//                                    urlConnection = (HttpURLConnection) url.openConnection();
//                                    urlConnection.connect();
//                                    int responseCode = urlConnection.getResponseCode();
//                                    if (responseCode == 200) {
//                                        albumCoverImage = BitmapFactory.decodeStream(urlConnection.getInputStream());
//                                        publishProgress(100);
//                                    }
//                                    FileOutputStream outputStream = openFileOutput(imageFileName, Context.MODE_PRIVATE);
//                                    albumCoverImage.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
//                                    outputStream.flush();
//                                    outputStream.close();
//                                    Log.i("DeezerActivity", "Image downloaded");
//                                } else{
//                                    FileInputStream fis = null;
//                                    try {    fis = openFileInput(imageFileName);   }
//                                    catch (FileNotFoundException e) {    e.printStackTrace();  }
//                                    albumCoverImage = BitmapFactory.decodeStream(fis);
//                                    Log.i("DeezerActivity", "Image used from " + iconName + ".png, already downloaded");
//
//                                }
//
//                            } catch(Exception e){
//
//                            }
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
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
//            deezerLoadingBar.setVisibility(View.VISIBLE);
//            deezerLoadingBar.setProgress(values[0]);
        }


        @Override
        protected void onPostExecute(String s) {
            myListAdapter.notifyDataSetChanged();
        }
    }

    private class ListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return savedSongList.size();
        }

        @Override
        public DeezerArtistClass getItem(int position) {
            return savedSongList.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return getItem(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = null;
            DeezerArtistClass tempMessage = getItem(position);
            LayoutInflater inflater = getLayoutInflater();

            row = inflater.inflate(R.layout.deezer_text_list_load, parent, false);
            TextView setDeezerList = row.findViewById(R.id.sampleSongText);
            setDeezerList.setText(tempMessage.getTitle());
            return row;

        }
    }
}