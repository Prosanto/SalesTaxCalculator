package com.sales.taxcalculator.adaper;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sales.taxcalculator.R;
import com.sales.taxcalculator.model.CountryList;

import java.util.ArrayList;

public class SpinnerCountryAdapter extends ArrayAdapter<CountryList> {

    Context context;
    int resource, textViewResourceId;
    ArrayList<CountryList> allSupplyCategory = new ArrayList<>();

    public SpinnerCountryAdapter(Context context, int resource,
                                 int textViewResourceId, ArrayList<CountryList> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.allSupplyCategory.clear();
        this.allSupplyCategory.addAll(items);

    }

    public CountryList getSupplyCategory(int pos) {
        return allSupplyCategory.get(pos);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.spinner_drop_down, parent, false);
        }
        CountryList people = allSupplyCategory.get(position);
        if (people != null) {
            TextView lblName = (TextView) view.findViewById(R.id.lbl_name);
            if (lblName != null)
                lblName.setText(people.getName());
            lblName.setTextColor(Color.parseColor("#000000"));
        }

        return view;

        //return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.spinner_drop_down, parent, false);
        }
        CountryList people = allSupplyCategory.get(position);
        if (people != null) {
            TextView lblName = (TextView) view.findViewById(R.id.lbl_name);
            if (lblName != null)
                lblName.setText(people.getName());
            lblName.setTextColor(Color.parseColor("#000000"));
        }
        return view;
        // return getCustomView(position, convertView, parent);
    }
}