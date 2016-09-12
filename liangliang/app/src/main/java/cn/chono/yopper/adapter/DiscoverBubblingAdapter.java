package cn.chono.yopper.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import cn.chono.yopper.R;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.BubblingList;
import cn.chono.yopper.data.LoginUser;
import cn.chono.yopper.glide.CropCircleTransformation;
import cn.chono.yopper.utils.CheckUtil;
import cn.chono.yopper.utils.ISO8601;
import cn.chono.yopper.utils.ImgUtils;
import cn.chono.yopper.utils.TimeUtil;
import cn.chono.yopper.utils.UnitUtil;
import cn.chono.yopper.utils.ViewsUtils;
import cn.chono.yopper.view.vewgroup.YPGridView;
import cn.chono.yopper.view.vewgroup.YPGridView.OnTouchInvalidPositionListener;

public class DiscoverBubblingAdapter extends BaseRecyclerAdapter<DiscoverBubblingAdapter.BViewHolder> {


    private List<BubblingList> list;

    private Context context;

    private BitmapPool mPool;

    private Drawable praise_number_icon;

    private Drawable praise_icon_no;

    private CropCircleTransformation transformation;

    public DiscoverBubblingAdapter(Context context) {

        this.context = context;

        mPool = Glide.get(context).getBitmapPool();

        transformation = new CropCircleTransformation(mPool);

        praise_number_icon = context.getResources().getDrawable(R.drawable.praise_number_icon);

        praise_icon_no = context.getResources().getDrawable(R.drawable.praise_icon_no);

    }

    public void setData(List<BubblingList> list) {
        this.list = list;
    }

    public void addData(List<BubblingList> addlist) {
        if (list == null) {
            list = new ArrayList<BubblingList>();
        }
        list.addAll(addlist);

    }

    public List<BubblingList> getData() {
        return list;
    }


    @Override
    public BViewHolder getViewHolder(View view) {
        return new BViewHolder(view, false);
    }

    @Override
    public int getAdapterItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public BViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.discover_bubbling_content_layout, parent, false);

        return new BViewHolder(view, true);
    }

    @Override
    public void onBindViewHolder(final BViewHolder holder, final int position, boolean isItem) {
        BubblingList dto = list.get(position);

        switch (dto.getUser().getCurrentUserPosition()) {
            case 0:
                //不是VIP
                holder.bubbing_vip_iv.setVisibility(View.GONE);
                break;

            case 1:
                //白银VIP
                holder.bubbing_vip_iv.setVisibility(View.VISIBLE);
                holder.bubbing_vip_iv.setBackgroundResource(R.drawable.ic_small_vip_silver);
                break;

            case 2:
                //黄金VIP
                holder.bubbing_vip_iv.setVisibility(View.VISIBLE);
                holder.bubbing_vip_iv.setBackgroundResource(R.drawable.ic_small_vip_gold);
                break;

            case 3:
                //铂金VIP
                holder.bubbing_vip_iv.setVisibility(View.VISIBLE);
                holder.bubbing_vip_iv.setBackgroundResource(R.drawable.ic_small_vip_platinum);
                break;

            case 4:
                //钻石VIP
                holder.bubbing_vip_iv.setVisibility(View.VISIBLE);
                holder.bubbing_vip_iv.setBackgroundResource(R.drawable.ic_small_vip_diamond);
                break;

        }


        if (position == 0) {
            holder.discover_bubbing_item_top_line.setVisibility(View.GONE);
        } else {
            holder.discover_bubbing_item_top_line.setVisibility(View.VISIBLE);
        }

        String uresUrl = dto.getUser().getHeadImg();
        if (!CheckUtil.isEmpty(uresUrl)) {
            String uresIcoUrl = ImgUtils.DealImageUrl(uresUrl, YpSettings.IMG_SIZE_150, YpSettings.IMG_SIZE_150);
            Glide.with(context).load(uresIcoUrl).bitmapTransform(transformation).placeholder(R.drawable.error_user_icon).error(R.drawable.error_user_icon).into(holder.userIcon);
        }
        if (!CheckUtil.isEmpty(dto.getUser().getName())) {
            holder.nameTv.setText(dto.getUser().getName());
        }
        int sex = dto.getUser().getSex();

        if (sex == 1) {
            Drawable sexDrawable = context.getResources().getDrawable(R.drawable.ic_sex_man);
            sexDrawable.setBounds(0, 0, sexDrawable.getMinimumWidth(), sexDrawable.getMinimumHeight());
            holder.constellationTv.setCompoundDrawables(sexDrawable, null, null, null);
            holder.constellationTv.setTextColor(context.getResources().getColor(R.color.color_8cd2ff));

        } else if (sex == 2) {
            Drawable sexDrawable = context.getResources().getDrawable(R.drawable.ic_sex_woman);
            sexDrawable.setBounds(0, 0, sexDrawable.getMinimumWidth(), sexDrawable.getMinimumHeight());
            holder.constellationTv.setCompoundDrawables(sexDrawable, null, null, null);
            holder.constellationTv.setTextColor(context.getResources().getColor(R.color.color_fe8cd9));
        }


        long time = ISO8601.getTime(dto.getCreateTime());
        String timeStr = TimeUtil.normalTimeFormat(time);
        holder.tiemTv.setText(timeStr);
        holder.constellationTv.setText(CheckUtil.ConstellationMatching(dto.getUser().getHoroscope()));

        if (!CheckUtil.isEmpty(dto.getText())) {
            holder.contentTv.setVisibility(View.VISIBLE);
            if (dto.getSource() == 0) {
                holder.contentTv.setTextColor(context.getResources().getColor(R.color.color_000000));
            } else if (dto.getSource() == 1) {
                holder.contentTv.setTextColor(context.getResources().getColor(R.color.color_999999));
            }
            holder.contentTv.setText(dto.getText());
        } else {
            holder.contentTv.setVisibility(View.GONE);
        }

        // V3.1 冒泡类型 TextImages = 0， VideoVerification = 1 ,PrivateImage =2 ,GeneralVideo =3
        if (dto.getType() == 0 || dto.getType() == 2) {
            Logger.e(position + " DiscoverBubblingAdapter  枷锁状态 ：：" + dto.isUnlockPrivateImage());
            if (dto.getImageUrls().size() == 1) {
                holder.ypGridView.setVisibility(View.GONE);
                holder.bubbing_layout.setVisibility(View.VISIBLE);
                holder.bubbling_video_layout.setVisibility(View.GONE);
                String imgurl = dto.getImageUrls().get(0);
                if (!CheckUtil.isEmpty(imgurl)) {
                    String dealimgurl = ImgUtils.DealImageUrl(imgurl, YpSettings.IMG_SIZE_300, YpSettings.IMG_SIZE_300);
                    Glide.with(context).load(dealimgurl).placeholder(R.drawable.error_default_icon).centerCrop().error(R.drawable.error_default_icon).into(holder.bubbling_one_img_iv);
                    if (dto.isUnlockPrivateImage()) {//false 加密  true 不加密
                        holder.bubbling_one_private_img_iv.setVisibility(View.GONE);
                    } else {
                        holder.bubbling_one_private_img_iv.setVisibility(View.VISIBLE);
                    }
                } else {
                    holder.bubbing_layout.setVisibility(View.GONE);
                }

                holder.bubbling_one_img_iv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickLitener.onIconItemClick(holder.bubbling_one_img_iv, position, dto.getImageUrls(), holder.bubbling_one_img_iv.getWidth(), holder.bubbling_one_img_iv.getHeight());
                    }
                });

            } else {
                holder.ypGridView.setVisibility(View.VISIBLE);
                holder.bubbling_one_private_img_iv.setVisibility(View.GONE);
                holder.bubbing_layout.setVisibility(View.GONE);
                holder.bubbling_video_layout.setVisibility(View.GONE);

                holder.iconAdapter = new DiscoverBubblingIconAdapter(context);
                holder.iconAdapter.setOnIconItemClickLitener(mOnItemClickLitener);
                holder.ypGridView.setAdapter(holder.iconAdapter);
                holder.iconAdapter.setData(dto, position);
                holder.iconAdapter.notifyDataSetChanged();
            }

        } else if (dto.getType() == 1 || dto.getType() == 3) {
            holder.ypGridView.setVisibility(View.GONE);
            holder.bubbing_layout.setVisibility(View.GONE);
            holder.bubbling_video_layout.setVisibility(View.VISIBLE);
            holder.bubbling_video_coverimg_iv.setVisibility(View.VISIBLE);
            holder.bubbling_video_hint_iv.setVisibility(View.VISIBLE);

            String imgurl = dto.getImageUrls().get(0);

            Logger.e("显示视频的Url ====" + imgurl + "       userid == " + dto.getUser().getId() + " type ==  " + dto.getType());
            if (!CheckUtil.isEmpty(imgurl)) {


                String dealimgurl = ImgUtils.DealVideoImageUrl(imgurl, YpSettings.IMG_SIZE_300, YpSettings.IMG_SIZE_300);

                Glide.with(context).load(dealimgurl).placeholder(R.drawable.error_default_icon).centerCrop().error(R.drawable.error_default_icon).into(holder.bubbling_video_coverimg_iv);

                holder.bubbling_video_hint_iv.setBackgroundResource(R.drawable.video_play_icon);

            } else {

                holder.bubbling_video_layout.setVisibility(View.GONE);
            }

            holder.bubbling_video_layout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    ViewsUtils.preventViewMultipleClick(v, 1000);

                    Logger.e("点击了冒泡视频");
//                    if(dto.getType() == 1){//认证视频
//
//                    }else if(dto.getType() == 3){//形象视频

                    mOnItemClickLitener.checkLookVideo(holder.bubbling_video_layout, position, LoginUser.getInstance().getUserId(), dto.getUser().getId(), dto.getVideoId(), dto.getType());

//                    }

                }
            });
        }


        final BubblingList.Location location = dto.getLocation();
        if (location != null) {
            Integer locationID = location.getId();
            String typeUrl = location.getTypeImgUrl();
            if (!CheckUtil.isEmpty(typeUrl) && !CheckUtil.isEmpty(location.getName())) {

                holder.typeLayout.setVisibility(View.VISIBLE);
                String typeIcoUrl = ImgUtils.DealImageUrl(typeUrl, YpSettings.IMG_SIZE_150, YpSettings.IMG_SIZE_150);
                if (locationID == null || locationID <= 0) {
                    holder.typeIcon.setBackgroundResource(R.drawable.discover_type_icon_no);
                    Glide.with(context).load(typeIcoUrl).into(holder.typeIcon);
                } else {
                    holder.typeIcon.setBackgroundResource(R.drawable.discover_type_icon);
                    Glide.with(context).load(typeIcoUrl).bitmapTransform(transformation).into(holder.typeIcon);
                }
                holder.typeTv.setText(location.getName() + "");
            } else {
                holder.typeLayout.setVisibility(View.GONE);
            }
        } else {
            holder.typeLayout.setVisibility(View.GONE);
        }

        holder.locationNumberTv.setText(CheckUtil.getSpacingTool(dto.getDistance()));
        holder.praiseNumberTv.setText(dto.getTotalLikes() + "");
        holder.evaluateNumberTv.setText(dto.getTotalComments() + "");
        final boolean isLiked = dto.isLiked();

        if (isLiked) {
            // / 这一步必须要做,否则不会显示.
            praise_number_icon.setBounds(0, 0, praise_number_icon.getMinimumWidth(), praise_number_icon.getMinimumHeight());
            holder.praiseNumberTv.setCompoundDrawables(praise_number_icon, null, null, null);
        } else {
            // / 这一步必须要做,否则不会显示.
            praise_icon_no.setBounds(0, 0, praise_icon_no.getMinimumWidth(), praise_icon_no.getMinimumHeight());
            holder.praiseNumberTv.setCompoundDrawables(praise_icon_no, null, null, null);
        }

        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {

            holder.bubble_item_layout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Logger.e("选择的BulddingList == " + dto.toString());
                    mOnItemClickLitener.onItemClick(holder.bubble_item_layout, position, dto.getId(), dto);
                }
            });
            holder.praiseNumberTv.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    String id = list.get(position).getId();
                    mOnItemClickLitener.onPraiseClick(holder.praiseNumberTv, position, isLiked, id);

                }
            });
            holder.evaluateNumberTv.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onEvaluateClick(holder.evaluateNumberTv, position, dto.getId(), dto);

                }
            });

            holder.userIcon.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onUserIconItemClick(holder.userIcon, position, dto.getUser().getId());

                }
            });
            holder.typeLayout.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onTypeLayoutItemClick(holder.typeLayout, position, location, dto.getId(), dto);
                }
            });

            holder.bubbling_one_private_img_iv.setOnClickListener(v -> {
                Logger.e("点击私密的discoverbubblingadapter item ==  " + position);
                mOnItemClickLitener.onCoveringAblumClick(holder.bubbling_one_private_img_iv, LoginUser.getInstance().getUserId(), dto.getUser().getId(), true, position);
            });
        }
    }


    public interface OnItemClickLitener {
        /**
         * 整条Item点击监听事件
         *
         * @param view
         * @param position
         */
        void onUserIconItemClick(View view, int position, int userID);

        /**
         * 整条Item点击监听事件
         *
         * @param view
         * @param position
         */
        void onItemClick(View view, int position, String bubblingID, BubblingList bubblingList);

        /**
         * 点赞点击监听事件
         *
         * @param view
         * @param position
         */
        void onPraiseClick(View view, int position, boolean isLide, String id);

        /**
         * 评价点击监听事件
         *
         * @param view
         * @param position
         */
        void onEvaluateClick(View view, int position, String bubblingID, BubblingList bubblingList);

        /**
         * 内容图片点击监听
         *
         * @param view
         * @param position
         */
        void onIconItemClick(View view, int position, List<String> list, int imgViewWidth, int imgViewHight);

        void onTypeLayoutItemClick(View view, int position, BubblingList.Location location, String bubblingID, BubblingList bubblingList);

        /**
         * 形象视频点击监听
         *
         * @param view
         * @param position
         */
        void checkLookVideo(View view, int position, int userid, int targetId, int videoid, int videoType);

        void onCoveringAblumClick(View view, int userId, int targetId, boolean islock, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


    class BViewHolder extends RecyclerView.ViewHolder {
        /**
         * 用户头像
         */
        public ImageView userIcon;
        public ImageView bubbing_vip_iv;
        /**
         * 名字
         */
        public TextView nameTv;
        /**
         * 星座
         */
        public TextView constellationTv;
        /**
         * 时间
         */
        public TextView tiemTv;
        /**
         * 内容
         */
        public TextView contentTv;
        /**
         * 图片
         */
        public YPGridView ypGridView;

        public RelativeLayout bubbing_layout;
        public ImageView bubbling_one_img_iv;
        public ImageView bubbling_one_private_img_iv;
        public RelativeLayout bubbling_video_layout;
        public ImageView bubbling_video_coverimg_iv;
        public ImageView bubbling_video_hint_iv;

        /**
         * 类型icon
         */

        public ImageView typeIcon;
        /**
         * 类型
         */
        public TextView typeTv;
        /**
         * 位置
         */
        public TextView locationNumberTv;
        /**
         * 点赞
         */
        public TextView praiseNumberTv;
        /**
         * 评价
         */
        public TextView evaluateNumberTv;

        public RelativeLayout discover_bubbling_location_layout;
        public LinearLayout bubble_item_layout;

        public LinearLayout typeLayout;

        public View discover_bubbing_item_top_line;

        private DiscoverBubblingIconAdapter iconAdapter;

        public BViewHolder(View itemView, boolean isItme) {
            super(itemView);

            if (isItme) {

                discover_bubbing_item_top_line = itemView.findViewById(R.id.discover_bubbing_item_top_line);

                bubble_item_layout = (LinearLayout) itemView.findViewById(R.id.bubble_item_layout);
                userIcon = (ImageView) itemView.findViewById(R.id.userIcon);

                bubbing_vip_iv = (ImageView) itemView.findViewById(R.id.bubbing_vip_iv);

                nameTv = (TextView) itemView.findViewById(R.id.nameTv);
                constellationTv = (TextView) itemView.findViewById(R.id.constellationTv);
                tiemTv = (TextView) itemView.findViewById(R.id.tiemTv);
                contentTv = (TextView) itemView.findViewById(R.id.contentTv);
                ypGridView = (YPGridView) itemView.findViewById(R.id.ypGridView);

                int weight = (int) (UnitUtil.getScreenWidthPixels(context) * 0.46);

                bubbing_layout = (RelativeLayout) itemView.findViewById(R.id.bubbing_layout);
                bubbling_one_img_iv = (ImageView) itemView.findViewById(R.id.bubbling_one_img_iv);
                bubbling_one_private_img_iv = (ImageView) itemView.findViewById(R.id.bubbling_one_private_img_iv);
                ViewGroup.LayoutParams imgpara = bubbling_one_img_iv.getLayoutParams();
//                ViewGroup.LayoutParams imgparaprivae = bubbling_one_img_iv.getLayoutParams();
                imgpara.height = weight;
                imgpara.width = weight;
//                imgparaprivae.height = weight;
//                imgparaprivae.width = weight;
                bubbling_one_img_iv.setLayoutParams(imgpara);
                bubbling_one_private_img_iv.setLayoutParams(imgpara);


                bubbling_video_layout = (RelativeLayout) itemView.findViewById(R.id.bubbling_video_layout);
                bubbling_video_coverimg_iv = (ImageView) itemView.findViewById(R.id.bubbling_video_coverimg_iv);
                bubbling_video_hint_iv = (ImageView) itemView.findViewById(R.id.bubbling_video_hint_iv);

                LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) bubbling_video_layout.getLayoutParams();
                linearParams.height = weight;
                linearParams.width = weight;
                bubbling_video_layout.setLayoutParams(linearParams);

                typeIcon = (ImageView) itemView.findViewById(R.id.typeIcon);
                typeTv = (TextView) itemView.findViewById(R.id.typeTv);
                locationNumberTv = (TextView) itemView.findViewById(R.id.locationNumberTv);
                praiseNumberTv = (TextView) itemView.findViewById(R.id.praiseNumberTv);
                evaluateNumberTv = (TextView) itemView.findViewById(R.id.evaluateNumberTv);

                typeLayout = (LinearLayout) itemView.findViewById(R.id.typeLayout);

                discover_bubbling_location_layout = (RelativeLayout) itemView.findViewById(R.id.discover_bubbling_location_layout);

                ypGridView.setOnTouchInvalidPositionListener(new OnTouchInvalidPositionListener() {

                    @Override
                    public boolean onTouchInvalidPosition(int motionEvent) {
                        return false;
                    }
                });
            }


        }
    }


    public void praiseIsLike(final int position, boolean isLike) {
        if (list == null) {
            return;
        }
        list.get(position).setLiked(isLike);
        int toTalLikes = list.get(position).getTotalLikes();
        if (isLike) {
            toTalLikes = toTalLikes + 1;
        } else {
            toTalLikes = toTalLikes - 1;
        }
        list.get(position).setTotalLikes(toTalLikes);
        notifyDataSetChanged();
    }

    public void unlockAblum(List<BubblingList> bubblingLists) {

        list = bubblingLists;

        notifyDataSetChanged();
    }

}
