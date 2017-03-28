package com.anthrino.nasdaqx;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anthrino.nasdaqx.dummy.DummyContent.DummyItem;
import com.anthrino.nasdaqx.stockFragment.OnListFragmentInteractionListener;

import java.util.ArrayList;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MystockRecyclerViewAdapter extends RecyclerView.Adapter<MystockRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<stockQuote> stockQuotes;
    private final OnListFragmentInteractionListener mListener;

    public MystockRecyclerViewAdapter(ArrayList<stockQuote> items, OnListFragmentInteractionListener listener) {
        this.stockQuotes = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_stock, parent, false);
//        Log.d("debug", "onCreateViewHolder: ");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.comp_name.setText(stockQuotes.get(position).getComp_name());
        holder.comp_symbol.setText(stockQuotes.get(position).getComp_symbol());
        holder.comp_stockEx.setText("StockEx : ");
        holder.comp_stockEx.append(stockQuotes.get(position).getComp_stockEx());
        holder.comp_pricing.setText("Stock Pricing : ");
        holder.comp_pricing.append((Double.toString(stockQuotes.get(position).getComp_ask())));
        holder.comp_epsratio.setText("Earnings per Share : ");
        holder.comp_epsratio.append((Double.toString(stockQuotes.get(position).getComp_epsratio())));

        holder.stockView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.stockView);
                }
            }
        });
//        Log.d("debug", "onBindViewHolder: "+position);
    }

    @Override
    public int getItemCount() {
        return stockQuotes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View stockView;
        public final TextView comp_name;
        public final TextView comp_symbol;
        public final TextView comp_stockEx;
        public final TextView comp_pricing;
        public final TextView comp_epsratio;

        public ViewHolder(View view) {
            super(view);
            stockView = view;
            comp_name = (TextView) view.findViewById(R.id.comp_name);
            comp_symbol = (TextView) view.findViewById(R.id.comp_symbol);
            comp_stockEx = (TextView) view.findViewById(R.id.comp_stockEx);
            comp_pricing = (TextView) view.findViewById(R.id.comp_pricing);
            comp_epsratio = (TextView) view.findViewById(R.id.comp_epsratio);
//            Log.d("debug", "ViewHolder: "+comp_name.getText()+comp_symbol.getText());

        }

    }
}
