package cn.chono.yopper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import java.util.ArrayList;
import java.util.List;
import cn.chono.yopper.R;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.BlockDto;
import cn.chono.yopper.glide.CropCircleTransformation;
import cn.chono.yopper.utils.ISO8601;
import cn.chono.yopper.utils.ImgUtils;
import cn.chono.yopper.utils.TimeUtil;

public class BlockListAdapter extends BaseAdapter {
	/**
	 * 这个用来填充list
	 */
	private List<BlockDto> list;
	/**
	 * context上下文,用来获得convertView
	 */
	private Context mContext;


	private CropCircleTransformation transformation;

	private BitmapPool mPool;
	
	public BlockListAdapter(Context context) {
		// 初始化
		mContext = context;
		
		mPool = Glide.get(context).getBitmapPool();
		transformation = new CropCircleTransformation(mPool);
	}

	public List<BlockDto> getList() {
		return list;
	}

	public void setList(List<BlockDto> list) {
		if (list == null) {
			list = new ArrayList<BlockDto>();
		}
		this.list = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (list != null) {
			return list.size();
		} else {
			return 0;
		}

	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final ViewHolder holder;
		// 如果没有设置过,初始化convertView


		if (convertView == null) {
			// 获得设置的view
			convertView = LayoutInflater.from(mContext).inflate(R.layout.block_list_item, parent, false);

			// 初始化holder
			holder = new ViewHolder();

			holder.icon_img_iv = (ImageView) convertView.findViewById(R.id.block_item_icon_img_iv);

			holder.name_tv = (TextView) convertView.findViewById(R.id.block_item_name_tv);

			holder.time_tv = (TextView) convertView.findViewById(R.id.block_item_time_tv);


			convertView.setTag(holder);
		}else {
			// 有直接获得ViewHolder
			holder = (ViewHolder) convertView.getTag();

		}
		final BlockDto dto = list.get(position);


		String imageurl=ImgUtils.DealImageUrl(dto.getUser().getHeadImg(), YpSettings.IMG_SIZE_150, YpSettings.IMG_SIZE_150);

		Glide.with(mContext).load(imageurl).bitmapTransform(transformation).into(holder.icon_img_iv);
		
		holder.name_tv.setText(dto.getUser().getName()+"");

		long time=ISO8601.getTime(dto.getBlockedTime());

		String time_str=TimeUtil.normalTimeFormat(time);
		
		// 时间设置

		holder.time_tv.setText("拉黑时间"+time_str + "");

		return convertView;

	}

	/**
	 * ViewHolder
	 * 
	 * @Title:
	 * @Description:主要是避免了不断的view获取初始化.
	 * @Since:2015-3-22
	 * @Version:
	 */
	class ViewHolder {

		public ImageView icon_img_iv;
		public TextView name_tv;
		public TextView time_tv;

	}

}