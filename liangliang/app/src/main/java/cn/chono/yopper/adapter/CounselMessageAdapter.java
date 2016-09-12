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
import cn.chono.yopper.Service.Http.CounselorsProfile.CounselorProfileEntity;
import cn.chono.yopper.Service.Http.OnCallBackFailListener;
import cn.chono.yopper.Service.Http.OnCallBackSuccessListener;
import cn.chono.yopper.Service.Http.RespBean;
import cn.chono.yopper.Service.Http.UserInfoID.UserInfoIDBean;
import cn.chono.yopper.Service.Http.UserInfoID.UserInfoIDRespBean;
import cn.chono.yopper.Service.Http.UserInfoID.UserInfoIDService;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.BaseUser;
import cn.chono.yopper.data.MessageDto;
import cn.chono.yopper.glide.CropCircleTransformation;
import cn.chono.yopper.utils.ChatUtils;
import cn.chono.yopper.utils.DbHelperUtils;
import cn.chono.yopper.utils.ImgUtils;
import cn.chono.yopper.utils.JsonUtils;

public class CounselMessageAdapter extends BaseAdapter {


    /**
     * 这个用来填充list
     */
    private List<MessageDto> list;

    private Context mContext;

    private CropCircleTransformation transformation;

    private BitmapPool mPool;


    public CounselMessageAdapter(Context context) {
        // 初始化
        this.mContext = context;
        mPool = Glide.get(context).getBitmapPool();
        transformation = new CropCircleTransformation(mPool);
    }

    public CounselMessageAdapter(Context context, List<MessageDto> list) {
        // 初始化
        this.mContext = context;
        this.list = list;
    }

    @Override
    public int getCount() {

        return list == null ? 0 : list.size();
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

        MessageDto dto = list.get(position);

        String message = dto.getLastmessage();
        // 如果没有设置过,初始化convertView

        if (convertView == null) {
            // 获得设置的view
            convertView = LayoutInflater.from(mContext).inflate(R.layout.find_message_list_item, null);

            // 初始化holder
            holder = new ViewHolder();

            holder.icon_img_iv = (ImageView) convertView.findViewById(R.id.message_item_icon_img_iv);

            holder.name_tv = (TextView) convertView.findViewById(R.id.message_item_name_tv);

            holder.time_tv = (TextView) convertView.findViewById(R.id.message_item_time_tv);

            holder.content_tv = (TextView) convertView.findViewById(R.id.message_item_content_tv);

            holder.num_tv = (TextView) convertView.findViewById(R.id.message_item_num_tv);

            convertView.setTag(holder);

        } else {
            // 有直接获得ViewHolder
            holder = (ViewHolder) convertView.getTag();
        }


        if (dto.getNo_read_num() > 0) {

            if (dto.getNo_read_num() < 10) {
                holder.num_tv.setBackgroundResource(R.drawable.circle_messaga_num_bg);
            } else {
                holder.num_tv.setBackgroundResource(R.drawable.center_messaga_num_bg);
            }

            holder.num_tv.setVisibility(View.VISIBLE);
            holder.num_tv.setText(dto.getNo_read_num() + "");

        } else {

            holder.num_tv.setVisibility(View.GONE);

        }


        String jid = dto.getJid();

        holder.content_tv.setTextColor(mContext.getResources().getColor(R.color.color_9a9a9a));


        String content = ChatUtils.getMsgcontent(message);
        holder.content_tv.setText(content);


        CounselorProfileEntity counselDto = DbHelperUtils.getCounselInfo(Integer.valueOf(jid));
        if (counselDto != null) {

            holder.name_tv.setText(counselDto.getNickName());

            String imageurl = ImgUtils.DealImageUrl(counselDto.getAvatar(), YpSettings.IMG_SIZE_150, YpSettings.IMG_SIZE_150);

            Glide.with(mContext).load(imageurl).bitmapTransform(transformation).into(holder.icon_img_iv);
        } else {
            getUserWithID(Integer.valueOf(jid), holder.icon_img_iv, holder.name_tv);
        }

        // 时间设置
        holder.time_tv.setText(ChatUtils.msgTimeFormat(dto.getLasttime()) + "");

        return convertView;


    }


    /**
     * ViewHolder
     *
     * @Title:
     * @Description:主要是避免了不断的view获取初始化.
     * @Author:justlcw
     * @Since:2013-11-22
     * @Version:
     */
    class ViewHolder {

        public ImageView icon_img_iv;
        public TextView name_tv;
        public TextView time_tv;
        public TextView content_tv;
        public TextView num_tv;


    }


    public List<MessageDto> getList() {
        return list;
    }

    public void setList(List<MessageDto> addlist) {
        list = new ArrayList<MessageDto>();
        list.addAll(addlist);
        super.notifyDataSetChanged();
    }


    private void getUserWithID(final int userid, final ImageView head_img, final TextView name_tv) {


        UserInfoIDBean infoIDBean = new UserInfoIDBean();
        infoIDBean.setUserId(userid);

        UserInfoIDService infoIDService = new UserInfoIDService(mContext);

        infoIDService.parameter(infoIDBean);

        infoIDService.callBack(new OnCallBackSuccessListener() {
            @Override
            public void onSuccess(RespBean respBean) {
                super.onSuccess(respBean);

                UserInfoIDRespBean idRespBean = (UserInfoIDRespBean) respBean;

                BaseUser baseUser = idRespBean.getResp();
                try {

                    name_tv.setText(baseUser.getName());

                    String imageurl = ImgUtils.DealImageUrl(baseUser.getHeadImg(), YpSettings.IMG_SIZE_150, YpSettings.IMG_SIZE_150);

                    Glide.with(mContext).load(imageurl).bitmapTransform(transformation).into(head_img);


                    CounselorProfileEntity counselorProfileEntity = new CounselorProfileEntity();
                    counselorProfileEntity.setUserId(userid);
                    counselorProfileEntity.setNickName(baseUser.getName());
                    counselorProfileEntity.setAvatar(baseUser.getHeadImg());
                    String info = JsonUtils.toJson(counselorProfileEntity);
                    DbHelperUtils.saveCounselInfo(userid, info);


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new OnCallBackFailListener() {
            @Override
            public void onFail(RespBean respBean) {
                super.onFail(respBean);

                name_tv.setText("未知");


            }
        });

        infoIDService.enqueue();
    }

}
