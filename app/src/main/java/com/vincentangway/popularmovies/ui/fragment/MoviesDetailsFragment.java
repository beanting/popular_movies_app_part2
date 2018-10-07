package com.vincentangway.popularmovies.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;
import com.vincentangway.popularmovies.R;
import com.vincentangway.popularmovies.data.model.Cast;
import com.vincentangway.popularmovies.data.model.Reviews;
import com.vincentangway.popularmovies.data.model.Videos;
import com.vincentangway.popularmovies.data.realm.RealmObjectCastMember;
import com.vincentangway.popularmovies.data.realm.RealmObjectMovie;
import com.vincentangway.popularmovies.data.realm.RealmObjectReview;
import com.vincentangway.popularmovies.data.realm.RealmObjectVideo;
import com.vincentangway.popularmovies.ui.activity.MainActivity;
import com.vincentangway.popularmovies.ui.adapter.CastMembersAdapter;
import com.vincentangway.popularmovies.ui.adapter.ReviewsAdapter;
import com.vincentangway.popularmovies.ui.adapter.VideosAdapter;
import com.vincentangway.popularmovies.ui.helper.RetrofitClients;
import com.vincentangway.popularmovies.utils.MovieUtils;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MoviesDetailsFragment extends Fragment {

    Realm realm;
    RealmObjectMovie movie;
    RetrofitClients.MovieDetailsClient moviesClient;
    Call<Reviews> reviewsCall;
    Call<Videos> videoCall;
    Call<Cast> castCall;
    FloatingActionButton favoriteFab;
    ImageView posterImageView;
    ImageView backdropImageView;
    TextView titleTextView;
    TextView captionTextView;
    TextView overviewTextView;
    RecyclerView videosRecyclerView;
    RecyclerView reviewsRecyclerView;
    RecyclerView castRecyclerView;
    NestedScrollView parentScrollView;
    CoordinatorLayout parentCoordinator;

    int id;
    String apiKey;
    boolean mTwoPane;
    String favoriteMessage;
    final String DATA_LOG_TAG = "Retrofit Log";

    public MoviesDetailsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movies_details, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        realm = Realm.getDefaultInstance();
        apiKey = getString(R.string.api_key);
        mTwoPane = getResources().getBoolean(R.bool.isTabletLand);

        if (mTwoPane && savedInstanceState == null) {
            id = 0;
        } else {
            id = getActivity().getIntent().getIntExtra(MainActivity.MOVIE_ID, 0);
        }

        initViews(view);
        initMovie();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initMovie() {

        boolean loadingFailure = false;

        movie = realm.where(RealmObjectMovie.class)
                .equalTo("id", id).findFirst();

        if (movie == null) {
            if (!mTwoPane) {
                Toast.makeText(getActivity(), getString(R.string.error_getting_id), Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
            loadingFailure = true;
        }

        if (loadingFailure) {
            hideViews();
        } else {
            DisplayMovieDetails();
            displayFavoriteIcon();
            setupFavoriteMessage();
            setupFavoriteFabListener();
            getVideosAndReviewsAndCast();
        }
    }

    private void hideViews() {
        parentScrollView.setVisibility(View.INVISIBLE);
    }

    private void showViews() {
        parentScrollView.setVisibility(View.VISIBLE);
        parentScrollView.fullScroll(View.FOCUS_UP);
    }

    private void initViews(View rootView) {
        posterImageView = (ImageView) rootView.findViewById(R.id.details_poster_image_view);
        backdropImageView = (ImageView) rootView.findViewById(R.id.details_backdrop_image_view);
        titleTextView = (TextView) rootView.findViewById(R.id.details_title_text_view);
        captionTextView = (TextView) rootView.findViewById(R.id.details_caption_text_view);
        overviewTextView = (TextView) rootView.findViewById(R.id.details_overview_text_view);
        videosRecyclerView = (RecyclerView) rootView.findViewById(R.id.videos_recycler);
        reviewsRecyclerView = (RecyclerView) rootView.findViewById(R.id.reviews_recycler);
        castRecyclerView = (RecyclerView) rootView.findViewById(R.id.cast_recycler);
        favoriteFab = (FloatingActionButton) rootView.findViewById(R.id.details_fab);
        parentScrollView = (NestedScrollView) rootView.findViewById(R.id.details_parent);
        parentCoordinator = (CoordinatorLayout) rootView.findViewById(R.id.parent_coordinator_details);
    }

    private void DisplayMovieDetails() {

        titleTextView.setText(movie.getTitle());
        overviewTextView.setText(movie.getOverview());

        String caption = String.format(getString(R.string.details_caption_format)
                , movie.getReleaseDate()
                , String.valueOf(movie.getVoteAverage()));

        captionTextView.setText(caption);

        String backdropURL = String.format(getString(R.string.image_url_format_original)
                , movie.getBackdropPath());

        String posterURL = String.format(getString(R.string.image_url_format_w300)
                , movie.getPosterPath());

        Picasso.with(getActivity())
                .load(posterURL)
                .placeholder(R.drawable.poster_placeholder_small)
                .fit()
                .centerCrop()
                .into(posterImageView);

        Picasso.with(getActivity())
                .load(backdropURL)
                .placeholder(R.drawable.backdrop_placeholder)
                .fit()
                .centerCrop()
                .into(backdropImageView);

        CastMembersAdapter castMembersAdapter = new CastMembersAdapter(getActivity()
                , realm.where(RealmObjectCastMember.class).equalTo("movieID", movie.getId()).findAll()
                , this);

        VideosAdapter videosRecyclerAdapter = new VideosAdapter(getActivity()
                , realm.where(RealmObjectVideo.class).equalTo("movieID", movie.getId()).findAll()
                , this);

        ReviewsAdapter reviewsRecyclerAdapter = new ReviewsAdapter(getActivity()
                , realm.where(RealmObjectReview.class).equalTo("movieID", movie.getId()).findAll()
                , this);

        castRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        castRecyclerView.setAdapter(castMembersAdapter);

        videosRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        videosRecyclerView.setAdapter(videosRecyclerAdapter);

        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        reviewsRecyclerView.setAdapter(reviewsRecyclerAdapter);
    }

    private void getVideosAndReviewsAndCast() {
        final String BASE_URL = "http://api.themoviedb.org/3/";
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        moviesClient = retrofit.create(RetrofitClients.MovieDetailsClient.class);

        reviewsCall = moviesClient.getMovieReviews(movie.getId(), apiKey);
        videoCall = moviesClient.getMovieVideos(movie.getId(), apiKey);
        castCall = moviesClient.getMovieCast(movie.getId(), apiKey);

        reviewsCall.enqueue(MovieUtils.getMovieReviewsCallBack(DATA_LOG_TAG, realm, movie));
        videoCall.enqueue(MovieUtils.getMovieVideosCallBack(DATA_LOG_TAG, realm, movie));
        castCall.enqueue(MovieUtils.getMovieCastCallBack(DATA_LOG_TAG, realm, movie));
    }

    private void setupFavoriteFabListener() {
        final boolean favoriteOld = movie.isFavorite();
        final String messageFinal = favoriteMessage;

        favoriteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(parentCoordinator, messageFinal, Snackbar.LENGTH_SHORT).show();
                toggleMovieFavorite(favoriteOld);
                displayFavoriteIcon();
                setupFavoriteMessage();
                setupFavoriteFabListener();
                if (mTwoPane) {
                    if (getActivity().getSupportFragmentManager().findFragmentByTag(getString(R.string.fragment_main_tag)) != null)
                        ((MainActivity) getActivity()).favoritesChanged();
                }
            }
        });
    }

    private void displayFavoriteIcon() {
        if (movie.isFavorite()) {
            favoriteFab.setImageResource(R.drawable.ic_favorite_true);
        } else {
            favoriteFab.setImageResource(R.drawable.ic_favorite_false);
        }
    }

    private void toggleMovieFavorite(boolean favoriteOld) {
        realm.beginTransaction();
        movie.setFavorite(!favoriteOld);
        realm.copyToRealmOrUpdate(movie);
        realm.commitTransaction();
    }

    private void setupFavoriteMessage() {
        if (movie.isFavorite()) {
            favoriteMessage = getString(R.string.favorite_message_removed);
        } else {
            favoriteMessage = getString(R.string.favorite_message_added);
        }
    }

    public void handleVideoKey(String key) {
        startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse(String.format(getString(R.string.youtube_url_format), key))));
    }

    public void handleReviewUri(String url) {
        startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse(url)));
    }

    public void handleVideoShare(RealmObjectVideo video) {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        share.putExtra(Intent.EXTRA_TEXT,
                String.format(getString(R.string.share_video_format)
                        , video.getMovieTitle()
                        , video.getName()
                        , video.getKey()));

        startActivity(Intent.createChooser(share, String.format(getString(R.string.share_video_message_format), video.getType())));
    }

    public void twoPaneDisplay(Integer id) {
        this.id = id;
        showViews();
        initMovie();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();

        if (videoCall != null) {
            videoCall.cancel();
            videoCall = null;
        }
        if (reviewsCall != null) {
            reviewsCall.cancel();
            reviewsCall = null;
        }
        if (castCall != null) {
            castCall.cancel();
            castCall = null;
        }
    }
}
