package com.sales.taxcalculator.networkcalls;

import android.support.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sales.taxcalculator.application.Myapplication;
import com.sales.taxcalculator.callbackinterface.ServerResponse;

import org.json.JSONObject;

import java.util.Map;

public class ServerCallsProvider {
    private static final String TAG = ServerCallsProvider.class.getSimpleName();

    public static void volleyGetRequest(@NonNull String url, @NonNull final Map<String, String> headerParams, String TAG, @NonNull final ServerResponse serverResponse) {
        StringRequest getRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String body = response.toString();
                String statusCode = "200";
                serverResponse.onSuccess(statusCode, body);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String body = "";
                String statusCode = "-1";
                try {
                    if (error.networkResponse.data != null) {
                        statusCode = String.valueOf(error.networkResponse.statusCode);
                        body = new String(error.networkResponse.data, "UTF-8");
                        serverResponse.onFailed(statusCode, body);

                    } else {
                        JSONObject mObject = new JSONObject();
                        mObject.put("msg", "Network issue.Please checked your mobile data.");
                        serverResponse.onFailed(statusCode, mObject.toString());
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headerParams;
            }
        };

        int socketTimeout = 50000;// 30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        getRequest.setRetryPolicy(policy);
        Myapplication.getInstance().addToRequestQueue(getRequest, TAG);
    }

}
