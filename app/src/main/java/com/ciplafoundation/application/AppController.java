package com.ciplafoundation.application;

import android.content.Context;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.ciplafoundation.utility.LruBitmapCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class AppController extends MultiDexApplication {

    private com.nostra13.universalimageloader.core.ImageLoader mUniversalImageuploader;
    public static final String TAG = AppController.class.getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mUniversalImageuploader = globalUniversalImageLoader(this);
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue, new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public com.nostra13.universalimageloader.core.ImageLoader getUniversalImageLoader() {

        if (mUniversalImageuploader == null) {
            mUniversalImageuploader = globalUniversalImageLoader(this);
        }
        return this.mUniversalImageuploader;
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
    /*
	 * display online image in image view using universal image loader.
	 */
    public void displayUniversalImg(String imgUrl,ImageView img_view,int defaultImgRes)
    {

        mUniversalImageuploader.displayImage(imgUrl, img_view, SetDisplaImageOption(defaultImgRes));
    }
    private  com.nostra13.universalimageloader.core.ImageLoader globalUniversalImageLoader(Context context){

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        // Initialize ImageLoader with configuration.
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(config);

        return com.nostra13.universalimageloader.core.ImageLoader.getInstance();

    }
    private  DisplayImageOptions SetDisplaImageOption(int defaultImg){

        DisplayImageOptions  options = new DisplayImageOptions.Builder()

                    .showImageForEmptyUri(defaultImg)
                    .showImageOnFail(defaultImg)
                    .cacheInMemory(true)
                    .build();


        return options;

    }

}
