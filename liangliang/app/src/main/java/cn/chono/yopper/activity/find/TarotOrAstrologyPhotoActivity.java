package cn.chono.yopper.activity.find;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import cn.chono.yopper.MainFrameActivity;
import cn.chono.yopper.R;
import cn.chono.yopper.common.YpSettings;
import cn.chono.yopper.activity.base.BigPhotoActivity;
import cn.chono.yopper.adapter.TarotAstrologyPhotoAdapter;
import cn.chono.yopper.recyclerview.AgileDividerLookup;
import cn.chono.yopper.recyclerview.DividerItemDecoration;
import cn.chono.yopper.utils.ActivityUtil;
import cn.chono.yopper.utils.OnAdapterItemClickLitener;

/**
 * 相册
 * Created by cc on 16/5/4.
 */
public class TarotOrAstrologyPhotoActivity extends MainFrameActivity implements OnAdapterItemClickLitener {

    RecyclerView tarotAstrologyPhoto_rlv;

    String[] mAlbumImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentLayout(R.layout.act_tarot_astrology_photo);

        PushAgent.getInstance(this).onAppStart();

        Bundle bundle = getIntent().getExtras();



        if (bundle.containsKey(YpSettings.PHOTO_STRING))
            mAlbumImages = bundle.getStringArray(YpSettings.PHOTO_STRING);

        initView();
    }


    @Override
    public void onResume() {

        super.onResume();
        MobclickAgent.onPageStart("塔罗占星相册"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onResume(this); // 统计时长

    }

    @Override
    public void onPause() {

        super.onPause();
        MobclickAgent.onPageEnd("塔罗占星相册"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onPause(this); // 统计时长

    }

    private void initView() {

        getGoBackLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getTvTitle().setText("相册");


        tarotAstrologyPhoto_rlv = (RecyclerView) findViewById(R.id.tarotAstrologyPhoto_rlv);

        DividerItemDecoration itemDecoration = new DividerItemDecoration();
        itemDecoration.setDividerLookup(new AgileDividerLookup());
        tarotAstrologyPhoto_rlv.addItemDecoration(itemDecoration);
        tarotAstrologyPhoto_rlv.setLayoutManager(new GridLayoutManager(this, 3));

        TarotAstrologyPhotoAdapter tarotAstrologyPhotoAdapter = new TarotAstrologyPhotoAdapter(this);

        tarotAstrologyPhotoAdapter.setData(mAlbumImages);
        tarotAstrologyPhotoAdapter.setOnAdapterItemClickLitener(this);

        tarotAstrologyPhoto_rlv.setAdapter(tarotAstrologyPhotoAdapter);

    }

    @Override
    public void onAdapterItemClick(int position, Object data) {

        Bundle bundle = new Bundle();

        bundle.putStringArray(YpSettings.PHOTO_STRING, (String[]) data);

        bundle.putInt(YpSettings.PHOTO_SUBSCRIPT, position);


        ActivityUtil.jump(TarotOrAstrologyPhotoActivity.this, BigPhotoActivity.class, bundle, 0, 100);
    }
}
