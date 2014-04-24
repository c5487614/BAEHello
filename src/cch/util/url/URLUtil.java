package cch.util.url;

import java.io.UnsupportedEncodingException;

public class URLUtil {
	public static String encode(String input,String encode){
		try {
			return java.net.URLEncoder.encode(input,encode);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	public static String decode(String input,String encode){
		try {
			return java.net.URLDecoder.decode(input, encode);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return input;
	}
}
