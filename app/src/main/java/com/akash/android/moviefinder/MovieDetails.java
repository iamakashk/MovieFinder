package com.akash.android.moviefinder;

/**
 * Created by AKASH on 22-03-2017.
 */

public class MovieDetails {
    public MovieDetails(String title, String genre, String released, String plot, String rated, String image) {
        Title = title;
        Genre = genre;
        Released = released;
        Plot = plot;
        Rated = rated;
        Image = image;
    }

    public final String Title,Genre,Released,Plot,Rated,Image;



}
