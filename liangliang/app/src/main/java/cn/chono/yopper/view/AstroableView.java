package cn.chono.yopper.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;

import com.umeng.analytics.MobclickAgent;

import cn.chono.yopper.R;

/**
 * Created by yangjinyu on 16/3/30.
 */
public class AstroableView extends View {

    float firstCircleX;
    float firstCircleY;
    float firstRadius;
    float secondCircleX;
    float secondCircleY;
    float secondRadius;
    float thirdCircleX;
    float thirdCircleY;
    float thirdRadius;
    Paint mFirstPaint;
    Paint mSecondPaint;
    Paint mThirdPaint;
    Paint mLinePaint;
    Paint mInnerTextPaint;
    float mInnerTextHeight;
    private int mTextHeightSpace;
    int type;
    public static final int TYPE_SINGLE = 0;
    public static final int TYPE_DOUBLE = 1;
    public static final int TYPE_LUCK = 2;

    public AstroableView(Context context) {
        super(context);
    }

    public AstroableView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AstroableView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    boolean mIsInitialized = false;
    @Override
    protected void onDraw(Canvas canvas) {
        if (!mIsInitialized) {
            init();
        }
        canvas.drawCircle(firstCircleX, firstCircleY, firstRadius, mFirstPaint);
        canvas.drawCircle(secondCircleX, secondCircleY, secondRadius, mSecondPaint);
        canvas.drawCircle(thirdCircleX, thirdCircleY, thirdRadius, mThirdPaint);
        canvas.drawLine(firstCircleX, firstCircleY, secondCircleX, secondCircleY, mLinePaint);
        canvas.drawLine(secondCircleX, secondCircleY, thirdCircleX, thirdCircleY, mLinePaint);
        canvas.drawText("个人命盘", firstCircleX, firstCircleY + mInnerTextHeight / 2 - mTextHeightSpace, mInnerTextPaint);
        canvas.drawText("双人合盘", secondCircleX, secondCircleY + mInnerTextHeight / 2 - mTextHeightSpace, mInnerTextPaint);
        canvas.drawText("运势推测", thirdCircleX, thirdCircleY + mInnerTextHeight / 2 - mTextHeightSpace, mInnerTextPaint);

    }

    void init() {
        WindowManager wm = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
        float midX = wm.getDefaultDisplay().getWidth()/2;
        float midY = wm.getDefaultDisplay().getHeight()/2;
        firstCircleX = midX - midX / 4 - midX /16;
        firstCircleY = midY - midY / 2 - 20;
        firstRadius = midX / 4 + midX / 16;
        secondCircleX = midX + midX / 4 + midX / 16;
        secondCircleY = midY - midY / 4;
        secondRadius = midX / 4 + 10 + midX /16;
        thirdCircleX = midX -  40;
        thirdCircleY = midY + midY / 4 - 40 ;
        thirdRadius = midX / 4 + midX/16 + 15 ;

        mFirstPaint = new Paint();
        mFirstPaint.setColor(getResources().getColor(R.color.color_d098ff));
        mSecondPaint = new Paint();
        mSecondPaint.setColor(getResources().getColor(R.color.color_d098ff));
        mThirdPaint = new Paint();
        mThirdPaint.setColor(getResources().getColor(R.color.color_d098ff));
        mIsInitialized = true;
        mLinePaint = new Paint();
        mLinePaint.setColor(getResources().getColor(R.color.color_d098ff));
        mLinePaint.setStrokeWidth(3);

        mInnerTextPaint = new Paint();
        mInnerTextPaint.setColor(Color.WHITE);
        mInnerTextPaint.setAntiAlias(true);
        mInnerTextPaint.setTextSize(CircleEnergy.dp2px(getContext(), 20));
        mInnerTextPaint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics metrics = mInnerTextPaint.getFontMetrics();
        mInnerTextHeight = metrics.bottom - metrics.top;
        mInnerTextHeight = mInnerTextHeight / 2 - metrics.bottom;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        System.out.println("circle onTouchEvent");
        if(getVisibility() == GONE){
            return false;
        }
        float x = event.getX();
        float y = event.getY();
        float firstX = x - firstCircleX;
        float firstY = y - firstCircleY;
        float secondX = x - secondCircleX;
        float secondY = y - secondCircleY;
        float thirdX = x - thirdCircleX;
        float thirdY = y - thirdCircleY;
        float firstResult = firstX * firstX + firstY * firstY;
        float secondResult = secondX * secondX + secondY * secondY;
        float thirdResult = thirdX * thirdX + thirdY * thirdY;
        if(firstResult <= firstRadius * firstRadius){
            type = TYPE_SINGLE;
            setDisapare();
            MobclickAgent.onEvent(getContext(), "btn_find_event_single_astrolabe");
        }else if(secondResult <= secondRadius * secondRadius){
            type = TYPE_DOUBLE;
            setDisapare();
            MobclickAgent.onEvent(getContext(), "btn_find_event_double_astrolabe");
        }else if(thirdResult <= thirdRadius * thirdRadius){
            type = TYPE_LUCK;
            setDisapare();
            MobclickAgent.onEvent(getContext(), "btn_find_event_single_luck");
        }else{

        }

        return false;
    }

    void setDisapare(){
        AlphaAnimation mHiddenAction = new AlphaAnimation(1,0);
        mHiddenAction.setDuration(500);
        startAnimation(mHiddenAction);
        setVisibility(GONE);
    }
    public int  getClickType(){
        return type;
    }
}
