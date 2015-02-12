package net.touchabillion.contenttools.Api;

/**
 * Created by daniil on 2/12/15.
 */
public class JSONParser {

    public static final String TAG = "JSONParser";

    private static JSONParser instance;

    public static JSONParser getInstance() {
        if (instance == null) {
            instance = new JSONParser();
        }
        return instance;
    }

    public interface Fields{
        public static final String NAME = "name";
    }
}
