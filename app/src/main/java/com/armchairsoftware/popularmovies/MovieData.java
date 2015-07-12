package com.armchairsoftware.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieData implements Parcelable {
    boolean adult;
    String backdropPath;
    int budget;
    String homepage;
    int id;
    String imdb_id;
    String originalLanguage;
    String originalTitle;
    String overview;
    double popularity;
    String posterPath;
    String releaseDate;
    int revenue;
    int runtime;
    String status;
    String tagline;
    String title;
    boolean video;
    double voteAverage;
    int voteCount;

    public MovieData() {}

    private MovieData(Parcel in) {
        adult =  in.readByte() != 0;
        backdropPath = in.readString();
        budget = in.readInt();
        homepage = in.readString();
        id = in.readInt();
        imdb_id = in.readString();
        originalLanguage = in.readString();
        originalTitle = in.readString();
        overview = in.readString();
        popularity = in.readDouble();
        posterPath = in.readString();
        releaseDate = in.readString();
        revenue = in.readInt();
        runtime = in.readInt();
        status = in.readString();
        tagline = in.readString();
        title = in.readString();
        video =  in.readByte() != 0;
        voteAverage = in.readDouble();
        voteCount = in.readInt();
    }
    
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (adult ? 1 : 0));
        dest.writeString(backdropPath);
        dest.writeInt(budget);
        dest.writeString(homepage);
        dest.writeInt(id);
        dest.writeString(imdb_id);
        dest.writeString(originalLanguage);
        dest.writeString(originalTitle);
        dest.writeString(overview);
        dest.writeDouble(popularity);
        dest.writeString(posterPath);
        dest.writeString(releaseDate);
        dest.writeInt(revenue);
        dest.writeInt(runtime);
        dest.writeString(status);
        dest.writeString(tagline);
        dest.writeString(title);
        dest.writeByte((byte) (video ? 1 : 0));
        dest.writeDouble(voteAverage);
        dest.writeInt(voteCount);
    }

    public static final Parcelable.Creator<MovieData> CREATOR = new Parcelable.Creator<MovieData>() {
        @Override
        public MovieData createFromParcel(Parcel in) {
            return new MovieData(in);
        }

        @Override
        public MovieData[] newArray(int size) {
            return new MovieData[size];
        }
    };
}
