package net.touchabillion.contenttools.Api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by daniil on 2/12/15.
 */
public class JSONParser {

    public static final String TAG = "JSONParser";

    private static Gson gson;

    public static Gson getGson() {
        if (gson == null) {
            gson = new GsonBuilder().setPrettyPrinting().create();
        }
        return gson;
    }
}
