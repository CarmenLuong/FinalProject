package com.example.finalproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CityListAdapter extends BaseAdapter {
    /**
     * List of cities that this adapter renders
     */
    private List<City> cityList;

    public void setCityList(List<City> cityList) {
        this.cityList = cityList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(cityList == null){
            return 0;
        }
        return cityList.size();
    }

    @Override
    public Object getItem(int position) {
        return cityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.layout_city_view, parent, false);
        }

        TextView cityText = view.findViewById(R.id.CityText);
        City city = cityList.get(position);
        cityText.setText(city.getCity());
        ImageView starImage = view.findViewById(R.id.StarImage);
        if(city.getId() != -1){
            starImage.setVisibility(View.VISIBLE);
        } else {
            starImage.setVisibility(View.INVISIBLE);
        }
        return view;
    }
}
