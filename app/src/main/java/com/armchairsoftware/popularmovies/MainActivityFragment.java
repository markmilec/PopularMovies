package com.armchairsoftware.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivityFragment extends Fragment {
    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private GridViewAdapter _adapter;
    private ArrayList<MovieData> _movies = new ArrayList<>();
    private String _sortOrder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        _sortOrder = getSortOrder();

        if (savedInstanceState != null && savedInstanceState.containsKey("Movies")) {
            _movies = savedInstanceState.getParcelableArrayList("Movies");
        } else {
            updateMoviePosters();
        }

        _adapter = new GridViewAdapter(getActivity(), _movies);

        GridView gv = (GridView) view.findViewById(R.id.grid_view);
        gv.setAdapter(_adapter);
        gv.setOnScrollListener(new ScrollListener(getActivity()));
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                ((ICallback) getActivity()).onItemSelected((MovieData) _adapter.getItem(position));
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        String sortOrder = getSortOrder();
        if (_sortOrder != sortOrder){
            _sortOrder = sortOrder;
            updateMoviePosters();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("Movies", _movies);
        super.onSaveInstanceState(outState);
    }

    private void updateMoviePosters() {
        if (isNetworkAvailable()) {
            new FetchMoviesTask().execute();
        } else {
            Toast.makeText(getActivity(), R.string.no_network, Toast.LENGTH_LONG).show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    private void addMoviesToAdapter(ArrayList<MovieData> movies) {
        if (movies == null) return;

        _adapter.clear();
        for (MovieData movie : movies) {
            _adapter.add(movie);
        }
    }

    private String getSortOrder() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        return prefs.getString(getString(R.string.pref_sort_order_key), getString(R.string.pref_sort_order_default));
    }

    private class FetchMoviesTask extends AsyncTask<Void, Void, ArrayList<MovieData>> {
        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        @Override
        protected ArrayList<MovieData> doInBackground(Void... voids) {
            String sortOrder = getSortOrder();
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
            addMoviesToAdapter(movies);
        }
    }
}
