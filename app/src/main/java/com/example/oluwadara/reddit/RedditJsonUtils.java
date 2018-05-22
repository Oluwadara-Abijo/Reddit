package com.example.oluwadara.reddit;


/*
 * Utility functions to handle Reddit JSON data
 */

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

public final class RedditJsonUtils {

    public static String[] getGameStringsFromJson(Context context, String gameJsonStr)
            throws JSONException {

        /* Game information. Each game info is an element of the "children" array */
        final String OWM_CHILDREN = "children";

        /* All game data are children of the "data" object */
        final String OWM_DATA = "data";

        final String TITLE = "title";

        final String SCORE = "score";

        final String SUBREDDIT = "subreddit";

        final String URL = "url";

        final String OWM_MESSAGE_CODE = "cod";

        String[] parsedGameData;

        JSONObject gameJson = new JSONObject(gameJsonStr);

        if (gameJson.has(OWM_MESSAGE_CODE)) {
            int errorCode = gameJson.getInt(OWM_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }

        JSONObject dataObject = gameJson.getJSONObject(OWM_DATA);

        JSONArray childrenArray = dataObject.getJSONArray(OWM_CHILDREN);

        parsedGameData = new String[childrenArray.length()];

        for (int i = 0; i < childrenArray.length(); i++) {

            /* Get the JSON object representing each game */
            JSONObject eachGame = childrenArray.getJSONObject(i);
            JSONObject eachGameData = eachGame.getJSONObject(OWM_DATA);

            /* These are the values that will be collected */
            String title = eachGameData.getString(TITLE);
            int score = eachGameData.getInt(SCORE);
            String subreddit = eachGameData.getString(SUBREDDIT);
            String url = eachGameData.getString(URL);

            parsedGameData[i] = title + "-" + score + "-" + subreddit + "-" + url;

        }

        return parsedGameData;

    }

}
