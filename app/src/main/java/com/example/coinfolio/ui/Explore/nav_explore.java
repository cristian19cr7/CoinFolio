package com.example.coinfolio.ui.Explore;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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
import com.example.coinfolio.ui.Addcoins.AddAssetInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class nav_explore extends Fragment implements AddCoinAdapter.ViewHolder.AssetSelectedListener{
    private List<Coin> list = new ArrayList<>();
    private AddCoinAdapter addCoinAdapter = new AddCoinAdapter(list, this);
    //private ProgressBar progressBar;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.nav_explore_fragment, container, false);
        setHasOptionsMenu(true);
        getListOfCoins(1,2);
        RV(view);

        return view;
    }

    public void RV(View v){
        RecyclerView recyclerView = v.findViewById(R.id.explore_rv);
        recyclerView.setAdapter(addCoinAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        addCoinAdapter.notifyDataSetChanged();
    }

    public void getListOfCoins(final int curr_page, final int end_page)
    {
        if(curr_page > end_page)
        {
            return;
        }
        else
        {
            String URL = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=250&page="+curr_page+"&sparkline=false";
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
                                    tempCoin.price_change_percentage_24h = temp.getDouble("price_change_percentage_24h");
                                    list.add(tempCoin);
                                }
                                addCoinAdapter.notifyDataSetChanged();
                                //progressBar.setVisibility(View.INVISIBLE);
                                int next = curr_page+1;
                                getListOfCoins(next, end_page);
                                Toast.makeText(getContext(),"Loading Currencies",Toast.LENGTH_SHORT).show();
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
            queue.add(stringRequest);
        }

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.searchmenu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_menu);
        SearchView searchView = (SearchView) menuItem.getActionView();

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
        intent.putExtra("coin_id", list.get(position).coin_ID);
        startActivity(intent);
    }
}
