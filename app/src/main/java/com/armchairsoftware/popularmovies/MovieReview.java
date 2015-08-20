package com.armchairsoftware.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieReview  implements Parcelable {
    String id;
    String author;
    String content;
    String url;

    public MovieReview(){}

    private MovieReview(Parcel in){
        id = in.readString();
        author = in.readString();;
        content = in.readString();;
        url = in.readString();;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(author);
        dest.writeString(content);
        dest.writeString(url);
    }

    public static final Parcelable.Creator<MovieReview> CREATOR = new Parcelable.Creator<MovieReview>() {
        @Override
        public MovieReview createFromParcel(Parcel in) {
            return new MovieReview(in);
        }

        @Override
        public MovieReview[] newArray(int size) {
            return new MovieReview[size];
        }
    };
}
