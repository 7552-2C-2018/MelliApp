package com.a7552_2c_2018.melliapp;


import android.content.Context;
//import android.graphics.Bitmap;
//import android.support.v4.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
//import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;



public class SingletonConnect {
    private static SingletonConnect mAppSingletonInstance;
    private RequestQueue mRequestQueue;
    // --Commented out by Inspection (27/08/18 16:49):private final ImageLoader mImageLoader;
    private static Context mContext;

    private SingletonConnect(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();

        /*mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });*/
    }

    public static synchronized SingletonConnect getInstance(Context context) {
        if (mAppSingletonInstance == null) {
            mAppSingletonInstance = new SingletonConnect(context);
        }
        return mAppSingletonInstance;
    }

    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(tag);
        getRequestQueue().add(req);
    }

// --Commented out by Inspection START (27/08/18 16:45):
//    public ImageLoader getImageLoader() {
//        return mImageLoader;
//    }
// --Commented out by Inspection STOP (27/08/18 16:45)

// --Commented out by Inspection START (27/08/18 16:45):
//    public void cancelPendingRequests(Object tag) {
//        if (mRequestQueue != null) {
//            mRequestQueue.cancelAll(tag);
//        }
//    }
// --Commented out by Inspection STOP (27/08/18 16:45)
}