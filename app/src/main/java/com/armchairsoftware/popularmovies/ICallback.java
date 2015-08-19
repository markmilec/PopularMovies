package com.armchairsoftware.popularmovies;

/**
 * A callback interface that all activities containing this fragment must
 * implement. This mechanism allows activities to be notified of item selections.
 */
public interface ICallback {
    /**
     * DetailFragmentCallback for when an item has been selected.
     */
    void onItemSelected(MovieData movieData);
}