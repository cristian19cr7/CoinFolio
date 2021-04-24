package com.example.coinfolio.ui.Explore;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.coinfolio.SparkAdapter;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.TextView;
import android.widget.Toast;

import com.example.coinfolio.R;
import com.example.coinfolio.VolleySingleton;
import com.robinhood.spark.SparkView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;

public class coin_market_data_chart extends AppCompatActivity {

    private TextView test;
    private SparkView sparkView;
    private float[] coinSparkData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_market_data_chart);
        final String asset = getIntent().getStringExtra("coin_id");
        test = findViewById(R.id.textView4);
        test.setText(asset);
        getSparklineData(asset);

    }

    public void setSparkView()
    {
        sparkView = findViewById(R.id.charts_spark);
        sparkView.setAdapter(new SparkAdapter(coinSparkData));
        final int current_price = coinSparkData.length-1;
        sparkView.setScrubEnabled(true);
        sparkView.setScrubListener(new SparkView.OnScrubListener() {
            @Override
            public void onScrubbed(Object value) {
                if(value != null)
                {
                    double price_scrub = Double.parseDouble(value.toString());
                    if(price_scrub > 2.01)
                    {
                        test.setText(String.format("$%.2f", price_scrub));
                    }
                    else
                    {
                        test.setText(String.format("$%.5f", price_scrub));
                    }
                }
                else
                {
                    test.setText(String.format("$%.2f", coinSparkData[current_price]));
                }


            }
        });
    }

    public void getSparklineData(String coinID)
    {
        RequestQueue queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        String url ="https://api.coingecko.com/api/v3/coins/" + coinID + "/market_chart?vs_currency=usd&days=1";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("prices");
                            float []data;

                            data = new float[array.length()];
                            for (int j = 0; j < array.length(); j++) {
                                JSONArray arraytemp = array.getJSONArray(j);
                                data[j] = (float) arraytemp.getDouble(1);
                            }
                            coinSparkData = data;
                            setSparkView();

                        }catch (Exception e)
                        {
                            Toast.makeText(getApplicationContext(),"error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"volley error", Toast.LENGTH_SHORT).show();
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

}
