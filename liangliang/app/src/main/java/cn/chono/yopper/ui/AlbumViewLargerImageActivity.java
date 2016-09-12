package cn.chono.yopper.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.chono.yopper.R;
import cn.chono.yopper.adapter.AlbumViewLargerImageAdapter;
import cn.chono.yopper.base.BaseActivity;
import cn.chono.yopper.data.UserPhoto;
import cn.chono.yopper.presenter.AlbumViewLargerImageContract;
import cn.chono.yopper.presenter.AlbumViewLargerImagePresenter;
import cn.chono.yopper.utils.BackCallListener;
import cn.chono.yopper.utils.DialogUtil;
import cn.chono.yopper.view.viewpager.HackyViewPager;

/**
 * Created by cc on 16/8/4.
 */
public class AlbumViewLargerImageActivity extends BaseActivity<AlbumViewLargerImagePresenter> implements AlbumViewLargerImageContract.View {

    @BindView(R.id.hacckyViewPager)
    HackyViewPager mHacckyViewPager;

    @Override
    protected int getLayout() {
        return R.layout.act_view_larger_imager;
    }

    @Override
    protected AlbumViewLargerImagePresenter getPresenter() {
        return new AlbumViewLargerImagePresenter(mContext, this);
    }


    List<UserPhoto> list;

    int position;

    int userId;

    /**
     * 初始化变量 包括intent带的数据
     */
    @Override
    protected void initVariables() {


        Bundle bundle = getIntent().getExtras();

        if (bundle.containsKey("Data"))

            list = (List<UserPhoto>) bundle.getSerializable("Data");


        if (bundle.containsKey("position"))

            position = bundle.getInt("position");

        if (bundle.containsKey("type"))

            userId = bundle.getInt("type");


        Logger.e("position===" + position);


    }


    AlbumViewLargerImageAdapter mAlbumViewLargerImageAdapter;

    /**
     * 初始化View 属性设置  初始状态等等
     */
    @Override
    protected void initView() {

        mAlbumViewLargerImageAdapter = new AlbumViewLargerImageAdapter(mContext);

        mHacckyViewPager.setAdapter(mAlbumViewLargerImageAdapter);

    }

    /**
     * 初始化数据并获取数据
     */
    @Override
    protected void initDataAndLoadData() {

        mPresenter.initDataAndLoadData(list, position, userId);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void finish() {


        super.finish();
    }

    Dialog dialog;

    @Override
    public void showCreateHintOperateDialog(String title, String content, String msg1, String msg2, BackCallListener backCallListener) {

        dialog = DialogUtil.createHintOperateDialog(mContext, title, content, msg1, msg2, backCallListener);

        if (!isFinishing())
            dialog.show();
    }

    @Override
    public void dismissCreateHintOperateDialog() {
        if (!isFinishing() && dialog != null)
            dialog.dismiss();
    }

    @Override
    public void setAdapterData(Object o, int position, int userId, int loginUserId) {

        List<UserPhoto> list = (List<UserPhoto>) o;

        mAlbumViewLargerImageAdapter.setList(list, userId, loginUserId);

        mHacckyViewPager.setAdapter(mAlbumViewLargerImageAdapter);

        mHacckyViewPager.setCurrentItem(position);

        mAlbumViewLargerImageAdapter.notifyDataSetChanged();


        Logger.e("setAdapterData=" + list.toString());


    }

    @Override
    public void showDisCoverNetToast(String msg) {

        if (TextUtils.isEmpty(msg)) {

            DialogUtil.showDisCoverNetToast(mContext);

        } else {

            DialogUtil.showDisCoverNetToast(mContext, msg);
        }

    }
}
