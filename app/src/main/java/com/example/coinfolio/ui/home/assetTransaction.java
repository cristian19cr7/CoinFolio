package com.example.coinfolio.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.coinfolio.AssetTransactionAdapter;
import com.example.coinfolio.R;
import com.example.coinfolio.User;
import com.example.coinfolio.transaction;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class assetTransaction extends AppCompatActivity {
    private List<transaction> asset_transaction_list = new ArrayList<>();
    private RecyclerView recyclerView;
    private User currentUser = User.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_transaction);
        final String asset = getIntent().getStringExtra("assetName");
        getTransaction(asset);
    }

    public void createRV()
    {
        AssetTransactionAdapter adapter = new AssetTransactionAdapter(asset_transaction_list);
        recyclerView = findViewById(R.id.transactionRV);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter.notifyDataSetChanged();
    }

    public void getTransaction(final String asset_name)
    {
        FirebaseDatabase start = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = start.getReference().child(currentUser.getUuid()).child("transaction");

        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(!task.isSuccessful())
                {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else
                {
                    for (DataSnapshot postSnapshot : task.getResult().getChildren())
                    {
                        transaction tempTrans = postSnapshot.getValue(transaction.class);
                        if(tempTrans.getAssetName().equals(asset_name))
                        {
                            asset_transaction_list.add(tempTrans);
                        }
                    }
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    createRV();
                }
            }
        });
    }
}
