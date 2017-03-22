package com.anthrino.nasdaqx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CSV_Processor
{
    InputStream inputStream;

    public CSV_Processor(InputStream inputStream)
    {
        this.inputStream = inputStream;
    }

    public List file_search_read(String key) throws IOException {
        List resultList = new ArrayList();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String csvLine;
        try {
            while ((csvLine = bufferedReader.readLine()) != null)
            {
                if(csvLine.contains(key))
                {
                    String[] row = csvLine.split(",");
                    resultList.add(row);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            inputStream.close();
        }
        return resultList;
    }
}
