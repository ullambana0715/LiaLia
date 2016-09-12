package cn.chono.yopper.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.chono.yopper.R;
import cn.chono.yopper.Service.Http.Banners.BannersData;
import cn.chono.yopper.data.NearPlaceDto;
import cn.chono.yopper.utils.CheckUtil;
import cn.chono.yopper.utils.UnitUtil;

/**
 * Created by sunquan on 16/2/29.
 */
public class BannerView extends LinearLayout{

    public View layoutView;
    private LayoutInflater inflate;


    private int index=0;


    private TextView name;



    private  boolean isSelect=false;


    public BannerView(Context context, BannersData dto, int position) {
        super(context);

        inflate=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutView=inflate.inflate(R.layout.find_article_banner_view, this);

        name=(TextView) layoutView.findViewById(R.id.find_article_banner_tv);




        if(!CheckUtil.isEmpty(dto.getName()+"")){

            name.setText(dto.getName());
        }





    }
    public void setIndex(int ind){
        index = ind;
    }

    public int getIndex(){
        return index;
    }


    public void clearSelect(Context context){

        name.setTextColor(context.getResources().getColor(R.color.color_9a9a9a));

        setisSelect(false);

    }

    public void setSelect(Context context){
        name.setTextColor(context.getResources().getColor(R.color.color_ff7462));
        setisSelect(true);
    }

    public boolean getisSelect(){

        return isSelect;
    }

    public void setisSelect(boolean issele){
        isSelect=issele;
    }
}
