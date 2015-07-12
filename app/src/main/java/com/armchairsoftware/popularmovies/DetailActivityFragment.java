package com.armchairsoftware.popularmovies;

import android.content.Intent;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra("MovieData")) {
            MovieData movie = intent.getParcelableExtra("MovieData");
            if (movie.posterPath != null && !movie.posterPath.isEmpty()) {
                ImageView imageView = ((ImageView) rootView.findViewById(R.id.detail_image));
                String url = TheMovieDbApi.BASE_IMAGE_URL + movie.posterPath;
                Picasso.with(getActivity()).load(url).fit().into(imageView);
            }

            ((TextView)rootView.findViewById(R.id.detail_title)).setText(movie.title);
            ((TextView)rootView.findViewById(R.id.detail_release_date)).setText(movie.releaseDate);
            ((TextView)rootView.findViewById(R.id.detail_vote_average)).setText(movie.voteAverage + "/10");
            ((TextView)rootView.findViewById(R.id.detail_plot_synopsis)).setText(movie.overview);

        }
        return rootView;
    }
}
