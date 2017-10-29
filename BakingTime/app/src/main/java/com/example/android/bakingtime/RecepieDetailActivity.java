package com.example.android.bakingtime;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RecepieDetailActivity extends AppCompatActivity implements
        RecepieDetailListFragment.OnStepClickListener
        ,DescriptionFragment.OnChangeStepClickListener        {

    Recepie mRecepie;
    private boolean mTwoPane;
    FragmentManager fragmentManager;
    DescriptionFragment descriptionFragment;
    MediaPlayerFragment mediaPlayerFragment;
    private static final String LOG_TAG = RecepieDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recepie_detail);

        //get the Intent and the selected recepie from MainActivity
        Intent intent = getIntent();
        mRecepie = (Recepie) intent.getSerializableExtra("CurrentRecepie");
        getIntent().putExtra("RecepieData", mRecepie);
        TextView currentName = (TextView) findViewById(R.id.tv_recepie_current);
        currentName.setText(mRecepie.getName());

        TextView currentServings = (TextView) findViewById(R.id.tv_servings);
        String servings = getString(R.string.servings_text) + String.valueOf(mRecepie.getServings());
        currentServings.setText(servings);

        fragmentManager = getSupportFragmentManager();
        RecepieDetailListFragment recepieDetailListFragment = new RecepieDetailListFragment();
        fragmentManager.beginTransaction().add(R.id.list_container, recepieDetailListFragment)
                .commit();
        //Check if it is for two pane or single pane
        if(findViewById(R.id.step_description_linear_layout) != null){
            mTwoPane = true;
            mediaPlayerFragment = new MediaPlayerFragment();
            descriptionFragment = new DescriptionFragment();
            descriptionFragment.singlePane = false;
            mediaPlayerFragment.singlePane = false;
            descriptionFragment.steps = mRecepie.getSteps();
            mediaPlayerFragment.steps = mRecepie.getSteps();
            Log.e(LOG_TAG,"steps are :" +descriptionFragment.steps.get(1).getDescription());

            fragmentManager.beginTransaction().add(R.id.descirption_container, descriptionFragment)
                    .commit();
            fragmentManager.beginTransaction().add(R.id.media_container,mediaPlayerFragment)
                    .commit();
        }else{
            mTwoPane = false;
        }
    }

    @Override
    public void onStepSelected(int position) {
        Toast.makeText(this, "Step Number:" + position + " seleceted",Toast.LENGTH_SHORT).show();

        ArrayList<Recepie.Steps> steps = mRecepie.getSteps();

        if(mTwoPane){
            String currentStepDscrp = steps.get(position).getDescription();
            descriptionFragment.stepNumber = position;
            descriptionFragment.textView.setText(currentStepDscrp);

            String oldVideoUrl = mediaPlayerFragment.videoURL;
            String newVideoURL = steps.get(position).getVideoURL();
            if (  newVideoURL.equals("") ) {
                if(!(oldVideoUrl).equals("")) {
                    mediaPlayerFragment.releasePlayer();
                    mediaPlayerFragment.videoIsVisible(false);
                }
                Log.e(LOG_TAG, " step Number is : " + position + " No video URL Dummy picture is on:");
            }else {
                if(!oldVideoUrl.equals("")) {
                    mediaPlayerFragment.releasePlayer();
                }
                mediaPlayerFragment.initializePlayer(Uri.parse(newVideoURL));
                Log.i(LOG_TAG, " step Number is : " + position + " &&&&& Video url is :" + newVideoURL);

                fragmentManager.beginTransaction().replace(R.id.media_container, mediaPlayerFragment).commit();
            }
            mediaPlayerFragment.videoURL = newVideoURL;
            Log.i(LOG_TAG,"this is working "+ steps.get(position).getVideoURL());
        }else {
            Intent stepIntent = new Intent(this, StepDescriptionActivity.class);
            stepIntent.putExtra("RecepieSteps", steps);
            stepIntent.putExtra("StepNumber", position);
            startActivity(stepIntent);
        }
    }

    @Override
    public void onStepChanged(int newStep) {
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}
