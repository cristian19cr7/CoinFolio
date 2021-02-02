package com.example.coinfolio.ui.Addcoins;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.coinfolio.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddCoinsFragment extends Fragment {

        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.fragment_add_coins, container, false);
            // Write a message to the database
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://coinfolio-87968-default-rtdb.firebaseio.com/");
            DatabaseReference myRef = database.getReference("message");

            myRef.setValue("Howdy!");
            String[] CoinList = getCoinsList();
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.dropdown_item,R.id.autoComplete_dropdown, CoinList);
            final AutoCompleteTextView textView = view.findViewById(R.id.autoCompleteTextView);

            textView.setOnItemClickListener( new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> adapterView , View view , int position ,long arg3)
                {
                    Toast.makeText(getContext(), "list clicked " + position, Toast.LENGTH_SHORT).show();
                }
            });
            textView.setAdapter(adapter);
        return view;
    }

    public String[] getCoinsList()
    {
        final String[] coins = new String[6174];
        String URL = "https://api.coingecko.com/api/v3/coins/list";
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < coins.length; i++) {
                                JSONObject temp = array.getJSONObject(i);
                                coins[i] = temp.getString("name");
                            }
                            Toast.makeText(getContext(),"done getting the list", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"error getting the coin lists",Toast.LENGTH_SHORT).show();

            }
        });
        queue.add(stringRequest);
        return coins;
    }

}