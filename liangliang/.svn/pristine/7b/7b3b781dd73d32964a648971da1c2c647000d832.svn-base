package cn.chono.yopper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import cn.chono.yopper.R;

/**
 * Created by yangjinyu on 16/2/22.
 */
public class MultipleItemListAdapter extends BaseAdapter {
    public static final int TYPE_CONTENT_ONLY = 1;
    public static final int TYPE_CONTENT_IMAGE = 2;
    public static final int TYPE_CONTENT_3IMAGE = 3;
    public static final int TYPE_BIG_IMAGE = 4;
    private List<String> mList = new ArrayList<>();
    private Context mContext;
    public MultipleItemListAdapter(Context context){
        mContext = context;
    }

    @Override
    public int getItemViewType(int position) {

        return super.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        switch (type){
            case TYPE_CONTENT_ONLY:{
                ContentOnlyHolder holder = null;
                if(convertView == null){
                    holder = new ContentOnlyHolder();
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_birth_year,null);

                    convertView.setTag(holder);
                }else{
                    holder = (ContentOnlyHolder)convertView.getTag();
                }
                break;
            }
            case TYPE_CONTENT_IMAGE:{
                ContentImageHolder holder = null;
                if(convertView == null){
                    holder = new ContentImageHolder();
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_birth_year,null);

                    convertView.setTag(holder);
                }else{
                    holder = (ContentImageHolder)convertView.getTag();
                }
                break;
            }
            case TYPE_CONTENT_3IMAGE:{
                Content3ImageHolder holder = null;
                if(convertView == null){
                    holder = new Content3ImageHolder();
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_birth_year,null);

                    convertView.setTag(holder);
                }else{
                    holder = (Content3ImageHolder)convertView.getTag();
                }
                break;
            }
            case TYPE_BIG_IMAGE:{
                ContentBigImageHolder holder = null;
                if(convertView == null){
                    holder = new ContentBigImageHolder();
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_birth_year,null);

                    convertView.setTag(holder);
                }else{
                    holder = (ContentBigImageHolder)convertView.getTag();
                }
                break;
            }
        }
        return convertView;
    }

    class ContentOnlyHolder{
        public String content;//文字内容
        public String constellationFrom;//星座
        public String timeStamp;//发送时间
        public String commentNo;//评论数
        public String readNo;//阅读数
    }

    class ContentImageHolder extends ContentOnlyHolder{
        public ImageView mImage;//右边配图
    }
    class Content3ImageHolder extends ContentOnlyHolder{
        public ImageView m1Image;//连排三张图
        public ImageView m2Image;
        public ImageView m3Image;
    }
    class ContentBigImageHolder extends ContentOnlyHolder{
        public ImageView mImage;//大配图
    }
}
