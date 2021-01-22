package com.example.coinfolio.ui.Addcoins;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.coinfolio.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddCoinsFragment extends Fragment {

        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.fragment_add_coins, container, false);
            // Write a message to the database
            FirebaseDatabase database = FirebaseDatabase.getInstance(getString(R.string.Firebase_Key));
            DatabaseReference myRef = database.getReference("message");

            myRef.setValue("l;ksadjhfg;lasdkjf!");
        return view;
    }
}