package com.example.oluwadara.reddit;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements GameAdapter.GameAdapterOnClickHandler{

    private RecyclerView mRecyclerView;

    private GameAdapter mGameAdapter;

    private TextView mErrorTextView;

    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recyclerview_game_data);

        mErrorTextView = findViewById(R.id.tv_error_message_display);

        LinearLayoutManager layoutManager = new
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mGameAdapter = new GameAdapter(this, this);

        mRecyclerView.setAdapter(mGameAdapter);

        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        loadGameData();

    }

    @Override
    public void onClick(String gameData) {
        String[] parts = gameData.split("-");
        String url = parts[3];

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void loadGameData() {
        URL redditUrl = QueryUtils.redditUrl();
        if (!isNetworkAvailable()) {
            showErrorMessage();
            mErrorTextView.setText(getString(R.string.no_network));
        } else {
            new RedditAsyncTask().execute(redditUrl);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    private void showGameDataView() {
        /* First, make sure the error is invisible */
        mErrorTextView.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorTextView.setVisibility(View.VISIBLE);
    }

    /**
     * {@link AsyncTask} to perform the network request on a background thread, and then
     * update the UI with the first earthquake in the response.
     */
    private class RedditAsyncTask extends AsyncTask<URL, Void, String[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(URL... params) {
            URL location = params[0];
            String[] GameJsonData;
            try {
                String jsonResponse = QueryUtils
                        .getResponseFromHttpUrl(location);
                GameJsonData = RedditJsonUtils
                        .getGameStringsFromJson(MainActivity.this, jsonResponse);
                return GameJsonData;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] gameData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (gameData != null) {
                showGameDataView();
                mGameAdapter.setGameData(gameData);
            } else {
                showErrorMessage();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            mGameAdapter.setGameData(null);
            loadGameData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}