package cn.chono.yopper.utils;

import android.content.Context;
import android.os.Bundle;
import cn.chono.yopper.data.HeadImgBase;

/**
 * 截图去向
 * 
 * @author zxb
 * 
 */
public class CropDirectionUtil {

	public static void cropDirection(Context context, Class<?> cls,
			int requestCode, String filePaht) {

		HeadImgBase headImgBase = new HeadImgBase();
		headImgBase.setId(requestCode);
		headImgBase.setFilePath(filePaht);

		Bundle obBundle = new Bundle();
		obBundle.putSerializable("headImgBase", headImgBase);

		ActivityUtil.jumpForResult(context, cls, obBundle, requestCode, 0,
				0);
	}

}
