package com.armchairsoftware.popularmovies;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

final class GridViewAdapter extends BaseAdapter {
    private final String LOG_TAG = GridViewAdapter.class.getSimpleName();
    private final Object _lock = new Object();
    private final Context _context;
    private ArrayList<MovieData> _movies;
    private int _cellHeight = 450;

    public GridViewAdapter(Context context, ArrayList<MovieData> movies) {
        _context = context;
        _movies = movies;
        int width = _context.getResources().getDimensionPixelSize(R.dimen.grid_column_width);
        _cellHeight = (int)Math.round(width * 1.5);
    }

    @Override
    public int getCount() {
        return _movies.size();
    }

    @Override
    public Object getItem(int position) {
        return _movies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView view;
        if (convertView == null) {
            view = new ImageView(_context);
            view.setScaleType(ImageView.ScaleType.FIT_CENTER);
            view.setMaxHeight(_cellHeight);
            view.setMinimumHeight(_cellHeight);
        } else {
            view = (ImageView)convertView;
        }

        MovieData movie = _movies.get(position);
        if (movie.posterPath != null && !movie.posterPath.isEmpty()) {
            String url = TheMovieDbApi.BASE_IMAGE_URL + movie.posterPath;
            Picasso.with(_context).load(url).fit().into(view);
        }
        return view;
    }

    public void add(MovieData movie) {
        synchronized (_lock) {
            _movies.add(movie);
        }
        notifyDataSetChanged();
    }

    public void clear(){
        synchronized (_lock) {
            _movies.clear();
        }
        notifyDataSetChanged();
    }
}