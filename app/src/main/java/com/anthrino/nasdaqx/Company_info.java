package com.anthrino.nasdaqx;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Company_info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_info);
        ArrayList<stockQuote> stockQuotes = getIntent().getExtras().getParcelableArrayList("stockQuotes");
        Log.d("debug", "Click fetch:" + stockQuotes.size() + stockQuotes.get(0).getComp_name() + stockQuotes.get(0).getComp_yearRange());

        ImageView comp_logo;
        TextView comp_name;
        TextView comp_symbol;
        TextView comp_stockEx;
        TextView comp_pricing;
        TextView comp_epsratio;
        TextView comp_bid;
        TextView comp_open;
        TextView comp_range;
        TextView comp_markcap;

        comp_logo = (ImageView) findViewById(R.id.comp_logo);
        comp_name = (TextView) findViewById(R.id.comp_name);
        comp_symbol = (TextView) findViewById(R.id.comp_symbol);
        comp_stockEx = (TextView) findViewById(R.id.comp_stockEx);
        comp_pricing = (TextView) findViewById(R.id.comp_pricing);
        comp_epsratio = (TextView) findViewById(R.id.comp_epsratio);
        comp_bid = (TextView) findViewById(R.id.comp_bid);
        comp_open = (TextView) findViewById(R.id.comp_open);
        comp_range = (TextView) findViewById(R.id.comp_yearange);
        comp_markcap = (TextView) findViewById(R.id.comp_markcap);

        comp_logo.setImageBitmap((Bitmap) getIntent().getParcelableExtra("Logo"));
        comp_name.setText(stockQuotes.get(0).getComp_name());
        comp_symbol.setText(stockQuotes.get(0).getComp_symbol());
        comp_stockEx.setText("StockEx : " + stockQuotes.get(0).getComp_stockEx());

        comp_open.setText("Open : ");
        if (stockQuotes.get(0).getComp_open() == 1010101)
            comp_open.append("N/A");
        else
            comp_open.append((Double.toString(stockQuotes.get(0).getComp_open())));

        comp_pricing.setText("Ask : ");
        if (stockQuotes.get(0).getComp_ask() == 1010101)
            comp_pricing.append("N/A");
        else
            comp_pricing.append((Double.toString(stockQuotes.get(0).getComp_ask())));

        comp_bid.setText("Bid : ");
        if (stockQuotes.get(0).getComp_bid() == 1010101)
            comp_bid.append("N/A");
        else
            comp_bid.append((Double.toString(stockQuotes.get(0).getComp_bid())));

        comp_epsratio.setText("Earnings per Share : ");
        if (stockQuotes.get(0).getComp_epsratio() == 1010101)
            comp_pricing.append("N/A");
        else
            comp_epsratio.append((Double.toString(stockQuotes.get(0).getComp_epsratio())));

        comp_range.setText("52-week Range : " + stockQuotes.get(0).getComp_yearRange());
        comp_markcap.setText("Market Cap : " + stockQuotes.get(0).getComp_marketCap());

    }
}
