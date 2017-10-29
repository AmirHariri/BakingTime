package com.example.android.bakingtime;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by Amir on 10/16/2017.
 */

public class MediaPlayerFragment extends Fragment implements ExoPlayer.EventListener {

    private static final String LOG_TAG = MediaPlayerFragment.class.getSimpleName();
    private SimpleExoPlayer mExoPlayer;
    public SimpleExoPlayerView mPlayerView;
    public ImageView dummyPic;
    public int stepNumber;
    public String videoURL;
    public ArrayList<Recepie.Steps> steps;
    boolean urlIsAvailble= false;
    public boolean singlePane = true;
    long mVideoPosition;
    private PlaybackStateCompat.Builder mStateBuilder;
    Bundle mSavedInsatanceState;

    private static MediaSessionCompat mMediaSession;

    public MediaPlayerFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (singlePane) {
            Intent i = getActivity().getIntent();
            steps = (ArrayList<Recepie.Steps>) getActivity().getIntent().getSerializableExtra("RecepieSteps");
            stepNumber = i.getIntExtra("StepNumber", 0);
            Log.e(LOG_TAG,"the stepNumber from Intent is :"+ stepNumber+ "and from SaveInstance " +
                    "State is :");
        }
            View rootViewMedia = inflater.inflate(R.layout.fragment_media_player, container, false);

            mPlayerView = (SimpleExoPlayerView) rootViewMedia.findViewById(R.id.exoplayer_view);

            dummyPic = (ImageView) rootViewMedia.findViewById(R.id.dummy_pic);

            videoURL = steps.get(stepNumber).getVideoURL();

            if (videoURL.equals("")) {
                videoIsVisible(urlIsAvailble);
                Log.e(LOG_TAG, "VideoUrl was Empty !!!!!!!!!!!!!!!!!!!! Now is : " + videoURL);
            } else {
                initializeMediaSession();
                initializePlayer(Uri.parse(videoURL));
            }
        // retain this fragment
        setRetainInstance(true);
        return rootViewMedia;
    }

    @Override
     public void onSaveInstanceState(Bundle savedInstanceState) {
            super.onSaveInstanceState(savedInstanceState);
            savedInstanceState.putLong("VideoPosition", mVideoPosition);
            savedInstanceState.putInt("StepNumber", stepNumber);
            mSavedInsatanceState = savedInstanceState;
    }

    /**
     * Initialize ExoPlayer.
     * @param mediaUri The URI of the sample to play.
     */
    public void initializePlayer(Uri mediaUri) {
        //to switch dummy pic with Exoplayer
        urlIsAvailble = true;
        videoIsVisible(urlIsAvailble);
        if (mExoPlayer == null) {

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), "BakingTime");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);

            // Create an instance of the ExoPlayer.
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            if (mPlayerView != null) {
                mPlayerView.setPlayer(mExoPlayer);
            }
            // Set the ExoPlayer.EventListener to this activity.
            mExoPlayer.addListener(this);
            // Start playing the video from the media source at the current position.
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
            mExoPlayer.seekTo(mVideoPosition);
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        if (mExoPlayer != null) {
            mVideoPosition = mExoPlayer.getCurrentPosition();
            releasePlayer();
        }
    }
    /**
     * Release the player when the activity is destroyed.
     */
    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
        if (urlIsAvailble) {
            mMediaSession.setActive(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!videoURL.equals("")) {
            initializePlayer(Uri.parse(videoURL));
        }
    }

    private void initializeMediaSession() {
        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(getContext(), TAG);

        // Enable callbacks from MediaButtons and TransportControls.
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());
        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(new MySessionCallback());
        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);
    }
    /**
     * Release ExoPlayer.
     */
    public void releasePlayer() {
        if(!videoURL.equals("")) {
            if(mExoPlayer!= null) {
                mExoPlayer.stop();
                mExoPlayer.release();
                mExoPlayer = null;
            }
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

        switch (error.type) {
            case ExoPlaybackException.TYPE_SOURCE:
                Log.e(LOG_TAG, "TYPE_SOURCE: " + error.getSourceException().getMessage());
                break;

            case ExoPlaybackException.TYPE_RENDERER:
                Log.e(LOG_TAG, "TYPE_RENDERER: " + error.getRendererException().getMessage());
                break;

            case ExoPlaybackException.TYPE_UNEXPECTED:
                Log.e(LOG_TAG, "TYPE_UNEXPECTED: " + error.getUnexpectedException().getMessage());
                break;
        }
    }

    @Override
    public void onPositionDiscontinuity() {
    }

    //meke the mPlayerView visible if the urlIsavailable
    public void videoIsVisible(boolean v){
        if (v){
            mPlayerView.setVisibility(View.VISIBLE);
            dummyPic.setVisibility(View.GONE);
        }else{
            mPlayerView.setVisibility(View.INVISIBLE);
            dummyPic.setVisibility(View.VISIBLE);
        }
    }
    /**
     * Inner class to handle the media session callback Where all external clients control the player.
     */
    private class MySessionCallback extends MediaSessionCompat.Callback {

        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekToDefaultPosition();
        }
    }





}
