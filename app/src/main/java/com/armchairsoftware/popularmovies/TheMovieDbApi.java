package com.armchairsoftware.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class TheMovieDbApi {
    private final String LOG_TAG = TheMovieDbApi.class.getSimpleName();
    private Context _context;
    private final String _baseUrl = "http://api.themoviedb.org/3";

    public TheMovieDbApi(Context context) {
        _context = context;
    }

    public String fetchDiscoverMovieResults(String sortOrder)
            throws IOException {

        final String discoverMovieUrl = "/discover/movie?";
        Uri uri = Uri.parse(_baseUrl + discoverMovieUrl)
                .buildUpon()
                .appendQueryParameter("api_key", _context.getString(R.string.api_key))
                .appendQueryParameter("sort_by", sortOrder)
                .build();

        URL url = new URL(uri.toString());
        return fetchJsonResults(url);
    }

    public String fetchMovieDetails(int id)
            throws IOException {

        final String movieDetailsUrl = "/movie/" + Integer.toString(id);
        Uri uri = Uri.parse(_baseUrl + movieDetailsUrl)
                .buildUpon()
                .appendQueryParameter("api_key", _context.getString(R.string.api_key))
                .build();

        URL url = new URL(uri.toString());
        return fetchJsonResults(url);
    }

    public String fetchMovieTrailers(int id)
            throws IOException {

        final String movieTrailersUrl = "/movie/" + Integer.toString(id) + "/videos";
        Uri uri = Uri.parse(_baseUrl + movieTrailersUrl)
                .buildUpon()
                .appendQueryParameter("api_key", _context.getString(R.string.api_key))
                .build();

        URL url = new URL(uri.toString());
        return fetchJsonResults(url);
    }

    public String fetchMovieReviews(int id)
            throws IOException {

        final String movieReviewsUrl = "/movie/" + Integer.toString(id) + "/reviews";
        Uri uri = Uri.parse(_baseUrl + movieReviewsUrl)
                .buildUpon()
                .appendQueryParameter("api_key", _context.getString(R.string.api_key))
                .build();

        URL url = new URL(uri.toString());
        return fetchJsonResults(url);
    }

    public ArrayList<MovieData> parseDiscoverMovieResults(String json)
        throws JSONException {

        ArrayList<MovieData> movieDataList = new ArrayList<>();
        JSONObject resultsJson = new JSONObject(json);
        JSONArray resultsArray = resultsJson.getJSONArray("results");
        for (int i = 0; i < resultsArray.length(); i++) {
            JSONObject result = resultsArray.getJSONObject(i);
            MovieData movieData = new MovieData();
            movieData.adult = result.getBoolean("adult");
            movieData.backdropPath = result.getString("backdrop_path");
            movieData.id = result.getInt("id");
            movieData.originalLanguage = result.getString("original_language");
            movieData.originalTitle = result.getString("original_title");
            movieData.overview = result.getString("overview");
            movieData.popularity = result.getDouble("popularity");
            movieData.posterPath = result.getString("poster_path");
            movieData.releaseDate = result.getString("release_date");
            movieData.title = result.getString("title");
            movieData.video = result.getBoolean("video");
            movieData.voteAverage = result.getDouble("vote_average");
            movieData.voteCount = result.getInt("vote_count");
            movieDataList.add(movieData);
        }
        return movieDataList;
    }

    public ArrayList<MovieData> parseMovieDetails(String json)
            throws JSONException {

        ArrayList<MovieData> movieDataList = new ArrayList<>();
        JSONObject resultsJson = new JSONObject(json);
        JSONArray resultsArray = resultsJson.getJSONArray("results");
        for (int i = 0; i < resultsArray.length(); i++) {
            JSONObject result = resultsArray.getJSONObject(i);
            MovieData movieData = new MovieData();
            movieData.adult = result.getBoolean("adult");
            movieData.backdropPath = result.getString("backdrop_path");
            movieData.budget = result.getInt("budget");
            movieData.homepage = result.getString("homepage");
            movieData.id = result.getInt("id");
            movieData.imdb_id = result.getString("imdb_id");
            movieData.originalLanguage = result.getString("original_language");
            movieData.originalTitle = result.getString("original_title");
            movieData.overview = result.getString("overview");
            movieData.releaseDate = result.getString("release_date");
            movieData.posterPath = result.getString("poster_path");
            movieData.popularity = result.getDouble("popularity");
            movieData.revenue = result.getInt("revenue");
            movieData.runtime = result.getInt("runtime");
            movieData.status = result.getString("status");
            movieData.tagline = result.getString("tagline");
            movieData.title = result.getString("title");
            movieData.video = result.getBoolean("video");
            movieData.voteAverage = result.getDouble("vote_average");
            movieData.voteCount = result.getInt("vote_count");
            movieDataList.add(movieData);
        }
        return movieDataList;
    }

    public ArrayList<MovieTrailer> parseMovieTrailers(String json)
            throws JSONException {

        ArrayList<MovieTrailer> movieTrailers = new ArrayList<>();
        JSONObject resultsJson = new JSONObject(json);
        JSONArray resultsArray = resultsJson.getJSONArray("results");
        for (int i = 0; i < resultsArray.length(); i++) {
            JSONObject result = resultsArray.getJSONObject(i);
            MovieTrailer movieTrailer = new MovieTrailer();
            movieTrailer.id = result.getString("id");
            movieTrailer.iso_639_1 = result.getString("iso_639_1");
            movieTrailer.key = result.getString("key");
            movieTrailer.name = result.getString("name");
            movieTrailer.site = result.getString("site");
            movieTrailer.size = result.getInt("size");
            movieTrailer.type = result.getString("type");
            movieTrailers.add(movieTrailer);
        }
        return movieTrailers;
    }

    public ArrayList<MovieReview> parseMovieReviews(String json)
            throws JSONException {

        ArrayList<MovieReview> movieReviews = new ArrayList<>();
        JSONObject resultsJson = new JSONObject(json);
        JSONArray resultsArray = resultsJson.getJSONArray("results");
        for (int i = 0; i < resultsArray.length(); i++) {
            JSONObject result = resultsArray.getJSONObject(i);
            MovieReview movieReview = new MovieReview();
            movieReview.id = result.getString("id");
            movieReview.author = result.getString("author");
            movieReview.content = result.getString("content");
            movieReview.url = result.getString("url");
            movieReviews.add(movieReview);
        }
        return movieReviews;
    }

    private String fetchJsonResults(URL url) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String jsonStr = null;

        try {
            // Create the request to TheMovieDb, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            jsonStr = buffer.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        return jsonStr;
    }
}
