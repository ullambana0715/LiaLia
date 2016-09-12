package cn.chono.yopper.recyclerview;

import android.graphics.Color;

/**
 * Created by cc on 16/3/17.
 */
public class AgileDividerLookup implements DividerItemDecoration.DividerLookup {
    @Override
    public Divider getVerticalDivider(int position) {
        return new Divider.Builder().color(Color.alpha(Color.WHITE)).size(4).build();
    }

    @Override
    public Divider getHorizontalDivider(int position) {
        return new Divider.Builder().color(Color.alpha(Color.WHITE)).size(4).build();
    }
}
