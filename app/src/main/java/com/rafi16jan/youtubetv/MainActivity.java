package com.rafi16jan.youtubetv;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.app.UiModeManager;


public class MainActivity extends Activity {

    //Define URL
    private static final String URL = "https://www.youtube.com/tv";
    //Define WebView
    private WebView webView;

    public Bitmap bitmap = Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UiModeManager uiModeManager = (UiModeManager) getSystemService(UI_MODE_SERVICE);
        if (uiModeManager.getCurrentModeType() != Configuration.UI_MODE_TYPE_TELEVISION) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                final WindowInsetsController insetsController = getWindow().getInsetsController();
                if (insetsController != null) {
                    insetsController.hide(WindowInsets.Type.statusBars());
                }
            } else {
                getWindow().setFlags(
                        WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN
                );
            }
        }
        webView = (WebView)findViewById(R.id.webView); //get webView
        WebSettings webSettings = webView.getSettings();// initiate webView settings
        webSettings.setJavaScriptEnabled(true); //allow webView perform javascript
        webSettings.setDomStorageEnabled(true);
        webSettings.setUserAgentString("Mozilla/5.0 (SMART-TV; LINUX; Tizen 7.0) AppleWebKit/537.36 (KHTML, like Gecko) 94.0.4606.31/7.0 TV Safari/537.36");
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public Bitmap getDefaultVideoPoster() {
                return bitmap;
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url){
                webView.evaluateJavascript("var touchstartX = 0;\n" +
                        "var touchstartY = 0;\n" +
                        "var touchendX = 0;\n" +
                        "var touchendY = 0;\n" +
                        "var throttle = false;\n" +
                        "\n" +
                        "var gesuredZone = document;\n" +
                        "\n" +
                        "gesuredZone.addEventListener('touchstart', function(event) {\n" +
                        "    console.log(event);\n" +
                        "    console.log(touchstartX = event.changedTouches[0].screenX);\n" +
                        "    console.log(touchstartY = event.changedTouches[0].screenY);\n" +
                        "}, false);\n" +
                        "\n" +
                        "gesuredZone.addEventListener('touchend', function(event) {\n" +
                        "    console.log(event);\n" +
                        "    console.log(touchendX = event.changedTouches[0].screenX);\n" +
                        "    console.log(touchendY = event.changedTouches[0].screenY);\n" +
                        "    handleGesure();\n" +
                        "}, false); \n" +
                        "\n" +
                        "function handleGesure() {\n" +
                        "    if (throttle) return setTimeout(() => (throttle = false), 0);\n" +
                        "    var swiped = 'swiped: ';\n" +
                        "    if ((touchendX - touchstartX) <= -130) {\n" +
                        "        console.log(swiped + 'left!');\n" +
                        "        document.dispatchEvent(new KeyboardEvent('keydown', {keyCode: 39}));\n" +
                        "        document.dispatchEvent(new KeyboardEvent('keyup', {keyCode: 39}))\n" +
                        "    }\n" +
                        "    else if ((touchendX - touchstartX) >= 130) {\n" +
                        "        console.log(swiped + 'right!');\n" +
                        "        document.dispatchEvent(new KeyboardEvent('keydown', {keyCode: 37}));\n" +
                        "        document.dispatchEvent(new KeyboardEvent('keyup', {keyCode: 37}))\n" +
                        "    }\n" +
                        "    else if ((touchendY - touchstartY) <= -130) {\n" +
                        "        console.log(swiped + 'down!');\n" +
                        "        document.dispatchEvent(new KeyboardEvent('keydown', {keyCode: 40}));\n" +
                        "        document.dispatchEvent(new KeyboardEvent('keyup', {keyCode: 40}))\n" +
                        "    }\n" +
                        "    else if ((touchendY - touchstartY) >= 130) {\n" +
                        "        console.log(swiped + 'up!');\n" +
                        "        document.dispatchEvent(new KeyboardEvent('keydown', {keyCode: 38}));\n" +
                        "        document.dispatchEvent(new KeyboardEvent('keyup', {keyCode: 38}))\n" +
                        "    }\n" +
                        "    throttle = true;\n" +
                        "}", null);
            }
        });
        webView.setBackgroundColor(Color.parseColor("#282828"));
        webView.loadUrl(URL); //load URL
    }

    @Override
    public void onBackPressed() {
        webView.evaluateJavascript("[document.dispatchEvent(new KeyboardEvent('keydown', {keyCode: 27})), document.dispatchEvent(new KeyboardEvent('keyup', {keyCode: 27}))]", null);
    }
}