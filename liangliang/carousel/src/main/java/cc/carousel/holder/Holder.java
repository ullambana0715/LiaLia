package cc.carousel.holder;

import android.content.Context;
import android.view.View;

/**
 * Created by cc on 16/6/12.
 */
public interface Holder<T> {

    View createView(Context context);

    void UpdateUI(Context context, int position, T data);
}
