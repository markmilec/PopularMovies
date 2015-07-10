package com.armchairsoftware.popularmovies;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DetailActivityFragment extends Fragment {
    private static final String LOG_TAG = DetailActivityFragment.class.getSimpleName();
    private String _movieId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            _movieId = intent.getStringExtra(Intent.EXTRA_TEXT);
            populateDetails(_movieId);
        }
        return rootView;
    }

    private void populateDetails(String _movieId) {
        //((TextView) rootView.findViewById(R.id.detail_text)).setText(_forecast);
    }
}
