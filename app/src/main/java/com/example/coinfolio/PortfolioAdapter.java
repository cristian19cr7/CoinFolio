package com.example.coinfolio;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PortfolioAdapter extends RecyclerView.Adapter<PortfolioAdapter.ViewHolder>{
    private List<PortfolioAsset> localDataSet;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView portfolioName, portfolioAmount;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            portfolioName = (TextView) view.findViewById(R.id.portfolio_asset_name);
            portfolioAmount = view.findViewById(R.id.portfolio_asset_amount);
        }

        public TextView getPortfolioName() {
            return portfolioName;
        }
        public TextView getPortfolioAmount() {
            return portfolioAmount;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public PortfolioAdapter(List<PortfolioAsset> dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.portfolio_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getPortfolioName().setText(localDataSet.get(position).getNameofAseet());
        viewHolder.getPortfolioAmount().setText(String.format("%f", localDataSet.get(position).getAmountofAsset()));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
