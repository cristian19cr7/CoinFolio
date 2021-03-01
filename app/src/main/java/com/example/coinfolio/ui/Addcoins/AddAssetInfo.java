package com.example.coinfolio.ui.Addcoins;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coinfolio.AddingCoinsComplete;
import com.example.coinfolio.Coin;
import com.example.coinfolio.R;
import com.example.coinfolio.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class AddAssetInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_asset_info);
        final Coin asset = (Coin) getIntent().getSerializableExtra("asset");
        final User mUser = User.getInstance();
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
                    Task<Void> myRef = database.getReference().child(mUser.getUuid()).child("transaction").child(asset.name).push().setValue(newTransaction);
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

}
