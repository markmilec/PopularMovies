package com.armchairsoftware.popularmovies;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivityFragment extends Fragment {
    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<String>> {
        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            if (params.length ==0) { return null; }
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String sortOrder = prefs.getString(getString(R.string.pref_sort_order_key), getString(R.string.pref_sort_order_default));
            try {
                String results = new TheMovieDbApi(getActivity()).fetchDiscoverMovieResults(sortOrder);
                ArrayList<MovieData> movies = new TheMovieDbApi(getActivity()).parseDiscoverMovieResults(results);
                ArrayList<String> imageUrls = new ArrayList<>();
                for (MovieData movie : movies){
                    imageUrls.add(TheMovieDbApi.BASE_IMAGE_URL + movie.posterPath);
                }
                return imageUrls;
            } catch (IOException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<String> imageUrls) {
            if (imageUrls == null) { return; }
        }
    }
}
