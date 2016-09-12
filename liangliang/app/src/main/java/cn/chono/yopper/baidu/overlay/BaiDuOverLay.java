package cn.chono.yopper.baidu.overlay;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;

import cn.chono.yopper.R;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.BubblingList;
import cn.chono.yopper.glide.CropCircleTransformation;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.utils.ImgUtils;
import cn.chono.yopper.utils.UnitUtil;

public class BaiDuOverLay {

	private LayoutInflater inflater;

	private Context context;

	private MapView mapView;

	private int wigth;

	private int height;

	private Dialog dailog;

	private ImageView typeIcon;

	private TextView content;

	private InfoWindow infoWindow;

	private Marker marKer;

	private BaiduMap mBaiduMap;

	private MapStatus mapStatus;

	private LinearLayout markerView;

	private BitmapDescriptor bd;

	private BitmapPool mPool;

	private CropCircleTransformation transformation;

	public BaiDuOverLay(Context context) {
		if (dailog != null) {
			dailog = null;

		}

		recycle();
		clearOverlay();
		onDestroy();
		mPool = null;
		transformation = null;
		mPool = Glide.get(context).getBitmapPool();
		transformation = new CropCircleTransformation(mPool);

		this.context = context;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		initView();
		initMarkerView();
	}

	@SuppressWarnings("unchecked")
	public void setBaiDuData(final BubblingList.Location location) {
		if (location != null) {

			String typeUrl = location.getTypeImgUrl();
			String typeIcoUrl = ImgUtils.DealImageUrl(typeUrl,
					YpSettings.IMG_SIZE_150, YpSettings.IMG_SIZE_150);
			Integer locationID = location.getId();
			if (locationID == null || locationID <= 0) {
				typeIcon.setBackgroundResource(R.drawable.discover_type_icon_no);
				Glide.with(context).load(typeIcoUrl)
						.into(typeIcon);
			} else {
				typeIcon.setBackgroundResource(R.drawable.discover_type_icon);
				Glide.with(context).load(typeIcoUrl)
						.bitmapTransform(transformation).into(typeIcon);
			}

			content.setText(location.getName());
			setBaiDuData(location.getLat(), location.getLng());
		}
	}

	public void setBaiDuData(double lat, double loc) {
		LatLng llA = new LatLng(lat, loc);
		OverlayOptions ooA = new MarkerOptions().position(llA).icon(bd)
				.zIndex(5).draggable(true);

		marKer = (Marker) (mBaiduMap.addOverlay(ooA));
		LatLng ll = marKer.getPosition();

		infoWindow = new InfoWindow(
				BitmapDescriptorFactory.fromView(markerView), ll, -100, null);
		mBaiduMap.showInfoWindow(infoWindow);

		MapStatusUpdate mu = MapStatusUpdateFactory.newLatLng(llA);
		// 改变地图状态
		mBaiduMap.setMapStatus(mu);

		// // 刷新
		// mapView.invalidate();
	}

	/**
	 * 清除所有Overlay
	 * 
	 */
	public void clearOverlay() {
		if (mBaiduMap != null) {
			mBaiduMap.clear();
		}

	}

	public void showBaiDuOverLay() {
		if (dailog != null) {
			dailog.show();
		}

	}

	public void dismissBaiDuOverLay() {
		clearOverlay();
		if (dailog != null) {
			dailog.dismiss();
		}

	}

	/**
	 * MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
	 */
	public void onPause() {
		if (mapView != null) {
			mapView.onPause();
		}

	}

	/**
	 * MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
	 */
	public void onResume() {
		if (mapView != null) {
			mapView.onResume();
		}

	}

	/**
	 * MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
	 */
	public void onDestroy() {
		if (mapView != null) {
			mapView.onDestroy();
		}
		recycle();

	}

	public void recycle() {
		if (bd != null) {
			bd.recycle();
		}
	}

	private void initView() {

		RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(
				R.layout.baidu_overlay_layout, null);
		relativeLayout.getBackground().setAlpha(100);
		dailog = DialogUtil.hineDialog(context, R.style.MyDialog_NOBG,
				relativeLayout, Gravity.BOTTOM, R.style.dialogBOT_style);
		dailog.setCancelable(true);
		dailog.setCanceledOnTouchOutside(true);

		mapView = (MapView) relativeLayout.findViewById(R.id.mapView);
		// 删除百度地图logo
		mapView.removeViewAt(1);
		mapView.showScaleControl(false);
		mapView.showZoomControls(false);

		wigth = UnitUtil.getScreenWidthPixels(context);
		height = UnitUtil.getScreenHeightPixels(context);

		FrameLayout.LayoutParams layoutparams = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.MATCH_PARENT);
		layoutparams.width = wigth;
		layoutparams.height = height;

		relativeLayout.setLayoutParams(layoutparams);

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);

		int h = height / 3;
		params.width = wigth;
		params.height = height - h;

		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, -1);
		mapView.setLayoutParams(params);

		mBaiduMap = mapView.getMap();
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(17.0f);

		mBaiduMap.setMapStatus(msu);

		bd = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);

		mBaiduMap.getUiSettings().setScrollGesturesEnabled(false);
		mBaiduMap.getUiSettings().setRotateGesturesEnabled(false);
		mBaiduMap.getUiSettings().setOverlookingGesturesEnabled(false);
		mBaiduMap.getUiSettings().setAllGesturesEnabled(false);
		mBaiduMap.getUiSettings().setCompassEnabled(false);
		mBaiduMap.getUiSettings().setZoomGesturesEnabled(false);

		relativeLayout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				dismissBaiDuOverLay();
				return false;
			}
		});

	}

	private void initMarkerView() {
		markerView = (LinearLayout) inflater.inflate(
				R.layout.baidu_overlay_marker_layout, null);
		typeIcon = (ImageView) markerView.findViewById(R.id.icon);
		content = (TextView) markerView.findViewById(R.id.content);

	}

}
