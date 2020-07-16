package com.androcrush.wallpaper;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.File;
import java.util.Date;

public class MainActivity extends AppCompatActivity{
    private float x1, x2;
    static final int MIN_DISTANCE = 150;
    private WebView webView = null;
    ProgressDialog pd;
    private Vibrator myVib;
    String address;
    String downloadUrl;
    GestureDetector gd;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        pd=new ProgressDialog(this);
        pd.setTitle("App is loading");
        pd.setMessage("Please Wait...");
        pd.setCancelable(false);
        pd.show();
        myVib = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        address = "https://source.unsplash.com/" + getScreenWidth() + "x" + getScreenHeight() + "/?wallpaper,backgrounds,nature,images,landscapes,inspirational,epic,amazing,plain,nice,good,best,hd,samsung,sports,event,people,city,nightview,pattern,phone,iphone,travel,screen,feeling,desktop,design,cute,company,colors,cars,arts,animals,android";
        webView = (WebView) findViewById(R.id.webview);
        webView.setOnTouchListener( new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                String loadurl=null;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x1 = event.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        x2 = event.getX();
                        float deltaX = x2 - x1;
                            if (deltaX < 0) {
                                loadurl=address;
                                if (loadurl != "") {
                                    myVib.vibrate(100);
                                    WebAction(loadurl);
                                }
                                loadurl=null;
                                break;
                            }else if(deltaX >0){
                                loadurl=address;
                            if (loadurl != "") {
                                myVib.vibrate(100);
                                WebAction(loadurl);
                            }
                            loadurl=null;
                            break;
                        }
                }
                return false;
            }
        });


        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final WebView.HitTestResult webViewHitTestResult = webView.getHitTestResult();
                String DownloadImageURL = webViewHitTestResult.getExtra();
                if (URLUtil.isValidUrl(DownloadImageURL)) {
                    File direct = new File(Environment.getExternalStorageDirectory()
                            + "/walle");

                    if (!direct.exists()) {
                        direct.mkdirs();
                    }
                    myVib.vibrate(100);
                    DownloadManager.Request mRequest = new DownloadManager.Request(Uri.parse(DownloadImageURL));
                    mRequest.allowScanningByMediaScanner();
                    mRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    mRequest.setDestinationInExternalFilesDir(MainActivity.this, "/walle", new Date().getTime() + ".jpg");
                    DownloadManager mDownloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                    mDownloadManager.enqueue(mRequest);

                    Toast.makeText(MainActivity.this, "Image Downloaded Successfully...", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "Sorry.. Something Went Wrong...", Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });

        if (address != "") {
            WebAction(address);
        }
    }

    public String getFileName() {
        String filenameWithoutExtension = "";
        filenameWithoutExtension = (System.currentTimeMillis()
                + ".jpg").toString();
        return filenameWithoutExtension;
    }


    @SuppressLint("SetJavaScriptEnabled")
    public void WebAction(String address) {
        webView.clearCache(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(false);
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        if (Build.VERSION.SDK_INT >= 19) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setBlockNetworkImage(false);
        webView.setInitialScale(1);
        webView.reload();
        webView.loadUrl(address);
        //webView.loadDataWithBaseURL(address,null,"text/html",null,null);
        webView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                webView.loadUrl("file:///android_assets/error.html");
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                pd.hide();
                pd = new ProgressDialog(MainActivity.this);
                downloadUrl = url;
                pd.setCancelable(false);
                pd.setMessage("Please wait ...");
                pd.show();

            }


            public void onPageFinished(WebView view, String url) {
                pd.hide();
            }

        });
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Context context;

    }


}