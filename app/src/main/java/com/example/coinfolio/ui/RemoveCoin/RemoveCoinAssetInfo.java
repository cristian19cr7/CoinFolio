package com.example.coinfolio.ui.RemoveCoin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coinfolio.AddingCoinsComplete;
import com.example.coinfolio.Coin;
import com.example.coinfolio.PortfolioAsset;
import com.example.coinfolio.R;
import com.example.coinfolio.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RemoveCoinAssetInfo extends AppCompatActivity {
    private EditText InputAmountSold, InputSellPrice;
    private TextView AssetNameTV;
    private Button removeBtn;
    private User user = User.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_coin_asset_info);
        final PortfolioAsset asset = (PortfolioAsset) getIntent().getSerializableExtra("portfolioAsset");
        AssetNameTV = findViewById(R.id.remove_asset_details);
        InputAmountSold = findViewById(R.id.amount_remove_input);
        InputSellPrice = findViewById(R.id.amount_sold_price_input);
        removeBtn = findViewById(R.id.remove_btn);
        AssetNameTV.setText(String.format("%s\nBalance: %.9f", asset.getNameofAseet(), asset.getAmountofAsset()));

        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFromPortfolio(asset.getNameofAseet(), Double.parseDouble(InputAmountSold.getText().toString()));
                Intent intent = new Intent(getApplicationContext(), AddingCoinsComplete.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public void removeFromPortfolio(final String assetName, final Double amount)
    {
        FirebaseDatabase start = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = start.getReference().child(user.getUuid()).child("Portfolio");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    if(dataSnapshot.getKey().equals(assetName))
                    {
                        PortfolioAsset portfolioAsset = dataSnapshot.getValue(PortfolioAsset.class);
                        portfolioAsset.removeAmount(amount);
                        myRef.child(assetName).setValue(portfolioAsset);

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
