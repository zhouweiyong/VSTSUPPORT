package com.vst.vstsupport.utils.webview;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ZoomButtonsController;

import com.vst.vstsupport.R;
import com.vst.vstsupport.common.TitleBarHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author: zwy
 * @类   说   明:	
 * @version 1.0
 * @创建时间：2015年3月24日 下午5:26:29
 * 
 */
public class WebActivity extends Activity implements OnClickListener{
	private CustomWebView mWebView;
	private ProgressBar pb;
	private RelativeLayout webView_parent;
	private LinearLayout ll_webNavi_root;
	
	private ProgressBar web_src_loadProgress;
	private View button_back,button_forword,button_refresh,button_close;
	
	private String url = "http://www.baidu.com";
	private String flag;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);
		
		Intent intent = getIntent();
		url = intent.getStringExtra("url");
		flag = intent.getStringExtra("flag");

		
		mWebView = (CustomWebView) findViewById(R.id.webview);
		pb = (ProgressBar) findViewById(R.id.progressbar);
		initWebView();
		mWebView.loadUrl(url);
	}
	
	@Override
	public void onBackPressed() {
		if (mWebView.canGoBack()) {
			mWebView.goBack();
		} else {
			super.onBackPressed();
		}
	}
	@Override
	protected void onDestroy() {
		webView_parent.removeView(mWebView);
		mWebView.setVisibility(View.GONE);
		mWebView.destroy();
		mWebView = null;
		super.onDestroy();
	}
	private void initWebView() {
		TitleBarHelper titleBarHelper = new TitleBarHelper(this,-1);
		if (flag.equals("2")){
			titleBarHelper.setTitleMsg("总库存金额");
		}else if(flag.equals("3")){
			titleBarHelper.setTitleMsg("90天以上库存");
		}else if(flag.equals("4")){
			titleBarHelper.setTitleMsg("制作团队");
		}
		titleBarHelper.setLeftMsg("");
		webView_parent = (RelativeLayout) findViewById(R.id.webView_parent);
		web_src_loadProgress = (ProgressBar) findViewById(R.id.web_src_loadProgress);
		button_back = findViewById(R.id.button_back);
		button_forword = findViewById(R.id.button_forword);
		button_refresh = findViewById(R.id.button_reload);
		button_close =findViewById(R.id.button_close);
		button_back.setOnClickListener(this);
		button_forword.setOnClickListener(this);
		button_refresh.setOnClickListener(this);
		button_close.setOnClickListener(this);
		ll_webNavi_root = (LinearLayout) findViewById(R.id.ll_webNavi_root);
		ll_webNavi_root.setVisibility(View.GONE);
		mWebView.setCanTouchZoom(false);
		hideBuiltInZoomControls(mWebView);

		mWebView.setDownloadListener(new DownloadListener() {// 下载
			public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
				Uri uri = Uri.parse(url);
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}
		});
		mWebView.setWebViewClient(new CustomWebViewClient(this));
		mWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onReceivedTitle(WebView view, String mTitle) {
				super.onReceivedTitle(view, mTitle);
				Log.i("zwy", mTitle);//网页标题
			}
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				pb.setProgress(newProgress);
				if (newProgress == 100) {
					pb.setVisibility(View.GONE);
				}
				super.onProgressChanged(view, newProgress);// 进度条
				// if (newProgress > 90) {
				// hideLoading();
				// }
				Log.i("", "the webview onProgressChanged newProgress" + newProgress);
				web_src_loadProgress.setProgress(newProgress);
			}

		});
	}
	public void onPageStarted(WebView view, String url2) {
		updataUI(view);
		showLoadingProgress();
	}

	public void onPageFinished(WebView view, String url2) {
		updataUI(view);
		hideLoadingProgress();
	}

	public void onReceivedError() {
		hideLoadingProgress();
	}

	public void shouldOverrideUrlLoading(WebView view, String url2) {
		updataUI(view);
	}
	
	private void hideBuiltInZoomControls(WebView view) {
		if (Build.VERSION.SDK_INT < 11) {
			try {
				Field field = WebView.class.getDeclaredField("mZoomButtonsController");
				field.setAccessible(true);
				ZoomButtonsController zoomCtrl = new ZoomButtonsController(view);
				zoomCtrl.getZoomControls().setVisibility(View.GONE);
				field.set(view, zoomCtrl);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				WebSettings settings = view.getSettings();
				Method method = WebSettings.class.getMethod("setDisplayZoomControls", boolean.class);
				method.invoke(settings, false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_back:
			onBackwordUrlClick();
			break;
		case R.id.button_forword:
			onForwordUrlClick();
			break;
		case R.id.button_reload:
			onReloadUrlClick();
			break;
		case R.id.button_close:
			onCloseClick();
			break;

		default:
			break;
		}
	}
	/**
	 * 显示进度
	 */
	private void showLoadingProgress() {
		if (pb.getVisibility() == View.GONE) {
			pb.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 隐藏进度
	 */
	private void hideLoadingProgress() {
		if (pb.getVisibility() == View.VISIBLE) {
			pb.setVisibility(View.GONE);
		}
	}

	private void updataUI(WebView view) {
		button_back.setEnabled(view.canGoBack());
		button_forword.setEnabled(view.canGoForward());
	}

	/**
	 * 关闭按钮
	 */
	public void onCloseClick() {
		mWebView.stopLoading();
		if (mWebView.canGoBack()) {
			mWebView.goBack();
		}else{
			finish();
		}
		
	}

	/**
	 * 刷新按钮
	 */
	public void onReloadUrlClick() {
		pb.setVisibility(View.VISIBLE);
		pb.setProgress(0);
		mWebView.reload();
//		if (NetUtil.isNetworkAvailable(this)) {
//			mWebView.reload();
			// if (loadingParentView.getVisibility() == View.VISIBLE) {
			// loading_tip_tv.setText(R.string.loading_progress_hint);
			// }
//		} else {
//			ToastMsg.showToastMsg(this, R.string.netunable_error);
//		}
	}

	/**
	 * @description：后退按钮
	 * @author samy
	 * @date 2014年8月14日 下午2:19:07
	 */
	public void onBackwordUrlClick() {
		mWebView.stopLoading();
		if (mWebView.canGoBack()) {
			mWebView.goBack();
		}
	}

	/**
	 * 前进按钮
	 */
	public void onForwordUrlClick() {
		mWebView.stopLoading();
		if (mWebView.canGoForward()) {
			mWebView.goForward();
		}
	}
}
