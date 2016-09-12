package cn.chono.yopper.utils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.UUID;

/**
 * 
 * @ClassName: UUIDGenerator
 * @Description:生成唯一的UUID(这里用一句话描述这个类的作用)
 * @author: xianbin.zou
 * @date: 2015年3月26日 下午5:43:30
 * 
 */
public class UUIDGenerator {
	public UUIDGenerator() {
	}

	/**
	 * 获得一个UUID
	 * 
	 * @return String UUID
	 */
	public static String getUUID() {
		String s = UUID.randomUUID().toString();
		// 去掉“-”符号
		return s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18)
				+ s.substring(19, 23) + s.substring(24);
	}

	/**
	 * 获得指定数目的UUID
	 * 
	 * @param number
	 *            int 需要获得的UUID数量
	 * @return String[] UUID数组
	 */
	public static String[] getUUID(int number) {
		if (number < 1) {
			return null;
		}
		String[] ss = new String[number];
		for (int i = 0; i < number; i++) {
			ss[i] = getUUID();
		}
		return ss;
	}

	public static String getUUIDForm(String uuid) {
		return uuid.substring(0, 8) + uuid.substring(9, 13)
				+ uuid.substring(14, 18) + uuid.substring(19, 23)
				+ uuid.substring(24);
	}

	public static void main(String[] args) {
		ArrayList arrayList=new ArrayList();
		for (int i = 0; i < 100000; i++) {
			String uuid=getUUID();
			if (arrayList.contains(uuid)) {
				System.out.println("i=====" + i);
				System.out.println("UUID=====" + uuid);
			}else{

				arrayList.add(uuid);
			}
		}

		try {
		JSONArray array=new JSONArray();
		for (int i=0;i<array.length();i++){

				String a=array.getString(0);

		}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
