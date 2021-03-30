package com.example.coinfolio.ui.Addcoins;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coinfolio.AddingCoinsComplete;
import com.example.coinfolio.Coin;
import com.example.coinfolio.PortfolioAsset;
import com.example.coinfolio.R;
import com.example.coinfolio.User;
import com.example.coinfolio.transaction;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddAssetInfo extends AppCompatActivity {
    User mUser = User.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_asset_info);
        final Coin asset = (Coin) getIntent().getSerializableExtra("asset");
        TextView textView = findViewById(R.id.AssetAmountTV);
        final EditText assetAmountInput = findViewById(R.id.AssetAmountInput);
        final EditText assetInvestInput = findViewById(R.id.AssetInvestmentInput);
        ImageView assetLogo = findViewById(R.id.AssetLogo);
        final Button AssetAddBtn = findViewById(R.id.AssetAddBtn);
        textView.setText(asset.symbol.toUpperCase());
        Picasso.get().load(asset.imageURL).into(assetLogo);

        AssetAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transaction newTransaction = new transaction();
                if(assetAmountInput.getText() != null || assetInvestInput.getText()!=null)
                {
                    newTransaction.assetAmount = Double.parseDouble(assetAmountInput.getText().toString());
                    newTransaction.investmentAmount = Double.parseDouble(assetInvestInput.getText().toString());
                    newTransaction.assetName = asset.name;
                    newTransaction.assetID = asset.coin_ID;
                    newTransaction.setBoughtPrice(newTransaction.investmentAmount/newTransaction.assetAmount);
                    FirebaseDatabase database = FirebaseDatabase.getInstance("https://coinfolio-87968-default-rtdb.firebaseio.com/");
                    Task<Void> myRef = database.getReference().child(mUser.getUuid()).child("transaction").push().setValue(newTransaction);
                    updatePortfolio(newTransaction);
                    Intent intent = new Intent(getApplicationContext(), AddingCoinsComplete.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"enter the data", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void updatePortfolio(final transaction newTransaction) {
        FirebaseDatabase start = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = start.getReference().child(mUser.getUuid()).child("Portfolio");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

//                if(snapshot.child(newTransaction.getAssetID()).exists())
//                {
//                    PortfolioAsset portfolioAsset = snapshot.getValue(PortfolioAsset.class);
//                    portfolioAsset.updateAmount(newTransaction.getAssetAmount());
//                    myRef.child(newTransaction.getAssetID()).setValue(portfolioAsset);
//                }
//                else
//                {
//                    PortfolioAsset newAsset = new PortfolioAsset(newTransaction.getAssetName(), newTransaction.getAssetAmount());
//                    myRef.child(newTransaction.getAssetID()).setValue(newAsset);
//                }
                boolean AssetDoesExistInTheDatabase = false;
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    if(dataSnapshot.getKey().equals(newTransaction.getAssetName()))
                    {
                        PortfolioAsset portfolioAsset = dataSnapshot.getValue(PortfolioAsset.class);
                        portfolioAsset.updateAmount(newTransaction.getAssetAmount());
                        myRef.child(newTransaction.getAssetName()).setValue(portfolioAsset);
                        AssetDoesExistInTheDatabase = true;

                    }
                    else if(dataSnapshot.getKey().equals("investment"))
                    {
                        PortfolioAsset investmentAsset = snapshot.child("investment").getValue(PortfolioAsset.class);
                        investmentAsset.updateAmount(newTransaction.getInvestmentAmount());
                        myRef.child("investment").setValue(investmentAsset);
                    }
                }
                if(!AssetDoesExistInTheDatabase)
                {
                    PortfolioAsset newAsset = new PortfolioAsset(newTransaction.getAssetID(), newTransaction.getAssetAmount());
                    myRef.child(newTransaction.getAssetName()).setValue(newAsset);
                }

//                HashMap<String, Double> data1 = (HashMap<String, Double>)snapshot.getValue();
//                if(data1.containsKey(newTransaction.getAssetID()))
//                {
//                    HashMap<String, HashMap<String, Double>> data = (HashMap<String, HashMap<String, Double>>)snapshot.getValue();
//                    HashMap<String, Double> amount = data.get(newTransaction.getAssetName());
//                    HashMap<String, Double> invest = data.get("investment");
//                    Double new_amount = BigDecimal.valueOf(amount.get(newTransaction.getAssetName()) + newTransaction.getAssetAmount()).setScale(9, RoundingMode.HALF_UP).doubleValue();
//                    Double new_investment = invest.get("investment")+ newTransaction.getInvestmentAmount();
//                    data.put(newTransaction.getAssetID(), amount);
//                    data.put("investment", invest);
//                    myRef.setValue(data);
//                }
//                else
//                {
//                    Double new_investment = data1.get("investment")+ newTransaction.getInvestmentAmount();
//                    Double scaledAmount = BigDecimal.valueOf(newTransaction.assetAmount+ 0.000000001).setScale(9, RoundingMode.HALF_UP).doubleValue();
//                    data1.put(newTransaction.getAssetID(),scaledAmount);
//                    data1.put("investment", new_investment);
//                    myRef.setValue(data1);
//                }
//                //myRef.setValue(data);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        final DatabaseReference investmentRef = start.getReference().child(mUser.getUuid()).child("Portfolio").child("investment");
//        investmentRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists())
//                {
//                    PortfolioAsset investmentAsset = snapshot.getValue(PortfolioAsset.class);
//                    investmentAsset.updateAmount(newTransaction.getInvestmentAmount());
//                    myRef.setValue(investmentAsset);
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }

}
