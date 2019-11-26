package com.reyco.shiro.core.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * 关闭工具
 * @author reyco
 *
 */
public class CloseUtils {
	/**
	 * 关闭
	 * @param closeable
	 */
	public static void close(Closeable... closeable){
		for (Closeable temp : closeable) {
			if(null != temp) {
				try {
					temp.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
