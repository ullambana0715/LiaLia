package cn.chono.yopper.view.WheelDialog;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import cn.chono.yopper.R;
import cn.chono.yopper.view.wheel.AbstractWheelTextAdapter;

/**
 * Created by cc on 16/3/24.
 */
public class WheelDialogAdapter extends AbstractWheelTextAdapter {

    private String[] list;

    protected WheelDialogAdapter(Context context, String[] list, int currentItem, int maxsize, int minsize) {
        super(context, R.layout.item_birth_year, NO_RESOURCE, currentItem, maxsize, minsize);
        this.list = list;
        setItemTextResource(R.id.tempValue);
    }

    @Override
    public View getItem(int index, View cachedView, ViewGroup parent) {
        View view = super.getItem(index, cachedView, parent);
        return view;
    }

    @Override
    public int getItemsCount() {
        return list == null ? 0 : list.length;
    }

    @Override
    protected CharSequence getItemText(int index) {
        return list[index];
    }


}
