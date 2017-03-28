package com.anthrino.nasdaqx;

import android.os.AsyncTask;
import android.os.Parcel;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Johns on 22-03-2017.
 */
public class yahoo_finance extends AsyncTask<String, Void, ArrayList<stockQuote>>
{
    private String query = "http://finance.yahoo.com/d/quotes.csv?s={SYMBOLS}&f={DATA}";
    private URL connURL;

    private String options = "nsxea"; // Name, Symbol, StockEx, EPS, Ask
    private String addnOptions = "nsxeabowj1"; //Name, Symbol, StockEx, EPS, Ask, Bid, Open, 52week range, market cap


    ArrayList<stockQuote> getStockQuotes(String comp_symbols, int mode) {
        String quote = query.replace("{SYMBOLS}", comp_symbols);
        String q1 = quote.replace("{DATA}", options);
        String q2 = quote.replace("{DATA}", addnOptions);
        quote = null;
        try {
            if (mode == 0)
                connURL = new URL(q1);
            else
                connURL = new URL(q2);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        ArrayList<stockQuote> stockQuotes = new ArrayList<>();

        try {
            HttpURLConnection connection = (HttpURLConnection) connURL.openConnection(); // open an httpconnection using the url
            connection.setRequestMethod("GET");
            connection.connect();
            Log.d("debug", "Connection successful");

            InputStream inputStream = connection.getInputStream(); //create a stream from the connection and assign to bufferedreader
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            //Log.d("debug", " Reader initialized");

            while ((quote = reader.readLine()) != null) {
                // Process CSV response from yahoo finance webservice
                //Log.d("debug", quote);
                stockQuote stock = new stockQuote(Parcel.obtain());
                String pattern = "(\\w*-?[^\"\\d])(,)(\\w*[^\"])";
                quote = quote.replaceAll(pattern, "$1, $3");
                quote = quote.replace("\"", "");
                quote = quote.replace(", ", " ");
//                quote = "\"" + quote.replace(",", "\",\"") + "\"";
                Log.d("debug", quote);
                String[] csvals = quote.split(",");
                if (!quote.contains("N/A")) {
                    //Log.d("debug", "Entered object parser 1");
                    stock.setComp_name(csvals[0]);
                    //Log.d("debug", "Entered object parser 2");
                    stock.setComp_symbol(csvals[1]);
                    //Log.d("debug", "Entered object parser 3");
                    stock.setComp_stockEx(csvals[2]);
                    //Log.d("debug", "Entered object parser 4");
                    stock.setComp_epsratio(Double.parseDouble(csvals[3].replace("\"", "")));
                    //Log.d("debug", "Entered object parse 5");
                    stock.setComp_ask(Double.parseDouble(csvals[4].replace("\"", "")));
                    Log.d("debug", "Completed object parser");


                    if (mode == 1) {
                        stock.setComp_bid(Double.parseDouble(csvals[5].replace("\"", "")));
                        stock.setComp_open(Double.parseDouble(csvals[6].replace("\"", "")));
                        stock.setComp_yearRange(csvals[7]);
                        stock.setComp_marketCap(csvals[8]);
                    }
                    stockQuotes.add(stock);
                    for (int i = 0; i < stockQuotes.size(); i++) {

                    }
                }
            }
        } catch (Exception e) {
            Log.d("debug", "Exception: " + e.getMessage());
        }
        return stockQuotes;
    }

    @Override
    protected ArrayList<stockQuote> doInBackground(String... strings) {
        return getStockQuotes(strings[0], Integer.parseInt(strings[1]));
    }
}
