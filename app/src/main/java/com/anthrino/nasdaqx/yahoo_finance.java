package com.anthrino.nasdaqx;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Parcel;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

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
public class yahoo_finance extends AsyncTask<String, String, ArrayList<stockQuote>> {
    private String query = "http://finance.yahoo.com/d/quotes.csv?s={SYMBOLS}&f={DATA}";
    private URL connURL;
    private Context context;
    private String options = "nsxea"; // Name, Symbol, StockEx, EPS, Ask
    private String addnOptions = "nsxeabowj1"; //Name, Symbol, StockEx, EPS, Ask, Bid, Open, 52week range, market cap
    private ProgressBar progressBar;
    private StockDBAdapter DbAdapter;
    private View fragContainer;

    public yahoo_finance(ProgressBar progressBar, StockDBAdapter DbAdapter, View fragContainer, Context context) {
        this.progressBar = progressBar;
        this.DbAdapter = DbAdapter;
        this.fragContainer = fragContainer;
        this.context = context;
    }

    private ArrayList<stockQuote> getStockQuotes(String comp_symbols, int mode, String search) {
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
            connection.disconnect();
            //Log.d("debug", " Reader initialized");
            while ((quote = reader.readLine()) != null) {
                // Process CSV response from yahoo finance webservice
                //Log.d("debug", quote);
                stockQuote stock = new stockQuote(Parcel.obtain());
                String pattern = "(\\w*-?[^\"\\d])(,)(\\w*[^\"])";
                quote = quote.replaceAll(pattern, "$1, $3");
                quote = quote.replace("\"", "");
                if (quote.contains("N/A, "))
                    quote = quote.replace("N/A, ", "N/A,");
                quote = quote.replace(", ", " ");
//                quote = "\"" + quote.replace(",", "\",\"") + "\"";
                Log.d("debug", quote);
                String[] csvals = quote.split(",");
                publishProgress(quote);
                if (csvals.length >= 5 && !csvals[0].contains("N/A")) {
                    //Log.d("debug", "Entered object parser 1");
                    stock.setComp_name(csvals[0]);
                    //Log.d("debug", "Entered object parser 2");
                    stock.setComp_symbol(csvals[1]);
                    //Log.d("debug", "Entered object parser 3");
                    stock.setComp_stockEx(csvals[2]);
                    //Log.d("debug", "Entered object parser 4");

                    if (!csvals[3].contains("N/A"))
                        stock.setComp_epsratio(Double.parseDouble(csvals[3].replace("\"", "")));
                    else
                        stock.setComp_epsratio(1010101);
                    if (!csvals[4].contains("N/A"))
                        stock.setComp_ask(Double.parseDouble(csvals[4].replace("\"", "")));
                    else
                        stock.setComp_ask(1010101);

//                    Log.d("debug", "Completed object parser");

                    if (mode == 1) {
                        String comp_domain = stock.getComp_name().split(" ")[0].toLowerCase();
                        String res_id = "comp_logo_" + comp_domain;
                        int checkExistence = context.getResources().getIdentifier(res_id, "drawable", context.getPackageName());

                        if (checkExistence != 0) {  // the resouce exists...

                            Drawable d = ContextCompat.getDrawable(context, checkExistence);
                            stock.setComp_logo(((BitmapDrawable) d).getBitmap());
                            Log.d("debug", "resource exists");

                        } else {  // checkExistence == 0  // the resource does NOT exist
                            Log.d("debug", "resource to be fetched");
                            if (comp_domain.startsWith(search.toLowerCase()))
                                stock.setComp_logo(LoadImageFromWebOperations(comp_domain));

                        }

                        if (!csvals[5].contains("N/A"))
                            stock.setComp_bid(Double.parseDouble(csvals[5].replace("\"", "")));
                        else
                            stock.setComp_bid(1010101);
                        if (!csvals[6].contains("N/A"))
                            stock.setComp_open(Double.parseDouble(csvals[6].replace("\"", "")));
                        else
                            stock.setComp_open(1010101);
                        stock.setComp_yearRange(csvals[7]);
                        stock.setComp_marketCap(csvals[8]);
                    }
                    DbAdapter.InsertQuote(stock);
                    stockQuotes.add(stock);
                }
            }
        } catch (Exception e) {
            Log.d("debug", "Exception: " + e.getMessage());
        }
        return stockQuotes;
    }

    private Bitmap LoadImageFromWebOperations(String name) {
        String url = "https://logo.clearbit.com/" + name.toLowerCase() + ".com?size=500";
//        Log.d("debug", "logo url:"+url);

        String icon_src_name = "comp_logo_" + name;
        try {
            InputStream inputStream = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(inputStream, icon_src_name);
            Bitmap icon = ((BitmapDrawable) d).getBitmap();
            return icon;
        } catch (Exception e) {
            return null;
        }
    }
    @Override
    protected void onPreExecute() {
        fragContainer.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(String... values) {
        progressBar.incrementProgressBy(values.length);
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(ArrayList<stockQuote> stockQuotes) {
        progressBar.setVisibility(View.GONE);
        fragContainer.setVisibility(View.VISIBLE);
        super.onPostExecute(stockQuotes);
    }

    @Override
    protected ArrayList<stockQuote> doInBackground(String... strings) {
        return getStockQuotes(strings[0], Integer.parseInt(strings[1]), strings[2]);
    }
}
