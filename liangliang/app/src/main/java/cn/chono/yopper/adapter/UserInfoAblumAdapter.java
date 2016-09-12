package cn.chono.yopper.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import cn.chono.yopper.R;
import cn.chono.yopper.utils.ImgUtils;
import cn.chono.yopper.utils.UnitUtil;

public class UserInfoAblumAdapter extends BaseAdapter {





	private Context context;

	List<String> list = new ArrayList<String>();

	public UserInfoAblumAdapter(Context context) {
		this.context = context;

	}

	public void setData(List<String> list) {
		this.list = list;
	}

	public List<String> getData() {

		return list;
	}


	public class NewViewHolder{

		public ImageView userIcon;
		public ImageView user_album_border;
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
		NewViewHolder holder=null;
		int weight = UnitUtil.getScreenWidthPixels(context);
		convertView=null;
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.user_info_ablum_item_layout, null);

			GridView.LayoutParams params = new GridView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			int range = UnitUtil.dip2px(28,context);
			params.width = (weight - range) / 4;
			params.height = (weight - range) / 4;
			convertView.setLayoutParams(params);

			holder=new NewViewHolder();
			holder.userIcon=(ImageView) convertView.findViewById(R.id.user_info_album_iv);
			holder.user_album_border=(ImageView) convertView.findViewById(R.id.user_album_border);

			convertView.setTag(holder);
		} else{
			holder=(NewViewHolder)convertView.getTag();
		}



		FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) holder.userIcon.getLayoutParams();

		int range = UnitUtil.dip2px(28,context);
		params.width = (weight - range) / 4;
		params.height = (weight - range) / 4;
		holder.userIcon.setLayoutParams(params);
		holder.user_album_border.setLayoutParams(params);

		String url = list.get(position).toString();

		holder.userIcon.setScaleType(ScaleType.FIT_XY);

		if (TextUtils.equals(url, "suppose")) {
			holder.user_album_border.setVisibility(View.GONE);
			holder.userIcon.setBackgroundResource(R.drawable.edit_info_add);

		} else if(TextUtils.equals(url, "album_invite")){

			holder.user_album_border.setVisibility(View.GONE);
			holder.userIcon.setBackgroundResource(R.drawable.edit_info_album_invite);

		}else{
			holder.user_album_border.setVisibility(View.VISIBLE);

			String imageurl=ImgUtils.DealImageUrl(url,150,150);

			Glide.with(context).load(imageurl.trim()).into(holder.userIcon);

		}

		return convertView;
	}



}
