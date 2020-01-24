package com.junior.test.jokes.fragments;


import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.junior.test.jokes.R;

public class WebFragment extends Fragment {

    private ProgressBar mLoadingProgressBar;
    private WebView mWebView;

    public WebFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_web, container, false);

        if (savedInstanceState != null) {
            mWebView.restoreState(savedInstanceState);}

            mWebView = view.findViewById(R.id.link_view);

            mLoadingProgressBar = view.findViewById(R.id.link_progress);

            initWebView();
            webViewGoBack();

            return view;
        }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mWebView.saveState(outState);
    }

    private void webViewGoBack(){

        mWebView.setOnKeyListener((v, keyCode, event) -> {

            if(keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()){
                mWebView.goBack();
                return true;
            }
            return false;
        });
    }

    private void initWebView(){

        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setDisplayZoomControls(false);
        mWebView.setWebViewClient(new WebViewClient());

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                mLoadingProgressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    mLoadingProgressBar.setVisibility(View.GONE);
                } else {
                    mLoadingProgressBar.setVisibility(View.VISIBLE);
                }
            }
        });

        mWebView.loadUrl("http://www.icndb.com/api/");
    }
}
