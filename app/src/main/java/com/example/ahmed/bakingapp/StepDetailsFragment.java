package com.example.ahmed.bakingapp;


import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ahmed.bakingapp.models.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;


import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class StepDetailsFragment extends Fragment {

    @BindView(R.id.tv_step_description) TextView mDescriptionTextView;
    @BindView(R.id.exo_player_view) SimpleExoPlayerView mSimpleExoPlayerView;
    @BindView(R.id.thumbnail) ImageView mThumbnailImageView;
    private Step mStep;
    private String mVideoURL;
    private SimpleExoPlayer mSimpleExoPlayer;
    private int mImageID;
    private boolean videoExists = true;
    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    private String mThumbnailURL;

    public StepDetailsFragment() {
        // Required empty public constructor
    }

    public static StepDetailsFragment newInstance(Step step) {

        Bundle args = new Bundle();
        args.putParcelable("step", step);

        StepDetailsFragment fragment = new StepDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mStep = getArguments().getParcelable("step");
        mThumbnailURL = mStep.getThumbnailURL();

        if(mStep.getVideoURL() != null && !mStep.getVideoURL().isEmpty())
            mVideoURL = mStep.getVideoURL();

        else
            videoExists = false;



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_step_details, container, false);
        ButterKnife.bind(this, view);


        mDescriptionTextView.setText(mStep.getDescription());

        if(!videoExists){
            mSimpleExoPlayerView.setVisibility(View.GONE);

            if(mThumbnailURL != null && !mThumbnailURL.isEmpty() && !mThumbnailURL.endsWith(".mp4")){
                mThumbnailImageView.setVisibility(View.VISIBLE);
                Picasso.with(getContext()).load(mThumbnailURL).into(mThumbnailImageView);
            }
        }



        else if(getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            mSimpleExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
            mDescriptionTextView.setVisibility(View.GONE);
        }





        return view;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if(videoExists) {
            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                mSimpleExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
                mDescriptionTextView.setVisibility(View.GONE);
            }
            else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                mSimpleExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
                mDescriptionTextView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if(videoExists)
            initializeExoPlayer(Uri.parse(mVideoURL));

    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();

    }


    @Override
    public void onPause(){
        super.onPause();
        releasePlayer();
    }





    private void initializeExoPlayer(Uri uri){

        if (mSimpleExoPlayer == null) {

            mSimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(getContext()),
                    new DefaultTrackSelector(), new DefaultLoadControl());

            mSimpleExoPlayerView.setPlayer(mSimpleExoPlayer);
            mSimpleExoPlayer.setPlayWhenReady(true);

        }

        MediaSource mediaSource = buildMediaSource(uri);
        mSimpleExoPlayer.prepare(mediaSource, true, false);

    }

    private MediaSource buildMediaSource(Uri uri) {

        String userAgent = Util.getUserAgent(getContext(), "BakingApp");
        return new ExtractorMediaSource(uri,
                new DefaultHttpDataSourceFactory(userAgent),
                new DefaultExtractorsFactory(), null, null);
    }

    private void releasePlayer() {
        if (mSimpleExoPlayer != null) {
            mSimpleExoPlayer.release();
            mSimpleExoPlayer = null;
        }

    }



}





