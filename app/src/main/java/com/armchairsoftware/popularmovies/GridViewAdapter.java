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

import static android.widget.ImageView.ScaleType.CENTER_CROP;

final class GridViewAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {
    private final String LOG_TAG = GridViewAdapter.class.getSimpleName();
    private final Object _lock = new Object();
    private final Context _context;
    private ArrayList<String> _urls;
    public GridViewAdapter(Context context, ArrayList<String> urls) {
        _context = context;
        _urls = urls;
    }

    @Override
    public int getCount() {
        return _urls.size();
    }

    @Override
    public Object getItem(int position) {
        return _urls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d(LOG_TAG, "getView[" + position + "]");

        WindowManager window = (WindowManager)_context.getSystemService(Context.WINDOW_SERVICE);
        Display display = window.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = Math.round(size.x/2);
        int height = (int)Math.round(width * 1.5);

        ImageView view = new ImageView(_context);
        view.setScaleType(ImageView.ScaleType.FIT_CENTER);

        view.setLayoutParams(new GridView.LayoutParams(width, height));

        String url = _urls.get(position);
        Log.d(LOG_TAG, url);
        Picasso.with(_context).load(url)
                //.placeholder(R.drawable.placeholder)
                //.error(R.drawable.error)
                .fit()
                .into(view);

        return view;
    }

    public void add(String url) {
        synchronized (_lock) {
            Log.d(LOG_TAG, "add:" + url);
            _urls.add(url);
        }
        notifyDataSetChanged();
    }

    public void clear(){
        synchronized (_lock) {
            _urls.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}