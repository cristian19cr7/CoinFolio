package com.example.coinfolio.ui.RemoveCoin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coinfolio.Coin;
import com.example.coinfolio.PortfolioAdapter;
import com.example.coinfolio.PortfolioAsset;
import com.example.coinfolio.R;
import com.example.coinfolio.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RemoveCoinFragment extends Fragment {

    private User currentUser = User.getInstance();
    private List<PortfolioAsset> portfolio = new ArrayList<>();
    private RecyclerView removeRV;
    private PortfolioAdapter portfolioAdapter;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_remove_coins,container,false);
        portfolioAdapter = new PortfolioAdapter(portfolio);

        getPortfolio();
        getRV(view);
        return  view;
    }

    public void getRV(View v)
    {
        removeRV = v.findViewById(R.id.removeRV);
        removeRV.setAdapter(portfolioAdapter);
        removeRV.setLayoutManager(new LinearLayoutManager(getContext()));
        portfolioAdapter.notifyDataSetChanged();
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
                    portfolio.add(Asset);
                }
                portfolioAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}