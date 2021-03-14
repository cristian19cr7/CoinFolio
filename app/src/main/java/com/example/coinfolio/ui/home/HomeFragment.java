package com.example.coinfolio.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.DragAndDropPermissions;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.coinfolio.R;
import com.example.coinfolio.SparkAdapter;
import com.example.coinfolio.User;
import com.example.coinfolio.transaction;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.robinhood.spark.SparkView;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class HomeFragment extends Fragment {
    private User currentUser = User.getInstance();
    private String uID = currentUser.getUuid();
    private double btcAmount = 0.2591234;
    private float[] btcdata;
    private List<Float> dataPrices = new ArrayList<>();
    private TickerView textView;
    private EditText editText;
    private SparkView sparkView;
    private RadioGroup group;
    private RadioButton radioButton;
    private List<transaction> portfolio = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home,container,false);
        textView = view.findViewById(R.id.text_home);
        editText = view.findViewById(R.id.coinInput);
        sparkView = view.findViewById(R.id.sparkview);
        group = view.findViewById(R.id.radioGroup);
        Button btn = view.findViewById(R.id.coinBTN);
        final RequestQueue queue = Volley.newRequestQueue(getContext());
        final String timeframeData = "1";
        final String url = "https://api.coingecko.com/api/v3/coins/";
        final String urlContinue = "/market_chart?vs_currency=usd&days=";
        textView.setCharacterLists(TickerUtils.provideNumberList());
        textView.setAnimationDuration(400);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSparkData(queue, url+editText.getText().toString().toLowerCase()+urlContinue,"1");
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

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = group.getCheckedRadioButtonId();
                radioButton = view.findViewById(selectedId);
                String temp = radioButton.getText().toString();
                if(temp.equals("week"))
                {
                    getSparkData(queue, url+editText.getText().toString().toLowerCase()+urlContinue,"7");
                    Toast.makeText(getContext(),radioButton.getText(),Toast.LENGTH_SHORT).show();
                }
                else if(temp.equals("month"))
                {
                    getSparkData(queue, url+editText.getText().toString().toLowerCase()+urlContinue,"30");
                    Toast.makeText(getContext(),radioButton.getText(),Toast.LENGTH_SHORT).show();
                }
                else if(temp.equals("day"))
                {
                    getSparkData(queue, url+editText.getText().toString().toLowerCase()+urlContinue,"1");
                    Toast.makeText(getContext(),radioButton.getText(),Toast.LENGTH_SHORT).show();
                }
            }
        });

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("/"+uID+"/transaction/");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                {
                    transaction post = new transaction(Double.parseDouble(postSnapshot.child("assetAmount").getValue().toString())
                            ,Double.parseDouble(postSnapshot.child("investmentAmount").getValue().toString())
                            ,postSnapshot.child("assetName").getValue().toString()
                            ,postSnapshot.child("assetID").getValue().toString());
                    portfolio.add(post);
                    Log.d("asset", post.getAssetName());
                }
                Log.d("done", "portfolio is done loading in");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        return view;
    }

    public void drawSpark(VolleyError error)
    {
        if(error != null)
            Toast.makeText(getContext(),"error with the volley chart data", Toast.LENGTH_SHORT).show();
        else{
            //portfolio();
            sparkView.setAdapter(new SparkAdapter(btcdata));
            sparkView.setScrubEnabled(true);
            sparkView.setScrubListener(new SparkView.OnScrubListener() {
                @Override
                public void onScrubbed(Object value) {
                    if (value == null) {
                        int lastIndex = btcdata.length-1;
                        textView.setText(String.format("$%.2f",btcdata[lastIndex]),true);
                    } else {
                        textView.setText(String.format("$%.2f",value),true);
                    }
                }
            });
            sparkView.setLineColor(getResources().getColor(R.color.colorAccent));
            sparkView.setPadding(20,20,20,0);
            sparkView.setLineWidth(6.5f);

        }

    }
    public void getSparkData(RequestQueue q, String APIurl, String timeframe)
    {
        APIurl = APIurl + timeframe;
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
    public void portfolio()
    {
        for (int i = 0; i < btcdata.length; i++) {
            btcdata[i] = (float) (btcdata[i] * btcAmount);
        }
    }

//    public void ParseTransactions(List<transaction> transactionList, HashMap<String, Double> asset_portfolio)
//    {
//        for (int i = 0; i < transactionList.size(); i++)
//        {
//            if(asset_portfolio.containsKey(transactionList.get(i).getAssetName()))
//            {
//                Double current_amount;
//                current_amount = asset_portfolio.get(transactionList.get(i).getAssetName());
//                current_amount += transactionList.get(i).getAssetAmount();
//                asset_portfolio.put(transactionList.get(i).getAssetName(), current_amount);
//            }
//            else
//            {
//                asset_portfolio.put(transactionList.get(i).getAssetName(), transactionList.get(i).getAssetAmount());
//            }
//        }
//    }
//    public HashMap<String, Double> getPortfolio()
//    {
//        HashMap<String, Double> coin_portfolio = new HashMap<>();
//        FirebaseDatabase start = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = start.getReference().child(currentUser.getUuid()).child("Portfolio");
//
//    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseDatabase start = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = start.getReference().child(currentUser.getUuid()).child("Portfolio");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue() == null)
                {
                    myRef.child("investment").setValue(0.0001);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}