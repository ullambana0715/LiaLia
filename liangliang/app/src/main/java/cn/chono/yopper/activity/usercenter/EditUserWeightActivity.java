package cn.chono.yopper.activity.usercenter;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.lidroid.xutils.util.LogUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.util.ArrayList;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.utils.ViewsUtils;
import cn.chono.yopper.view.wheel.AbstractWheelTextAdapter;
import cn.chono.yopper.view.wheel.OnWheelChangedListener;
import cn.chono.yopper.view.wheel.OnWheelScrollListener;
import cn.chono.yopper.view.wheel.WheelView;


/**
 * 修改编辑用户体重
 * 
 * @author sam.sun
 * 
 */
public class EditUserWeightActivity extends MainFrameActivity {


	// 本地缓存数据
	private LayoutInflater mInflater;
	private View contextView;

	private TextView user_info_weight_tv;

	private WheelView wvWeight;


	private int result_code;


	private ArrayList<String> arry_weight = new ArrayList<String>();

	private int maxTextSize = 24;
	private int minTextSize = 14;


	private HeightTextAdapter weightAdapter;


	private int currentWeight;





	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PushAgent.getInstance(this).onAppStart();
		initComponent();

		Bundle bundle=this.getIntent().getExtras();
		if(bundle!=null){
			currentWeight=bundle.getInt(YpSettings.USER_WEIGHT,50);
			result_code=bundle.getInt(YpSettings.INTENT_RESULT_CODE);
		}

		if(currentWeight==0){
			currentWeight=50;
		}

		user_info_weight_tv.setText(currentWeight+"kg");

		initHeight();


		weightAdapter = new HeightTextAdapter(this, arry_weight, setHeight(currentWeight), maxTextSize, minTextSize);

		wvWeight.setVisibleItems(5);
		wvWeight.setViewAdapter(weightAdapter);
		wvWeight.setCurrentItem(setHeight(currentWeight));


		wvWeight.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {

				String currentText = (String) weightAdapter.getItemText(wheel.getCurrentItem());
				setTextviewSize(currentText, weightAdapter);

				currentWeight = Integer.parseInt(currentText.split("k")[0]);

				setHeight(currentWeight);

				user_info_weight_tv.setText(currentWeight+"kg");

			}
		});

		wvWeight.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				String currentText = (String) weightAdapter.getItemText(wheel.getCurrentItem());
				setTextviewSize(currentText, weightAdapter);
			}
		});

	}


	public void initHeight() {
		for (int i =30; i < 150; i++) {
			arry_weight.add(i + "kg");
		}
	}



	/**
	 * 设置年份
	 * 
	 * @param height
	 */
	public int setHeight(int height) {
		int heightIndex = 0;

		for (int i = 30; i < 150; i++) {
			if (i == height) {
				LogUtils.e("yearIndex=="+heightIndex);
				return heightIndex;
			}
			heightIndex++;
		}


		return heightIndex;
	}



	private class HeightTextAdapter extends AbstractWheelTextAdapter {
		ArrayList<String> list;

		protected HeightTextAdapter(Context context, ArrayList<String> list, int currentItem, int maxsize, int minsize) {
			super(context, R.layout.item_birth_year, NO_RESOURCE, currentItem, maxsize, minsize);
			this.list = list;
			setItemTextResource(R.id.tempValue);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			return view;
		}

		@Override
		public int getItemsCount() {
			return list.size();
		}

		@Override
		protected CharSequence getItemText(int index) {
			return list.get(index) + "";
		}
	}

	/**
	 * 设置字体大小
	 * 
	 * @param curriteItemText
	 * @param adapter
	 */
	public void setTextviewSize(String curriteItemText, HeightTextAdapter adapter) {
		ArrayList<View> arrayList = adapter.getTestViews();
		int size = arrayList.size();
		String currentText;
		for (int i = 0; i < size; i++) {
			TextView textvew = (TextView) arrayList.get(i);
			currentText = textvew.getText().toString();
			if (TextUtils.equals(curriteItemText,currentText)) {
				textvew.setTextSize(maxTextSize);
			} else {
				textvew.setTextSize(minTextSize);
			}
		}
	}



	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("体重修改"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
		MobclickAgent.onResume(this); // 统计时长

	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("体重修改"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
		MobclickAgent.onPause(this); // 统计时长
	}

	/**
	 * 初始化
	 */
	private void initComponent() {

		// 设置标题栏
		this.getTvTitle().setText("体重");
		this.getBtnGoBack().setVisibility(View.VISIBLE);
		this.getOptionLayout().setVisibility(View.INVISIBLE);
		this.getGoBackLayout().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ViewsUtils.preventViewMultipleClick(arg0, 500);
				return_name();
			}
		});

		// 内容部分
		mInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		contextView = mInflater.inflate(R.layout.user_info_edit_income_activity, null);

		user_info_weight_tv=(TextView) contextView.findViewById(R.id.edit_income_tv);


		wvWeight=(WheelView) contextView.findViewById(R.id.edit_income_wheel);



		this.getMainLayout().addView(contextView, LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			return_name();

		}
		return true;
	}

	private void return_name(){


		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putInt(YpSettings.USER_WEIGHT, currentWeight);
		intent.putExtras(bundle);
		EditUserWeightActivity.this.setResult(result_code, intent);
		EditUserWeightActivity.this.finish();
	}


}
