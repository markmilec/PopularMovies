package com.armchairsoftware.popularmovies;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivityFragment extends Fragment {
    private static final String LOG_TAG = DetailActivityFragment.class.getSimpleName();
    public static final String DATA_KEY = "MOVIE_DATA";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        Bundle arguments = getArguments();
        MovieData movieData = null;

        if (arguments != null) {
            movieData = arguments.getParcelable(DATA_KEY);
        }

        if (movieData != null) {
            if (movieData.posterPath != null && !movieData.posterPath.isEmpty()) {
                ImageView imageView = ((ImageView) rootView.findViewById(R.id.detail_image));
                String url = TheMovieDbApi.BASE_IMAGE_URL + movieData.posterPath;
                Picasso.with(getActivity()).load(url).fit().into(imageView);
            }

            ((TextView) rootView.findViewById(R.id.detail_title)).setText(movieData.title);
            ((TextView) rootView.findViewById(R.id.detail_release_date)).setText(movieData.releaseDate);
            ((TextView) rootView.findViewById(R.id.detail_vote_average)).setText(movieData.voteAverage + "/10");
            ((TextView) rootView.findViewById(R.id.detail_plot_synopsis)).setText(movieData.overview);
        }
        return rootView;
    }
}
