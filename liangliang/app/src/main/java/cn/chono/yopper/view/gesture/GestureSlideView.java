package cn.chono.yopper.view.gesture;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lidroid.xutils.util.LogUtils;

import cn.chono.yopper.R;

public class GestureSlideView extends FrameLayout {

    private Context context;

    private String[] videoImage;

    private ImageRecyclerViewAdapter adapter;

    private RecyclerView recyclerView;

    private RelativeLayout indicateLayout;

    private LinearLayout recyclerLayout;

    private ImageView indicateVideoView;

    private float indicateORoriginalInterval = 10;

    private int filterSelectView_margin = 10;

    private float recyclerORoriginalInterval = 5;

    // 用于记录正常的布局位置
    private Rect originalRect = new Rect();

    private Integer videoSize;

    private OnValueListener onValueListener;

    public interface OnValueListener {


        public Bitmap onValueScale(int intervalSize, int currentValue);

        public void onValueSelected();

    }

    public void setOnValueListener(OnValueListener onValueListener) {
        this.onValueListener = onValueListener;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GestureSlideView(Context context, AttributeSet attrs,
                            int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        init();
    }

    public GestureSlideView(Context context, AttributeSet attrs,
                            int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    public GestureSlideView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public GestureSlideView(Context context) {
        super(context);
        this.context = context;

        init();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (indicateLayout == null) {
            return;
        }

        originalRect.set(indicateLayout.getLeft(), indicateLayout.getTop(),
                indicateLayout.getRight(), indicateLayout.getBottom());

    }

    public void setData(String[] videoImage) {
        if (videoImage == null) {
            videoImage = new String[0];
        }

        this.videoImage = videoImage;
        adapter = new ImageRecyclerViewAdapter();
        recyclerView.setLayoutManager(new GridLayoutManager(context,videoImage.length));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        recyclerLayout.setPadding(dip2px(recyclerORoriginalInterval), dip2px(recyclerORoriginalInterval), dip2px(recyclerORoriginalInterval), dip2px(recyclerORoriginalInterval));


       //底层
        FrameLayout.LayoutParams params_recyclerLayout = new FrameLayout.LayoutParams(
                FrameLayout.  LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        params_recyclerLayout.width=recyclerViewBgWinth();
        params_recyclerLayout.height=recyclerViewBgHigth();
        params_recyclerLayout.setMargins(0, dip2px(indicateORoriginalInterval), 0, dip2px(indicateORoriginalInterval));
        recyclerLayout.setLayoutParams(params_recyclerLayout);

        //底层
        LinearLayout.LayoutParams params_recyclerView = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params_recyclerView.width=recyclerViewWinth();
        params_recyclerView.height=recyclerViewImageViewSize();

        recyclerView.setLayoutParams(params_recyclerView);

        //指示层

        FrameLayout.LayoutParams params_indicateLayout = new FrameLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);

        params_indicateLayout.width = recyclerViewBgHigth() + dip2px(indicateORoriginalInterval);
        params_indicateLayout.height = recyclerViewBgHigth() + dip2px(indicateORoriginalInterval);

        params_indicateLayout.setMargins(0, dip2px(indicateORoriginalInterval) / 2, 0, dip2px(indicateORoriginalInterval) / 2);
        indicateLayout.setLayoutParams(params_indicateLayout);

        RelativeLayout.LayoutParams params_indicateVideoView = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params_indicateVideoView.width = recyclerViewBgHigth() + dip2px(indicateORoriginalInterval);
        params_indicateVideoView.height = recyclerViewBgHigth() + dip2px(indicateORoriginalInterval);
        indicateVideoView.setLayoutParams(params_indicateVideoView);


    }

    /**
     * 创建图片列表图层
     */
    private void init() {
        /**
         * 创建指示图层背景
         */

        recyclerLayout= (LinearLayout) LayoutInflater.from(context).inflate(
                R.layout.recycler_filter_select_view, null);

        recyclerView = (RecyclerView) recyclerLayout.findViewById(R.id.recyclerView);



        addView(recyclerLayout);

        /**
         * 创建指示图层
         */
        indicateLayout = (RelativeLayout) LayoutInflater.from(context).inflate(
                R.layout.indicate_filter_select_view, null);
        indicateVideoView = (ImageView) indicateLayout.findViewById(R.id.indicate_video_view);
        addView(indicateLayout);

    }




    public void setVideoSeekTo(final Bitmap bitmap) {
        if(bitmap ==null){
            Log.d("FilterSelectView", "FilterSelectView-------bitmap=null " );
            return;
        }
        post(new Runnable() {
            @Override
            public void run() {
                indicateVideoView.setImageBitmap(bitmap);
                if (onValueListener != null) {
                    onValueListener.onValueSelected();

                }


            }
        });


    }





    /**
     * 偏离量
     */
    private int deltaX;
    private float startX;
    private float startY;

    private int leftX;
    private int rightX;

    private int viewMiddleSize;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: // 表示用户开始触摸.
                // 记录按下时的 X值
                startX = ev.getX();
                startY = ev.getY();
                float left = originalRect.left;
                float right = originalRect.right;
                float top = originalRect.top;
                float bottom = originalRect.bottom;

                if (startX >= left && startX <= right && startY >= top
                        && startY <= bottom) {
                    return true;
                } else {
                    return false;
                }

            case MotionEvent.ACTION_MOVE: // 表示用户在移动(手指或者其他)

                // 计算手指移动的距离
                float nowX = ev.getX();
                deltaX = (int) (nowX - startX);

                leftX = originalRect.left + deltaX;
                rightX = originalRect.right + deltaX;
                int indicateLayoutLeft = getLeft();
                int indicateLayoutRight = getRight();
                int X_Y = 0;
                Log.e("TT", "indicateLayoutLeft=" + indicateLayoutLeft);
                Log.e("TT", "indicateLayoutRight=" + indicateLayoutRight);
                Log.e("TT", "leftX=" + leftX);
                Log.e("TT", "rightX=" + rightX);
                if (leftX < 0) {

                    X_Y = 0 - leftX;
                    leftX = 0;
                    rightX = rightX + X_Y;
                    Log.e("TT", "X_Y--=" + X_Y);
                    Log.e("TT", "leftX--=" + leftX);
                    Log.e("TT", "rightX--=" + rightX);
                }
                if (rightX > indicateLayoutRight - indicateLayoutLeft) {
                    X_Y = rightX - (indicateLayoutRight - indicateLayoutLeft);
                    rightX = indicateLayoutRight - indicateLayoutLeft;
                    leftX = leftX - X_Y;
                    Log.e("TT", "leftX=" + leftX);
                    Log.e("TT", "rightX=" + rightX);
                }

                viewMiddleSize = (getRight() - getLeft()) - (rightX - leftX);






                // 随着手指的移动而移动布局
                indicateLayout.layout(leftX, originalRect.top, rightX,
                        originalRect.bottom);
                if (onValueListener != null) {
                    setVideoSeekTo(onValueListener.onValueScale(viewMiddleSize, leftX));

                }


                break;
            case MotionEvent.ACTION_UP: // 表示用户抬起了手指

            case MotionEvent.ACTION_CANCEL: // 表示手势被取消了
                // 随着手指的移动而移动布局
                originalRect.set(indicateLayout.getLeft(), indicateLayout.getTop(),
                        indicateLayout.getRight(), indicateLayout.getBottom());

                break;

            default:
                break;
        }
        return super.dispatchTouchEvent(ev);

    }

    /**
     * 背景图片的view大小
     * @return
     */
    private int  recyclerViewImageViewSize(){
        int indicateLayoutSise = getScreenWidthPixels() - (dip2px(filterSelectView_margin)*2)-(dip2px(recyclerORoriginalInterval)*2);
        int dataLength = videoImage.length;
        int wh = indicateLayoutSise / dataLength;
        return wh;
    }

    private int recyclerViewWinth(){
        int indicateLayoutSise = getScreenWidthPixels() - (dip2px(filterSelectView_margin)*2)-(dip2px(recyclerORoriginalInterval)*2);
        return indicateLayoutSise;
    }

    /**
     * 背景图片层的大小
     * @return
     */
    private int  recyclerViewBgHigth(){
        return recyclerViewImageViewSize()+((dip2px(recyclerORoriginalInterval)*2));
    }

    private int recyclerViewBgWinth(){
        int indicateLayoutSise = getScreenWidthPixels() - (dip2px(filterSelectView_margin)*2);
        return indicateLayoutSise;
    }
    private class ImageRecyclerViewAdapter extends
            RecyclerView.Adapter<ImageRecyclerViewAdapter.ImageViewHolder> {

        public class ImageViewHolder extends ViewHolder {

            private ImageView imageView;

            public ImageViewHolder(View view) {
                super(view);
                imageView = (ImageView) view;
            RecyclerView.LayoutParams params= new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,RecyclerView.LayoutParams.MATCH_PARENT);
            params.width=recyclerViewImageViewSize();
            params.height=recyclerViewImageViewSize();
            imageView.setLayoutParams(params);



            }

        }

        @Override
        public int getItemCount() {
            return videoImage == null ? 0 : videoImage.length;
        }

        @Override
        public void onBindViewHolder(ImageViewHolder holder, int arg1) {


            int position = holder.getLayoutPosition();

            Glide.with(context).load(videoImage[position])
                    .error(R.drawable.error_default_icon)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(holder.imageView);


            LogUtils.e("-------onBindViewHolder---" + position);

        }

        @Override
        public ImageViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
            View LconvertView = LayoutInflater.from(context).inflate(
                    R.layout.filter_select_view, null);

            ImageViewHolder holder = new ImageViewHolder(LconvertView);
            return holder;
        }

    }

    /**
     * 获取屏宽度像素数
     *
     * @return
     */
    public int getScreenWidthPixels() {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕高度像素数
     *
     * @return
     */
    public int getScreenHeightPixels() {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 将dip转换为像素
     *
     * @param dipValue
     * @return
     */
    private int dip2px(float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将像素转换为dip
     *
     * @param pxvalue
     * @return
     */
    private float px2dip(int pxvalue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return pxvalue / scale;
    }
}
