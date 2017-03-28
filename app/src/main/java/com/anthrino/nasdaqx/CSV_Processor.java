package com.anthrino.nasdaqx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CSV_Processor
{
    InputStream inputStream;

    public CSV_Processor(InputStream inputStream)
    {
        this.inputStream = inputStream;
    }

    String file_search_read(CharSequence key) {
        ArrayList<String> resultList = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream)); // buffered reader for CSV file inputstream
        String csvLine;
        try {
            while ((csvLine = bufferedReader.readLine()) != null)
            {
//                String pattern = "(\\w*-?[^\"\\d])(,)(\\w*[^\"])";
//                csvLine = csvLine.replaceAll(pattern, "$1, $3");
//                csvLine = csvLine.replace("\"", "");
//                csvLine = csvLine.replace(", ", " ");
//                csvLine = "\"" + csvLine.replace(",", "\",\"") + "\"";
                String[] csvals = csvLine.split(",");
                // search for title and retrieve corresponding symbol
                if (csvals[1].contains(key))
                {
                    resultList.add(csvals[0]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
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

//    private Dictionary CSVtoDict()
//    {
//
//
//
//    }
}
