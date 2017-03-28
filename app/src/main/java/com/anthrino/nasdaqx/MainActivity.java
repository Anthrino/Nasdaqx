package com.anthrino.nasdaqx;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(0);
        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent = new Intent();
        if (id == R.id.nav_search)
        {
            setContentView(R.layout.activity_main);
        }
        else if (id == R.id.nav_trending)
        {
            intent.setClassName(this, "trending.class");
            setContentView(R.layout.activity_trending);
        }
        else if (id == R.id.nav_news)
        {
            intent.setClassName(this, "news_screen.class");
            setContentView(R.layout.activity_news_screen);
        }
        else if (id == R.id.nav_about)
        {
            setContentView(R.layout.activity_splash_screen);
        }
        startActivity(intent);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void search_process(View view) {

        try {
            EditText search_bar = (EditText) findViewById(R.id.search_bar); // initialize search field
            CharSequence query = search_bar.getText(); // retrieve search key

            InputStream inputStream = getResources().openRawResource(R.raw.company_list_db); // open an inputstream for quote list
            final String tickers = new CSV_Processor(inputStream).file_search_read(query);  // initialize csvprocessor with inputstream and get tickers from csv file search
            Log.d("debug", "CSV file search successful");

            ArrayList<stockQuote> stockQuotes = new yahoo_finance().execute(new String[]{tickers, "0"}).get();  // get quotes for the retrieved symbols by executing the AsyncTask yahoo_finance
            Log.d("debug", "Finance Fetch and object parse successful, length:" + stockQuotes.size());

            if (findViewById(R.id.stock_fragment_container) != null) // check if stock_fragment_container is present in current view
            {
                stockFragment firstFragment = new stockFragment();
                Bundle args = new Bundle();
//                args = getIntent().getExtras();
                args.putParcelableArrayList("stocklist", stockQuotes);
                Log.d("debug", "Putting parceable to bundle fragment");
                firstFragment.setArguments(args);
                Log.d("debug", "Fragment setting successful");
                getFragmentManager().beginTransaction().replace(R.id.stock_fragment_container, firstFragment, "stocklistfrag").commit();
                Log.d("debug", "List generation successful");
                Toast.makeText(this, "Loaded Successfully", Toast.LENGTH_SHORT);

//                TextView textView = (TextView) findViewById(R.id.search_title);
//                textView.setText("Success");
            }

        } catch (Exception e) {
            Log.d("debug", "Exception: " + e.getMessage());
        }
    }
}
