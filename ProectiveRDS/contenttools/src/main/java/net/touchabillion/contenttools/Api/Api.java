package net.touchabillion.contenttools.Api;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import net.touchabillion.contenttools.Constants.App;
import net.touchabillion.contenttools.PreferenceManager;
import net.touchabillion.contenttools.Tools;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by daniil on 2/12/15.
 */
public class Api {

    public static final String TAG = "Api";

    private static RequestQueue requestQueue;
    private Context context;

    public Api(Context context) {
        this.context = context;
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
    }

    public interface PathUri{

        public static final String MAIN = "main";

        public static final String SECURITY = "security";

        public static final String DEPT = "dept";
        public static final String DEPARTMENT = "department";
        public static final String ACTIVES = "actives";
        public static final String RESPONSE = "response";
        public static final String INBOX = "inbox";
        public static final String j_LOGIN = "j_spring_security_check";
        public static final String LOGIN = "login";
        public static final String LANDING = "landing";
        public static final String COOKIES = "cookies";
        public static final String USER = "user";
    }
    public interface Fields {
        public static final String USERNAME = "j_username";
        public static final String PASSWORD = "j_password";
        public static final String SUBMIT = "submit";
        public static final String COOKIE = "Cookie";
    }
    public Uri buildUrlRequest(String path[], String params[][]) {
        Uri.Builder builder = new Uri.Builder();

        builder.scheme(App.Api.SHEME)
               .authority(App.Api.AUTHORITY);

        if (path != null && path.length > 0) {
            for (String itemPath : path) {
                builder.appendPath(itemPath);
            }
        }

        if (params != null && path.length > 0) {
            for (String param[] : params) {
                if (param != null && param.length > 1) {
                    builder.appendQueryParameter(param[0], param[1]);
                }
            }
        }

        Uri uri = builder.build();
        Log.i(TAG, "REQUEST ## " + uri.toString());

        return uri;
    }
    public void requestLogin(final HashMap<String, String> userData, Response.Listener<String> successListener, Response.ErrorListener errorListener) {
        Uri url = buildUrlRequest(new String[]{
                PathUri.j_LOGIN
        }, null);

        StringRequest request = new StringRequest(Request.Method.POST, url.toString(), successListener, errorListener){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return userData;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                if (response != null && response.headers != null) {
                    Tools.saveCookies(context, response.headers);
                }
                return super.parseNetworkResponse(response);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                Log.d(TAG, "" + volleyError.getLocalizedMessage());
                if (volleyError.networkResponse.statusCode == 401) {
                    parseNetworkResponse(volleyError.networkResponse);
                    return null;
                }
                return super.parseNetworkError(volleyError);
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(3000, 3, 0));

        requestQueue.add(request);
    }

    public void requestLanding(Response.Listener<String> successListener, Response.ErrorListener errorListener) {
        Uri url = buildUrlRequest(new String[]{
                PathUri.MAIN,
                PathUri.SECURITY,
                PathUri.LANDING
        }, null);

        StringRequest request = new StringRequest(Request.Method.GET, url.toString(), successListener, errorListener){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put(Fields.COOKIE, PreferenceManager.getInstance(context).getCookies());
                return headers;
            }

            /*@Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                if (response != null && response.headers != null) {
                    Tools.saveCookies(context, response.headers);
                }
                return super.parseNetworkResponse(response);
            }*/

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                Log.d(TAG, "" + volleyError.getLocalizedMessage());
                if (volleyError.networkResponse.statusCode == 401) {
                    parseNetworkResponse(volleyError.networkResponse);
                    return null;
                }
                return super.parseNetworkError(volleyError);
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(3000, 3, 0));

        requestQueue.add(request);
    }

    public void requestCookies(Response.Listener<String> successListener, Response.ErrorListener errorListener) {
        Uri url = buildUrlRequest(new String[]{
                PathUri.MAIN,
                PathUri.SECURITY,
                PathUri.COOKIES
        }, null);

        StringRequest request = new StringRequest(Request.Method.GET, url.toString(), successListener, errorListener){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put(Fields.COOKIE, PreferenceManager.getInstance(context).getCookies());
                return headers;
            }

            /*@Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                if (response != null && response.headers != null) {
                    Tools.saveCookies(context, response.headers);
                }
                return super.parseNetworkResponse(response);
            }*/

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                Log.d(TAG, "" + volleyError.getLocalizedMessage());
                if (volleyError.networkResponse.statusCode == 401) {
                    parseNetworkResponse(volleyError.networkResponse);
                    return null;
                }
                return super.parseNetworkError(volleyError);
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(3000, 3, 0));

        requestQueue.add(request);
    }

    public void requestUser(Response.Listener<String> successListener, Response.ErrorListener errorListener) {
        Uri url = buildUrlRequest(new String[]{
                PathUri.MAIN,
                PathUri.SECURITY,
                PathUri.USER,
                ""
        }, null);

        StringRequest request = new StringRequest(Request.Method.GET, url.toString(), successListener, errorListener){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put(Fields.COOKIE, PreferenceManager.getInstance(context).getCookies());
                return headers;
            }

            /*@Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                if (response != null && response.headers != null) {
                    Tools.saveCookies(context, response.headers);
                }
                return super.parseNetworkResponse(response);
            }*/

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                Log.d(TAG, "" + volleyError.getLocalizedMessage());
                if (volleyError.networkResponse.statusCode == 401) {
                    parseNetworkResponse(volleyError.networkResponse);
                    return null;
                }
                return super.parseNetworkError(volleyError);
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(3000, 3, 0));

        requestQueue.add(request);
    }
}
