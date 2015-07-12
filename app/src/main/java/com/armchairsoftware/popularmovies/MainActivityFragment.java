package com.armchairsoftware.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

        _adapter = new GridViewAdapter(getActivity(), new ArrayList<MovieData>());

        GridView gv = (GridView) view.findViewById(R.id.grid_view);
        gv.setAdapter(_adapter);
        gv.setOnScrollListener(new ScrollListener(getActivity()));
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent detailIntent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra("MovieData", (MovieData)_adapter.getItem(position));
                startActivity(detailIntent);
            }
        });

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

    private class FetchMoviesTask extends AsyncTask<Void, Void, ArrayList<MovieData>> {
        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        @Override
        protected ArrayList<MovieData> doInBackground(Void... voids) {
            Log.d(LOG_TAG, "Fetching movies");

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String sortOrder = prefs.getString(getString(R.string.pref_sort_order_key), getString(R.string.pref_sort_order_default));
            Log.d(LOG_TAG, "sort order:" + sortOrder);
            try {
                String results = new TheMovieDbApi(getActivity()).fetchDiscoverMovieResults(sortOrder);
                Log.d(LOG_TAG, "results:" + results);
                return new TheMovieDbApi(getActivity()).parseDiscoverMovieResults(results);
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
        protected void onPostExecute(ArrayList<MovieData> movies) {
            Log.d(LOG_TAG, "onPostExecute");
            if (movies == null) { return; }

            _adapter.clear();
            for(MovieData movie: movies){
                _adapter.add(movie);
            }
        }
    }
}
