/*
package com.ciplafoundation.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.ciplafoundation.R;
import com.ciplafoundation.model.UserClass;
import com.ciplafoundation.utility.Prefs;
import com.ciplafoundation.utility.Util;

public class WebViewActivity extends AppCompatActivity {

    private Prefs prefs;
    //private Button button;
    public static long back_pressed;
    private WebView webView;
    private Context mContext;
    ProgressDialog progressDialog;
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_web_view_layout);
        mContext = WebViewActivity.this;
        prefs = new Prefs(mContext);
        if (Util.checkConnectivity(mContext))
        {
            UserClass user = Util.fetchUserClass(mContext);
            String user_id = user.getUserId();
            //Get webview
            webView = (WebView) findViewById(R.id.wv_myWebView);
            String url="https://track.ciplafoundation.org/app/dashboard/view/get?USR_USER_ID="+user_id+"&DIVISION_ID="+prefs.getDivisionId();
           // System.out.println("url==="+url);
            webView.loadUrl(url);
            // Javascript inabled on webview
            webView.getSettings().setJavaScriptEnabled(true);

            new Handler().post(new Runnable() {

                @Override
                public void run() {
                    progressDialog= new ProgressDialog(WebViewActivity.this);
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();
                }
            }); // Other webview options
            webView.getSettings().setUseWideViewPort(true);
            webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
            webView.setScrollbarFadingEnabled(false);
            webView.setWebChromeClient(new WebChromeClient());
            webView.setWebViewClient(new MyWebViewClient());
        }

        else
            Util.showMessageWithOk(mContext, getString(R.string.no_internet));

    }


    private class MyWebViewClient extends WebViewClient {


        // when error occurred in case of network not present or something
        @SuppressWarnings("deprecation")
        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {

            super.onReceivedError(view, errorCode, description, failingUrl);



        }
        // to show override the url on evidence click
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view,  url);
        }

        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error){
            handler.proceed();
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            progressDialog.dismiss();
            //remove progress and add comment list
            super.onPageFinished(view, url);

            //System.out.println("newProgress ");


        }
    }
    @Override
    public void onBackPressed() {

        if (isTaskRoot()) {
            if (back_pressed + 2000 > System.currentTimeMillis())
                super.onBackPressed();
            else
                Toast.makeText(mContext, "Press once again to exit!", Toast.LENGTH_SHORT).show();

            back_pressed = System.currentTimeMillis();
        } else {
            super.onBackPressed();
        }
    }

   */
/* public class WebClientClass extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if(newProgress>99)
            {
                progressDialog.dismiss();

            }
        }
    }
*//*

}




*/
