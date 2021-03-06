package com.example.android.bakingtime;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.SimpleExoPlayer;

import java.util.ArrayList;

public class StepDescriptionActivity extends AppCompatActivity implements DescriptionFragment.OnChangeStepClickListener {

    private static final String LOG_TAG = StepDescriptionActivity.class.getSimpleName();
    ArrayList<Recepie.Steps> steps;
    MediaPlayerFragment mediaPlayerFragment;
    DescriptionFragment descriptionFragment;
    FragmentManager fm;
    boolean displayIsInPortrait;
    FrameLayout descriptionContainer;
    View seperator;
    int currentStepNumber = 0;
    private static final String TAG_RETAINED_FRAGMENT = "RetainedFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_description);
        Intent intentFromRDActivity = getIntent();
        steps = (ArrayList< Recepie.Steps>) intentFromRDActivity.getSerializableExtra("RecepieSteps");
        int currentStep = intentFromRDActivity.getIntExtra("StepNumber", 0);
        fm = getSupportFragmentManager();
        int rotation = getRotation();
        if(rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) {
            displayIsInPortrait = true;
            descriptionFragment = new DescriptionFragment();
            fm.beginTransaction().add(R.id.descirption_container, descriptionFragment)
                    .commit();
        }else{
            displayIsInPortrait = false;
            if(descriptionFragment!=null){
                fm.beginTransaction().remove(descriptionFragment).commit();
            }
        }
        mediaPlayerFragment = (MediaPlayerFragment)fm.findFragmentByTag(TAG_RETAINED_FRAGMENT);
        // create the fragment and data the first time
        if (mediaPlayerFragment == null) {
            // add the fragment
            mediaPlayerFragment = new MediaPlayerFragment();
            fm.beginTransaction().add(R.id.media_container, mediaPlayerFragment,TAG_RETAINED_FRAGMENT)
                    .commit();
        }
        descriptionContainer = (FrameLayout)findViewById(R.id.descirption_container);
        seperator = (View) findViewById(R.id.v_seperator);
        Log.e(LOG_TAG," step Number is : " + currentStep + " &&&&& Video url is :" + steps.get(currentStep).getVideoURL());
    }
    //this method called when one of the two buttons in the DescriptionFragment clicked
    @Override
    public void onStepChanged(int newStep) {
        Log.i(LOG_TAG, "THE NEW STEP NUMBER IS : " + newStep + "///*************");

        String oldVideoUrl = mediaPlayerFragment.videoURL;
        String newVideoURL = steps.get(newStep).getVideoURL();
        mediaPlayerFragment.stepNumber = newStep;
        if (  newVideoURL.equals("") ) {
            if(!(oldVideoUrl).equals("")) {
                mediaPlayerFragment.mVideoPosition = C.TIME_UNSET;
                mediaPlayerFragment.releasePlayer();
                mediaPlayerFragment.videoIsVisible(false);
            }
            Log.e(LOG_TAG, " step Number is : " + newStep + " No video URL Dummy picture is on:");
        }else {
            if(!oldVideoUrl.equals("")) {
                mediaPlayerFragment.mVideoPosition = C.TIME_UNSET;
                mediaPlayerFragment.releasePlayer();
            }
            mediaPlayerFragment.mSavedInsatanceState = null;
            mediaPlayerFragment.initializePlayer(Uri.parse(newVideoURL));
            Log.i(LOG_TAG, " step Number is : " + newStep + " &&&&& Video url is :" + newVideoURL);
            fm.beginTransaction().replace(R.id.media_container, mediaPlayerFragment).commit();
        }
        mediaPlayerFragment.videoURL = newVideoURL;
    }

    private int getRotation(){
        Display display = ((WindowManager) this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        return display.getRotation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(isFinishing()){
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().remove(mediaPlayerFragment).commit();
            if (descriptionFragment!=null){
                fm.beginTransaction().remove(descriptionFragment).commit();
            }
        }
    }
}
