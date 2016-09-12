package cn.chono.yopper.utils;

import android.view.View;

/**
 * Created by zxb on 2015/11/5.
 */
public interface BackCallListener {

    public void onCancel(View view, Object... obj);

    public void onEnsure(View view, Object... obj);
}
