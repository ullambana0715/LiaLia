package cn.chono.yopper.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import cn.chono.yopper.R;

/**
 * Created by zxb on 2015/12/15.
 */
public class ProgressBarView extends View {


    public ProgressBarView(Context context) {
        super(context);
        initView(null);

    }

    public ProgressBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    public ProgressBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ProgressBarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(attrs);
    }


    /**
     * 圆环的颜色
     */
    private int outside_round_color;

    /**
     * 圆环进度的颜色
     */
    private int outside_round_progress_color;

    /**
     * 圆环的宽度
     */
    private float outside_round_width;

    /**
     * 进度的风格，实心或者空心
     */
    private int outside_round_progress_style;
    /**
     * 进度条背景圆环的风格，实心或者空心,半空心
     */
    private int outside_round_style;

    public static final int STROKE = 0;
    public static final int FILL = 1;
    public static final int STROKE_FILL = 3;

    /**
     * 最大进度
     */
    private int progressBar_max=100;

    /**
     * 当前进度
     */
    private int progress;

    /**
     * 中间进度百分比的字符串的颜色
     */
    private int textColor;

    /**
     * 中间进度百分比的字符串的字体
     */
    private float textSize;

    /**
     * 是否显示中间的进度
     */
    private boolean textIsDisplayable;

    /**
     * 中间进度百分比的字符串的颜色
     */
    private int promptTextColor;

    /**
     * 中间进度百分比的字符串的字体
     */
    private float promptTextSize;

    /**
     * 是否显示百分比的字符串
     */
    private boolean promptTextIsDisplayable;
    /**
     * 显示百分比的字符串
     */
    private CharSequence promptTextCrompttext="匹配度";

    /**
     * 中间进度百分比的字符串的字体
     */
    private float prompt_text_moment_from;


    /**
     * 圆环内的背景颜色与外框的间矩大小
     */
    private float inside_round_moment_from;

    /**
     * 圆环内的背景的颜色
     */
    private int inside_round_color;


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }


    private void initView(AttributeSet attrs) {

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ProgressBarView);

        outside_round_color = typedArray.getColor(R.styleable.ProgressBarView_outside_round_color, Color.RED);
        outside_round_progress_color = typedArray.getColor(R.styleable.ProgressBarView_outside_round_progress_color, Color.GREEN);

        outside_round_width = typedArray.getDimension(R.styleable.ProgressBarView_outside_round_width, 15);
        outside_round_progress_style = typedArray.getInt(R.styleable.ProgressBarView_outside_round_progress_style, 0);
        outside_round_style = typedArray.getInt(R.styleable.ProgressBarView_outside_round_style, 0);

        textColor = typedArray.getColor(R.styleable.ProgressBarView_textColor, Color.GREEN);
        textSize = typedArray.getDimension(R.styleable.ProgressBarView_textSize, 15);
        textIsDisplayable = typedArray.getBoolean(R.styleable.ProgressBarView_textIsDisplayable, true);

        promptTextColor = typedArray.getColor(R.styleable.ProgressBarView_promptTextColor, Color.GREEN);
        promptTextSize = typedArray.getDimension(R.styleable.ProgressBarView_promptTextSize, 15);
        promptTextIsDisplayable = typedArray.getBoolean(R.styleable.ProgressBarView_promptTextIsDisplayable, true);

        inside_round_moment_from = typedArray.getDimension(R.styleable.ProgressBarView_inside_round_moment_from, 10);

        inside_round_color = typedArray.getColor(R.styleable.ProgressBarView_inside_round_color, Color.GREEN);

        prompt_text_moment_from = typedArray.getDimension(R.styleable.ProgressBarView_prompt_text_moment_from, 10);

        typedArray.recycle();

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG)); //消除锯齿

        /**
         * 画最外层的圈，
         */

        int xCentre = getWidth() / 2; //获取圆心的x坐标
        int yCentre = getHeight() / 2;
        int radius = (int) (xCentre - outside_round_width / 2); //圆环的半径


        Paint outside_round_paint = new Paint();
        outside_round_paint.setColor(outside_round_color); //设置圆环的颜色
        outside_round_paint.setStrokeWidth(outside_round_width); //设置圆环的宽度
        outside_round_paint.setAntiAlias(true);  //消除锯齿

        switch (outside_round_style) {  //设置进度是实心还是空心
            case STROKE:

                outside_round_paint.setStyle(Paint.Style.STROKE); //设置空心
                break;
            case FILL:
                outside_round_paint.setStyle(Paint.Style.FILL_AND_STROKE); //设置实心
                break;
            case STROKE_FILL:
                outside_round_paint.setStyle(Paint.Style.STROKE); //设置空心

                float moment_from = radius - inside_round_moment_from;

                Paint inside_round_paint = new Paint();
                inside_round_paint.setColor(inside_round_color); //设置圆环的颜色
                inside_round_paint.setStrokeWidth(0); //设置圆环的宽度
                inside_round_paint.setAntiAlias(true);  //消除锯齿
                inside_round_paint.setStyle(Paint.Style.FILL_AND_STROKE); //设置实心
                canvas.drawCircle(xCentre, yCentre, moment_from, inside_round_paint);//真正开始画外圆
                break;
        }
        canvas.drawCircle(xCentre, yCentre, radius, outside_round_paint);//真正开始画外圆

        /**
         * 画圆弧 ，画圆环的进度
         */
        Paint outside_round_progress_paint = new Paint();

        outside_round_progress_paint.setColor(outside_round_progress_color);  //设置进度的颜色
        outside_round_progress_paint.setStrokeWidth(outside_round_width); //设置圆环的宽度


        RectF oval = new RectF();  //用于定义的圆弧的形状和大小的界限
        oval.left = xCentre - radius;
        oval.top = yCentre - radius;
        oval.right = xCentre + radius;
        oval.bottom = yCentre + radius;


        switch (outside_round_progress_style) {  //设置进度是实心还是空心
            case STROKE:
                outside_round_progress_paint.setStyle(Paint.Style.STROKE);
                outside_round_progress_paint.setStrokeCap(Paint.Cap.ROUND);
                canvas.drawArc(oval, -90, 360 * progress / progressBar_max, false, outside_round_progress_paint);  //根据进度画圆弧

                break;
            case FILL:
                outside_round_progress_paint.setStyle(Paint.Style.FILL_AND_STROKE);
                outside_round_progress_paint.setStrokeCap(Paint.Cap.ROUND);
                if (progress != 0)
                    canvas.drawArc(oval,-90, 360 * progress / progressBar_max, true, outside_round_progress_paint);  //根据进度画圆弧
                break;
        }


        /**
         * 画进度百分比
         */

        Paint progress_paint = new Paint();
        progress_paint.setStrokeWidth(0);
        progress_paint.setColor(textColor);
        progress_paint.setTextSize(textSize);
        progress_paint.setTextAlign(Paint.Align.CENTER);
        int percent = (int) (((float) progress / (float) progressBar_max) * 100);  //中间的进度百分比，先转换成float在进行除法运算，不然都为0

        Paint.FontMetrics progress_paint_fontMetrics = progress_paint.getFontMetrics();


        /**
         * 画进度提示文字
         */

        String prompttextCrompttextStr = promptTextCrompttext.toString();

        Paint prompt_progress_paint = new Paint();

        prompt_progress_paint.setStrokeWidth(0);
        prompt_progress_paint.setColor(promptTextColor);
        prompt_progress_paint.setTextSize(promptTextSize);
        prompt_progress_paint.setTextAlign(Paint.Align.CENTER);

        Paint.FontMetrics prompt_progress_paintfontMetrics = prompt_progress_paint.getFontMetrics();


        float baseline = oval.top + ((oval.bottom - oval.top) - (progress_paint_fontMetrics.bottom - progress_paint_fontMetrics.top)) / 2 - progress_paint_fontMetrics.top;

        if (promptTextIsDisplayable) {


            float baselines = oval.top + ((oval.bottom - oval.top) - (progress_paint_fontMetrics.bottom - progress_paint_fontMetrics.top) - (prompt_progress_paintfontMetrics.bottom - prompt_progress_paintfontMetrics.top) + inside_round_moment_from) / 2 - progress_paint_fontMetrics.top - inside_round_moment_from/ 2;
            float baseline_prompt = baselines  + progress_paint_fontMetrics.bottom +inside_round_moment_from*2;
            if (textIsDisplayable && percent != 0 && outside_round_progress_style == STROKE) {


                float progress_x = xCentre;
                float progress_y = baselines;
                canvas.drawText(percent + "%", progress_x, progress_y, progress_paint); //画出进度百分比
            }

            if (null != promptTextCrompttext) {
                float prompt_x = xCentre;
                float prompt_y = baseline_prompt;

                canvas.drawText(prompttextCrompttextStr, prompt_x, prompt_y, prompt_progress_paint); //画出进度百分比提示文字
            }

        } else {
            float progress_x = xCentre;
            float progress_y = baseline;
            if (textIsDisplayable && percent != 0 && outside_round_progress_style == STROKE) {
                canvas.drawText(percent + "%", progress_x, progress_y, progress_paint); //画出进度百分比
            }
        }

    }


    public synchronized int getProgressBar_max() {
        return progressBar_max;
    }

    public synchronized void setProgressBar_max(int progressBar_max) {
        if (progressBar_max < 0) {
            throw new IllegalArgumentException("max not less than 0");
        }
        this.progressBar_max = progressBar_max;
    }

    public synchronized int getProgress() {
        return progress;
    }

    public synchronized void setProgress(int progress) {

        if (progress < 0) {
            throw new IllegalArgumentException("progress not less than 0");
        }
        if (progress > progressBar_max) {
            progress = progressBar_max;
        }
        if (progress <= progressBar_max) {
            this.progress = progress;
            postInvalidate();
        }

    }

    public int getPromptTextColor() {
        return promptTextColor;
    }

    public void setPromptTextColor(int promptTextColor) {
        this.promptTextColor = promptTextColor;
    }

    public float getPromptTextSize() {
        return promptTextSize;
    }

    public void setPromptTextSize(float promptTextSize) {
        this.promptTextSize = promptTextSize;
    }

    public boolean isPromptTextIsDisplayable() {
        return promptTextIsDisplayable;
    }

    public void setPromptTextIsDisplayable(boolean promptTextIsDisplayable) {
        this.promptTextIsDisplayable = promptTextIsDisplayable;
    }

    public float getPrompt_text_moment_from() {
        return prompt_text_moment_from;
    }

    public void setPrompt_text_moment_from(float prompt_text_moment_from) {
        this.prompt_text_moment_from = prompt_text_moment_from;
    }

    public float getInside_round_moment_from() {
        return inside_round_moment_from;
    }

    public void setInside_round_moment_from(float inside_round_moment_from) {
        this.inside_round_moment_from = inside_round_moment_from;
    }

    public int getInside_round_color() {
        return inside_round_color;
    }

    public void setInside_round_color(int inside_round_color) {
        this.inside_round_color = inside_round_color;
    }

    public CharSequence getPromptTextCrompttext() {
        return promptTextCrompttext;
    }

    public synchronized void  setPromptTextCrompttext(CharSequence promptTextCrompttext) {
        this.promptTextCrompttext = promptTextCrompttext;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public boolean isTextIsDisplayable() {
        return textIsDisplayable;
    }

    public void setTextIsDisplayable(boolean textIsDisplayable) {
        this.textIsDisplayable = textIsDisplayable;
    }

    public int getOutside_round_style() {
        return outside_round_style;
    }

    public void setOutside_round_style(int outside_round_style) {
        this.outside_round_style = outside_round_style;
    }

    public int getOutside_round_progress_style() {
        return outside_round_progress_style;
    }

    public void setOutside_round_progress_style(int outside_round_progress_style) {
        this.outside_round_progress_style = outside_round_progress_style;
    }

    public float getOutside_round_width() {
        return outside_round_width;
    }

    public void setOutside_round_width(float outside_round_width) {
        this.outside_round_width = outside_round_width;
    }

    public int getOutside_round_progress_color() {
        return outside_round_progress_color;
    }

    public void setOutside_round_progress_color(int outside_round_progress_color) {
        this.outside_round_progress_color = outside_round_progress_color;
    }

    public int getOutside_round_color() {
        return outside_round_color;
    }

    public void setOutside_round_color(int outside_round_color) {
        this.outside_round_color = outside_round_color;
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
//    public static int dip2px(Context context, float dpValue) {
//        final float scale = context.getResources().getDisplayMetrics().density;
//        return (int) (dpValue * scale + 0.5f);
//    }


}
