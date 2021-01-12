package com.example.coinfolio.ui.home;

import android.app.DownloadManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class HomeFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        final TextView textView = view.findViewById(R.id.text_home);
        final ImageView imageView = view.findViewById(R.id.coinImg);
        final EditText editText = view.findViewById(R.id.coinInput);
        Button btn = view.findViewById(R.id.coinBTN);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final RequestQueue queue = Volley.newRequestQueue(getContext());
                final String URL = "https://api.coingecko.com/api/v3/coins/" + editText.getText();
                StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String price = jsonObject.getJSONObject("market_data").getJSONObject("current_price").getString("usd");
                                    textView.setText("$" + price);
                                    String imageURL = jsonObject.getJSONObject("image").getString("large");
                                    Toast.makeText(getContext(),imageURL,Toast.LENGTH_SHORT).show();
                                    Picasso.get().load(imageURL).into(imageView);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    textView.setText("error");
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        textView.setText(error.toString());
                        Log.d("Error", error.toString());
                    }
                });

                queue.add(stringRequest);
            }
        });
        return view;
    }
}