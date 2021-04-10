package com.example.coinfolio;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class PortfolioAdapter extends RecyclerView.Adapter<PortfolioAdapter.ViewHolder>{
    private List<PortfolioAsset> localDataSet;
    private ViewHolder.OnClick ClickListener;
    private Context mContext;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView portfolioName, portfolioAmount, portfolioValue, portfolioPercentage;
        private OnClick clickListener;
        private Context context;
        public ViewHolder(View view, OnClick listener, Context c) {
            super(view);
            // Define click listener for the ViewHolder's View

            portfolioName = (TextView) view.findViewById(R.id.portfolio_asset_name);
            portfolioAmount = view.findViewById(R.id.portfolio_asset_amount);
            portfolioValue = view.findViewById(R.id.portfolio_asset_value);
            portfolioPercentage = view.findViewById(R.id.portfolio_asset_value_percentage);
            this.clickListener = listener;
            context = c;
            view.setOnClickListener(this);
        }

        public TextView getPortfolioName() {
            return portfolioName;
        }
        public TextView getPortfolioAmount() {
            return portfolioAmount;
        }
        public TextView getPortfolioValue() {   return portfolioValue; }
        public TextView getPortfolioPercentage() {  return portfolioPercentage; }

        @Override
        public void onClick(View v)
        {
            clickListener.AssetClicked(getAdapterPosition());
        }

        public interface OnClick
        {
            void AssetClicked(int position);
        }
    }


    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public PortfolioAdapter(List<PortfolioAsset> dataSet, ViewHolder.OnClick clickListener, Context context) {
        localDataSet = dataSet;
        ClickListener = clickListener;
        mContext = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.portfolio_item, viewGroup, false);

        return new ViewHolder(view, ClickListener, mContext);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        final String id = localDataSet.get(position).getAssetID();
        viewHolder.getPortfolioName().setText(localDataSet.get(position).getNameofAseet());
        viewHolder.getPortfolioAmount().setText(String.format("%f", localDataSet.get(position).getAmountofAsset()));
        String URL = "https://api.coingecko.com/api/v3/simple/price?ids="+id+"&vs_currencies=usd&include_24hr_change=true";
        RequestQueue queue = VolleySingleton.getInstance(viewHolder.context).getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONObject jsonObject = new JSONObject(response).getJSONObject(id);
                            Double price = jsonObject.getDouble("usd") * localDataSet.get(position).getAmountofAsset();
                            viewHolder.getPortfolioValue().setText(String.format("$%.2f", price));
                            Double percentage = (price/localDataSet.get(position).getInvestmentonThisAsset()) - 1.0;
                            if(percentage > 0)
                            {
                                viewHolder.getPortfolioPercentage().setText(String.format("%+.2f", percentage*100)+ "%");
                                viewHolder.getPortfolioPercentage().setTextColor(Color.GREEN);
                            }
                            else
                            {
                                viewHolder.getPortfolioPercentage().setText(String.format("%.2f", percentage*100)+ "%");
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mContext,"Error getting the coin lists",Toast.LENGTH_SHORT).show();

            }
        });
        queue.add(stringRequest);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

}
