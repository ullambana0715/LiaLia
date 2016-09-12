package cn.chono.yopper.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by SQ on 2016/1/13.
 */
public class SquareWidthRelativeLayout extends RelativeLayout{

    public SquareWidthRelativeLayout(Context context) {
        super(context);
    }

    public SquareWidthRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareWidthRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }


}
