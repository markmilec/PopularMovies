package com.armchairsoftware.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class DetailActivityFragment extends Fragment {
    private static final String LOG_TAG = DetailActivityFragment.class.getSimpleName();
    public static final String DATA_KEY = "MOVIE_DATA";

    private HashSet<String> _favorites = new HashSet<>();
    private MovieData _movieData = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        Bundle arguments = getArguments();
        if (arguments != null) {
            _movieData = arguments.getParcelable(DATA_KEY);
        }

        if (_movieData != null) {
            if (_movieData.posterPath != null && !_movieData.posterPath.isEmpty()) {
                ImageView imageView = ((ImageView) rootView.findViewById(R.id.detail_image));
                String url = TheMovieDbApi.BASE_IMAGE_URL + _movieData.posterPath;
                Picasso.with(getActivity()).load(url).fit().into(imageView);
            }

            ((TextView) rootView.findViewById(R.id.detail_title)).setText(_movieData.title);
            ((TextView) rootView.findViewById(R.id.detail_release_date)).setText(_movieData.releaseDate);
            ((TextView) rootView.findViewById(R.id.detail_vote_average)).setText(_movieData.voteAverage + "/10");
            ((TextView) rootView.findViewById(R.id.detail_plot_synopsis)).setText(_movieData.overview);
        }

        if (_movieData != null) {
            final String movieID = Integer.toString(_movieData.id);
            final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            final String favorites_key = getActivity().getString(R.string.favorites_key);

            if (prefs.contains(favorites_key)) {
                _favorites = (HashSet<String>) prefs.getStringSet(favorites_key, new HashSet<String>());
            }

            CheckBox checkBox = (CheckBox) rootView.findViewById(R.id.favorite);
            checkBox.setChecked(_favorites.contains(movieID));
            checkBox.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if (((CheckBox) view).isChecked()) {
                        if (!_favorites.contains(movieID)) {
                            _favorites.add(movieID);
                        }
                    } else {
                        if (_favorites.contains(movieID)) {
                            _favorites.remove(movieID);
                        }
                    }
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putStringSet(favorites_key, _favorites);
                    editor.commit();
                }
            });
        }
        return rootView;
    }
}
