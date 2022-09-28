package com.example.coinfolio.ui.Explore;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.coinfolio.AddCoinAdapter;
import com.example.coinfolio.Coin;
import com.example.coinfolio.R;
import com.example.coinfolio.VolleyCallback;
import com.example.coinfolio.ui.Addcoins.AddAssetInfo;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static com.android.volley.VolleyLog.TAG;

enum sortingStates{
    desc, asc, NA
}
public class nav_explore extends Fragment implements AddCoinAdapter.ViewHolder.AssetSelectedListener{
    private List<Coin> list = new ArrayList<>();
    private List<Coin> orginalList = new ArrayList<>();
    private AddCoinAdapter addCoinAdapter = new AddCoinAdapter(list, this);
    private Button marketCapSortBtn, coinSortBtn, percentChangeSortBtn;
    private sortingStates marketCapSortingState = sortingStates.NA;
    private sortingStates coinSortState = sortingStates.NA;
    private sortingStates percentChangeSortState = sortingStates.NA;
    RecyclerView recyclerView;
    //private ProgressBar progressBar;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.nav_explore_fragment, container, false);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        marketCapSortBtn = view.findViewById(R.id.market_cap_sortBTN);
        coinSortBtn = view.findViewById(R.id.coin_name_sortBTN);
        percentChangeSortBtn = view.findViewById(R.id.price_percent_changeSortBTN);
        setHasOptionsMenu(true);
        queue.add(getListOfCoins("1"));
        queue.add(getListOfCoins("2"));
        Collections.sort(list, Coin.RankSortAscComparator);
        RV(view);

        marketCapSortBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                marketCapSortingState = sortBtnUpdate(marketCapSortBtn, marketCapSortingState);
                if(marketCapSortingState == sortingStates.asc)
                {
                    Collections.sort(list, Coin.RankSortAscComparator);
                }
                else
                {
                    Collections.sort(list, Coin.RankSortDescComparator);
                }
                addCoinAdapter.notifyDataSetChanged();
                //reset visuals for the other sorting buttons
                coinSortBtn.setText("Coin ");
                percentChangeSortBtn.setText("24h ");

            }
        });

        coinSortBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                coinSortState = sortBtnUpdate(coinSortBtn, coinSortState);
                if(coinSortState == sortingStates.asc)
                {
                    Collections.sort(list, Coin.CoinNameSortAscComparator);
                }
                else
                {
                    Collections.sort(list, Coin.CoinNameSortDescComparator);
                }
                addCoinAdapter.notifyDataSetChanged();
                //reset visuals for the other sorting buttons
                marketCapSortBtn.setText("Rank ");
                percentChangeSortBtn.setText("24h ");
            }
        });

        percentChangeSortBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                percentChangeSortState = sortBtnUpdate(percentChangeSortBtn, percentChangeSortState);
                if(percentChangeSortState == sortingStates.asc)
                {
                    Collections.sort(list, Coin.percentSortAscComparator);
                }
                else
                {
                    Collections.sort(list, Coin.percentSortDescComparator);
                }
                addCoinAdapter.notifyDataSetChanged();

                //reset visuals for the other sorting buttons
                marketCapSortBtn.setText("Rank ");
                coinSortBtn.setText("Coin ");
            }
        });


        return view;
    }

    public sortingStates sortBtnUpdate(Button currentBtn, sortingStates state)
    {
        String name;
        if(state != sortingStates.NA)
            name = currentBtn.getText().subSequence(0, currentBtn.getText().toString().length()-1).toString();
        else
            name = currentBtn.getText().toString();
        if(state == sortingStates.NA)
        {
            currentBtn.setText(name + "▲");
            return sortingStates.desc;
        }

        else if(state == sortingStates.desc)
        {
            currentBtn.setText(name + "▼");
            return sortingStates.asc;
        }
        else
        {
            currentBtn.setText(name +"▲");
            return sortingStates.desc;
        }
    }

    public void RV(View v){
        recyclerView = v.findViewById(R.id.explore_rv);
        recyclerView.setAdapter(addCoinAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        addCoinAdapter.notifyDataSetChanged();


    }

    public StringRequest getListOfCoins(String page)
    {
        String URL = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=250&page=" + page + "&sparkline=false";
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject temp = array.getJSONObject(i);
                                Coin tempCoin = new Coin();
                                tempCoin.name = temp.getString("name");
                                tempCoin.coin_ID = temp.getString("id");
                                tempCoin.imageURL = temp.getString("image");
                                tempCoin.market_cap_rank = temp.getInt("market_cap_rank");
                                tempCoin.symbol = temp.getString("symbol");
                                tempCoin.current_price = temp.getDouble("current_price");
                                if(!temp.isNull("price_change_percentage_24h"))
                                    tempCoin.price_change_percentage_24h = temp.getDouble("price_change_percentage_24h");
                                list.add(tempCoin);
                            }
                            orginalList.addAll(list);
                            addCoinAdapter.notifyDataSetChanged();
                            //progressBar.setVisibility(View.INVISIBLE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"Error getting the coin lists",Toast.LENGTH_SHORT).show();

            }
        });
        return stringRequest;


    }



    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.searchmenu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_menu);
        SearchView searchView = (SearchView) menuItem.getActionView();
        list.clear();
        list.addAll(orginalList);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                addCoinAdapter.getFilter().filter(s);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void OnAssetSelected(int position) {
        Intent intent = new Intent(getContext(), coin_market_data_chart.class);
        intent.putExtra("coin_id", list.get(position).getCoin_ID());
        startActivity(intent);
    }
}
