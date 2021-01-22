package com.example.coinfolio.ui.home;

import android.app.DownloadManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.coinfolio.R;
import com.example.coinfolio.SparkAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.robinhood.spark.SparkView;
import com.robinhood.spark.animation.LineSparkAnimator;
import com.robinhood.spark.animation.MorphSparkAnimator;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class HomeFragment extends Fragment {

private float[] btcdata;
private TextView textView;
private ImageView imageView;
private EditText editText;
private SparkView sparkView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        textView = view.findViewById(R.id.text_home);
        imageView = view.findViewById(R.id.coinImg);
        editText = view.findViewById(R.id.coinInput);
        sparkView = view.findViewById(R.id.sparkview);
        Button btn = view.findViewById(R.id.coinBTN);
        final RequestQueue queue = Volley.newRequestQueue(getContext());
        final String url = "https://api.coingecko.com/api/v3/coins/";
        final String urlContinue = "/market_chart?vs_currency=usd&days=1";

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSparkData(queue, url+editText.getText().toString().toLowerCase()+urlContinue);
//                final RequestQueue queue = Volley.newRequestQueue(getContext());
//                final String URL = "https://api.coingecko.com/api/v3/coins/" + editText.getText().toString().toLowerCase();
//                StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
//                        new Response.Listener<String>() {
//                            @Override
//                            public void onResponse(String response) {
//                                // Display the first 500 characters of the response string.
//                                try {
//                                    JSONObject jsonObject = new JSONObject(response);
//                                    String price = jsonObject.getJSONObject("market_data").getJSONObject("current_price").getString("usd");
//                                    textView.setText("$" + price);
//                                    String imageURL = jsonObject.getJSONObject("image").getString("large");
//                                    Toast.makeText(getContext(),imageURL,Toast.LENGTH_SHORT).show();
//                                    Picasso.get().load(imageURL).into(imageView);
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                    textView.setText("error");
//                                }
//                            }
//                        }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        textView.setText(error.toString());
//                        Log.d("Error", error.toString());
//                    }
//                });
//
//                queue.add(stringRequest);
            }
        });

        //getSparkData(queue,url);

        return view;
    }

    public void drawSpark(VolleyError error)
    {
        if(error != null)
            Toast.makeText(getContext(),"error with the volley chart data", Toast.LENGTH_SHORT).show();
        else{
            sparkView.setAdapter(new SparkAdapter(btcdata));
            sparkView.setScrubEnabled(true);
            sparkView.setScrubListener(new SparkView.OnScrubListener() {
                @Override
                public void onScrubbed(Object value) {
                    if (value == null) {
                        int lastIndex = btcdata.length-1;
                        textView.setText(String.format("$%.2f",btcdata[lastIndex]));
                    } else {
                        textView.setText("$" + value.toString());
                    }
                }
            });
            sparkView.setLineColor(getResources().getColor(R.color.colorAccent));
            sparkView.setPadding(20,20,20,0);
            sparkView.setLineWidth(6.5f);

        }

    }
    public void getSparkData(RequestQueue q, String APIurl)
    {
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, APIurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("prices");
                            btcdata = new float[array.length()];
                            for (int i = 0; i < array.length(); i++) {
                                JSONArray arraytemp = array.getJSONArray(i);
                                btcdata[i] = (float) arraytemp.getDouble(1);
                            }

                            textView.setText(String.format("$%.2f",btcdata[array.length()-1]));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            textView.setText("error");
                        }
                        drawSpark(null);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textView.setText("That didn't work!");
            }
        });

        // Add the request to the RequestQueue.
        q.add(stringRequest);
    }
}