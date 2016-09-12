package cn.chono.yopper.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.callback.IFooterCallBack;
import com.andview.refreshview.utils.Utils;
import com.lidroid.xutils.util.LogUtils;

import cn.chono.yopper.R;

/**
 * Created by cc on 16/5/11.
 */
public class XRefreshViewFooters extends LinearLayout implements IFooterCallBack {

    private Context mContext;

    private View xrefreshview_footer_content;
    private View xrefreshview_footer_progressbar;
    private TextView xrefreshview_footer_hint_textview;
    private TextView xrefreshview_footer_click_textview;
    private boolean showing = false;

    private boolean loadcomplete = false;

    public void setLoadcomplete(boolean loadcomplete) {
        this.loadcomplete = loadcomplete;
    }

    public XRefreshViewFooters(Context context) {
        super(context);
        initView(context);
    }

    public XRefreshViewFooters(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }


    private RecyclerView recyclerView;

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @Override
    public void callWhenNotAutoLoadMore(final XRefreshView.XRefreshViewListener listener) {


        //当数据不满一屏时不显示点击加载更多
        if (recyclerView != null) {
            show(Utils.isRecyclerViewFullscreen(recyclerView));
        }

        xrefreshview_footer_click_textview.setText("点击加载更多");
        xrefreshview_footer_click_textview.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onLoadMore(false);
                    onStateRefreshing();
                }
            }
        });
    }


    public void onAutoLoadMoreFail(final XRefreshView.XRefreshViewListener listener) {

        if (loadcomplete) return;


        xrefreshview_footer_hint_textview.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onLoadMore(false);
                    onStateRefreshing();
                }
            }
        });
    }

    @Override
    public void onStateReady() {

        LogUtils.e("onStateReady----" + loadcomplete);
        xrefreshview_footer_hint_textview.setVisibility(View.GONE);
        xrefreshview_footer_progressbar.setVisibility(View.GONE);
        xrefreshview_footer_click_textview.setVisibility(VISIBLE);
        show(true);
    }

    @Override
    public void onStateRefreshing() {

        LogUtils.e("onStateRefreshing----" + loadcomplete);

        if (loadcomplete) {
            //处理数据加载失败时ui显示的逻辑，也可以不处理，看自己的需求
            xrefreshview_footer_hint_textview.setText("已全部加载完毕");

            xrefreshview_footer_hint_textview.setVisibility(View.VISIBLE);
            xrefreshview_footer_progressbar.setVisibility(View.GONE);
            xrefreshview_footer_click_textview.setVisibility(View.GONE);

        } else {
            //处理数据加载失败时ui显示的逻辑，也可以不处理，看自己的需求
            xrefreshview_footer_hint_textview.setText("加载失败，请点击重试");

            xrefreshview_footer_hint_textview.setVisibility(View.GONE);
            xrefreshview_footer_progressbar.setVisibility(View.VISIBLE);
            xrefreshview_footer_click_textview.setVisibility(View.GONE);
        }


        show(true);
    }

    @Override
    public void onStateFinish(boolean hideFooter) {

        LogUtils.e("onStateFinish----" + hideFooter);
        LogUtils.e("loadcomplete----" + loadcomplete);
        if (hideFooter) {
            xrefreshview_footer_hint_textview.setText("加载成功");
        } else {

            if (loadcomplete) {
                //处理数据加载失败时ui显示的逻辑，也可以不处理，看自己的需求
                xrefreshview_footer_hint_textview.setText("已全部加载完毕");
            } else {
                //处理数据加载失败时ui显示的逻辑，也可以不处理，看自己的需求
                xrefreshview_footer_hint_textview.setText("加载失败，请点击重试");
            }


        }

        xrefreshview_footer_hint_textview.setVisibility(View.VISIBLE);
        xrefreshview_footer_progressbar.setVisibility(View.GONE);
        xrefreshview_footer_click_textview.setVisibility(View.GONE);
    }

    @Override
    public void onStateComplete() {
        xrefreshview_footer_hint_textview.setText("已全部加载完毕");
        xrefreshview_footer_hint_textview.setVisibility(View.VISIBLE);
        xrefreshview_footer_progressbar.setVisibility(View.GONE);
        show(true);
    }

    @Override
    public void show(final boolean show) {
//        post(new Runnable() {
//            @Override
//            public void run() {
//                showing = show;
//                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) xrefreshview_footer_content
//                        .getLayoutParams();
//                lp.height = show ? LayoutParams.WRAP_CONTENT : 0;
//                xrefreshview_footer_content.setLayoutParams(lp);
//            }
//        });


        showing = show;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) xrefreshview_footer_content
                .getLayoutParams();
        lp.height = show ? LayoutParams.WRAP_CONTENT : 0;
        xrefreshview_footer_content.setLayoutParams(lp);

    }

    @Override
    public boolean isShowing() {
        return showing;
    }

    private void initView(Context context) {
        mContext = context;
        ViewGroup moreView = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.xrefreshview_footers, this);
        moreView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        xrefreshview_footer_content = moreView.findViewById(com.andview.refreshview.R.id.xrefreshview_footer_content);
        xrefreshview_footer_progressbar = moreView
                .findViewById(com.andview.refreshview.R.id.xrefreshview_footer_progressbar);
        xrefreshview_footer_hint_textview = (TextView) moreView
                .findViewById(com.andview.refreshview.R.id.xrefreshview_footer_hint_textview);
        xrefreshview_footer_click_textview = (TextView) moreView
                .findViewById(com.andview.refreshview.R.id.xrefreshview_footer_click_textview);
    }

    @Override
    public int getFooterHeight() {
        return getMeasuredHeight();
    }

}