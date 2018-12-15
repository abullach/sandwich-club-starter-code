package com.udacity.sandwichclub.utils;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility functions to handle Sandwich JSON data.
 */
public class JsonUtils {

    /**
     * This method parses JSON representing a single sandwich and converts it into Sandwich model object.
     * <br>
     * @param json String with JSON from a single sandwich object
     * @return Sandwich model object
     */
    public static Sandwich parseSandwichJson(String json) {

        final String SANDWICH_NAME_OBJECT = "name";

        /* All names are children of the "name' object */
        final String SANDWICH_NAME_MAIN = "mainName";
        final String SANDWICH_NAME_KNOWN_AS = "alsoKnownAs";

        final String SANDWICH_ORIGIN = "placeOfOrigin";
        final String SANDWICH_DESCRIPTION = "description";
        final String SANDWICH_IMAGE = "image";
        final String SANDWICH_INGREDIENTS = "ingredients";

        Sandwich sandwich = new Sandwich();

        try {
            JSONObject sandwichJson = new JSONObject(json);
            /* Get the JSON object representing the sandwich name */
            JSONObject nameObject = sandwichJson.getJSONObject(SANDWICH_NAME_OBJECT);
            /* Set sandwich object field "mainName" from the JSON representing the sandwich main name */
            sandwich.setMainName(nameObject.getString(SANDWICH_NAME_MAIN));
            // Get the alternative name(s) from the JSONArray representing the sandwich alsoKnownAs array
            JSONArray knownAsArray = nameObject.getJSONArray(SANDWICH_NAME_KNOWN_AS);
            if(knownAsArray.length() != 0) {
                List<String> knownAsList = new ArrayList<>(knownAsArray.length());
                for(int i = 0; i < knownAsArray.length(); i++){
                    // Add name to the list of alternative names
                    knownAsList.add(knownAsArray.getString(i));
                }
                /* Set sandwich object field "alsoKnownAs" from the list of alternative name(s) */
                sandwich.setAlsoKnownAs(knownAsList);
            }
            /* Set sandwich object field "placeOfOrigin" from the JSON representing the sandwich place of origin */
            sandwich.setPlaceOfOrigin(sandwichJson.optString(SANDWICH_ORIGIN));
            /* Set sandwich object field "description" from the JSON representing the sandwich description */
            sandwich.setDescription(sandwichJson.optString(SANDWICH_DESCRIPTION));
            /* Set the image url to sandwich object field "image" from the JSON representing the sandwich image */
            sandwich.setImage(sandwichJson.optString(SANDWICH_IMAGE));
            // Get the ingredient(s) from the JSONArray representing the sandwich ingredients
            JSONArray ingredientsArray = sandwichJson.getJSONArray(SANDWICH_INGREDIENTS);
            if(ingredientsArray.length() !=0) {
                List<String> ingredients = new ArrayList<>(ingredientsArray.length());
                for(int x = 0; x < ingredientsArray.length(); x++){
                    // Add ingredient to the list of ingredients
                    ingredients.add(ingredientsArray.getString(x));
                }
                /* Set sandwich object field "ingredients" from the list of ingredients */
                sandwich.setIngredients(ingredients);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        // Return new sandwich object
        return sandwich;
    }
}
