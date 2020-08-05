package com.example.finalproject;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CityDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CityDetailFragment extends Fragment {

    private static final String ARG_ID = "id";
    private static final String ARG_COUNTRY = "country";
    private static final String ARG_REGION = "region";
    private static final String ARG_CITY = "city";
    private static final String ARG_CURRENCY = "currency";
    private static final String ARG_LATITUDE = "latitude";
    private static final String ARG_LONGITUDE = "longitude";

    private long mId;
    private String mCountry;
    private String mRegion;
    private String mCity;
    private String mCurrency;
    private String mLatitude;
    private String mLongitude;
    private TextView mCountryText, mRegionText, mCityText, mCurrencyText, mLatitudeText, mLongitudeText;
    private Button mGoogleMapButton, mFavouriteButton;


    public CityDetailFragment() {
    }

    public static CityDetailFragment newInstance(long id, String country, String region, String city, String currency, String latitude, String longitude) {
        CityDetailFragment fragment = new CityDetailFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ID, id);
        args.putString(ARG_COUNTRY, country);
        args.putString(ARG_REGION, region);
        args.putString(ARG_CITY, city);
        args.putString(ARG_CURRENCY, currency);
        args.putString(ARG_LATITUDE, latitude);
        args.putString(ARG_LONGITUDE, longitude);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mId = getArguments().getLong(ARG_ID);
            mCountry = getArguments().getString(ARG_COUNTRY);
            mRegion = getArguments().getString(ARG_REGION);
            mCity = getArguments().getString(ARG_CITY);
            mCurrency = getArguments().getString(ARG_CURRENCY);
            mLatitude = getArguments().getString(ARG_LATITUDE);
            mLongitude = getArguments().getString(ARG_LONGITUDE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_city_detail, container, false);
        mCountryText = view.findViewById(R.id.CountryText);
        mRegionText = view.findViewById(R.id.RegionText);
        mCityText = view.findViewById(R.id.CityText);
        mCurrencyText = view.findViewById(R.id.CurrencyText);
        mLatitudeText = view.findViewById(R.id.LatitudeText);
        mLongitudeText = view.findViewById(R.id.LongitudeText);

        mCountryText.setText(getString(R.string.country_text_variable, mCountry));
        mRegionText.setText(getString(R.string.region_text_variable, mRegion));
        mCityText.setText(getString(R.string.city_text_variable, mCity));
        mCurrencyText.setText(getString(R.string.currency_text_variable, mCurrency));
        mLatitudeText.setText(getString(R.string.latitude_text_variable, mLatitude));
        mLongitudeText.setText(getString(R.string.longitude_text_variable, mLongitude));

        mGoogleMapButton = view.findViewById(R.id.GoogleMapButton);
        mGoogleMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://maps.google.com?q=" + mLatitude + "," + mLongitude));
                startActivity(browserIntent);
            }
        });

        CityDbHelper dbHelper = new CityDbHelper(getContext());
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        mFavouriteButton = view.findViewById(R.id.FavouriteButton);
        if (mId != -1) {
            mFavouriteButton.setText(R.string.remove_from_favourite);
        } else {
            mFavouriteButton.setText(R.string.add_to_favourite);
        }
        mFavouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mId != -1) {
                    db.execSQL("DELETE FROM " + CityDbHelper.TABLE_NAME +  " WHERE " + CityDbHelper.COLUMN_ID + "=?",
                            new String[] {String.valueOf(mId)});
                    mId = -1;
                    Snackbar.make(v, getString(R.string.removed_variable_from_favourites, mCity), Snackbar.LENGTH_LONG).show();
                    mFavouriteButton.setText(R.string.add_to_favourite);
                } else {
                    ContentValues values = new ContentValues();
                    values.put(CityDbHelper.COLUMN_COUNTRY, mCountry);
                    values.put(CityDbHelper.COLUMN_REGION, mRegion);
                    values.put(CityDbHelper.COLUMN_CITY, mCity);
                    values.put(CityDbHelper.COLUMN_CURRENCY, mCurrency);
                    values.put(CityDbHelper.COLUMN_LATITUDE, mLatitude);
                    values.put(CityDbHelper.COLUMN_LONGITUDE, mLongitude);
                    mId = db.insert(CityDbHelper.TABLE_NAME, null, values);
                    Snackbar.make(v, getString(R.string.added_variable_to_favourites, mCity), Snackbar.LENGTH_LONG).show();
                    mFavouriteButton.setText(R.string.remove_from_favourite);
                }
            }
        });

        return view;
    }
}