package cn.chono.yopper.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.chono.yopper.R;
import cn.chono.yopper.entity.DatingRequirment;
import cn.chono.yopper.view.MyDialog;

/**
 * 系统提示对话框
 *
 * @author SQ
 */
public class DialogUtil {

    private static Context context;

    private static Toast toast;

    public static void setContext(Context ctx) {
        context = ctx;
        toast = new Toast(context);
    }

    /*
     * 适用于通用对话框的初始化事件和销毁事件监听接口
     */
    public interface DialogEventListener {
        /**
         * @param contentView 对话框的内容区
         * @param dialog      对话框
         */
        public void onInit(View contentView, PopupWindow dialog);
    }


    /**
     * 显示提示框，判断时间 防止--（顶部弹出）
     */
    public static void showTopToast(Context ctx, String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        if (!ActivityUtil.isOnForeground(ctx)) {
            return;
        }
        makeTopToast(ctx, msg);
    }


    /**
     * 制作Toast
     *
     * @param ctx
     * @param msg
     * @return
     */
    public static void makeTopToast(Context ctx, String msg) {
        // 自定义Toast内容

        Display mDisplay = ((Activity) ctx).getWindowManager()
                .getDefaultDisplay();


        View contentview = View.inflate(ctx, R.layout.toast_layout, null);
        contentview.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));

        TextView text = (TextView) contentview
                .findViewById(R.id.toast_content_tv);
        text.setLayoutParams(new LayoutParams(mDisplay.getWidth() - 100,
                LayoutParams.WRAP_CONTENT));

        text.setText(msg);
        // toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 150);
        // toast.setMargin(40, 40);
        toast.setDuration(Toast.LENGTH_SHORT);
        // toast.se
        toast.setView(contentview);
        toast.show();

    }


    /**
     * 显示提示框，判断时间 防止
     */
    public static void showDisCoverNetToast(Activity ctx) {

        if (ctx == null) {
            return;
        }
        if (ctx.isFinishing()) {
            return;
        }

        if (!ActivityUtil.isOnForeground(ctx)) {
            return;
        }
        makeDisCoverNetToast(ctx);
    }


    /**
     * 显示提示框，判断时间 防止
     */
    public static void showDisCoverNetToast(Activity ctx, String str) {

        if (ctx == null) {
            return;
        }
        if (ctx.isFinishing()) {
            return;
        }

        if (!ActivityUtil.isOnForeground(ctx)) {
            return;
        }
        makeDisCoverNetToast(ctx, str);
    }


    public static void makeDisCoverNetToast(Context ctx, String str) {
        // 自定义Toast内容

        Display mDisplay = ((Activity) ctx).getWindowManager()
                .getDefaultDisplay();

        View contentview = View.inflate(ctx, R.layout.discover_net_toast_layout, null);

        TextView tv = (TextView) contentview.findViewById(R.id.toast_tv);
        tv.setText(str);

        contentview.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(contentview);
        toast.show();

    }


    /**
     * 制作Toast
     *
     * @param ctx
     * @param
     * @return
     */
    public static void makeDisCoverNetToast(Context ctx) {
        // 自定义Toast内容

        Display mDisplay = ((Activity) ctx).getWindowManager()
                .getDefaultDisplay();

        View contentview = View.inflate(ctx, R.layout.discover_net_toast_layout, null);
        contentview.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(contentview);
        toast.show();

    }

    /**
     * 显示P果发送状态的提示框，判断时间 防止
     */
    public static void showPsendToast(Context ctx, int type) {

        if (!ActivityUtil.isOnForeground(ctx)) {
            return;
        }
        makeshowPsendToast(ctx, type);
    }

    /**
     * 制作Toast
     *
     * @param ctx
     * @param
     * @return
     */
    public static void makeshowPsendToast(Context ctx, int type) {
        // 自定义Toast内容

        Display mDisplay = ((Activity) ctx).getWindowManager().getDefaultDisplay();

        View contentview = View
                .inflate(ctx, R.layout.p_send_toast_layout, null);
        contentview.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));

        ImageView p_send_iv = (ImageView) contentview.findViewById(R.id.p_send_iv);

        switch (type) {
            case 1:
                p_send_iv.setBackgroundResource(R.drawable.p_send_not_enough);
                break;
            case 2:
                p_send_iv.setBackgroundResource(R.drawable.p_send_net_error);
                break;
            case 3:
                p_send_iv.setBackgroundResource(R.drawable.p_send_server_error);
                break;
            default:
                break;
        }
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(contentview);
        toast.show();

    }

    /**
     * 显示位置需要设置
     *
     * @param context
     * @param
     * @return
     */
    public static Dialog hineDialog(Context context, View Layout, int gravity,
                                    int Animations) {

        return hineDialog(context, -1, Layout, gravity, Animations);
    }

    /**
     * 显示位置需要设置
     *
     * @param context
     * @param
     * @return
     */
    public static Dialog hineDialog(Context context, View Layout, int gravity) {

        return hineDialog(context, -1, Layout, gravity, -1);
    }


    /**
     * 居中显示
     *
     * @param context
     * @param Layout
     * @return
     */
    public static Dialog hineDialog(Context context, View Layout) {

        return hineDialog(context, -1, Layout, -1, -1);
    }

    /**
     * @param context    上下文对象
     * @param
     * @param gravity    Gravity.CENTER||Gravity.BOTTOM||Gravity.TOP
     * @param Animations 启动的动画
     * @return
     */
    public static Dialog hineDialog(Context context, int styleID, View Layout,
                                    int gravity, int Animations) {
        final Dialog dialog;
        if (styleID <= 0) {
            dialog = new Dialog(context, R.style.MyDialog);

        } else {
            dialog = new Dialog(context, styleID);

        }

        Window window = dialog.getWindow();

        if (gravity <= 0) {
            window.setGravity(Gravity.CENTER);

        } else {
            window.setGravity(gravity);

        }

        if (Animations != -1) {
            window.setWindowAnimations(Animations); // 添加动画
        }

        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = (int) (display.getWidth());
        dialog.getWindow().setAttributes(params);

        dialog.onWindowAttributesChanged(params);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setContentView(Layout);

        return dialog;
    }


    public static Dialog createHotHintDialog(Context context, final BackCallListener backCallListener) {

        final Dialog dialog = new MyDialog(context, R.style.MyDialog, R.layout.dialog_hot_hint_layout, new MyDialog.DialogEventListener() {

            @Override
            public void onInit(View contentView) {

                final TextView dlg_hot_hint_sure_tv = (TextView) contentView.findViewById(R.id.dlg_hot_hint_sure_tv);

                final TextView dlg_hot_hint_cancel_tv = (TextView) contentView.findViewById(R.id.dlg_hot_hint_cancel_tv);

                // 点击保存按钮
                dlg_hot_hint_sure_tv.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ViewsUtils.preventViewMultipleClick(v, 1000);
                        if (backCallListener != null) {
                            backCallListener.onEnsure(dlg_hot_hint_sure_tv, 1);
                        }


                    }

                });

                dlg_hot_hint_cancel_tv.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ViewsUtils.preventViewMultipleClick(v, 1000);
                        if (backCallListener != null) {
                            backCallListener.onCancel(dlg_hot_hint_cancel_tv, 1);
                        }


                    }

                });


            }
        });

        return dialog;
    }


    public static Dialog createHintOperateDialog(Context context, final String hinttitle, final String content, final String cancel_str, final String ensure_str, final BackCallListener backCallListener) {

        final Dialog dialog = new MyDialog(context, R.style.MyDialog, R.layout.my_hint_operate_dialog_layout, new MyDialog.DialogEventListener() {

            @Override
            public void onInit(View contentView) {

                TextView my_dialog_hint_title = (TextView) contentView.findViewById(R.id.my_dialog_hint_title);
                TextView my_dialog_hint_content = (TextView) contentView.findViewById(R.id.my_dialog_hint_content);
                final TextView my_dialog_hint_ensure = (TextView) contentView.findViewById(R.id.my_dialog_hint_ensure);
                final TextView my_dialog_hint_cancel = (TextView) contentView.findViewById(R.id.my_dialog_hint_cancel);

                my_dialog_hint_content.setText(content);

                if (!CheckUtil.isEmpty(ensure_str)) {
                    my_dialog_hint_ensure.setText(ensure_str);
                }

                if (CheckUtil.isEmpty(hinttitle)) {
                    my_dialog_hint_title.setVisibility(View.GONE);
                } else {
                    my_dialog_hint_title.setVisibility(View.VISIBLE);
                    my_dialog_hint_title.setText(hinttitle);
                }

                if (CheckUtil.isEmpty(cancel_str)) {
                    my_dialog_hint_cancel.setVisibility(View.GONE);
                } else {
                    my_dialog_hint_cancel.setVisibility(View.VISIBLE);
                    my_dialog_hint_cancel.setText(cancel_str);
                }


                // 点击取消按钮
                my_dialog_hint_cancel.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ViewsUtils.preventViewMultipleClick(v, 1000);

                        if (backCallListener != null) {
                            backCallListener.onCancel(my_dialog_hint_cancel, 2);
                        }
                    }
                });


                // 点击保存按钮
                my_dialog_hint_ensure.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ViewsUtils.preventViewMultipleClick(v, 1000);
                        if (backCallListener != null) {
                            backCallListener.onEnsure(my_dialog_hint_ensure, 1);
                        }


                    }

                });

            }
        });

        dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        dialog.setCancelable(false);
        return dialog;
    }


    public static Dialog createHintOperateDialog(Context context, final String hinttitle, final CharSequence content, final String cancel_str, final String ensure_str, final BackCallListener backCallListener) {

        final Dialog dialog = new MyDialog(context, R.style.MyDialog, R.layout.my_hint_operate_dialog_layout, new MyDialog.DialogEventListener() {

            @Override
            public void onInit(View contentView) {

                TextView my_dialog_hint_title = (TextView) contentView.findViewById(R.id.my_dialog_hint_title);
                TextView my_dialog_hint_content = (TextView) contentView.findViewById(R.id.my_dialog_hint_content);
                final TextView my_dialog_hint_ensure = (TextView) contentView.findViewById(R.id.my_dialog_hint_ensure);
                final TextView my_dialog_hint_cancel = (TextView) contentView.findViewById(R.id.my_dialog_hint_cancel);

                my_dialog_hint_content.setText(content);

                if (!CheckUtil.isEmpty(ensure_str)) {
                    my_dialog_hint_ensure.setText(ensure_str);
                }

                if (CheckUtil.isEmpty(hinttitle)) {
                    my_dialog_hint_title.setVisibility(View.GONE);
                } else {
                    my_dialog_hint_title.setVisibility(View.VISIBLE);
                    my_dialog_hint_title.setText(hinttitle);
                }

                if (CheckUtil.isEmpty(cancel_str)) {
                    my_dialog_hint_cancel.setVisibility(View.GONE);
                } else {
                    my_dialog_hint_cancel.setVisibility(View.VISIBLE);
                    my_dialog_hint_cancel.setText(cancel_str);
                }

                // 点击保存按钮
                my_dialog_hint_ensure.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ViewsUtils.preventViewMultipleClick(v, 1000);
                        if (backCallListener != null) {
                            backCallListener.onEnsure(my_dialog_hint_ensure, 1);
                        }


                    }

                });
                // 点击取消按钮
                my_dialog_hint_cancel.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ViewsUtils.preventViewMultipleClick(v, 1000);

                        if (backCallListener != null) {
                            backCallListener.onCancel(my_dialog_hint_cancel, 2);
                        }
                    }
                });

            }
        });

        return dialog;
    }


    /**
     * 照片提示操作-dialog
     * 可自行设置提示dialog的title 和提示内容 已经确认取消的text,也可设置是否显示title和是否显示取消控件
     *
     * @param context
     * @param
     * @param backCall
     * @return
     */

    public static Dialog createPhotoDialog(Context context, final String hinttitle, final String onestr, final String twostr, final boolean isshowSample, final BackCall backCall) {

        final Dialog dialog = new MyDialog(context, R.style.MyDialog, R.layout.select_operate_photo_dialog_layout, new MyDialog.DialogEventListener() {

            @Override
            public void onInit(View contentView) {

                TextView select_operate_dialog_title_tv = (TextView) contentView.findViewById(R.id.select_operate_dialog_title_tv);
                LinearLayout select_operate_dialog_one_layout = (LinearLayout) contentView.findViewById(R.id.select_operate_dialog_one_layout);
                LinearLayout select_operate_dialog_two_layout = (LinearLayout) contentView.findViewById(R.id.select_operate_dialog_two_layout);
                LinearLayout select_operate_dialog_photo_standard_layout = (LinearLayout) contentView.findViewById(R.id.select_operate_dialog_photo_standard_layout);

                TextView select_operate_dialog_one_tv = (TextView) contentView.findViewById(R.id.select_operate_dialog_one_tv);
                TextView select_operate_dialog_two_tv = (TextView) contentView.findViewById(R.id.select_operate_dialog_two_tv);


                select_operate_dialog_title_tv.setText(hinttitle);
                select_operate_dialog_one_tv.setText(onestr);
                select_operate_dialog_two_tv.setText(twostr);

                if (isshowSample) {
                    select_operate_dialog_photo_standard_layout.setVisibility(View.VISIBLE);
                } else {
                    select_operate_dialog_photo_standard_layout.setVisibility(View.GONE);
                }

                select_operate_dialog_one_layout.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ViewsUtils.preventViewMultipleClick(v, 1000);
                        backCall.deal(R.id.select_operate_dialog_one_layout);
                    }

                });

                select_operate_dialog_two_layout.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ViewsUtils.preventViewMultipleClick(v, 1000);
                        backCall.deal(R.id.select_operate_dialog_two_layout);
                    }
                });

            }
        });

        return dialog;
    }


    /**
     * 照片提示操作-dialog
     * 可自行设置提示dialog的title 和提示内容 已经确认取消的text,也可设置是否显示title和是否显示取消控件
     *
     * @param context
     * @param
     * @param backCall
     * @return
     */

    public static Dialog createPhotoDialog(Context context, final String hinttitle, final String onestr, final String twostr, final boolean isshowSample, final BackCallListener backCall) {

        final Dialog dialog = new MyDialog(context, R.style.MyDialog, R.layout.select_operate_photo_dialog_layout, new MyDialog.DialogEventListener() {

            @Override
            public void onInit(View contentView) {

                TextView select_operate_dialog_title_tv = (TextView) contentView.findViewById(R.id.select_operate_dialog_title_tv);
                LinearLayout select_operate_dialog_one_layout = (LinearLayout) contentView.findViewById(R.id.select_operate_dialog_one_layout);
                LinearLayout select_operate_dialog_two_layout = (LinearLayout) contentView.findViewById(R.id.select_operate_dialog_two_layout);
                LinearLayout select_operate_dialog_photo_standard_layout = (LinearLayout) contentView.findViewById(R.id.select_operate_dialog_photo_standard_layout);

                TextView select_operate_dialog_one_tv = (TextView) contentView.findViewById(R.id.select_operate_dialog_one_tv);
                TextView select_operate_dialog_two_tv = (TextView) contentView.findViewById(R.id.select_operate_dialog_two_tv);


                select_operate_dialog_title_tv.setText(hinttitle);
                select_operate_dialog_one_tv.setText(onestr);
                select_operate_dialog_two_tv.setText(twostr);

                if (isshowSample) {
                    select_operate_dialog_photo_standard_layout.setVisibility(View.VISIBLE);
                } else {
                    select_operate_dialog_photo_standard_layout.setVisibility(View.GONE);
                }

                select_operate_dialog_one_layout.setOnClickListener(v -> {

                    ViewsUtils.preventViewMultipleClick(v, 1000);

                    backCall.onCancel(v);

                });

                select_operate_dialog_two_layout.setOnClickListener(v -> {

                    ViewsUtils.preventViewMultipleClick(v, 1000);
                    backCall.onEnsure(v);
                });

            }
        });

        return dialog;
    }

    public static Dialog createGiftPhotoDialog(Context context, final String imgurl, final String giftname, final String giftcount, final String cancle, final String ensure, final BackCallListener backCall) {

        final Dialog dialog = new MyDialog(context, R.style.MyDialog, R.layout.dlg_send_gift, new MyDialog.DialogEventListener() {

            @Override
            public void onInit(View contentView) {
                ImageView gifticon = (ImageView) contentView.findViewById(R.id.send_gift_icon_iv);
                TextView send_gift_name_tv = (TextView) contentView.findViewById(R.id.send_gift_name_tv);
                TextView send_gift_applecount_tv = (TextView) contentView.findViewById(R.id.send_gift_applecount_tv);

                Glide.with(context).load(imgurl).into(gifticon);

                send_gift_name_tv.setText(giftname);

                send_gift_applecount_tv.setText("需" + giftcount + "个苹果");


                TextView send_gift_sure_tv = (TextView) contentView.findViewById(R.id.send_gift_sure_tv);
                TextView send_gift_cancel_tv = (TextView) contentView.findViewById(R.id.send_gift_cancel_tv);

                send_gift_sure_tv.setText(ensure);
                send_gift_cancel_tv.setText(cancle);


                send_gift_cancel_tv.setOnClickListener(v -> {

                    ViewsUtils.preventViewMultipleClick(v, 1000);

                    backCall.onCancel(v);

                });

                send_gift_sure_tv.setOnClickListener(v -> {

                    ViewsUtils.preventViewMultipleClick(v, 1000);
                    backCall.onEnsure(v);
                });

            }
        });

        return dialog;
    }

    /**
     * 照片提示操作-dialog
     * 可自行设置提示dialog的title 和提示内容 已经确认取消的text,也可设置是否显示title和是否显示取消控件
     *
     * @param context
     * @param
     * @param
     * @return
     */

    public static Dialog createPhotoDialog(Context context, final OnClickListener onClickListener) {

        final Dialog dialog = new MyDialog(context, R.style.MyDialog, R.layout.select_operate_photo_herd_dialog_layout, new MyDialog.DialogEventListener() {

            @Override
            public void onInit(View contentView) {

                TextView select_operate_dialog_title_tv = (TextView) contentView.findViewById(R.id.select_operate_dialog_title_tv);
                LinearLayout select_operate_dialog_one_layout = (LinearLayout) contentView.findViewById(R.id.select_operate_dialog_one_layout);
                LinearLayout select_operate_dialog_photo_standard_layout = (LinearLayout) contentView.findViewById(R.id.select_operate_dialog_photo_standard_layout);

                TextView select_operate_dialog_one_tv = (TextView) contentView.findViewById(R.id.select_operate_dialog_one_tv);
                TextView select_operate_dialog_two_tv = (TextView) contentView.findViewById(R.id.select_operate_dialog_two_tv);

                select_operate_dialog_one_layout.setOnClickListener(v -> {

                    ViewsUtils.preventViewMultipleClick(v, 1000);
                    onClickListener.onClick(v);

                });


            }
        });

        return dialog;
    }


    public static Dialog createSexDialog(Context context, final String hinttitle, final String onestr, final String twostr, final String threestr, final BackCallSex backCallSex) {

        final Dialog dialog = new MyDialog(context, R.style.MyDialog, R.layout.select_operate_dialog_layout, new MyDialog.DialogEventListener() {

            @Override
            public void onInit(View contentView) {

                final TextView select_operate_dialog_title_tv = (TextView) contentView.findViewById(R.id.select_operate_dialog_title_tv);
                LinearLayout select_operate_dialog_one_layout = (LinearLayout) contentView.findViewById(R.id.select_operate_dialog_one_layout);
                LinearLayout select_operate_dialog_two_layout = (LinearLayout) contentView.findViewById(R.id.select_operate_dialog_two_layout);
                LinearLayout select_operate_dialog_three_layout = (LinearLayout) contentView.findViewById(R.id.select_operate_dialog_three_layout);
                LinearLayout select_operate_dialog_four_layout = (LinearLayout) contentView.findViewById(R.id.select_operate_dialog_four_layout);

                final TextView select_operate_dialog_one_tv = (TextView) contentView.findViewById(R.id.select_operate_dialog_one_tv);
                final TextView select_operate_dialog_two_tv = (TextView) contentView.findViewById(R.id.select_operate_dialog_two_tv);
                final TextView select_operate_dialog_three_tv = (TextView) contentView.findViewById(R.id.select_operate_dialog_three_tv);
                final TextView select_operate_dialog_four_tv = (TextView) contentView.findViewById(R.id.select_operate_dialog_four_tv);


                select_operate_dialog_title_tv.setText(hinttitle);
                select_operate_dialog_one_tv.setText(onestr);
                select_operate_dialog_two_tv.setText(twostr);
                select_operate_dialog_three_tv.setText(threestr);
//                select_operate_dialog_four_tv.setText(fourstr);


                select_operate_dialog_four_layout.setVisibility(View.GONE);

                select_operate_dialog_one_layout.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ViewsUtils.preventViewMultipleClick(v, 1000);
                        if (null != backCallSex) {
                            backCallSex.onAllPeopleLayout(select_operate_dialog_one_tv);
                        }

                    }

                });

                select_operate_dialog_two_layout.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ViewsUtils.preventViewMultipleClick(v, 1000);
                        if (null != backCallSex) {
                            backCallSex.onMenLayout(select_operate_dialog_two_tv);
                        }

                    }
                });

                select_operate_dialog_three_layout.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ViewsUtils.preventViewMultipleClick(v, 1000);
                        if (null != backCallSex) {
                            backCallSex.onWomenLayout(select_operate_dialog_three_tv);
                        }

                    }
                });
//                select_operate_dialog_four_layout.setOnClickListener(new OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        ViewsUtils.preventViewMultipleClick(v, 1000);
//                        if (null != backCallSex) {
//                            backCallSex.onFourLayout(select_operate_dialog_four_tv);
//                        }
//
//                    }
//                });


            }
        });

        return dialog;
    }

    /**
     * 冒泡举报
     *
     * @param context
     * @param hinttitle
     * @param onestr
     * @param twostr
     * @param threestr
     * @param backCall
     * @return
     */
    public static Dialog createBubbleReportDialog(Context context, final String hinttitle, final String onestr, final String twostr, final String threestr, final BackCall backCall) {

        final Dialog dialog = new MyDialog(context, R.style.MyDialog, R.layout.select_operate_dialog_layout, new MyDialog.DialogEventListener() {

            @Override
            public void onInit(View contentView) {

                TextView select_operate_dialog_title_tv = (TextView) contentView.findViewById(R.id.select_operate_dialog_title_tv);
                LinearLayout select_operate_dialog_one_layout = (LinearLayout) contentView.findViewById(R.id.select_operate_dialog_one_layout);
                LinearLayout select_operate_dialog_two_layout = (LinearLayout) contentView.findViewById(R.id.select_operate_dialog_two_layout);
                LinearLayout select_operate_dialog_three_layout = (LinearLayout) contentView.findViewById(R.id.select_operate_dialog_three_layout);

                TextView select_operate_dialog_one_tv = (TextView) contentView.findViewById(R.id.select_operate_dialog_one_tv);
                TextView select_operate_dialog_two_tv = (TextView) contentView.findViewById(R.id.select_operate_dialog_two_tv);
                TextView select_operate_dialog_three_tv = (TextView) contentView.findViewById(R.id.select_operate_dialog_three_tv);


                select_operate_dialog_title_tv.setText(hinttitle);
                select_operate_dialog_one_tv.setText(onestr);
                select_operate_dialog_two_tv.setText(twostr);
                select_operate_dialog_three_tv.setText(threestr);


                select_operate_dialog_one_layout.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ViewsUtils.preventViewMultipleClick(v, 1000);
                        backCall.deal(R.id.select_operate_dialog_one_layout);
                    }

                });

                select_operate_dialog_two_layout.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ViewsUtils.preventViewMultipleClick(v, 1000);
                        backCall.deal(R.id.select_operate_dialog_two_layout);
                    }
                });

                select_operate_dialog_three_layout.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ViewsUtils.preventViewMultipleClick(v, 1000);
                        backCall.deal(R.id.select_operate_dialog_three_layout);
                    }
                });


            }
        });

        return dialog;
    }

    public static Dialog sendPguoDialog(Context context, final String oneStr, final String twoStr, final String threeStr, final String fourStr, final BackCall backCall) {

        final Dialog dialog = new MyDialog(context, R.style.MyDialog, R.layout.view_send_pguo_layout, new MyDialog.DialogEventListener() {
            @Override
            public void onInit(View contentView) {
                ImageView cancle_send_apple_iv = (ImageView) contentView.findViewById(R.id.cancle_send_apple_iv);

                TextView send_one_apple_tv = (TextView) contentView.findViewById(R.id.send_one_apple_tv);
                TextView send_five_apple_tv = (TextView) contentView.findViewById(R.id.send_five_apple_tv);
                TextView send_twenty_apple_tv = (TextView) contentView.findViewById(R.id.send_twenty_apple_tv);
                TextView send_one_hundred_apple_tv = (TextView) contentView.findViewById(R.id.send_one_hundred_apple_tv);

                send_one_apple_tv.setText(oneStr);
                send_five_apple_tv.setText(twoStr);
                send_twenty_apple_tv.setText(threeStr);
                send_one_hundred_apple_tv.setText(fourStr);

                cancle_send_apple_iv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ViewsUtils.preventViewMultipleClick(v, 1000);
                        backCall.deal(R.id.cancle_send_apple_iv);
                    }
                });

                send_one_apple_tv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ViewsUtils.preventViewMultipleClick(v, 1000);
                        backCall.deal(R.id.send_one_apple_tv);
                    }
                });

                send_five_apple_tv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ViewsUtils.preventViewMultipleClick(v, 1000);
                        backCall.deal(R.id.send_five_apple_tv);
                    }
                });

                send_twenty_apple_tv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ViewsUtils.preventViewMultipleClick(v, 1000);
                        backCall.deal(R.id.send_twenty_apple_tv);
                    }
                });

                send_one_hundred_apple_tv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ViewsUtils.preventViewMultipleClick(v, 1000);
                        backCall.deal(R.id.send_one_hundred_apple_tv);
                    }
                });
            }
        });
        return dialog;
    }

    /**
     * @param context
     * @param hinttitle
     * @param onestr
     * @param twostr
     * @param threestr
     * @param backCall
     * @return
     */
    public static Dialog createOperateDialog(Context context, final String hinttitle, final String onestr, final String twostr, final String threestr, final boolean onegone, final boolean twogone, final boolean threegone, final BackCall backCall) {

        final Dialog dialog = new MyDialog(context, R.style.MyDialog, R.layout.select_operate_dialog_layout, new MyDialog.DialogEventListener() {

            @Override
            public void onInit(View contentView) {

                TextView select_operate_dialog_title_tv = (TextView) contentView.findViewById(R.id.select_operate_dialog_title_tv);
                LinearLayout select_operate_dialog_one_layout = (LinearLayout) contentView.findViewById(R.id.select_operate_dialog_one_layout);
                LinearLayout select_operate_dialog_two_layout = (LinearLayout) contentView.findViewById(R.id.select_operate_dialog_two_layout);
                LinearLayout select_operate_dialog_three_layout = (LinearLayout) contentView.findViewById(R.id.select_operate_dialog_three_layout);

                TextView select_operate_dialog_one_tv = (TextView) contentView.findViewById(R.id.select_operate_dialog_one_tv);
                TextView select_operate_dialog_two_tv = (TextView) contentView.findViewById(R.id.select_operate_dialog_two_tv);
                TextView select_operate_dialog_three_tv = (TextView) contentView.findViewById(R.id.select_operate_dialog_three_tv);

                select_operate_dialog_title_tv.setText(hinttitle);
                select_operate_dialog_one_tv.setText(onestr);
                select_operate_dialog_two_tv.setText(twostr);
                select_operate_dialog_three_tv.setText(threestr);

                if (onegone) {
                    select_operate_dialog_one_layout.setVisibility(View.GONE);
                } else {
                    select_operate_dialog_one_layout.setVisibility(View.VISIBLE);
                }

                if (twogone) {
                    select_operate_dialog_two_layout.setVisibility(View.GONE);
                } else {
                    select_operate_dialog_two_layout.setVisibility(View.VISIBLE);
                }

                if (threegone) {
                    select_operate_dialog_three_layout.setVisibility(View.GONE);
                } else {
                    select_operate_dialog_three_layout.setVisibility(View.VISIBLE);
                }


                select_operate_dialog_one_layout.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ViewsUtils.preventViewMultipleClick(v, 1000);
                        backCall.deal(R.id.select_operate_dialog_one_layout);
                    }

                });

                select_operate_dialog_two_layout.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ViewsUtils.preventViewMultipleClick(v, 1000);
                        backCall.deal(R.id.select_operate_dialog_two_layout);
                    }
                });

                select_operate_dialog_three_layout.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ViewsUtils.preventViewMultipleClick(v, 1000);
                        backCall.deal(R.id.select_operate_dialog_three_layout);
                    }
                });


            }
        });

        return dialog;
    }


    /**
     * 网络加载。。。。
     *
     * @throws
     * @Title: LoadingDialog
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param: @param context
     * @param: @return
     * @return: Dialog
     */

    public static Dialog LoadingDialog(Context context, String hingstr) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.loading_dialog, null);

        TextView loadtv = (TextView) view.findViewById(R.id.loading_tv);
        if (hingstr == null) {
            loadtv.setVisibility(View.GONE);
        } else {
            loadtv.setVisibility(View.VISIBLE);
            loadtv.setText(hingstr);
        }
        final Dialog dialog = new Dialog(context, R.style.dialog_BOT_style_no);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);

        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = (int) (display.getWidth());
        dialog.getWindow().setAttributes(params);

        dialog.onWindowAttributesChanged(params);
        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                        | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.setContentView(view);


        return dialog;

    }

    public static Dialog LoadingDialog(Context context) {


        return LoadingDialog(context, null);

    }

    /**
     * 设置分享面板
     *
     * @param context
     * @param backCall
     * @return
     */

    public static Dialog createShareDialog(Context context, final BackCall backCall) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.setting_app_share_panel, null);

        final Dialog dialog = new Dialog(context, R.style.dialog_BOT_style);

        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.setWindowAnimations(R.style.dialogBOT_style); // 添加动画
        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = (int) (display.getWidth());
        dialog.getWindow().setAttributes(params);

        dialog.onWindowAttributesChanged(params);
        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                        | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(view);

        // 获得按钮

        LinearLayout share_sina_weibo_layout = (LinearLayout) view.findViewById(R.id.setting_share_to_sina_weibo);
        LinearLayout share_qq_layout = (LinearLayout) view.findViewById(R.id.setting_share_to_qq);
        LinearLayout share_qq_zone_layout = (LinearLayout) view.findViewById(R.id.setting_share_to_qq_zone);
        // 分享微信好友
        LinearLayout share_weixin_layout = (LinearLayout) view.findViewById(R.id.setting_share_to_weixin);
        // 分享到朋友圈
        LinearLayout share_wx_friend_layout = (LinearLayout) view.findViewById(R.id.setting_share_to_weixin_friend);

        LinearLayout share_cancel_layout = (LinearLayout) view.findViewById(R.id.setting_share_cancel_layout);

        share_sina_weibo_layout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                backCall.deal(R.id.setting_share_to_sina_weibo);
                dialog.dismiss();

            }
        });
        share_qq_layout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                backCall.deal(R.id.setting_share_to_qq);
                dialog.dismiss();
            }
        });
        share_qq_zone_layout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                backCall.deal(R.id.setting_share_to_qq_zone);
                dialog.dismiss();
            }
        });

        share_weixin_layout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                backCall.deal(R.id.setting_share_to_weixin);
                dialog.dismiss();
            }
        });

        share_wx_friend_layout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                backCall.deal(R.id.setting_share_to_weixin_friend);
                dialog.dismiss();
            }
        });

        share_cancel_layout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                backCall.deal(R.id.setting_share_cancel_layout);
                dialog.dismiss();
            }
        });
        return dialog;

    }


    /**
     * 每日登陆
     *
     * @param context
     * @param num
     * @return
     */
    public static Dialog createDailyhintDialog(Context context, String num) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.daily_hint_layout, null);

        final Dialog dialog = new Dialog(context, R.style.dialog_BOT_style);

        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.getDecorView().setPadding(0, 0, 0, 0);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(view);

        // 获得按钮
        TextView daily_hint_num_tv = (TextView) view.findViewById(R.id.daily_hint_num_tv);

        daily_hint_num_tv.setText(num + "");

        TextView daily_hint_sure_tv = (TextView) view.findViewById(R.id.daily_hint_sure_tv);

        daily_hint_sure_tv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        return dialog;

    }


    /**
     * 不能发布约会 资料补全提示
     *
     * @param context
     * @return
     */
    public static Dialog createNotCanPublishDatingHintDialog(Context context, List<DatingRequirment> list, String sureStr, final BackCallListener backCallListener) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.cannot_publish_dating_hint_layout, null);

        final Dialog dialog = new Dialog(context, R.style.dialog_BOT_style);

        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.getDecorView().setPadding(0, 0, 0, 0);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(view);

        // 获得按钮


        TextView can_not_publish_dating_sure_tv = (TextView) view.findViewById(R.id.can_not_publish_dating_sure_tv);
        LinearLayout can_not_publish_info_layout = (LinearLayout) view.findViewById(R.id.can_not_publish_info_layout);

        can_not_publish_dating_sure_tv.setText(sureStr);

        if (can_not_publish_info_layout.getChildCount() > 0) {
            can_not_publish_info_layout.removeAllViews();
        }

        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                View info_view = inflater.inflate(R.layout.can_not_publish_hint_detail_layout, null);
                ImageView can_not_publish_dating_iv = (ImageView) info_view.findViewById(R.id.can_not_publish_dating_iv);
                TextView can_not_publish_dating_tv = (TextView) info_view.findViewById(R.id.can_not_publish_dating_tv);
                DatingRequirment dto = list.get(i);
                if (dto.isReady()) {
                    can_not_publish_dating_iv.setBackgroundResource(R.drawable.cannot_publish_dating_ready_icon);
                } else {
                    can_not_publish_dating_iv.setBackgroundResource(R.drawable.cannot_publish_dating_not_ready_icon);
                }
                if (!CheckUtil.isEmpty(dto.getMsg())) {
                    can_not_publish_dating_tv.setText(dto.getMsg());
                }
                can_not_publish_info_layout.addView(info_view);
            }
        }

        can_not_publish_dating_sure_tv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                backCallListener.onEnsure(v);
            }
        });

        return dialog;

    }


    /**
     * 发布约会信息提示 和编辑资料成功提示
     *
     * @param context
     * @param hintstr
     * @return
     */
    public static Dialog createSuccessHintDialog(Context context, String hintstr) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.success_done_hint_layout, null);

        final Dialog dialog = new Dialog(context, R.style.dialog_BOT_style);

        TextView success_hint_tv = (TextView) view.findViewById(R.id.success_hint_tv);

        success_hint_tv.setText(hintstr);

        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.getDecorView().setPadding(0, 0, 0, 0);

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(view);
        return dialog;

    }


    public static Dialog createTravelDialog(Context context, final String hinttitle, final String onestr, final String twostr, final String threestr, final String fourstr, final String fivesstr, final String sixstr, final String sevenstr, final BackTravelCall backTravelCall) {

        final Dialog dialog = new MyDialog(context, R.style.MyDialog, R.layout.select_operate_travel_dialog_layout, new MyDialog.DialogEventListener() {

            @Override
            public void onInit(View contentView) {

                final TextView dialog_title_tv = (TextView) contentView.findViewById(R.id.dialog_title_tv);
                LinearLayout one_layout = (LinearLayout) contentView.findViewById(R.id.one_layout);
                LinearLayout two_layout = (LinearLayout) contentView.findViewById(R.id.two_layout);
                LinearLayout three_layout = (LinearLayout) contentView.findViewById(R.id.three_layout);
                LinearLayout four_layout = (LinearLayout) contentView.findViewById(R.id.four_layout);
                LinearLayout fives_layout = (LinearLayout) contentView.findViewById(R.id.fives_layout);
                LinearLayout six_layout = (LinearLayout) contentView.findViewById(R.id.six_layout);
                LinearLayout seven_layout = (LinearLayout) contentView.findViewById(R.id.seven_layout);

                final TextView one_tv = (TextView) contentView.findViewById(R.id.one_tv);
                final TextView two_tv = (TextView) contentView.findViewById(R.id.two_tv);
                final TextView three_tv = (TextView) contentView.findViewById(R.id.three_tv);
                final TextView four_tv = (TextView) contentView.findViewById(R.id.four_tv);
                final TextView fives_tv = (TextView) contentView.findViewById(R.id.fives_tv);
                final TextView six_tv = (TextView) contentView.findViewById(R.id.six_tv);
                final TextView seven_tv = (TextView) contentView.findViewById(R.id.seven_tv);


                dialog_title_tv.setText(hinttitle);


                if (!TextUtils.isEmpty(onestr)) {
                    one_layout.setVisibility(View.VISIBLE);
                    one_tv.setText(onestr);
                } else {
                    one_layout.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(twostr)) {
                    two_layout.setVisibility(View.VISIBLE);
                    two_tv.setText(twostr);
                } else {
                    two_layout.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(threestr)) {
                    three_layout.setVisibility(View.VISIBLE);
                    three_tv.setText(threestr);
                } else {
                    three_layout.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(fourstr)) {
                    four_layout.setVisibility(View.VISIBLE);
                    four_tv.setText(fourstr);
                } else {
                    four_layout.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(fivesstr)) {
                    fives_layout.setVisibility(View.VISIBLE);
                    fives_tv.setText(fivesstr);
                } else {
                    fives_layout.setVisibility(View.GONE);
                }

                if (!TextUtils.isEmpty(sixstr)) {
                    six_layout.setVisibility(View.VISIBLE);
                    six_tv.setText(sixstr);
                } else {
                    six_layout.setVisibility(View.GONE);
                }


                if (!TextUtils.isEmpty(sevenstr)) {
                    seven_layout.setVisibility(View.VISIBLE);
                    seven_tv.setText(sevenstr);
                } else {
                    seven_layout.setVisibility(View.GONE);
                }


                one_layout.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ViewsUtils.preventViewMultipleClick(v, 1000);
                        if (null != backTravelCall) {
                            backTravelCall.onOneLayout(one_tv);
                        }

                    }

                });

                two_layout.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ViewsUtils.preventViewMultipleClick(v, 1000);
                        if (null != backTravelCall) {
                            backTravelCall.onTwoLayout(two_tv);
                        }

                    }
                });

                three_layout.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ViewsUtils.preventViewMultipleClick(v, 1000);
                        if (null != backTravelCall) {
                            backTravelCall.onThreeLayout(three_tv);
                        }

                    }
                });
                four_layout.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ViewsUtils.preventViewMultipleClick(v, 1000);
                        if (null != backTravelCall) {
                            backTravelCall.onFourLayout(four_tv);
                        }

                    }
                });

                fives_layout.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ViewsUtils.preventViewMultipleClick(v, 1000);
                        if (null != backTravelCall) {
                            backTravelCall.onFivesLayout(fives_tv);
                        }
                    }
                });
                six_layout.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ViewsUtils.preventViewMultipleClick(v, 1000);
                        if (null != backTravelCall) {
                            backTravelCall.onSixLayout(six_tv);
                        }
                    }
                });
                seven_layout.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ViewsUtils.preventViewMultipleClick(v, 1000);
                        if (null != backTravelCall) {
                            backTravelCall.onSevenLayout(seven_tv);
                        }
                    }
                });


            }
        });

        return dialog;
    }

    public static Dialog createAppointDialog(Context context, final String hinttitle, final String onestr, final String twostr, final String threestr, final BackAppointCall backAppointCall) {

        final Dialog dialog = new MyDialog(context, R.style.MyDialog, R.layout.select_operate_travel_dialog_layout, new MyDialog.DialogEventListener() {

            @Override
            public void onInit(View contentView) {

                final TextView dialog_title_tv = (TextView) contentView.findViewById(R.id.dialog_title_tv);
                LinearLayout one_layout = (LinearLayout) contentView.findViewById(R.id.one_layout);
                LinearLayout two_layout = (LinearLayout) contentView.findViewById(R.id.two_layout);
                LinearLayout three_layout = (LinearLayout) contentView.findViewById(R.id.three_layout);
                LinearLayout four_layout = (LinearLayout) contentView.findViewById(R.id.four_layout);
                LinearLayout fives_layout = (LinearLayout) contentView.findViewById(R.id.fives_layout);
                LinearLayout six_layout = (LinearLayout) contentView.findViewById(R.id.six_layout);
                LinearLayout seven_layout = (LinearLayout) contentView.findViewById(R.id.seven_layout);

                final TextView one_tv = (TextView) contentView.findViewById(R.id.one_tv);
                final TextView two_tv = (TextView) contentView.findViewById(R.id.two_tv);
                final TextView three_tv = (TextView) contentView.findViewById(R.id.three_tv);
                final TextView four_tv = (TextView) contentView.findViewById(R.id.four_tv);
                final TextView fives_tv = (TextView) contentView.findViewById(R.id.fives_tv);
                final TextView six_tv = (TextView) contentView.findViewById(R.id.six_tv);
                final TextView seven_tv = (TextView) contentView.findViewById(R.id.seven_tv);

                four_layout.setVisibility(View.GONE);
                fives_layout.setVisibility(View.GONE);
                six_layout.setVisibility(View.GONE);
                seven_layout.setVisibility(View.GONE);


                dialog_title_tv.setText(hinttitle);


                if (!TextUtils.isEmpty(onestr)) {
                    one_layout.setVisibility(View.VISIBLE);
                    one_tv.setText(onestr);
                } else {
                    one_layout.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(twostr)) {
                    two_layout.setVisibility(View.VISIBLE);
                    two_tv.setText(twostr);
                } else {
                    two_layout.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(threestr)) {
                    three_layout.setVisibility(View.VISIBLE);
                    three_tv.setText(threestr);
                } else {
                    three_layout.setVisibility(View.GONE);
                }


                one_layout.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ViewsUtils.preventViewMultipleClick(v, 1000);
                        if (null != backAppointCall) {
                            backAppointCall.onOneLayout(one_tv);
                        }

                    }

                });

                two_layout.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ViewsUtils.preventViewMultipleClick(v, 1000);
                        if (null != backAppointCall) {
                            backAppointCall.onTwoLayout(two_tv);
                        }

                    }
                });

                three_layout.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ViewsUtils.preventViewMultipleClick(v, 1000);
                        if (null != backAppointCall) {
                            backAppointCall.onThreeLayout(three_tv);
                        }

                    }
                });


            }
        });

        return dialog;
    }


    /**
     * 聊天解锁 钥匙不足的提示框
     *
     * @param context
     * @param backCallListener
     * @return
     */
    public static Dialog createChatUnderKeyDialog(Context context, final BackCallListener backCallListener) {

        final Dialog dialog = new MyDialog(context, R.style.MyDialog, R.layout.dlg_chat_underkey, new MyDialog.DialogEventListener() {

            @Override
            public void onInit(View contentView) {

                final TextView dlg_chat_underkey_quick_get_tv = (TextView) contentView.findViewById(R.id.dlg_chat_underkey_quick_get_tv);
                final TextView dlg_chat_underkey_cancel_tv = (TextView) contentView.findViewById(R.id.dlg_chat_underkey_cancel_tv);


                // 点击保存按钮
                dlg_chat_underkey_quick_get_tv.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ViewsUtils.preventViewMultipleClick(v, 1000);
                        if (backCallListener != null) {
                            backCallListener.onEnsure(dlg_chat_underkey_quick_get_tv, 1);
                        }

                    }

                });
                // 点击取消按钮
                dlg_chat_underkey_cancel_tv.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ViewsUtils.preventViewMultipleClick(v, 1000);

                        if (backCallListener != null) {
                            backCallListener.onCancel(dlg_chat_underkey_cancel_tv, 2);
                        }
                    }
                });

            }
        });

        return dialog;
    }


    /**
     * 聊天解锁 钥匙不足的提示框
     *
     * @param context
     * @param left_key
     * @param backCallListener
     * @return
     */
    public static Dialog createChatBrokenKeyDialog(final Context context, final String left_key, final BackCallListener backCallListener) {

        final Dialog dialog = new MyDialog(context, R.style.MyDialog, R.layout.dlg_chat_broken_key, new MyDialog.DialogEventListener() {

            @Override
            public void onInit(View contentView) {


                ForegroundColorSpan defaultColor = new ForegroundColorSpan(context.getResources().getColor(R.color.color_666666));

                ForegroundColorSpan ff7462Color = new ForegroundColorSpan(context.getResources().getColor(R.color.color_ff7462));

                SpannableStringBuilder needBuilder = new SpannableStringBuilder("解锁需1把钥匙");

                SpannableStringBuilder haveBuilder = new SpannableStringBuilder("您现在有" + left_key + "把钥匙");

                needBuilder.setSpan(defaultColor, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                needBuilder.setSpan(ff7462Color, 3, 4, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                needBuilder.setSpan(defaultColor, 4, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                haveBuilder.setSpan(defaultColor, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                haveBuilder.setSpan(ff7462Color, 4, 4 + left_key.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                haveBuilder.setSpan(defaultColor, 4 + left_key.length(), 7 + left_key.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                TextView dlg_chat_broken_key_need_tv = (TextView) contentView.findViewById(R.id.dlg_chat_broken_key_need_tv);

                TextView dlg_chat_broken_key_have_tv = (TextView) contentView.findViewById(R.id.dlg_chat_broken_key_have_tv);

                dlg_chat_broken_key_need_tv.setText(needBuilder);

                dlg_chat_broken_key_have_tv.setText(haveBuilder);

                final TextView dlg_chat_broken_key_use_tv = (TextView) contentView.findViewById(R.id.dlg_chat_broken_key_use_tv);

                final TextView dlg_chat_broken_key_cancel_tv = (TextView) contentView.findViewById(R.id.dlg_chat_broken_key_cancel_tv);


                // 点击保存按钮
                dlg_chat_broken_key_use_tv.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ViewsUtils.preventViewMultipleClick(v, 1000);
                        if (backCallListener != null) {
                            backCallListener.onEnsure(dlg_chat_broken_key_use_tv, 1);
                        }

                    }

                });
                // 点击取消按钮
                dlg_chat_broken_key_cancel_tv.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ViewsUtils.preventViewMultipleClick(v, 1000);

                        if (backCallListener != null) {
                            backCallListener.onCancel(dlg_chat_broken_key_cancel_tv, 2);
                        }
                    }
                });

            }
        });

        return dialog;
    }


    public static Dialog createHintTimeDialog(Context context, final String name_title,
                                              final String name, final String time_title,
                                              final String time, final String price_title,
                                              final String price,
                                              final String cancel_str, final String ensure_str,
                                              final BackCallListener backCallListener) {

        final Dialog dialog = new MyDialog(context, R.style.MyDialog, R.layout.my_hint_time_dialog_layout, new MyDialog.DialogEventListener() {

            @Override
            public void onInit(View contentView) {

                TextView hint_tv_name_title = (TextView) contentView.findViewById(R.id.hint_tv_name_title);
                TextView hint_tv_name = (TextView) contentView.findViewById(R.id.hint_tv_name);


                TextView hint_tv_time_title = (TextView) contentView.findViewById(R.id.hint_tv_time_title);
                TextView hint_tv_time = (TextView) contentView.findViewById(R.id.hint_tv_time);

                TextView hint_tv_price_title = (TextView) contentView.findViewById(R.id.hint_tv_price_title);
                TextView hint_tv_price = (TextView) contentView.findViewById(R.id.hint_tv_price);

                final TextView my_dialog_hint_ensure = (TextView) contentView.findViewById(R.id.my_dialog_hint_ensure);
                final TextView my_dialog_hint_cancel = (TextView) contentView.findViewById(R.id.my_dialog_hint_cancel);


                hint_tv_name_title.setText(name_title);
                hint_tv_name.setText(name);
                hint_tv_time_title.setText(time_title);
                hint_tv_time.setText(time);
                hint_tv_price_title.setText(price_title);
                hint_tv_price.setText(price);


                if (!CheckUtil.isEmpty(ensure_str)) {
                    my_dialog_hint_ensure.setText(ensure_str);
                }


                if (CheckUtil.isEmpty(cancel_str)) {
                    my_dialog_hint_cancel.setVisibility(View.GONE);
                } else {
                    my_dialog_hint_cancel.setVisibility(View.VISIBLE);
                    my_dialog_hint_cancel.setText(cancel_str);
                }


                // 点击取消按钮
                my_dialog_hint_cancel.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ViewsUtils.preventViewMultipleClick(v, 1000);

                        if (backCallListener != null) {
                            backCallListener.onCancel(my_dialog_hint_cancel, 2);
                        }
                    }
                });


                // 点击保存按钮
                my_dialog_hint_ensure.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ViewsUtils.preventViewMultipleClick(v, 1000);
                        if (backCallListener != null) {
                            backCallListener.onEnsure(my_dialog_hint_ensure, 1);
                        }


                    }

                });

            }
        });

        return dialog;
    }

    /**
     * 拉黑
     *
     * @param context
     * @param title
     * @param lift
     * @param right
     * @param backCallListener
     * @return
     */
    public static Dialog BlockDialog(Context context, String title, String lift, String right, BackCallListener backCallListener) {

        // 初始化一个自定义的Dialog
        Dialog optionsDialog = new MyDialog(context, R.style.MyDialog, R.layout.select_operate_dialog_layout, new MyDialog.DialogEventListener() {

            @Override
            public void onInit(View contentView) {


                TextView select_operate_dialog_title_tv = (TextView) contentView.findViewById(R.id.select_operate_dialog_title_tv);
                LinearLayout select_operate_dialog_one_layout = (LinearLayout) contentView.findViewById(R.id.select_operate_dialog_one_layout);
                LinearLayout select_operate_dialog_two_layout = (LinearLayout) contentView.findViewById(R.id.select_operate_dialog_two_layout);
                LinearLayout select_operate_dialog_three_layout = (LinearLayout) contentView.findViewById(R.id.select_operate_dialog_three_layout);

                TextView select_operate_dialog_one_tv = (TextView) contentView.findViewById(R.id.select_operate_dialog_one_tv);

                TextView select_operate_dialog_two_tv = (TextView) contentView.findViewById(R.id.select_operate_dialog_two_tv);


                select_operate_dialog_title_tv.setText(title);
                select_operate_dialog_one_tv.setText(lift);
                select_operate_dialog_two_tv.setText(right);


                select_operate_dialog_one_layout.setVisibility(View.VISIBLE);
                select_operate_dialog_two_layout.setVisibility(View.VISIBLE);
                select_operate_dialog_three_layout.setVisibility(View.GONE);

                select_operate_dialog_one_layout.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {


                        backCallListener.onCancel(v, "");


                    }

                });

                select_operate_dialog_two_layout.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ViewsUtils.preventViewMultipleClick(v, 3000);

                        backCallListener.onEnsure(v, "");
                    }

                });
            }
        });
        optionsDialog.setCanceledOnTouchOutside(true);// 设置点击屏幕Dialog不消失


        return optionsDialog;


    }

    /**
     * 举报
     *
     * @param context
     * @param title
     * @param one
     * @param two
     * @param three
     * @param four
     * @param five
     * @param rePortCallListener
     * @return
     */
    public static Dialog RePortDialog(Context context, String title, String one, String two, String three, String four, String five, RePortCallListener rePortCallListener) {


        // 初始化一个自定义的Dialog
        Dialog reportDialog = new MyDialog(context, R.style.MyDialog, R.layout.select_operate_post_dialog_layout, new MyDialog.DialogEventListener() {

            @Override
            public void onInit(View contentView) {


                TextView select_operate_post_dialog_title_tv = (TextView) contentView.findViewById(R.id.select_operate_post_dialog_title_tv);
                LinearLayout select_operate_post_dialog_one_layout = (LinearLayout) contentView.findViewById(R.id.select_operate_post_dialog_one_layout);
                LinearLayout select_operate_post_dialog_two_layout = (LinearLayout) contentView.findViewById(R.id.select_operate_post_dialog_two_layout);
                LinearLayout select_operate_post_dialog_three_layout = (LinearLayout) contentView.findViewById(R.id.select_operate_post_dialog_three_layout);
                LinearLayout select_operate_post_dialog_four_layout = (LinearLayout) contentView.findViewById(R.id.select_operate_post_dialog_four_layout);
                LinearLayout select_operate_post_dialog_five_layout = (LinearLayout) contentView.findViewById(R.id.select_operate_post_dialog_five_layout);

                TextView select_operate_post_dialog_one_tv = (TextView) contentView.findViewById(R.id.select_operate_post_dialog_one_tv);

                TextView select_operate_post_dialog_two_tv = (TextView) contentView.findViewById(R.id.select_operate_post_dialog_two_tv);

                TextView select_operate_post_dialog_three_tv = (TextView) contentView.findViewById(R.id.select_operate_post_dialog_three_tv);

                TextView select_operate_post_dialog_four_tv = (TextView) contentView.findViewById(R.id.select_operate_post_dialog_four_tv);

                TextView select_operate_post_dialog_five_tv = (TextView) contentView.findViewById(R.id.select_operate_post_dialog_five_tv);

                select_operate_post_dialog_title_tv.setText(title);
                select_operate_post_dialog_one_tv.setText(one);
                select_operate_post_dialog_two_tv.setText(two);
                select_operate_post_dialog_three_tv.setText(three);
                select_operate_post_dialog_four_tv.setText(four);
                select_operate_post_dialog_five_tv.setText(five);

                select_operate_post_dialog_one_layout.setOnClickListener(v -> rePortCallListener.onOne(v, ""));

                select_operate_post_dialog_two_layout.setOnClickListener(v -> rePortCallListener.onTwo(v, ""));

                select_operate_post_dialog_three_layout.setOnClickListener(v -> rePortCallListener.onThree(v, ""));

                select_operate_post_dialog_four_layout.setOnClickListener(v -> rePortCallListener.onFour(v, ""));

                select_operate_post_dialog_five_layout.setOnClickListener(v -> rePortCallListener.onFive(v, ""));


            }
        });
        reportDialog.setCanceledOnTouchOutside(true);// 设置点击屏幕Dialog不消失


        return reportDialog;

    }

}
