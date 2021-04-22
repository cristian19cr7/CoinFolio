package com.example.coinfolio.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.DragAndDropPermissions;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.coinfolio.PortfolioAdapter;
import com.example.coinfolio.PortfolioAsset;
import com.example.coinfolio.R;
import com.example.coinfolio.SparkAdapter;
import com.example.coinfolio.User;
import com.example.coinfolio.VolleyCallback;
import com.example.coinfolio.VolleySingleton;
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
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.android.volley.VolleyLog.TAG;

public class HomeFragment extends Fragment implements PortfolioAdapter.ViewHolder.OnClick{
    private User currentUser = User.getInstance();
    private String uID = currentUser.getUuid();
    private float[] portfolioSparkdata;
    private TickerView textView;
    private EditText editText;
    private SparkView sparkView;
    private RadioGroup group;
    private RadioButton radioButton;
    private RecyclerView portfolioRV;
    private List<PortfolioAsset> coin_portfolio = new ArrayList<>();
    private PortfolioAsset investment_item;
    private PortfolioAdapter portfolioAdapter= new PortfolioAdapter(coin_portfolio, this, getContext());
    private List<List<Float>> coin_data = new ArrayList<>();
    RequestQueue queue;
    ProgressBar progressBar;
    TextView totalInvestmentTV, totalProfitsTV;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home,container,false);
        totalInvestmentTV = view.findViewById(R.id.textView2);
        totalProfitsTV = view.findViewById(R.id.profitsTV);
        textView = view.findViewById(R.id.text_home);
        sparkView = view.findViewById(R.id.sparkview);
        group = view.findViewById(R.id.radioGroup);
        progressBar = view.findViewById(R.id.progressBarHome);
        progressBar.setVisibility(View.INVISIBLE);
        queue = VolleySingleton.getInstance(getContext()).getRequestQueue();
        final String timeframeData = "1";
        final String url = "https://api.coingecko.com/api/v3/coins/";
        final String urlContinue = "/market_chart?vs_currency=usd&days=";
        getPortfolio();
        setPortfolioRV(view);
        textView.setCharacterLists(TickerUtils.provideNumberList());
        textView.setAnimationDuration(400);

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = group.getCheckedRadioButtonId();
                radioButton = view.findViewById(selectedId);
                String temp = radioButton.getText().toString();
                switch (temp) {
                    case "Week":
                        progressBar.setVisibility(View.VISIBLE);
                        if(coin_portfolio.size() == 0)
                        {
                            float[] emptyPortfolio = new float[100];
                            Arrays.fill(emptyPortfolio, 0.0f);
                            drawSpark(null, emptyPortfolio);
                        }
                        else
                        {
                            getSparkData(url, urlContinue, "7", new VolleyCallback() {
                                @Override
                                public void OnSuccess(int i, float[] portfolioArr) {
                                    completeAllcoinData();
                                    //drawSpark(null, portfolioArr);
                                }}, 0, null);
                        }

                        break;
                    case "Month":
                        if(coin_portfolio.size() == 0)
                        {
                            float[] emptyPortfolio = new float[100];
                            Arrays.fill(emptyPortfolio, 0.0f);
                            drawSpark(null, emptyPortfolio);
                        }
                        else
                        {
                            progressBar.setVisibility(View.VISIBLE);
                            getSparkData(url, urlContinue, "30", new VolleyCallback() {
                                @Override
                                public void OnSuccess(int i,float[] portfolioArr) {
                                    completeAllcoinData();
                                    //drawSpark(null,portfolioArr);
                                }},0,null);
                        }

                        break;
                    case "Day":
                        if(coin_portfolio.size() == 0)
                        {
                            float[] emptyPortfolio = new float[100];
                            Arrays.fill(emptyPortfolio, 0.0f);
                            drawSpark(null, emptyPortfolio);
                        }
                        else
                        {
                            progressBar.setVisibility(View.VISIBLE);
                            getSparkData(url, urlContinue, "1", new VolleyCallback() {
                                @Override
                                public void OnSuccess(int i, float[] portfolioArr) {
                                    completeAllcoinData();
                                    //drawSpark(null,portfolioArr);
                                }},0,null);
                        }
                        break;
                }
            }
        });

        return view;
    }

    public void completeAllcoinData()
    {
        int smallest = coin_data.get(0).size();
        for (int i = 0; i < coin_data.size(); i++)
        {
            if(coin_data.get(i).size() < smallest) {
                smallest = coin_data.get(i).size();
            }
        }
        float[] finalCoinData = new float[smallest];
        Arrays.fill(finalCoinData, 0.0f);
        for (int i = 0; i < coin_data.size(); i++) {
            if (coin_data.get(i).size() == smallest)
            {
                for (int j = 0; j < coin_data.get(i).size(); j++) {
                    finalCoinData[j] += coin_data.get(i).get(j);
                }
            }
            else
            {
                int diff = coin_data.get(i).size()- smallest;
                for (int j = diff; j < coin_data.get(i).size(); j++) {
                    finalCoinData[j-diff] += coin_data.get(i).get(j);
                }
            }

        }

        coin_data.clear();
        drawSpark(null, finalCoinData);

    }

    public void setPortfolioRV(View v)
    {
        portfolioRV = v.findViewById(R.id.homeRV);
        portfolioRV.setAdapter(portfolioAdapter);
        portfolioRV.setLayoutManager(new LinearLayoutManager(getContext()));
        portfolioAdapter.notifyDataSetChanged();

    }

    public void drawSpark(VolleyError error, final float[] sparkArr)
    {
        if(error != null || sparkArr == null)
            Toast.makeText(getContext(),"error with the volley chart data", Toast.LENGTH_SHORT).show();
        else{
            //portfolio();
            sparkView.setAdapter(new SparkAdapter(sparkArr));
            sparkView.setScrubEnabled(true);
            sparkView.setScrubListener(new SparkView.OnScrubListener() {
                @Override
                public void onScrubbed(Object value) {
                    if (value == null) {
                        int lastIndex = sparkArr.length-1;
                        textView.setText(String.format("$%.2f",sparkArr[lastIndex]),true);
                    } else {
                        textView.setText(String.format("$%.2f",value),true);
                    }
                }
            });
            sparkView.setLineColor(getResources().getColor(R.color.colorAccent));
            sparkView.setPadding(20,20,20,0);
            sparkView.setLineWidth(6.5f);
            textView.setText(String.format("$%.2f",sparkArr[sparkArr.length-1]),true);
            progressBar.setVisibility(View.INVISIBLE);
        }

    }
    public void getSparkData(final String URLfirst, final String URLend, final String timeframe, final VolleyCallback callback, int index, final float[] dataArr)
    {


        if(index >= coin_portfolio.size()) {
            callback.OnSuccess(index, dataArr);
            return;
        }
        final PortfolioAsset current = coin_portfolio.get(index);
        String APIurl1 = URLfirst + coin_portfolio.get(index).getAssetID() + URLend + timeframe;
        final int next = index+1;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, APIurl1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("prices");
                            float []data;
                            List<Float> coindata = new ArrayList<>();
                            if(dataArr == null)
                            {
                               data = new float[array.length()];
                                for (int j = 0; j < array.length(); j++) {
                                    JSONArray arraytemp = array.getJSONArray(j);
                                    data[j] = (float) arraytemp.getDouble(1)* current.getAmountofAsset().floatValue();
                                    coindata.add((float) arraytemp.getDouble(1)* current.getAmountofAsset().floatValue());

                                }
                                Log.d(TAG, "onResponse: null "+ array.length());
                            }
                            else
                            {
                                if(dataArr.length > array.length())
                                {
                                    for (int j = 0; j < array.length(); j++) {
                                        JSONArray arraytemp = array.getJSONArray(j);
                                        dataArr[j] = dataArr[j] + (float) arraytemp.getDouble(1)* current.getAmountofAsset().floatValue();
                                        coindata.add((float) arraytemp.getDouble(1)* current.getAmountofAsset().floatValue());
                                    }
                                }
                                else
                                {
                                    for (int j = 0; j < dataArr.length; j++) {
                                        JSONArray arraytemp = array.getJSONArray(j);
                                        dataArr[j] = dataArr[j] + (float) arraytemp.getDouble(1)* current.getAmountofAsset().floatValue();
                                        coindata.add((float) arraytemp.getDouble(1)* current.getAmountofAsset().floatValue());
                                    }
                                }
                                data = dataArr;
                                Log.d(TAG, "onResponse: not null "+ array.length());
                            }
                            coin_data.add(coindata);
                            getSparkData(URLfirst, URLend, timeframe, callback, next, data);
                            //textView.setText(String.format("$%.2f",btcdata[array.length()-1]));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            textView.setText("error");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyError er = error;
                textView.setText("That didn't work!");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    public void getPortfolio()
    {
        FirebaseDatabase start = FirebaseDatabase.getInstance();
        DatabaseReference myRef = start.getReference().child(currentUser.getUuid()).child("Portfolio");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    PortfolioAsset Asset = postSnapshot.getValue(PortfolioAsset.class);
                    if(Asset.getNameofAseet().equals("investment"))
                    {
                        investment_item = Asset;
                        totalInvestmentTV.setText(String.format("$%.2f", investment_item.getAmountofAsset()));

                    }
                    else if(Asset.getNameofAseet().equals("profit"))
                    {
                        totalProfitsTV.setText(String.format("$%.2f", Asset.getAmountofAsset()));
                    }
                    else
                    {
                        coin_portfolio.add(Asset);
                    }

                }
                portfolioAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

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
                    PortfolioAsset defaultInvestment = new PortfolioAsset("investment","investment_id",0.001, 0.001);
                    PortfolioAsset defaultProfits = new PortfolioAsset("profit", "profit_id", 0.0001, 0.001);
                    myRef.child("investment").setValue(defaultInvestment);
                    myRef.child("profit").setValue(defaultProfits);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void AssetClicked(int position) {
        Toast.makeText(getContext(),coin_portfolio.get(position).getNameofAseet(), Toast.LENGTH_SHORT).show();


    }

}