package fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import aa.os_info_sdk.MainActivity;
import aa.os_info_sdk.R;

/**
 * Created by ancun on 2017/12/15.
 */

public class FragmentWebView extends Fragment {
    //用于关联主activity
    private MainActivity relateActivity;
    //当前视图
    private View view;
    private WebView webView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //取得主activity对象
        relateActivity = (MainActivity) getActivity();
        //获取当前视图对象
        view = inflater.inflate(R.layout.fragment_web_view, container, false);


        //web浏览器
        webView = (WebView)view.findViewById(R.id.mywebview);
        webView.getSettings().setJavaScriptEnabled(true);

        WebSettings webSettings = webView.getSettings();
        webSettings.setSupportZoom(true); //设置可以支持缩放
        webSettings.setBuiltInZoomControls(true); //设置出现缩放工具
        webSettings.setUseWideViewPort(true);//设置可在大视野范围内上下左右拖动，并且可以任意比例缩放
        webSettings.setLoadWithOverviewMode(true);//设置默认加载的可视范围是大视野范围
        webSettings.setDomStorageEnabled(true);//设置缓存的方式

        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);// 自适应屏幕



        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String
                    url) {
                view.loadUrl(url); // 根据传入的参数再去加载新的网页
                return true; // 表示当前WebView可以处理打开新网页的请求，不用借助
            }
        });
        webView.loadUrl("http://10.14.19.32:9001/frameset");


        return view;
    }
}
