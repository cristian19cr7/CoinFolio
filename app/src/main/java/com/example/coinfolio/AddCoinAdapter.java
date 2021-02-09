package com.example.coinfolio;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AddCoinAdapter extends RecyclerView.Adapter<AddCoinAdapter.ViewHolder> {
    private List<Coin> localDataSet;
    //private List<Coin> localDataSetSearch;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView, coin_rank;
        private final ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            textView = (TextView) view.findViewById(R.id.item_tv);
            imageView = view.findViewById(R.id.coinImg);
            coin_rank = view.findViewById(R.id.item_rank_tv);
        }

        public TextView getTextView() {
            return textView;
        }
        public TextView getCoin_rank()
        {
            return coin_rank;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public AddCoinAdapter(List<Coin> dataSet) {
        localDataSet = dataSet;
        //localDataSetSearch = new ArrayList<>(dataSet);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.add_coin_rv_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getCoin_rank().setText(String.valueOf(localDataSet.get(position).market_cap_rank));
        viewHolder.getTextView().setText(localDataSet.get(position).name);
        Picasso.get().load(localDataSet.get(position).imageURL).into(viewHolder.imageView);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
