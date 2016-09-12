package cn.chono.yopper.activity.usercenter;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.util.ArrayList;
import java.util.Calendar;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.utils.ViewsUtils;
import cn.chono.yopper.view.SwitchButton;
import cn.chono.yopper.view.wheel.AbstractWheelTextAdapter;
import cn.chono.yopper.view.wheel.OnWheelChangedListener;
import cn.chono.yopper.view.wheel.OnWheelScrollListener;
import cn.chono.yopper.view.wheel.WheelView;


/**
 * 修改编辑用户年龄
 * 
 * @author sam.sun
 * 
 */
public class EditUserAgeActivity extends MainFrameActivity {


	// 本地缓存数据
	private LayoutInflater mInflater;
	private View contextView;

	private TextView user_info_age_tv;

	private WheelView wvYear;

	private SwitchButton edit_age_visiable_switch;

	private int  age_num=18;

	private int result_code;

	private ArrayList<String> arry_years = new ArrayList<String>();

	private int maxTextSize = 24;

	private int minTextSize = 14;

	private boolean isVisibility_age;

	private CalendarTextAdapter mYearAdapter;

	private int currentYear = getYear();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PushAgent.getInstance(this).onAppStart();
		

		Bundle bundle=this.getIntent().getExtras();
		if(bundle!=null){
			age_num=bundle.getInt(YpSettings.USER_AGE,18);
			isVisibility_age=bundle.getBoolean(YpSettings.USER_AGE_VISIBILITY);
			result_code=bundle.getInt(YpSettings.INTENT_RESULT_CODE);
		}

		if(age_num<18){
			age_num=18;
		}
		if(age_num>99){
			age_num=99;
		}
		initComponent();
		
		if(age_num==0){
			age_num=18;
		}
	
		user_info_age_tv.setText(age_num+"岁");
		
		initYears();
		
		currentYear=getYear()-age_num;

		mYearAdapter = new CalendarTextAdapter(this, arry_years, setYear(currentYear), maxTextSize, minTextSize);
		wvYear.setVisibleItems(5);
		wvYear.setViewAdapter(mYearAdapter);
		wvYear.setCurrentItem(setYear(currentYear));
		

		wvYear.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {

				String currentText = (String) mYearAdapter.getItemText(wheel.getCurrentItem());
				setTextviewSize(currentText, mYearAdapter);
				currentYear = Integer.parseInt(currentText);
				age_num=getYear()-currentYear;
				setYear(currentYear);
				
				user_info_age_tv.setText(age_num+"岁");
			
			}
		});

		wvYear.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {

				String currentText = (String) mYearAdapter.getItemText(wheel.getCurrentItem());
				setTextviewSize(currentText, mYearAdapter);
			}
		});

	}


	public void initYears() {

		for (int i = getYear()-18; i > getYear()-100; i--) {
			arry_years.add(i + "");
		}

	}
	
	
	public int getYear() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.YEAR);
	}
	
	/**
	 * 设置年份
	 * 
	 * @param year
	 */
	public int setYear(int year) {
		int yearIndex = 0;
		
		for (int i = getYear()-18; i > getYear()-100; i--) {
			if (i == year) {
				return yearIndex;
			}
			yearIndex++;
		}
		
		
		return yearIndex;
	}
	
	
	
	private class CalendarTextAdapter extends AbstractWheelTextAdapter {
		ArrayList<String> list;

		protected CalendarTextAdapter(Context context, ArrayList<String> list, int currentItem, int maxsize, int minsize) {
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
	public void setTextviewSize(String curriteItemText, CalendarTextAdapter adapter) {
		ArrayList<View> arrayList = adapter.getTestViews();
		int size = arrayList.size();
		String currentText;
		for (int i = 0; i < size; i++) {
			TextView textvew = (TextView) arrayList.get(i);
			currentText = textvew.getText().toString();
			if (curriteItemText.equals(currentText)) {
				textvew.setTextSize(maxTextSize);
			} else {
				textvew.setTextSize(minTextSize);
			}
		}
	}



	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("年龄修改"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
		MobclickAgent.onResume(this); // 统计时长

	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("年龄修改"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
		MobclickAgent.onPause(this); // 统计时长
	}

	/**
	 * 初始化
	 */
	private void initComponent() {

		// 设置标题栏
		this.getTvTitle().setText("年龄");
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
		contextView = mInflater.inflate(R.layout.user_info_edit_age_activity, null);

		user_info_age_tv=(TextView) contextView.findViewById(R.id.edit_age_tv);

		edit_age_visiable_switch=(SwitchButton) contextView.findViewById(R.id.edit_age_visiable_switch);

		wvYear=(WheelView) contextView.findViewById(R.id.edit_age_wheel);

		edit_age_visiable_switch.setChecked(!isVisibility_age);
		edit_age_visiable_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				isVisibility_age=!isChecked;
			}
		});

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
		bundle.putInt(YpSettings.USER_AGE, age_num);
		bundle.putBoolean(YpSettings.USER_AGE_VISIBILITY, isVisibility_age);
		intent.putExtras(bundle);
		EditUserAgeActivity.this.setResult(result_code, intent);
		EditUserAgeActivity.this.finish();
	}


}
