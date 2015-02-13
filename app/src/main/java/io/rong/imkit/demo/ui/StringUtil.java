// Copyright (C) 2012-2015 UUZZ All rights reserved
package io.rong.imkit.demo.ui;

/** 
 * 类 名: StringUtil<br/>
 * 描 述: <br/>
 * 作 者: GW<br/>
 * 创 建： 2015年2月5日<br/>
 *
 * 历 史: (版本) 作者 时间 注释 <br/>
 */
public class StringUtil {

	/**
	 * 描 述：判定是否为空<br/>
	 * 作 者：GW<br/>
	 * 历 史: (版本) 作者 时间 注释 <br/>
	 * @param source
	 * @return
	 */
	public static boolean isEmpty(String source) {
		if (source == null || "".equals(source)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 描 述：分割字符串<br/>
	 * 作 者：GW<br/>
	 * 历 史: (版本) 作者 时间 注释 <br/>
	 * @param src
	 * @param regex
	 * @return
	 */
	public static String[] stringToArray(String src, String regex) {
		if (isEmpty(src) || isEmpty(regex)) {
			return null;
		}
		
		String[] srcs = src.split(regex);
		return srcs;
	}
}
