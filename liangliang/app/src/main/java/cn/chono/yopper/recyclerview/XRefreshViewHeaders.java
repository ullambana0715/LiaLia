package cn.chono.yopper.recyclerview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.andview.refreshview.callback.IHeaderCallBack;

import cn.chono.yopper.R;

/**
 * Created by cc on 16/3/16.
 */
public class XRefreshViewHeaders extends LinearLayout implements IHeaderCallBack {
    public XRefreshViewHeaders(Context context) {
        super(context);
        initView(context);
    }

    public XRefreshViewHeaders(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public XRefreshViewHeaders(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public XRefreshViewHeaders(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }



    TextView xrefreshview_header_tv_hint;

    ProgressBar xrefreshview_header_pb;


    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.xrefreshview_headers, this);

        xrefreshview_header_tv_hint = (TextView) view.findViewById(R.id.xrefreshview_header_tv_hint);

        xrefreshview_header_pb = (ProgressBar) view.findViewById(R.id.xrefreshview_header_pb);


    }

    public void setRefreshTime(long lastRefreshTime) {

    }

    /**
     * hide footer when disable pull load more
     */
    public void hide() {
        setVisibility(View.GONE);
    }

    public void show() {
        setVisibility(View.VISIBLE);
    }



    boolean RefreshFail;

    @Override
    public void onStateNormal() {

        xrefreshview_header_tv_hint.setText("下拉刷新");
        xrefreshview_header_pb.setVisibility(GONE);
        RefreshFail=false;

    }

    @Override
    public void onStateReady() {
        xrefreshview_header_tv_hint.setText("释放更新");
        xrefreshview_header_pb.setVisibility(GONE);
        RefreshFail=false;

    }

    @Override
    public void onStateRefreshing() {

       xrefreshview_header_tv_hint.setText("更新加载中");
        xrefreshview_header_pb.setVisibility(VISIBLE);
        RefreshFail=false;

    }

    @Override
    public void onStateFinish() {


        if (! RefreshFail){
            xrefreshview_header_tv_hint.setText("刷新成功");
        }else {
            xrefreshview_header_tv_hint.setText("刷新失败");
        }

        xrefreshview_header_pb.setVisibility(GONE);


    }

    public void onRefreshFail(){
        RefreshFail=true;
    }

    @Override
    public void onHeaderMove(double offset, int offsetY, int deltaY) {

    }

    @Override
    public int getHeaderHeight() {
        return getMeasuredHeight();
    }
}
