package com.salestracker.sales.application;

import android.content.BroadcastReceiver;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.salestracker.sales.cache.LruBitmapCache;

/**
 * Created by Tarun on 19/5/16.
 */
public class AppController extends android.support.multidex.MultiDexApplication {
    final public static String BASE_URL = "https://api.salestracker.com";

    public static final String TAG = AppController.class
            .getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    private static AppController mInstance;

    /*private User mUserInfo;*/

    /*synchronized public void updateUserInfoObject() {

        ObjectMapper mapper = new ObjectMapper();
        try
        {
            JsonNode node = mapper.readTree(PreferenceUtils.getInstance(this).getUserData());
            TypeReference<User> typeRef = new TypeReference<User>(){};
            mUserInfo = mapper.readValue(node.traverse(), typeRef);
        } catch (JsonGenerationException e)
        {
            e.printStackTrace();
        } catch (JsonMappingException e)
        {
            e.printStackTrace();
            Toast.makeText(AppController.getInstance(), "Something went wrong!", Toast.LENGTH_LONG).show();
        } catch (IOException e)
        {
            e.printStackTrace();
            Toast.makeText(AppController.getInstance(), "Something went wrong!", Toast.LENGTH_LONG).show();
        }
    }

    synchronized public User getUserInfo() {
        if (mUserInfo == null) {
            updateUserInfoObject();
        }
        return mUserInfo;
    }*/

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        /*updateUserInfoObject();*/
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext()/*, new OkHttpStack()*/);
        }
        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
