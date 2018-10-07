package com.vincentangway.popularmovies.ui.helper;

import com.vincentangway.popularmovies.data.model.Cast;
import com.vincentangway.popularmovies.data.model.Movies;
import com.vincentangway.popularmovies.data.model.Reviews;
import com.vincentangway.popularmovies.data.model.Videos;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class RetrofitClients {
    public interface AllMoviesClient {

        String popularEndPoint = "movie/popular";
        String topRatedEndPoint = "movie/top_rated";

        @GET(popularEndPoint)
        Call<Movies> getPopularMovies(
                @Query("api_key") String apiKey
        );

        @GET(topRatedEndPoint)
        Call<Movies> getTopRatedMovies(
                @Query("api_key") String apiKey
        );
    }

    public interface MovieDetailsClient {
        String movieVideosEndPoint = "movie/{movieID}/videos";
        String movieReviewsEndPoint = "movie/{movieID}/reviews";
        String movieCastEndPoint = "movie/{movieID}/credits";

        @GET(movieVideosEndPoint)
        Call<Videos> getMovieVideos(
                @Path("movieID") int movieID,
                @Query("api_key") String apiKey
        );

        @GET(movieReviewsEndPoint)
        Call<Reviews> getMovieReviews(
                @Path("movieID") int movieID,
                @Query("api_key") String apiKey
        );

        @GET(movieCastEndPoint)
        Call<Cast> getMovieCast(
                @Path("movieID") int movieID,
                @Query("api_key") String apiKey
        );
    }
}
