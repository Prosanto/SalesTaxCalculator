package com.sales.taxcalculator;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.sales.taxcalculator.adaper.SpinnerCountryAdapter;
import com.sales.taxcalculator.callbackinterface.ServerResponse;
import com.sales.taxcalculator.model.CountryList;
import com.sales.taxcalculator.model.Periods;
import com.sales.taxcalculator.networkcalls.ServerCallsProvider;
import com.sales.taxcalculator.utils.Helpers;
import com.sales.taxcalculator.utils.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HomeAcvity extends AppCompatActivity {
    private static final String TAG = HomeAcvity.class.getSimpleName();
    private Context mContext;
    private Spinner spinner;
    private ArrayList<CountryList> allCountryList = new ArrayList<>();
    private SpinnerCountryAdapter mSpinnerCountryAdapter;
    private LinearLayout itme_add_layout;
    private LinearLayout layouteffectivefrom;
    private TextView totalTax;
    private TextView totalAmount;
    private EditText EdCurrency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mContext = this;
        initUi();
    }

    private void initUi() {
        layouteffectivefrom = (LinearLayout) this.findViewById(R.id.layouteffectivefrom);
        itme_add_layout = (LinearLayout) this.findViewById(R.id.itme_add_layout);
        totalTax = (TextView) this.findViewById(R.id.totalTax);
        totalAmount = (TextView) this.findViewById(R.id.totalAmount);
        EdCurrency = (EditText) this.findViewById(R.id.EdCurrency);
        spinner = (Spinner) this.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setSpinnerDataChange(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        EdCurrency.setTag("0");
        EdCurrency.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                calculationAmount();
                // TODO Auto-generated method stub
            }
        });
        webRequest();
    }

    public void calculationAmount() {
        String currencyInput = EdCurrency.getText().toString().trim();
        if (currencyInput.length() == 0) {
            totalTax.setText("0");
            totalAmount.setText("0");
        } else {
            double currency = Double.parseDouble(currencyInput);
            double totaltax = currency * (Double.parseDouble(EdCurrency.getTag().toString()));
            totaltax = totaltax / 100;

            totalTax.setText("" + totaltax);
            totalAmount.setText("" + (totaltax + currency));


        }
    }

    public void setSpinnerDataChange(int position) {
        LayoutInflater infalInflater = (LayoutInflater) this.mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        CountryList mCountryList = mSpinnerCountryAdapter.getSupplyCategory(position);
        if (mCountryList.getArrayList().size() > 1) {
            layouteffectivefrom.removeAllViews();
            layouteffectivefrom.setVisibility(View.VISIBLE);
            View mView = infalInflater.inflate(R.layout.row_selectedrate,
                    null, false);
            RadioGroup buttonLayout = (RadioGroup) mView
                    .findViewById(R.id.item_radio_group);
            buttonLayout.setOrientation(LinearLayout.HORIZONTAL);

            for (int inn = 0; inn < mCountryList.getArrayList().size(); inn++) {

                Periods mPeriods = mCountryList.getArrayList().get(inn);
                RadioButton button = new RadioButton(this);
                RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
                        mContext, null);
                params.setMargins(0, 10, 0, 10);
                button.setLayoutParams(params);
                button.setPadding(15, 15, 0, 15);
                button.setTag("" + mPeriods.getRates());
                button.setText(mPeriods.getEffective_from());
                button.setId(inn);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        try {
                            JSONObject mObject = new JSONObject(v.getTag().toString());
                            parentPrice(mObject);

                        } catch (Exception e) {
                        }
                    }
                });
                if (inn == 0) {
                    buttonLayout.check(button.getId());
                    parentPrice(mPeriods.getRates());
                }
                buttonLayout.addView(button);


            }
            layouteffectivefrom.addView(mView);

        } else {
            layouteffectivefrom.setVisibility(View.GONE);
            itme_add_layout.removeAllViews();
            View mView = infalInflater.inflate(R.layout.row_selectedrate,
                    null, false);
            RadioGroup buttonLayout = (RadioGroup) mView
                    .findViewById(R.id.item_radio_group);
            JSONObject json = mCountryList.getArrayList().get(0).getRates();
            Iterator<String> iter = json.keys();
            int indexSelection = 0;
            while (iter.hasNext()) {
                final String key = iter.next();
                try {
                    Object value = json.get(key);
                    RadioButton button = new RadioButton(this);
                    RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
                            mContext, null);
                    params.setMargins(0, 10, 0, 10);
                    button.setLayoutParams(params);
                    button.setPadding(15, 15, 0, 15);
                    button.setTag("" + value);
                    button.setText(key);
                    button.setId(indexSelection);

                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            double value = Double.parseDouble(v.getTag().toString().trim());
                            EdCurrency.setTag(value);
                            calculationAmount();
                        }
                    });
                    if (indexSelection == 0) {
                        double value3 = Double.parseDouble(button.getTag().toString().trim());
                        EdCurrency.setTag(value3);
                        button.setChecked(true);
                        calculationAmount();
                    }
                    indexSelection++;
                    buttonLayout.addView(button);

                } catch (JSONException e) {

                }
            }

            itme_add_layout.addView(mView);


        }
    }

    public void parentPrice(JSONObject json) {
        itme_add_layout.removeAllViews();
        LayoutInflater infalInflater = (LayoutInflater) this.mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mView = infalInflater.inflate(R.layout.row_selectedrate,
                null, false);
        RadioGroup buttonLayout = (RadioGroup) mView
                .findViewById(R.id.item_radio_group);

        Iterator<String> iter = json.keys();
        int indexSelection = 0;

        while (iter.hasNext()) {

            String key = iter.next();
            try {
                Object value = json.get(key);
                RadioButton button = new RadioButton(this);
                RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
                        mContext, null);
                params.setMargins(0, 10, 0, 10);
                button.setLayoutParams(params);
                button.setPadding(15, 15, 0, 15);
                button.setTag("" + value);
                button.setText(key);
                button.setId(indexSelection);

                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        double value = Double.parseDouble(v.getTag().toString().trim());
                        EdCurrency.setTag(value);
                        calculationAmount();

                    }
                });
                if (indexSelection == 0) {
                    button.setChecked(true);
                    double value3 = Double.parseDouble(button.getTag().toString().trim());
                    EdCurrency.setTag(value3);
                    calculationAmount();

                }
                indexSelection++;
                buttonLayout.addView(button);

            } catch (JSONException e) {

            }
        }


        itme_add_layout.addView(mView);
    }

    private void webRequest() {
        if (!Helpers.isNetworkAvailable(mContext)) {
            Helpers.showOkayDialog(mContext, R.string.no_internet_connection);
            return;
        }
        allCountryList.clear();
        final String url = "https://jsonvat.com/";
        Map<String, String> headerParams = new HashMap<String, String>();
        ServerCallsProvider.volleyGetRequest(url, headerParams, TAG, new ServerResponse() {
            @Override
            public void onSuccess(String statusCode, String responseServer) {
                try {
                    Logger.debugLog("responseServer", responseServer);
                    JSONObject mObject = new JSONObject(responseServer);
                    if (mObject.has("rates")) {
                        JSONArray jsonArrayrates = mObject.getJSONArray("rates");
                        for (int index = 0; index < jsonArrayrates.length(); index++) {
                            JSONObject mObjectRate = jsonArrayrates.getJSONObject(index);
                            CountryList mCountryList = new CountryList();
                            mCountryList.setCode(mObjectRate.getString("code"));
                            mCountryList.setName(mObjectRate.getString("name"));
                            mCountryList.setCountry_code(mObjectRate.getString("country_code"));
                            ArrayList<Periods> arrayList = new ArrayList<>();

                            JSONArray periods = mObjectRate.getJSONArray("periods");
                            for (int indexperiods = 0; indexperiods < periods.length(); indexperiods++) {
                                Periods mPeriods = new Periods();
                                JSONObject mObjectperiods = periods.getJSONObject(indexperiods);
                                mPeriods.setEffective_from(mObjectperiods.getString("effective_from"));
                                mPeriods.setRates(mObjectperiods.getJSONObject("rates"));
                                arrayList.add(mPeriods);
                            }
                            mCountryList.setArrayList(arrayList);
                            allCountryList.add(mCountryList);

                        }

                        mSpinnerCountryAdapter = new SpinnerCountryAdapter(
                                mContext, R.layout.spinner_drop_down, R.id.lbl_name, allCountryList);
                        spinner.setAdapter(mSpinnerCountryAdapter);
                        mSpinnerCountryAdapter.notifyDataSetChanged();

                    }

                } catch (Exception e) {
                    Logger.debugLog("Exception", e.getMessage());
                }
            }

            @Override
            public void onFailed(String statusCode, String serverResponse) {
                Logger.debugLog("onFailed serverResponse", serverResponse);
            }
        });
    }
}
