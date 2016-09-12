package cn.chono.yopper.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.lidroid.xutils.util.LogUtils;

import java.io.File;
import java.util.List;

import cn.chono.yopper.R;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.data.VideoMusicList;
import cn.chono.yopper.utils.ImageTools;
import cn.chono.yopper.utils.ImgUtils;
import cn.chono.yopper.utils.UnitUtil;
import cn.chono.yopper.utils.video.VideoFileUtils;

/**
 * Created by zxb on 2015/10/30.
 */
public class VideoMusicAdapter extends BaseAdapter{


    @Override
    public int getCount() {
        return vmList == null ? 0 : vmList.size();
    }

    @Override
    public Object getItem(int position) {
        return vmList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int posi, View convertView, ViewGroup parent) {
        final VMViewHolder holder;
        if (convertView ==null) {
            holder = new VMViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.video_music_layout, null);
            holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
            holder.icon = (ImageView) convertView.findViewById(R.id.icon);
            holder.textView = (TextView) convertView.findViewById(R.id.textView);
            holder.linearLayout= (LinearLayout) convertView.findViewById(R.id.linearLayout);




            convertView.setTag(holder);
        }else{
            holder= (VMViewHolder) convertView.getTag();
        }


        FrameLayout.LayoutParams GridViewParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.WRAP_CONTENT);
            int weight = UnitUtil.getScreenWidthPixels(context)-UnitUtil.dip2px(20,context);

            GridViewParams.width = (weight) / 4;
            GridViewParams.height=(weight) / 4;
            holder.icon.setLayoutParams(GridViewParams);


//


        holder.textView.setText(vmList.get(posi).getName());

        holder.progressBar.setTag(vmList.get(posi).getId());

        String url = vmList.get(posi).getCoverImgUrl();
        String imageurl = ImgUtils.DealImageUrl(url, YpSettings.IMG_SIZE_150, YpSettings.IMG_SIZE_150);

        Glide.with(context).load(imageurl).error(R.drawable.error_default_icon).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String s, Target<GlideDrawable> target, boolean b) {
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable glideDrawable, String s, Target<GlideDrawable> target, boolean b, boolean b1) {

                Bitmap bitmap = ImageTools.drawableToBitmap(glideDrawable);
                LogUtils.e("---------------------------onResourceReady   posi------------------------------"+posi);
                if (bitmap != null) {
                    if (onItmeClickListener != null) {
                        onItmeClickListener.onItmeBitmapSave(bitmap, vmList.get(posi).getName(), vmList.get(posi).getId());
                    }
                }
                return false;
            }
        }).centerCrop().into(holder.icon);


//        final   boolean isVideoMuiacFile = vmList.get(posi).isMuiscFilseDataPath();
        //去掉final修饰
        boolean isVideoMuiacFile = vmList.get(posi).isMuiscFilseDataPath();
        if (!isVideoMuiacFile) {//判断是否已经在本地
            if (onItmeClickListener != null) {
                holder.icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ( !vmList.get(posi).isMuiscFilseDataPath()){
                            holder.progressBar.setVisibility(View.VISIBLE);
                            onItmeClickListener.onItmeOnClick(posi);

                        }


                    }
                });

            }




        } else {//已经在本地了

            holder.progressBar.setVisibility(View.GONE);
            if (onItmeClickListener !=null){
                holder.icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                         File dir=new File(VideoFileUtils.getVideoMuiacFilePath(vmList.get(posi).getName(), vmList.get(posi).getId())+File.separator+"audio.mp3");

                         onItmeClickListener.onPlayNativeMusic(dir.getPath());

                    }
                });
            }

        }
//        GridView.LayoutParams f_layoutParams = new GridView.LayoutParams(
//                GridView.LayoutParams.MATCH_PARENT,
//                GridView.LayoutParams.MATCH_PARENT);
//        if ((posi % 4) == 0) {
//            holder.linearLayout.setPadding(8, 4, 4, 4);
//        } else if ((posi % 4) == 1) {
//            holder.linearLayout.setPadding(4, 4, 4, 4);
//        }else if ((posi % 4) == 2) {
//            holder.linearLayout.setPadding(4, 4, 4, 4);
//        } else if ((posi % 4) == 3) {
//            holder.linearLayout.setPadding(4, 4, 8, 4);
//        }else{
//            LogUtils.e("---------------------------o------------------------------");
//        }
//        holder.linearLayout.setLayoutParams(f_layoutParams);

        return convertView;
    }

    public interface OnItmeClickListener {
        public void onItmeOnClick(int position);

        public void onItmeBitmapSave(Bitmap bitmap, String name, int id);

        public void onPlayNativeMusic(String musicPath);
    }

    public void setOnItmeClickListener(OnItmeClickListener onItmeClickListener) {
        this.onItmeClickListener = onItmeClickListener;
    }



    private OnItmeClickListener onItmeClickListener;

    public VideoMusicAdapter(Context context) {
        this.context = context;
    }



    public void setData(List<VideoMusicList.VideoMusic> vmList) {

        this.vmList = vmList;
        svaeMuiscData();
    }


    public void svaeMuiscData(){
        for (int i=0;i<vmList.size();i++){
            boolean isVideoMuiacFile = VideoFileUtils.isVideoMuiacFile(vmList.get(i).getName(), vmList.get(i).getId());
            vmList.get(i).setIsMuiscFilseDataPath(isVideoMuiacFile);
        }
    }

    private Context context;
    private List<VideoMusicList.VideoMusic> vmList;

  private   class VMViewHolder {

        private ImageView icon;

        private ProgressBar progressBar;

        private TextView textView;

        private LinearLayout linearLayout;





    }

}
