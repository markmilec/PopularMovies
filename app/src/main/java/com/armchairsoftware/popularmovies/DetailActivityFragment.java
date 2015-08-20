package com.armchairsoftware.popularmovies;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class DetailActivityFragment extends Fragment {
    private static final String LOG_TAG = DetailActivityFragment.class.getSimpleName();
    public static final String DATA_KEY = "MOVIE_DATA";
    public static final String TRAILERS_KEY = "TRAILERS";
    public static final String REVIEWS_KEY = "REVIEWS";

    private HashSet<String> _favorites = new HashSet<>();
    private MovieData _movieData = null;
    private ArrayList<MovieTrailer> _trailers = new ArrayList<>();
    private ArrayList<MovieReview> _reviews = new ArrayList<>();
    private LinearLayout _trailerLayout;
    private LinearLayout _reviewLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        _trailerLayout = (LinearLayout)rootView.findViewById(R.id.detail_trailers);
        _reviewLayout = (LinearLayout)rootView.findViewById(R.id.detail_reviews);

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

            if (savedInstanceState != null) {
                if (savedInstanceState.containsKey(TRAILERS_KEY)) {
                    _trailers = savedInstanceState.getParcelableArrayList(TRAILERS_KEY);
                    updateTrailers(_trailers);
                }
                if (savedInstanceState.containsKey(REVIEWS_KEY)) {
                    _reviews = savedInstanceState.getParcelableArrayList(REVIEWS_KEY);
                    updateReviews(_reviews);
                }
            } else {
                getReviews();
                getTrailers();
            }

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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(TRAILERS_KEY, _trailers);
        outState.putParcelableArrayList(REVIEWS_KEY, _reviews);
        super.onSaveInstanceState(outState);
    }

    private void getTrailers() {
        if (Utility.isNetworkAvailable(getActivity())) {
            new FetchMovieTrailersTask().execute();
        } else {
            Toast.makeText(getActivity(), R.string.no_network, Toast.LENGTH_LONG).show();
        }
    }

    private void getReviews() {
        if (Utility.isNetworkAvailable(getActivity())) {
            new FetchMovieReviewsTask().execute();
        } else {
            Toast.makeText(getActivity(), R.string.no_network, Toast.LENGTH_LONG).show();
        }
    }

    private void updateTrailers(ArrayList<MovieTrailer> trailers){
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (!trailers.isEmpty()){
            ((TextView)_trailerLayout.findViewById(R.id.detail_trailers_title)).setText(getString(R.string.detail_trailers_title));
        }

        for(final MovieTrailer trailer: trailers) {
            CardView view = (CardView)inflater.inflate(R.layout.view_trailer, _trailerLayout, false);

            TextView textView = (TextView)view.findViewById(R.id.trailer_name);
            textView.setText(trailer.name);

            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Utility.launchYouTube(getActivity(), trailer.key);
                }
            });

            _trailerLayout.addView(view);
        }
    }

    private void updateReviews(ArrayList<MovieReview> reviews){
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (!reviews.isEmpty()){
            ((TextView)_reviewLayout.findViewById(R.id.detail_reviews_title)).setText(getString(R.string.detail_reviews_title));
        }

        for(MovieReview review: reviews) {
            CardView view = (CardView)inflater.inflate(R.layout.view_review, _reviewLayout, false);

            TextView authorView= (TextView)view.findViewById(R.id.review_author);
            authorView.setText(review.author);

            TextView contentView = (TextView)view.findViewById(R.id.review_content);
            contentView.setText(review.content);
            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    TextView textView = (TextView)view.findViewById(R.id.review_content);
                    if (textView.getMaxLines() == 1) {
                        textView.setMaxLines(Integer.MAX_VALUE);
                        textView.setEllipsize(null);
                    } else {
                        textView.setMaxLines(1);
                        textView.setEllipsize(TextUtils.TruncateAt.END);
                    }
                }
            });

            _reviewLayout.addView(view);
        }
    }

    private class FetchMovieReviewsTask extends AsyncTask<Void, Void, ArrayList<MovieReview>> {
        private final String LOG_TAG = FetchMovieReviewsTask.class.getSimpleName();

        @Override
        protected ArrayList<MovieReview> doInBackground(Void... voids) {
            return new TheMovieDbApi(getActivity()).getMovieReviews(_movieData.id);
        }

        @Override
        protected void onPostExecute(ArrayList<MovieReview> reviews) {
            _reviews = reviews;
            updateReviews(reviews);
        }
    }

    private class FetchMovieTrailersTask extends AsyncTask<Void, Void, ArrayList<MovieTrailer>> {
        private final String LOG_TAG = FetchMovieReviewsTask.class.getSimpleName();

        @Override
        protected ArrayList<MovieTrailer> doInBackground(Void... voids) {
            return new TheMovieDbApi(getActivity()).getMovieTrailers(_movieData.id);
        }

        @Override
        protected void onPostExecute(ArrayList<MovieTrailer> trailers) {
            _trailers = trailers;
            updateTrailers(trailers);
        }
    }
}
