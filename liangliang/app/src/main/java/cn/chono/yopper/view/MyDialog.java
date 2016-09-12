package cn.chono.yopper.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

public class MyDialog extends Dialog {

    Context context;
    public MyDialog(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        this.context = context;
    }
    public MyDialog(Context context, int theme, int layoutResourceId,final DialogEventListener listener){
        super(context, theme);
        this.context = context;
        View view = View.inflate(context, layoutResourceId, null);
        this.setContentView(view);
        if (listener != null) {
			listener.onInit(view);
		}
        
    }

    public MyDialog(Context context, int theme, int layoutResourceId){
        super(context, theme);
        this.context = context;
        View view = View.inflate(context, layoutResourceId, null);
        this.setContentView(view);

    }


    public MyDialog(Context context, int theme, View view){
        super(context, theme);
        this.context = context;
        this.setContentView(view);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        
    }

	public interface DialogEventListener {
		/**
		 * @param contentView
		 *            对话框的内容区
        框* @param dialog
		 *            对话
		 */
		public void onInit(View contentView);
	}
	
}
