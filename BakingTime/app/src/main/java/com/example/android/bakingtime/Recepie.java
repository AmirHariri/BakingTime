package com.example.android.bakingtime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amir on 10/10/2017.
 */

public class Recepie implements Serializable {
    int mId;
    String mName;
    ArrayList<Ingredient> mIngredient;
    ArrayList<Steps> mSteps;
    int mServings;
    String mImage;



    Recepie(int id, String  name, ArrayList<Ingredient> ingredient, ArrayList<Steps> steps, int servings, String image){
        mId = id;
        mName = name;
        mIngredient = ingredient;
        mSteps = steps;
        mServings = servings;
        mImage = image;

    }
    public int getId() {
        return mId;
    }
    public String getName() {
        return mName;
    }
    public ArrayList<Ingredient> getIngredient() {
        return mIngredient;
    }
    public ArrayList<Steps> getSteps() {
        return mSteps;
    }
    public int getServings() {
        return mServings;
    }
    public String getImage() {
        return mImage;
    }

    public static class Ingredient implements Serializable {
        double mQuantity;
        String mMeasure;
        String mSingleIngredient;

        public Ingredient(double quantity, String measure, String singleIngredient){
            mQuantity = quantity;
            mMeasure = measure;
            mSingleIngredient = singleIngredient;
        }
        public double getQuantity() {
            return mQuantity;
        }
        public String getMeasure() {
            return mMeasure;
        }
        public String getSingleIngredient() {
            return mSingleIngredient;
        }
    }

    public static class Steps implements Serializable {
        int mStepId;
        String mShortDescription;
        String mDescription;
        String mVideoURL;
        String mThumbnaleURL;



        Steps(int stepId, String shortDescription, String description, String videoURL, String thumbnailURL){
            mStepId = stepId;
            mShortDescription = shortDescription;
            mDescription = description;
            mVideoURL = videoURL;
            mThumbnaleURL = thumbnailURL;
        }
        public int getStepId() {
            return mStepId;
        }
        public String getShortDescription() {
            return mShortDescription;
        }
        public String getDescription() {
            return mDescription;
        }
        public String getVideoURL() {
            return mVideoURL;
        }
        public String getThumbnaileURL() {
            return mThumbnaleURL;
        }
    }

}
