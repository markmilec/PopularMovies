package com.armchairsoftware.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivityFragment extends Fragment {
    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private GridViewAdapter _adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        Log.d(LOG_TAG, "Inflating Fragment");

        _adapter = new GridViewAdapter(getActivity(), new ArrayList<String>());

        GridView gv = (GridView) view.findViewById(R.id.grid_view);
        gv.setAdapter(_adapter);
        gv.setOnScrollListener(new ScrollListener(getActivity()));

        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        updateMoviePosters();
    }

    private void updateMoviePosters(){
        new FetchMoviesTask().execute();
    }

    private class FetchMoviesTask extends AsyncTask<Void, Void, ArrayList<String>> {
        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            Log.d(LOG_TAG, "Fetching movies");

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String sortOrder = prefs.getString(getString(R.string.pref_sort_order_key), getString(R.string.pref_sort_order_default));
            Log.d(LOG_TAG, "sort order:" + sortOrder);
            try {
                String results = new TheMovieDbApi(getActivity()).fetchDiscoverMovieResults(sortOrder);
                Log.d(LOG_TAG, "results:" + results);
                ArrayList<MovieData> movies = new TheMovieDbApi(getActivity()).parseDiscoverMovieResults(results);
                ArrayList<String> imageUrls = new ArrayList<>();
                for (MovieData movie : movies){
                    if (movie.posterPath != null && !movie.posterPath.isEmpty()){
                        String url = TheMovieDbApi.BASE_IMAGE_URL + movie.posterPath;
                        Log.d(LOG_TAG, "adding url:" + url);
                        imageUrls.add(url);
                    }
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
            Log.d(LOG_TAG, "onPostExecute");
            if (imageUrls == null) { return; }

            _adapter.clear();
            for(String url: imageUrls){
                _adapter.add(url);
            }
        }
    }
}
