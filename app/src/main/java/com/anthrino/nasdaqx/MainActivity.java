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
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, stockFragment.OnListFragmentInteractionListener {

    private static StockDBAdapter DbAdapter;

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
        navigationView.setCheckedItem(R.id.nav_search);
        navigationView.setNavigationItemSelectedListener(this);

        //Creating Database to cache prev searched Quote Symbols
        DbAdapter = new StockDBAdapter(this); // db constructor with context.

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

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent = new Intent();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (id == R.id.nav_search) {
            drawer.closeDrawer(R.id.drawer_layout);
        } else if (id == R.id.nav_trending) {
            intent.setClass(MainActivity.this, trending.class);
            Log.d("debug", "nav trending");
        } else if (id == R.id.nav_news) {
            intent.setClass(MainActivity.this, news_screen.class);
            Log.d("debug", "nav news");
        }
//        else if (id == R.id.nav_about)
//        {
//            setContentView(R.layout.activity_splash_screen);
//        }
        startActivity(intent);
        return true;
    }

    public void search_process(View view) {

        try {

//            FrameLayout frameLayout = (FrameLayout) findViewById(R.id.stock_fragment_container);
//            frameLayout.setVisibility(View.INVISIBLE);
            if (getFragmentManager().findFragmentByTag("stocklistfrag") != null)
                getFragmentManager().beginTransaction().detach(getFragmentManager().findFragmentByTag("stocklistfrag")).commit();
            // Removing existing fragments in the layout

            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
            View fragmentContainer = findViewById(R.id.stock_fragment_container);
            EditText search_bar = (EditText) findViewById(R.id.search_bar); // initialize search field
            CharSequence query = search_bar.getText(); // retrieve search key

            String tickers = new CSV_Processor(this, DbAdapter, progressBar, fragmentContainer).execute(new String[]{query.toString()}).get();

            ArrayList<stockQuote> stockQuotes = new yahoo_finance(progressBar, DbAdapter, fragmentContainer, this).execute(new String[]{tickers, "0", String.valueOf(search_bar.getText())}).get();
            // get quotes for the retrieved symbols by executing the AsyncTask yahoo_finance

            Log.d("debug", "Finance Fetch and object parse successful, length:" + stockQuotes.size());

            if (findViewById(R.id.stock_fragment_container) != null) // check if stock_fragment_container is present in current view
            {
                stockFragment firstFragment = new stockFragment();
                Bundle args = new Bundle();
//                args = getIntent().getExtras();
                args.putParcelableArrayList("stocklist", stockQuotes);
//                Log.d("debug", "Putting parceable to bundle fragment");
                firstFragment.setArguments(args);
//                Log.d("debug", "Fragment setting successful");
//                frameLayout.setVisibility(View.VISIBLE);
                getFragmentManager().beginTransaction().replace(R.id.stock_fragment_container, firstFragment, "stocklistfrag").commit();
//                Log.d("debug", "List generation successful");
                if (stockQuotes.size() > 0)
                    Toast.makeText(this, "Loaded Successfully", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();

//                TextView textView = (TextView) findViewById(R.id.search_title);
//                textView.setText("Success");
            }

        } catch (Exception e) {
            Log.d("debug", "Exception: " + e.getMessage());
        }
    }

    @Override
    public void onListFragmentInteraction(View stockView, String symbol) {
//        Toast.makeText(this, "Loaded Successfully", Toast.LENGTH_SHORT).show();
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        View fragmentContainer = findViewById(R.id.stock_fragment_container);
        EditText search_bar = (EditText) findViewById(R.id.search_bar);
        Intent intent = new Intent();

        try {
            ArrayList<stockQuote> stockQuotes = new yahoo_finance(progressBar, DbAdapter, fragmentContainer, this).execute(new String[]{symbol, "1", String.valueOf(search_bar.getText())}).get();
//            Log.d("debug", "Click fetch:" + stockQuotes.size()+stockQuotes.get(0).getComp_name()+stockQuotes.get(0).getComp_yearRange());
            Bundle args = new Bundle();
            args.putParcelableArrayList("stockQuotes", stockQuotes);
            intent.putExtras(args);
            intent.putExtra("Logo", stockQuotes.get(0).getComp_logo());
            intent.setClass(MainActivity.this, Company_info.class);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
