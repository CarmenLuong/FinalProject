package com.example.finalproject;

import android.app.DownloadManager;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class GeoDataFragment extends Fragment {

    /**
     * Key for the latitude
     */
    private static final String LATITUDE_KEY = "latitude";
    /**
     * Key for the longitude
     */
    private static final String LONGITUDE_KEY = "longitude";
    /**
     * EditText views for longitude and latitude
     */
    private EditText mLongitudeEdit, mLatitudeEdit;
    /**
     * Button views for mSearchButton and mShowFavouriteButton
     */
    private Button mSearchButton, mShowFavouriteButton;
    /**
     * ListView view for mLocationListView
     */
    private ListView mLocationListView;
    /**
     * ProgressBar view for mProgressBar
     */
    private ProgressBar mProgressBar;
    /**
     * CityListAdapter adapter used to render the cities inside mLocationListView
     */
    private CityListAdapter mAdapter;
    /**
     * SQLiteDatabase used to fetch and store the favourite cities to the SQLite database
     */
    private SQLiteDatabase db;
    /**
     * SharedPreferences used to store the last latitude and longitude entered by the user
     */
    private SharedPreferences mSharedPreference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_geo_data, container, false);
        mLongitudeEdit = view.findViewById(R.id.LongitudeEdit);
        mLatitudeEdit = view.findViewById(R.id.LatitudeEdit);
        mLocationListView = view.findViewById(R.id.LocationListView);

        CityDbHelper dbHelper = new CityDbHelper(getContext());
        db = dbHelper.getWritableDatabase();

        mAdapter = new CityListAdapter();
        mLocationListView.setAdapter(mAdapter);
        mLocationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                City city = (City) mAdapter.getItem(position);
                CityDetailFragment cityDetailFragment = CityDetailFragment.newInstance(city.getId(), city.getCountry(), city.getRegion(), city.getCity(), city.getCurrency(), city.getLatitude(), city.getLongitude());
                getFragmentManager().beginTransaction().replace(R.id.Container, cityDetailFragment).addToBackStack(null).commit();
            }
        });

        mSearchButton = view.findViewById(R.id.SearchButton);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.VISIBLE);
                GeoDataQuery query = new GeoDataQuery();
                query.execute(mLatitudeEdit.getText().toString(), mLongitudeEdit.getText().toString());
            }
        });

        mShowFavouriteButton = view.findViewById(R.id.ShowFavouriteButton);
        mShowFavouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.setCityList(loadCityFromDatabase());
            }
        });

        mProgressBar = view.findViewById(R.id.ProgressBar);
        mSharedPreference = getActivity().getSharedPreferences("GEO_PREF", 0);
        mLatitudeEdit.setText(mSharedPreference.getString(LATITUDE_KEY, ""));
        mLongitudeEdit.setText(mSharedPreference.getString(LONGITUDE_KEY, ""));
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        mSharedPreference.edit().putString(LATITUDE_KEY, mLatitudeEdit.getText().toString()).apply();
        mSharedPreference.edit().putString(LONGITUDE_KEY, mLongitudeEdit.getText().toString()).apply();
    }

    /**
     * This method is used to fetch all stored cities stored in the database that were favourited by the user
     * @return the list of cities in the database
     */
    private List<City> loadCityFromDatabase() {
        List<City> cityList = new ArrayList<>();

        Cursor c = db.rawQuery("SELECT * from " + CityDbHelper.TABLE_NAME, null);
        int idIndex = c.getColumnIndex(CityDbHelper.COLUMN_ID);
        int countryIndex = c.getColumnIndex(CityDbHelper.COLUMN_COUNTRY);
        int regionIndex = c.getColumnIndex(CityDbHelper.COLUMN_REGION);
        int cityIndex = c.getColumnIndex(CityDbHelper.COLUMN_CITY);
        int currencyIndex = c.getColumnIndex(CityDbHelper.COLUMN_CURRENCY);
        int latitudeIndex = c.getColumnIndex(CityDbHelper.COLUMN_LATITUDE);
        int longitudeIndex = c.getColumnIndex(CityDbHelper.COLUMN_LONGITUDE);
        c.moveToFirst();
        for(int i = 0; i< c.getCount(); i++) {
            long id = c.getLong(idIndex);
            String country = c.getString(countryIndex);
            String region = c.getString(regionIndex);
            String city = c.getString(cityIndex);
            String currency = c.getString(currencyIndex);
            String latitude = c.getString(latitudeIndex);
            String longitude = c.getString(longitudeIndex);
            City cityObject = new City(id, country, region, city, currency, latitude, longitude);
            cityList.add(cityObject);
            c.moveToNext();
        }
        c.close();
        return cityList;
    }

    /**
     * AsyncTask used to fetch the cities nearby the latitude and longitude entered by the user
     */
    public class GeoDataQuery extends AsyncTask<String, Integer, String> {

        List<City> cityList = new ArrayList<>();

        @Override
        protected String doInBackground(String... strings) {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL("https://api.geodatasource.com/cities?key=YRNKVGQTXL1RCIZPOD1JJ4YQFNLTKNBE&lat=" + strings[0] + "&lng="+ strings[1] +"&format=JSON").openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                String result = sb.toString();

                JSONArray array = new JSONArray(result);
                for (int i = 0 ; i < array.length() ; i++){
                    JSONObject jsonObject = array.getJSONObject(i);
                    String country = jsonObject.getString("country");
                    String region = jsonObject.getString("region");
                    String cityName = jsonObject.getString("city");
                    String latitude = jsonObject.getString("latitude");
                    String longitude = jsonObject.getString("longitude");
                    String currencyCode = jsonObject.getString("currency_code");
                    long newRowId = -1;
                    Cursor c = db.rawQuery("SELECT " + CityDbHelper.COLUMN_ID + " FROM " + CityDbHelper.TABLE_NAME + " WHERE " + CityDbHelper.COLUMN_LATITUDE + "=? AND " + CityDbHelper.COLUMN_LONGITUDE + "=?",
                            new String[]{latitude, longitude});
                    int idIndex = c.getColumnIndex(CityDbHelper.COLUMN_ID);
                    if (c.moveToFirst()) {
                        newRowId = c.getLong(idIndex);
                    }
                    City city = new City(newRowId, country, region, cityName, currencyCode, latitude, longitude);
                    cityList.add(city);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mProgressBar.setVisibility(View.GONE);
            mAdapter.setCityList(cityList);
            Toast.makeText(getActivity(), getString(R.string.found_cities, cityList.size()),
                    Toast.LENGTH_LONG).show();
        }
    }
}