package cn.chono.yopper.view.cropper;

import com.lidroid.xutils.util.LogUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.RelativeLayout;
import cn.chono.yopper.R;

/**
 * http://blog.csdn.net/lmj623565791/article/details/39761281
 * 
 * @author zhy
 * 
 */
public class ClipImageLayout extends RelativeLayout {

	private ClipZoomImageView mZoomImageView;
	private ClipImageBorderView mClipImageView;

	/**
	 * 这里测试，直接写死了大小，真正使用过程中，可以提取为自定义属性
	 */
	private int mHorizontalPadding = 0;
	public ClipImageLayout(Context context, AttributeSet attrs) {
		super(context, attrs);

		mZoomImageView = new ClipZoomImageView(context);
		mClipImageView = new ClipImageBorderView(context);

		android.view.ViewGroup.LayoutParams lp = new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);

		/**
		 * 这里测试，直接写死了图片，真正使用过程中，可以提取为自定义属性
		 */
//		mZoomImageView.setImageDrawable(getResources().getDrawable(
//				R.drawable.image_user_default));
		this.addView(mZoomImageView, lp);
		this.addView(mClipImageView, lp);

		// 计算padding的px
		mHorizontalPadding = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, mHorizontalPadding, getResources()
						.getDisplayMetrics());
		mZoomImageView.setHorizontalPadding(mHorizontalPadding);
		mClipImageView.setHorizontalPadding(mHorizontalPadding);

	}

	/**
	 * 对外公布设置边距的方法,单位为dp
	 * 
	 * @param mHorizontalPadding
	 */
	public void setHorizontalPadding(int mHorizontalPadding) {
		this.mHorizontalPadding = mHorizontalPadding;
	}

	/**
	 * 裁切图片
	 * 
	 * @return
	 */
	public Bitmap clip() {
		return mZoomImageView.clip();
	}

	public float getImageLift() {
		LogUtils.i("rectLeft()="+ mZoomImageView.rectLeft());
		return mZoomImageView.rectLeft();
	}

	public float getImageRight() {
		LogUtils.i("rectRight()="+ mZoomImageView.rectRight());
		return mZoomImageView.rectRight();
	}

	public float getImageTop() {
		LogUtils.i("rectTop()="+ mZoomImageView.rectTop());
		return mZoomImageView.rectTop();
	}

	public float getImageBottom() {
		LogUtils.i("rectBottom()="+ mZoomImageView.rectBottom());
		return mZoomImageView.rectBottom();
	}

	public float getImageBordeX() {
		LogUtils.i("getImageBordeX()="+ mClipImageView.getImageBordeX());
		return mClipImageView.getImageBordeX();
	}

	public float getImageBordeY() {
		LogUtils.i("getImageBordeY()="+ mClipImageView.getImageBordeY());
		return mClipImageView.getImageBordeY();
	}

	public void setZoomImageDrawable(Bitmap bmp) {
		if (bmp == null) {
			return;
		}
//		android.view.ViewGroup.LayoutParams lp = new LayoutParams(
//				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
//				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		Drawable drawable = new BitmapDrawable(bmp);
		
		mZoomImageView.setImageDrawable(drawable);
//		this.addView(mZoomImageView, lp);
//		this.addView(mClipImageView, lp);
	}

	public int getImageBordeWidth() {
		LogUtils.i("getImageBordeHight()="+ mClipImageView.getImageBordeWidth());
		return mClipImageView.getImageBordeWidth();

	}
	public int getImageBordeHight() {
		LogUtils.i("getImageBordeHight()="+ mClipImageView.getImageBordeHight());
		return mClipImageView.getImageBordeHight();
		
	}

}
