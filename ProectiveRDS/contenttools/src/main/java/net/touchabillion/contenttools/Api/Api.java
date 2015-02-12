package net.touchabillion.contenttools.Api;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import net.touchabillion.contenttools.Constants.App;
import net.touchabillion.contenttools.Models.LoginData;
import net.touchabillion.contenttools.Tools;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by daniil on 2/12/15.
 */
public class Api {

    public static final String TAG = "Api";

    private RequestQueue requestQueue;
    private Context context;

    public Api(Context context) {
        requestQueue = Volley.newRequestQueue(context);
        this.context = context;
    }

    public interface PathUri{

        public static final String MAIN = "main";
        public static final String SECURITY = "security";
        public static final String DEPT = "dept";
        public static final String DEPARTMENT = "department";
        public static final String ACTIVES = "actives";
        public static final String RESPONSE = "response";
        public static final String INBOX = "inbox";
        public static final String LOGIN = "j_spring_security_check";
    }
    public interface Params{

        public static final String USERNAME = "j_username";
        public static final String PASSWORD = "j_password";
        public static final String SUBMIT = "submit";
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

    public void requestLogin(final LoginData loginData, Response.Listener<String> successListener, Response.ErrorListener errorListener) {
        Uri url = buildUrlRequest(new String[]{
                PathUri.LOGIN
        }, null);

        StringRequest request = new StringRequest(Request.Method.POST, url.toString(), successListener, errorListener){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                if (loginData != null) {
                    if (loginData.password != null) {
                        params.put(Params.PASSWORD, loginData.password);
                    }
                    if (loginData.email != null) {
                        params.put(Params.USERNAME, loginData.email);
                    }
                    params.put(Params.SUBMIT, Uri.encode("Логин"));
                }
                Log.d(TAG, "PARAMS: " + params);
                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                Tools.saveCookies(context, response.headers);
                return super.parseNetworkResponse(response);
            }
        };

        requestQueue.add(request);
    }
}
