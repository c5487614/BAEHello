package cch.reminder.busi;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FeixinUtil {
	private static FeixinUtil uniqueInstance = null;
	HttpClient httpclient = null;
	String sessionId = "";
	Logger logger = null;
	int i = 8;
	private FeixinUtil(){
		
	}
	public String Login(String ccpSession,String random){
		httpclient = new DefaultHttpClient();
		logger = Logger.getLogger("name");
		HttpResponse response2 = null;
		try{
			String strUrl = "https://webim.feixin.10086.cn/WebIM/Login.aspx";
			HttpPost httpPost = new HttpPost(strUrl);
			List <NameValuePair> nvps = new ArrayList <NameValuePair>();
			nvps.add(new BasicNameValuePair("AccountType", "1"));
			nvps.add(new BasicNameValuePair("OnlineStatus", "400"));
			nvps.add(new BasicNameValuePair("Pwd", "119215277"));
			nvps.add(new BasicNameValuePair("UserName", "13627110845"));
			nvps.add(new BasicNameValuePair("Ccp", random));
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			httpPost.setHeader("cookie", "ccpsession="+ccpSession);
			response2 = httpclient.execute(httpPost);
		    HttpEntity entity2 = response2.getEntity();
		   
		    byte[] b = new byte[(int) entity2.getContentLength()];
		    entity2.getContent().read(b);
		    String retValue = new String(b,"GBK");
		    logger.log(Level.INFO, retValue);
		    ObjectMapper mapper = new ObjectMapper();
		    JsonNode node= mapper.readTree(retValue);
		    Header h = response2.getFirstHeader("Set-Cookie");
		    sessionId = h.getValue().split(";")[0].split("=")[1];
		    logger.log(Level.INFO, sessionId);
		    
		}
		catch(Exception e){
			logger.log(Level.INFO, e.getMessage());
		}finally{
			try {
				response2.getEntity().getContent().close();
				//response2.
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "";
	}
	public void sendMSM(String msmContent,String receivers){
		String strUrl = "https://webim.feixin.10086.cn/content/WebIM/SendSMS.aspx?Version=7";
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		
	    nvps.add(new BasicNameValuePair("Message", msmContent));
	    nvps.add(new BasicNameValuePair("Receivers", receivers));
	    nvps.add(new BasicNameValuePair("UserName", "1330441788"));
	    nvps.add(new BasicNameValuePair("ssid", sessionId));
	    HttpResponse response3 = null;
	    try{
		    HttpPost httpPost = new HttpPost(strUrl);
		    httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		    httpPost.setEntity(new UrlEncodedFormEntity(nvps,"UTF-8"));
		    byte[] b2 = new byte[(int) httpPost.getEntity().getContentLength()];
		    httpPost.getEntity().getContent().read(b2);
		    logger.log(Level.INFO, new String(b2,"UTF-8"));
		    response3 = httpclient.execute(httpPost);
			System.out.println(response3.getStatusLine());
			HttpEntity entity3 = response3.getEntity();
		    byte[] b3 = new byte[(int) entity3.getContentLength()];
		    entity3.getContent().read(b3);
		    logger.log(Level.INFO, new String(b3,"GBK"));
	    }catch(Exception e){
	    	logger.log(Level.INFO, e.getMessage());
	    }finally{
	    	try {
	    		response3.getEntity().getContent().close();
				//response3.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	}
	public void keepAlive(){
		if(i<Integer.MAX_VALUE){
			i++;
		}
		String strUrl = "https://webim.feixin.10086.cn/WebIM/GetConnect.aspx?Version=" + i;
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
	    nvps.add(new BasicNameValuePair("reported", ""));
	    nvps.add(new BasicNameValuePair("ssid", sessionId));
	    HttpResponse response3 = null;
		try{
			HttpPost httpPost = new HttpPost(strUrl);
		    httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		    response3 = httpclient.execute(httpPost);
			System.out.println(response3.getStatusLine());
			HttpEntity entity3 = response3.getEntity();
		    byte[] b3 = new byte[(int) entity3.getContentLength()];
		    entity3.getContent().read(b3);
		    logger.log(Level.INFO, new String(b3,"GBK"));
		}catch(Exception e){
	    	logger.log(Level.INFO, e.getMessage());
	    }finally{
	    	try {
	    		response3.getEntity().getContent().close();
				//response3.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	}
	public static FeixinUtil getInstance(){
		if(uniqueInstance == null){
			uniqueInstance = new FeixinUtil();
		}
		return uniqueInstance;
	}
	public static FeixinUtil relogin(){
		uniqueInstance = new FeixinUtil();
		return uniqueInstance;
	}
}
