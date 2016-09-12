package cn.chono.yopper.view.WebView;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.widget.ProgressBar;

/**
 * Created by cc on 16/3/2.
 */
public class PragressWebView extends WebView {

    private ProgressBar progressbar;


    public PragressWebView(Context context) {
        super(context);
        init();
    }

    public PragressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();

    }

    public PragressWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PragressWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public PragressWebView(Context context, AttributeSet attrs, int defStyleAttr, boolean privateBrowsing) {
        super(context, attrs, defStyleAttr, privateBrowsing);
        init();
    }


    private void init() {
        progressbar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleHorizontal);


        progressbar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 30, 0, 0));

        addView(progressbar);
        setWebChromeClient(new WebChromeClient());

    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        progressbar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }

    public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressbar.setVisibility(GONE);
            } else {
                if (progressbar.getVisibility() == GONE)
                    progressbar.setVisibility(VISIBLE);
                progressbar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }

    }
}
