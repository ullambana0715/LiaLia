package cn.chono.yopper.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 
 * 
 * MiListView
 * 
 * zouxianbin
 * 
 * 2014年12月31日 下午5:55:55
 * 
 * @version 1.0.0
 *
 */
public class MiListView extends ListView {

	 public MiListView(Context context) {  
	        // TODO Auto-generated method stub  
	        super(context);  
	    }  
	  
	    public MiListView(Context context, AttributeSet attrs) {  
	        // TODO Auto-generated method stub  
	        super(context, attrs);  
	    }  
	  
	    public MiListView(Context context, AttributeSet attrs, int defStyle) {  
	        // TODO Auto-generated method stub  
	        super(context, attrs, defStyle);  
	    }  
	  
	    @Override  
	    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {  
	        // TODO Auto-generated method stub  
	        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,  
	                MeasureSpec.AT_MOST);  
	        super.onMeasure(widthMeasureSpec, expandSpec);  
	    }  
}
