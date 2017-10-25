package com.example.android.bakingtime;

import android.graphics.Movie;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.id;

/**
 * Created by Amir on 10/10/2017.
 */

class QueryUtils {
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();
    private QueryUtils() {
    }
    /**
     * Query the TMDB dataset and return a list of {@link Movie} objects.
     */
    static List<Recepie> fetchRecepieData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);
        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        // Extract relevant fields from the JSON response and create a list of {@link Movie}s
        return extractFeatureFromJson(jsonResponse);
    }
    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(1000 /* milliseconds */);
            urlConnection.setConnectTimeout(1500 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the movie JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link Recepie} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<Recepie> extractFeatureFromJson(String recepieJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(recepieJSON)) {
            return null;
        }
        // Create an empty ArrayList that we can start adding recepies to
        List<Recepie> recepies = new ArrayList<>();
        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // Create a JSONObject from the JSON response string
            //JSONObject baseJsonResponse = new JSONObject(recepieJSON);

            // Extract the JSONArray From the respond,
            // which represents a list of Result (or movies).
            JSONArray recepieArray = new JSONArray(recepieJSON);

            // For each recepie in the recepieArray, create an {@link Recepie} object
            for (int i = 0; i < recepieArray.length(); i++) {

                // Get a single recepie at position i within the list of recepies
                JSONObject currentRecepie = recepieArray.getJSONObject(i);

                //Extract the value for the key called "id"
                int recepieId = currentRecepie.getInt("id");

                //Extract the value for the key called "name"
                String recepieName = currentRecepie.getString("name");

                //Extract the JSONArray for the key called "ingredients"
                JSONArray ingredientsArray = currentRecepie.getJSONArray("ingredients");

                ArrayList<Recepie.Ingredient> ingredients = new ArrayList<>();
                //For each ingredient in the ingredientsArray, create an (@linl Ingredients) object
                for(int j = 0; j < ingredientsArray.length(); j++){
                    //Get the j th ingredient and add it to the Ingredient ArrayList
                    JSONObject currentIngredient = ingredientsArray.getJSONObject(j);
                    //Extract the value for the key called "quantity"
                    double quantity = currentIngredient.getDouble("quantity");
                    //Extract the value for the key called "measure"
                    String measure = currentIngredient.getString("measure");
                    //Extract the value for the key called "ingredient"
                    String singleIngredient = currentIngredient.getString("ingredient");
                    Recepie.Ingredient ingredient = new Recepie.Ingredient(quantity, measure, singleIngredient);
                    ingredients.add(ingredient);
                }
                ArrayList<Recepie.Steps> steps = new ArrayList<>();
                //Extract the JSONArray for the key called "steps"
                JSONArray stepsArray = currentRecepie.getJSONArray("steps");
                //For each step in the stepsArray, create an (@link Steps) object
                for(int k= 0; k < stepsArray.length(); k++){
                    //Get the k th step and add it to the steps ArrayList
                    JSONObject currentStep =  stepsArray.getJSONObject(k);
                    //Extract the value for the key called "id"
                    int stepId = currentStep.getInt("id");
                    //Extract the value for the key called "shortDescription"
                    String shortDescription = currentStep.getString("shortDescription");
                    //Extract the value for the key called "description"
                    String description =  currentStep.getString("description");
                    //Extract the value for the key called "videoURL"
                    String videoURL = currentStep.getString("videoURL");
                    //Extract the value for the key called "thumbnailURL"
                    String thumbnailURL = currentStep.getString("thumbnailURL");
                    Recepie.Steps step = new Recepie.Steps(stepId, shortDescription, description, videoURL,thumbnailURL);
                    steps.add(step);
                }
                //Extract the value for the key called "servings"
                int servings = currentRecepie.getInt("servings");
                //Extract the valu for the key called "image"
                String image =  currentRecepie.getString("image");

                Recepie recepie = new Recepie(recepieId, recepieName, ingredients, steps, servings,image);
                recepies.add(recepie);

            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the movie JSON results", e);
        }
        // Return the list of movies
        return recepies;
    }


    public static URL createUrl(String  stringUrl){
        URL url = null;
        try{
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }


}
