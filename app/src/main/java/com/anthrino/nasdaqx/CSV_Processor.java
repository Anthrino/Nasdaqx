package com.anthrino.nasdaqx;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CSV_Processor extends AsyncTask<String, String, String> {
    Context context;
    private ProgressBar progressBar;
    private StockDBAdapter DbAdapter;
    private View fragContainer;

    public CSV_Processor(Context context, StockDBAdapter DbAdapter, ProgressBar progressBar, View fragContainer) {
        this.context = context;
        this.progressBar = progressBar;
        this.DbAdapter = DbAdapter;
        this.fragContainer = fragContainer;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        progressBar.setProgress(values.length);
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPreExecute() {
        fragContainer.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        progressBar.setVisibility(View.GONE);
        fragContainer.setVisibility(View.VISIBLE);
        super.onPostExecute(s);
    }

    private String file_search_read(CharSequence key, InputStream inputStream) {
        ArrayList<String> resultList = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream)); // buffered reader for CSV file inputstream
        String csvLine;
        try {
            while ((csvLine = bufferedReader.readLine()) != null) {

//                publishProgress(csvLine);
//                String pattern = "(\\w*-?[^\"\\d])(,)(\\w*[^\"])";
//                csvLine = csvLine.replaceAll(pattern, "$1, $3");
//                csvLine = csvLine.replace("\"", "");
//                csvLine = csvLine.replace(", ", " ");
//                csvLine = "\"" + csvLine.replace(",", "\",\"") + "\"";
                String[] csvals = csvLine.split(",");
                // search for title and retrieve corresponding symbol
                if ((csvals.length >= 3) && (csvals[1].toLowerCase().contains(key.toString().toLowerCase()))) {
                    resultList.add(csvals[0]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //return string list of possible symbols
        String tickers = resultList.toString();
        tickers = tickers.replace('[', ' ');
        tickers = tickers.replace(']', ' ');
        tickers = tickers.replace(" ", "");
        return tickers;
    }

    private void writeFileCache(String Filename, String data) {
        File folder = context.getExternalCacheDir();
        File dataFile = new File(folder, Filename);
        FileOutputStream fileOutputStream = null;
        try {
            if (dataFile.exists())
                fileOutputStream = new FileOutputStream(dataFile, true);
            else
                fileOutputStream = new FileOutputStream(dataFile);
            fileOutputStream.write(data.getBytes());
            Log.d("debug", "writeFileCache: successful.");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String readFileCache(String Filename, CharSequence search_key) {

        Log.d("debug", "Cache file search read");
        File folder = context.getExternalCacheDir();
        File dataFile = new File(folder, Filename);
        if (dataFile.exists()) {
            try {
                return file_search_read(search_key, new FileInputStream(dataFile));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected String doInBackground(String... strings) {
//        DbAdapter.DisplayAllQuotes();
//        String tickers = readFileCache("tickerCache.txt", strings[0]);
        String tickers = DbAdapter.SearchQuote(strings[0]).toString();
        publishProgress(tickers);
        Log.d("debug", "DB searched //" + tickers + "//");
        if (tickers == null || tickers == "") {
            InputStream inputStream = context.getResources().openRawResource(R.raw.company_list_db); // open an inputstream for quote list
            tickers += file_search_read(strings[0], inputStream);  // initialize csvprocessor with inputstream and get tickers from csv file search
            inputStream = context.getResources().openRawResource(R.raw.company_list_db2); // open an inputstream for quote list
            tickers += file_search_read(strings[0], inputStream);
            inputStream = context.getResources().openRawResource(R.raw.company_list_db3); // open an inputstream for quote list
            tickers += file_search_read(strings[0], inputStream);
            inputStream = context.getResources().openRawResource(R.raw.company_list_db4); // open an inputstream for quote list
            tickers += file_search_read(strings[0], inputStream);
            inputStream = context.getResources().openRawResource(R.raw.company_list_db5); // open an inputstream for quote list
            tickers += file_search_read(strings[0], inputStream);
            inputStream = context.getResources().openRawResource(R.raw.company_list_db6); // open an inputstream for quote list
            tickers += file_search_read(strings[0], inputStream);
            Log.d("debug", "CSV file search successful");
//            writeFileCache("tickerCache.txt", tickers);
//            Log.d("debug", "Cache Update: successful");
        }
        return tickers;
    }


}
