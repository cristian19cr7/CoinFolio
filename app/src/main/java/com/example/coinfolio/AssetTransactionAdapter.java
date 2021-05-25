package com.example.coinfolio;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AssetTransactionAdapter extends RecyclerView.Adapter<AssetTransactionAdapter.ViewHolder> {
    private List<transaction> localDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView assetName, assetAmount, assetPrice, assetInvestment;

        public ViewHolder(View view) {
            super(view);

            assetName = view.findViewById(R.id.asset_transaction_nameTV);
            assetAmount = view.findViewById(R.id.asset_transaction_amountTV);
            assetPrice = view.findViewById(R.id.asset_transaction_boughtPriceTV);
            assetInvestment = view.findViewById(R.id.asset_transaction_investmentTV);
        }

        public TextView getassetName() {
            return assetName;
        }

        public TextView getAssetAmount() {
            return assetAmount;
        }

        public TextView getAssetPrice() {
            return assetPrice;
        }

        public TextView getAssetInvestment() {
            return assetInvestment;
        }
    }

    public AssetTransactionAdapter(List<transaction> dataSet) {
        localDataSet = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.asset_transaction_card, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        viewHolder.getassetName().setText(String.format("in %s", localDataSet.get(position).getAssetName()));
        viewHolder.getAssetAmount().setText(String.format("+ %f", localDataSet.get(position).getAssetAmount()));
        viewHolder.getAssetInvestment().setText(String.format("$%.2f", localDataSet.get(position).getInvestmentAmount()));
        if(localDataSet.get(position).getBoughtPrice() < 0.001)
            viewHolder.getAssetPrice().setText(String.format("~$%.7f", localDataSet.get(position).getBoughtPrice()));
        else
            viewHolder.getAssetPrice().setText(String.format("~$%.2f", localDataSet.get(position).getBoughtPrice()));

    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
