package cn.chono.yopper.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.chono.yopper.R;
/**
 *
 * 聊天文本消息插入表情工具类
 *
 *
 * @author SQ
 *
 */
public class FaceTextUtils {

	public static  List<String> emoList=new ArrayList<String>();

	static {
		emoList.add("chat_expression_smile");
		emoList.add("chat_expression_haha");
		emoList.add("chat_expression_deyi");
		emoList.add("chat_expression_qin");
		emoList.add("chat_expression_shengqi");
		emoList.add("chat_expression_yun");

		emoList.add("chat_expression_ku");
		emoList.add("chat_expression_baituo");
		emoList.add("chat_expression_guiqiu");
		emoList.add("chat_expression_weiqu");
		emoList.add("chat_expression_nu");
		emoList.add("chat_expression_cry");

		emoList.add("chat_expression_sick");
		emoList.add("chat_expression_haipai");
		emoList.add("chat_expression_haose");
		emoList.add("chat_expression_jingkong");
		emoList.add("chat_expression_bushufu");
		emoList.add("chat_expression_del");

		emoList.add("chat_expression_han");
		emoList.add("chat_expression_wuyu");
		emoList.add("chat_expression_xiaoku");
		emoList.add("chat_expression_sleep");
		emoList.add("chat_expression_youxian");
		emoList.add("chat_expression_bizui");

		emoList.add("chat_expression_daku");
		emoList.add("chat_expression_fadai");
		emoList.add("chat_expression_haixiu");
		emoList.add("chat_expression_flower");
		emoList.add("chat_expression_chijing");
		emoList.add("chat_expression_koubi");

		emoList.add("chat_expression_dage");
		emoList.add("chat_expression_kun");
		emoList.add("chat_expression_nanguo");
		emoList.add("chat_expression_qiuda");
		emoList.add("chat_expression_tiaomei");
		emoList.add("chat_expression_del");

		emoList.add("chat_expression_tiaopi");
		emoList.add("chat_expression_kiss");
		emoList.add("chat_expression_yiwen");
		emoList.add("chat_expression_baibai");
		emoList.add("chat_expression_zhutou");
		emoList.add("chat_expression_del");

	}
	private static int[] expression_id={
			R.drawable.chat_expression_smile,
			R.drawable.chat_expression_haha,
			R.drawable.chat_expression_deyi,
			R.drawable.chat_expression_qin,
			R.drawable.chat_expression_shengqi,
			R.drawable.chat_expression_yun,

			R.drawable.chat_expression_ku,
			R.drawable.chat_expression_baituo,
			R.drawable.chat_expression_guiqiu,
			R.drawable.chat_expression_weiqu,
			R.drawable.chat_expression_nu,
			R.drawable.chat_expression_cry,

			R.drawable.chat_expression_sick,
			R.drawable.chat_expression_haipai,
			R.drawable.chat_expression_haose,
			R.drawable.chat_expression_jingkong,
			R.drawable.chat_expression_bushufu,
			R.drawable.chat_expression_del,

			R.drawable.chat_expression_han,
			R.drawable.chat_expression_wuyu,
			R.drawable.chat_expression_xiaoku,
			R.drawable.chat_expression_sleep,
			R.drawable.chat_expression_youxian,
			R.drawable.chat_expression_bizui,

			R.drawable.chat_expression_daku,
			R.drawable.chat_expression_fadai,
			R.drawable.chat_expression_haixiu,
			R.drawable.chat_expression_flower,
			R.drawable.chat_expression_chijing,
			R.drawable.chat_expression_koubi,

			R.drawable.chat_expression_dage,
			R.drawable.chat_expression_kun,
			R.drawable.chat_expression_nanguo,
			R.drawable.chat_expression_qiuda,
			R.drawable.chat_expression_tiaomei,
			R.drawable.chat_expression_del,

			R.drawable.chat_expression_tiaopi,
			R.drawable.chat_expression_kiss,
			R.drawable.chat_expression_yiwen,
			R.drawable.chat_expression_baibai,
			R.drawable.chat_expression_zhutou,

			R.drawable.chat_expression_del,

			R.drawable.chat_expression_p};

	//本地表情中文  跟上面图片id必须一一对应
	public static String[] expression_text={
			"微笑",
			"哈哈",
			"得意",
			"亲",
			"生气",
			"晕",

			"酷",
			"拜托",
			"跪求",
			"委屈",
			"怒",
			"哭",

			"生病",
			"害怕",
			"好色",
			"惊恐",
			"不舒服",
			"删除",

			"汗",
			"无语",
			"笑哭",
			"睡觉",
			"悠闲",
			"闭嘴",

			"大哭",
			"发呆",
			"害羞",
			"花",
			"吃惊",
			"抠鼻",

			"大哥",
			"困",
			"难过",
			"糗大了",
			"挑眉",
			"删除",

			"调皮",
			"吻",
			"疑问",
			"再见",
			"猪头",
			"删除",
			"P果"};

	public static SpannableString toSpannableString(Context context, String text) {
		if (!TextUtils.isEmpty(text)) {
			SpannableString spannableString = new SpannableString(text);
			int start = 0;
			Pattern pattern = Pattern.compile("("+"\\"+"[[^+"+"\\"+"["+"\\"+"]]+"+"\\"+"])", Pattern.CASE_INSENSITIVE);
			//			Pattern pattern = Pattern.compile("\\"+"[("+"\\"+"+S+?)"+"\\"+"]"); 
			Matcher matcher = pattern.matcher(text);
			while (matcher.find()) {
				String faceText = matcher.group();
				String key = faceText.substring(1,faceText.length()-1);
				//String key ="亲";
				int size=expression_text.length-1;
				for(int i=0;i<=size;i++){
					if(key.equals(expression_text[i])){
						Drawable drawable = context.getResources().getDrawable(expression_id[i]);
						ImageSpan span;
						if(key.equals("P果")){
							drawable.setBounds(0, 0, drawable.getIntrinsicWidth()/3, drawable.getIntrinsicHeight()/3);
							span = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
						}else{
							drawable.setBounds(0, 0, drawable.getIntrinsicWidth()/4, drawable.getIntrinsicHeight()/4);
							span = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);
						}

						int startIndex = text.indexOf(faceText, start);
						int endIndex = startIndex + faceText.length();
						if (startIndex >= 0)
							spannableString.setSpan(span, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						start = (endIndex - 1);

					}
				}
			}
			return spannableString;
		} else {
			return new SpannableString("");
		}
	}
}
