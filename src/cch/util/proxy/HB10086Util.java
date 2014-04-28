package cch.util.proxy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HB10086Util {
	private static HB10086Util uniqueInstance = null;
	HttpClient httpclient = null;
	String sessionId = "";
	Logger logger = null;
	public static String CW7YGvVXmx = "";
	public static String jsessionid1 = "";
	public static String cmtokenid = "";
	public static String n62fO8j8LA = "";
	public static String JSESSIONIDMY = "";
	public static String CmProvid = "hb";
	public static String CmWebtokenid = "13627110845,hb";
	public static String hhhzdrBwTU = "";
	public static String JSESSIONIDSHOPPING = "";

	public static String SAMLart = "";
	private HB10086Util(){
		httpclient = new DefaultHttpClient();
		logger = Logger.getLogger("name");
		try{
			logger.log(Level.INFO, jsessionid1);
			logger.log(Level.INFO, CW7YGvVXmx);
		}catch(Exception e){
			e.printStackTrace();
			logger.log(Level.INFO, e.getMessage());
		}
	}
	public static HB10086Util getInstance(){
		//uniqueInstance = new HB10086Util();
		if(uniqueInstance==null || "".equals(jsessionid1)){
			uniqueInstance = new HB10086Util();
		}
		return uniqueInstance;
	}
	public String login(String userId, String passWord,String rdnCode){
		//
		httpclient = new DefaultHttpClient();
		String url = "https://hb.ac.10086.cn/SSO/loginbox";
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair("accountType", "0"));
		nvps.add(new BasicNameValuePair("action", "/SSO/loginbox"));
		nvps.add(new BasicNameValuePair("continue", ""));
		nvps.add(new BasicNameValuePair("guestIP", "221.123.65.242"));
		nvps.add(new BasicNameValuePair("password", passWord));
		nvps.add(new BasicNameValuePair("passwordType", "1"));
		nvps.add(new BasicNameValuePair("service", "my"));
		nvps.add(new BasicNameValuePair("smsRandomCode", ""));
		nvps.add(new BasicNameValuePair("style", "mymobile"));
		nvps.add(new BasicNameValuePair("submitMode", "login"));
		nvps.add(new BasicNameValuePair("username", userId));
		nvps.add(new BasicNameValuePair("validateCode", rdnCode));
		
		HttpPost httpPost = new HttpPost(url);
		System.out.println("CW7YGvVXmx="+CW7YGvVXmx+";jsessionid1="+jsessionid1);
		httpPost.setHeader("cookie", "CW7YGvVXmx="+CW7YGvVXmx+";jsessionid1="+jsessionid1);
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			HttpResponse response = httpclient.execute(httpPost);
			if(response.getStatusLine().getStatusCode()==200){
				cmtokenid = getCookie(response,"Set-Cookie","cmtokenid");
			}

			String retValue = Response2String(response);
			System.out.println("cmtokenid"+cmtokenid);
			logger.log(Level.INFO, cmtokenid);
			SAMLart = getSAMLart(retValue);
			System.out.println("SAMLart="+SAMLart);
			this.notifyAction();
			this.doShoppingCart();
			System.out.println("hhhzdrBwTU="+hhhzdrBwTU);
			System.out.println("JSESSIONIDSHOPPING="+JSESSIONIDSHOPPING);
			if(hhhzdrBwTU.equals("")){
				return "false";
			}
			return "true";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.log(Level.INFO, e.getMessage());
			e.printStackTrace();
		}
		return "";
	}
	public String loginCMCCiLearn(String random){
		httpclient = new DefaultHttpClient();
		String url = "http://cmuonline.chinamobile.com/ilearn/en/learner/jsp/hp_loginRedirect.jsp";
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair("loginType", "N"));
		nvps.add(new BasicNameValuePair("password", "cch119215277"));
		nvps.add(new BasicNameValuePair("siteName", "hb"));
		nvps.add(new BasicNameValuePair("username", "35032438"));
		nvps.add(new BasicNameValuePair("yzInput", random));
		HttpPost httpPost = new HttpPost(url);
		try{
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			HttpResponse response = httpclient.execute(httpPost);
			String retValue = this.Response2String(response);
		}catch(Exception ex){
			
		}finally{}
		return null;
	}
	public String getCMCCRandom(){
		httpclient = new DefaultHttpClient();
		String url = "http://cmuonline.chinamobile.com/ilearn/en/learner/jsp/hp_login_ajaxServer.jsp";
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair("type", "randomCode"));
		HttpPost httpPost = new HttpPost(url);
		try{
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			HttpResponse response = httpclient.execute(httpPost);
			String retValue = this.Response2String(response);
			if(response.getStatusLine().getStatusCode()==200){
				ObjectMapper map = new ObjectMapper();
				JsonNode node = null;
				node = map.readTree(retValue);
				return node.get("code").asText();
			}else{
				logger.log(Level.INFO,retValue);
			}
			
		}catch(Exception ex){
			
		}finally{}
		return "";
	}
	public void notifyAction(){
		String url = "http://www.hb.10086.cn/my/notify.action?timeStamp=" + new Random();
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair("PasswordType", "1"));
		nvps.add(new BasicNameValuePair("RelayState", ""));
		nvps.add(new BasicNameValuePair("SAMLart", SAMLart));
		nvps.add(new BasicNameValuePair("errorMsg", ""));

		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Cookie","cmtokenid="+cmtokenid);
		try {
			httpclient = new DefaultHttpClient();
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			HttpResponse response = httpclient.execute(httpPost);
			if(response.getStatusLine().getStatusCode()==200){
				n62fO8j8LA = getCookie(response,"Set-Cookie","n62fO8j8LA");
				JSESSIONIDMY = getCookie(response,"Set-Cookie","JSESSIONIDMY");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String doShoppingCart(){
		String url = "http://www.hb.10086.cn/service/shoppingCart!cartNum.action?source=my";
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Cookie","cmtokenid="+cmtokenid+";n62fO8j8LA="+n62fO8j8LA+";JSESSIONIDMY="+JSESSIONIDMY);
		httpclient = new DefaultHttpClient();
		try {
			HttpResponse response = httpclient.execute(httpPost);
			if(response.getStatusLine().getStatusCode()==200){
				hhhzdrBwTU = getCookie(response,"Set-Cookie","hhhzdrBwTU");
				JSESSIONIDSHOPPING = getCookie(response,"Set-Cookie","JSESSIONIDSHOPPING");
			}
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public String doLottery(){
		String url = "http://www.hb.10086.cn/service/lottery/lottery/drawLottery.action";
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Cookie"," cmtokenid="+cmtokenid+"; n62fO8j8LA="+n62fO8j8LA+"; JSESSIONIDMY="+JSESSIONIDMY+";hhhzdrBwTU="+hhhzdrBwTU+";JSESSIONIDSHOPPING="+JSESSIONIDSHOPPING);
		try {
			HttpResponse response = httpclient.execute(httpPost);
			String retValue = this.Response2String(response);
			
			logger.log(Level.INFO, retValue);
			return retValue;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public String KeepAlive(){
		String url = "http://www.hb.10086.cn/my/balance/queryBalance.action";
		httpclient = new DefaultHttpClient();
		HttpGet httpPost = new HttpGet(url);
		httpPost.setHeader("Cookie"," cmtokenid="+cmtokenid+"; n62fO8j8LA="+n62fO8j8LA+"; JSESSIONIDMY="+JSESSIONIDMY+";hhhzdrBwTU="+hhhzdrBwTU+";JSESSIONIDSHOPPING="+JSESSIONIDSHOPPING);
		try {
			HttpResponse response = httpclient.execute(httpPost);
			String retValue = this.Response2String(response);
			
			logger.log(Level.INFO, retValue);
			return retValue;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public String goURL(String url,String method){
		httpclient = new DefaultHttpClient();
		org.apache.http.client.methods.HttpRequestBase httpGet = null;
		if(method.equals("post")){
			httpGet = new HttpPost(url);
		}else{
			httpGet = new HttpGet(url);
		}
		httpGet.setHeader("Cookie"," cmtokenid="+cmtokenid+"; n62fO8j8LA="+n62fO8j8LA+"; JSESSIONIDMY="+JSESSIONIDMY+";hhhzdrBwTU="+hhhzdrBwTU+";JSESSIONIDSHOPPING="+JSESSIONIDSHOPPING);
		try {
			HttpResponse response = httpclient.execute(httpGet);
			String value = this.Response2String(response);
			logger.log(Level.INFO, value);
			return value;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public String getSAMLart(String value){
		value = value.replaceAll("\r\n", "");
		int iBegin = value.indexOf("name=\"SAMLart\"");
		System.out.println(iBegin);
		value = value.substring(iBegin);
		iBegin = value.indexOf("value=\"");
		value = value.substring(iBegin+7);
		int iEnd = value.indexOf("\" />");
		
		value = value.substring(0, iEnd);
		return value;
	}
	
	
	private String getCookie(HttpResponse response,String headName ,String name){
		//headName = Set-Cookie
		Header[] cookieHeaders = response.getHeaders(headName);
		for(Header h : cookieHeaders){
			//cmtokenid=b67a2463bbe4466bab5ce7ef10891982@hb.ac.10086.cn
			String cookie = h.getValue().split(";")[0];
			if(cookie.indexOf("=")>0){
				String cookieName = cookie.split("=")[0];
				String value = cookie.split("=")[1];
				if(cookieName.equals(name)){
					return value;
				}
			}
		}
		return null;
	}
	public String Response2String(HttpResponse response) throws IllegalStateException, IOException{
		int statusCode = response.getStatusLine().getStatusCode();
		System.out.println(statusCode);
		if(200==statusCode){
			HttpEntity entity = response.getEntity();
			InputStream input = response.getEntity().getContent();
			byte[] b = new byte[1024*1024];
			int i=0,j = 0;
			while(i!=-1){
				i = input.read();
				b[j++] = (byte) i;
			}
			byte[] b1 = new byte[j];
			for(i=0;i<b1.length;i++){
				b1[i] = b[i];
			}
			log2File("E:\\10086.txt",b1);
            return new String(b1,"GBK");
		}else{
			HttpEntity entity = response.getEntity();
		    byte[] b = new byte[(int) entity.getContentLength()];
		    entity.getContent().read(b);
		    return new String(b,"GBK");
		}
	}
	private void log2File(String filePath,byte[] b) throws IOException{
		File file = new File(filePath);
		file.setWritable(true);
		FileOutputStream fos = new FileOutputStream(file); 
		fos.write(b);
		fos.close();
	}

}
