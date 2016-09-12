package cn.chono.yopper.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.hwangjr.rxbus.RxBus;
import com.orhanobut.logger.Logger;

import java.util.List;

import cn.chono.yopper.R;
import cn.chono.yopper.data.UserPhoto;
import cn.chono.yopper.event.CommonEvent;
import cn.chono.yopper.event.CommonItemEvent;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.view.viewpager.PhotoView;

/**
 * Created by cc on 16/8/4.
 */
public class AlbumViewLargerImageAdapter extends PagerAdapter {


    Context context;

    public AlbumViewLargerImageAdapter(Context context) {
        this.context = context;
    }


    List<UserPhoto> list;

    int userId;

    int loginUserId;

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    public void setList(List<UserPhoto> list, int userId, int loginUserId) {
        this.list = list;
        this.userId = userId;
        this.loginUserId = loginUserId;
    }


    @Override
    public View instantiateItem(ViewGroup container, final int position) {

        View v = LayoutInflater.from(context).inflate(R.layout.album_image_viewer_item_layout, null);

        container.addView(v);

        final ProgressBar progressBar = (ProgressBar) v.findViewById(R.id.progressBar);

        final PhotoView photoView = (PhotoView) v.findViewById(R.id.photoView);

        final TextView parise_count_tv = (TextView) v.findViewById(R.id.parise_count_tv);

        final ImageView iv_parise = (ImageView) v.findViewById(R.id.iv_parise);


        ImageView iv_delect = (ImageView) v.findViewById(R.id.iv_delect);


        UserPhoto userPhoto = (UserPhoto) list.get(position);


        progressBar.setVisibility(View.VISIBLE);


        Glide.with(context).load(userPhoto.getImageUrl()).error(R.drawable.error_default_icon).listener(new RequestListener<String, GlideDrawable>() {

            @Override
            public boolean onException(Exception arg0, String arg1, Target<GlideDrawable> arg2, boolean arg3) {

                progressBar.setVisibility(View.GONE);

                DialogUtil.showDisCoverNetToast((Activity) context);

                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable arg0, String arg1, Target<GlideDrawable> arg2, boolean arg3, boolean arg4) {

                progressBar.setVisibility(View.GONE);


                return false;
            }

        }).into(photoView);


        photoView.setOnPhotoTapListener((view, x, t) -> {


            RxBus.get().post("AlbumOnPhotoTapListenerEvent", new CommonEvent());


        });


        if (userId != loginUserId) {//登录账户不是自己时

            iv_delect.setVisibility(View.GONE);//没有删除

            parise_count_tv.setVisibility(View.VISIBLE);//有点赞

            iv_parise.setVisibility(View.VISIBLE);


            int praiseStatus = userPhoto.getPraiseStatus();

            int praiseCount = userPhoto.getPraiseCount();


            if (praiseStatus == 1) {//点赞过

                iv_parise.setBackgroundResource(R.drawable.photo_parised_icon);

            } else {


                iv_parise.setBackgroundResource(R.drawable.photo_parise_icon);


            }

            if (praiseCount == 0) {
                parise_count_tv.setVisibility(View.INVISIBLE);
            } else {
                parise_count_tv.setVisibility(View.VISIBLE);
                parise_count_tv.setText(praiseCount + "");
            }


            iv_parise.setOnClickListener(view -> {


                iv_parise.post(new Runnable() {

                    @Override
                    public void run() {


                        int umber = userPhoto.getPraiseCount();

                        if (userPhoto.getPraiseStatus() == 1) {

                            userPhoto.setPraiseStatus(2);//取消

                            umber = umber - 1;

                            iv_parise.setBackgroundResource(R.drawable.photo_parise_icon);


                        } else {

                            userPhoto.setPraiseStatus(1);//点赞

                            umber = umber + 1;


                            iv_parise.setBackgroundResource(R.drawable.photo_parised_icon);


                        }

                        parise_count_tv.setText("" + umber);

                        userPhoto.setPraiseCount(umber);


                        CommonItemEvent commonItemEvent = new CommonItemEvent();

                        commonItemEvent.event = userPhoto;

                        commonItemEvent.position = position;

                        RxBus.get().post("AlbumOnClickListenerEvent", commonItemEvent);


                    }
                });

            });


        } else {


            iv_delect.setVisibility(View.VISIBLE);//有删除

            parise_count_tv.setVisibility(View.INVISIBLE);

            iv_parise.setVisibility(View.VISIBLE);


            iv_parise.setBackgroundResource(R.drawable.photo_parised_icon);


            iv_parise.setOnClickListener(view -> {

                iv_parise.post(() -> {
                });
            });


            iv_delect.setOnClickListener(view -> {


                iv_delect.post(() -> {



                    CommonItemEvent commonItemEvent = new CommonItemEvent();

                    commonItemEvent.event = list;

                    commonItemEvent.position = position;


                    RxBus.get().post("AlbumDelectOnClickListenerEvent", commonItemEvent);

                });


            });

        }


        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


}
