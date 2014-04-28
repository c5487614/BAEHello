package cch.util.proxy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CMCCiLearn {
	private static CMCCiLearn uniqueInstance = null;
	HttpClient httpclient = null;
	String JSESSIONID = "";
	String cmccapp1cookie = "";
	String orclSiteCookie = "CBHRMB1i1s0$";
	String randomNum = "";
	Logger logger = null;
	private CMCCiLearn(){
		httpclient = new DefaultHttpClient();
		logger = Logger.getLogger("name");
		try{
			logger.log(Level.INFO, JSESSIONID);
		}catch(Exception e){
			e.printStackTrace();
			logger.log(Level.INFO, e.getMessage());
		}
	}
	public static CMCCiLearn getInstance(){
		if(uniqueInstance==null){
			uniqueInstance = new CMCCiLearn();
		}
		return uniqueInstance;
//		return new CMCCiLearn();
	}
	
	public String openClassRoom(){
		httpclient = new DefaultHttpClient();
		String url = "http://cmuonline.chinamobile.com/ilearn/en/learner/jsp/attempt.jsp?action=start&rco_id=-1&classroom_id=99445398";
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair("loginType", "N"));
		HttpGet httpget = new HttpGet(url);
		httpget.setHeader("Cookie","oracle.ila.siteCookie=CBHRMB1i1s0$;JSESSIONID="+JSESSIONID+";cmccapp1cookie="+cmccapp1cookie);
		try{
			HttpResponse response = httpclient.execute(httpget);
			int statusCode = response.getStatusLine().getStatusCode();
			logger.log(Level.INFO, "statusCode : " + statusCode);
			String retValue = this.Response2String(response,"E:/10086open1.txt");
			
			url = "http://cmuonline.chinamobile.com/ilearn/en/learner/jsp/player/player_redirect.jsp?rco_id=98707575&classroom_id=99445398";
			httpclient = new DefaultHttpClient();
			httpget = new HttpGet(url);
			httpget.setHeader("Cookie","oracle.ila.siteCookie=CBHRMB1i1s0$;JSESSIONID="+JSESSIONID+";cmccapp1cookie="+cmccapp1cookie);
			HttpResponse response1 = httpclient.execute(httpget);
			retValue = this.Response2String(response,"E:/10086open2.txt");
			return "openClass";
		}catch(Exception ex){
			
		}
		return "";
	}
	public String logout(){
		httpclient = new DefaultHttpClient();
		String url = "http://cmuonline.chinamobile.com/ilearn/en/learner/jsp/hp_authenticate.jsp?action=1201&key=f68cfaff95b7608acff9bc973fc9a65925801ce4755fba3f6eadb8edbf53274d";
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		HttpGet httpget = new HttpGet(url);
		httpget.setHeader("Cookie","oracle.ila.siteCookie=CBHRMB1i1s0$;JSESSIONID="+JSESSIONID+";cmccapp1cookie="+cmccapp1cookie);
		try{
			HttpResponse response = httpclient.execute(httpget);
			int statusCode = response.getStatusLine().getStatusCode();
			logger.log(Level.INFO, "statusCode : " + statusCode);
			String retValue = this.Response2String(response,"E:/10086logout.txt");
			
			url = "http://cmuonline.chinamobile.com/ilearn/en/learner/jsp/hp_login.jsp";
			httpget = new HttpGet(url);
			httpget.setHeader("Cookie","oracle.ila.siteCookie=CBHRMB1i1s0$;JSESSIONID="+JSESSIONID+";cmccapp1cookie="+cmccapp1cookie);
			response = httpclient.execute(httpget);
			statusCode = response.getStatusLine().getStatusCode();
			logger.log(Level.INFO, "statusCode : " + statusCode);
			retValue = this.Response2String(response,"E:/10086logout1.txt");
			
			url = "http://cmuonline.chinamobile.com/ilearn/en/cst/jsp/s/front/s_login.jsp ";
			httpget = new HttpGet(url);
			httpget.setHeader("Cookie","oracle.ila.siteCookie=CBHRMB1i1s0$;JSESSIONID="+JSESSIONID+";cmccapp1cookie="+cmccapp1cookie);
			response = httpclient.execute(httpget);
			statusCode = response.getStatusLine().getStatusCode();
			logger.log(Level.INFO, "statusCode : " + statusCode);
			retValue = this.Response2String(response,"E:/10086logout2.txt");
			
			return "logout";
		}catch(Exception ex){
			
		}
		return "";
	}
	public String getUserProfile(){
		httpclient = new DefaultHttpClient();
		//
		//http://cmuonline.chinamobile.com/ilearn/en/learner/jsp/hp_user_profile.jsp
		String url = "http://cmuonline.chinamobile.com/ilearn/en/learner/jsp/hp_personalContent.jsp";
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		//nvps.add(new BasicNameValuePair("loginType", "N"));
		HttpGet httpget = new HttpGet(url);
		httpget.setHeader("Cookie","JSESSIONID="+JSESSIONID+";cmccapp1cookie="+cmccapp1cookie+";113112083=%5B%7B%22infoName%22%3A%22%E5%A6%82%E4%BD%95%E5%AF%BB%E6%89%BE%E4%B8%8E%E5%AE%A2%E6%88%B7%E7%9A%84%E5%85%B1%E5%90%8C%E8%AF%9D%E9%A2%98%22%2C%22enterId%22%3A%22156286313%22%2C%22type%22%3A%22CL%22%2C%22clickTime%22%3A%222014-04-23%2014%3A44%3A54%22%2C%22examType%22%3A%22%22%7D%2C%7B%22infoName%22%3A%22%E6%9C%89%E6%95%88%E8%AF%B4%E6%9C%8D%E5%AE%A2%E6%88%B7%E8%B4%AD%E4%B9%B0%E7%9A%84%E6%8A%80%E5%B7%A7%22%2C%22enterId%22%3A%22156286317%22%2C%22type%22%3A%22CL%22%2C%22clickTime%22%3A%222014-04-23%2014%3A44%3A49%22%2C%22examType%22%3A%22%22%7D%2C%7B%22infoName%22%3A%22%E5%A6%82%E4%BD%95%E4%B8%BA%E5%AE%A2%E6%88%B7%E7%AE%97%E5%A5%BD%E4%B8%80%E7%AC%94%E5%B8%90%22%2C%22enterId%22%3A%22156286321%22%2C%22type%22%3A%22CL%22%2C%22clickTime%22%3A%222014-04-23%2014%3A44%3A40%22%2C%22examType%22%3A%22%22%7D%2C%7B%22infoName%22%3A%22%E5%A6%82%E4%BD%95%E4%BB%8B%E7%BB%8D%E4%BA%A7%E5%93%81%E5%AE%B9%E6%98%93%E8%8E%B7%E5%BE%97%E8%AE%A4%E5%8F%AF%22%2C%22enterId%22%3A%22156286323%22%2C%22type%22%3A%22CL%22%2C%22clickTime%22%3A%222014-04-23%2014%3A44%3A31%22%2C%22examType%22%3A%22%22%7D%2C%7B%22infoName%22%3A%22%E5%A6%82%E4%BD%95%E5%BA%94%E5%AF%B9%E5%AE%A2%E6%88%B7%E6%8B%96%E5%BB%B6%22%2C%22enterId%22%3A%22156286325%22%2C%22type%22%3A%22CL%22%2C%22clickTime%22%3A%222014-04-23%2014%3A44%3A25%22%2C%22examType%22%3A%22%22%7D%2C%7B%22infoName%22%3A%22%E5%A6%82%E4%BD%95%E6%89%AB%E9%99%A4%E5%89%8D%E5%8F%B0%E9%9A%9C%E7%A2%8D%22%2C%22enterId%22%3A%22156286331%22%2C%22type%22%3A%22CL%22%2C%22clickTime%22%3A%222014-04-23%2014%3A44%3A16%22%2C%22examType%22%3A%22%22%7D%2C%7B%22infoName%22%3A%22%22%2C%22enterId%22%3A%22%22%2C%22type%22%3A%22%22%2C%22clickTime%22%3A%22%22%2C%22examType%22%3A%22%22%7D%5D; zhhmmplusv4=mode%7Escorm%7Cbandwidth%7Elow%7C; cookieDetect=test%7Etestvalue%7C; cmccapp1cookie=cmcc_app1_5_80; oracle.ila.player=331udYAmKQFdavOmUmya1qlrvEg78DP/fBFce+lLF1kRu6h+hyQl+I2f/q1Ca6CZs2K6j0rt+fSI%B1BIsg/jW923oL27Y2AyjV/YhiotYJp0;oracle.ila.siteCookie=CBHRMB1i1s0$; ");
		try{
			HttpResponse response = httpclient.execute(httpget);
			int statusCode = response.getStatusLine().getStatusCode();
			logger.log(Level.INFO, "statusCode : " + statusCode);
			String retValue = this.Response2String(response,"E:/10086userProfile.txt");
		}catch(Exception ex){
			
		}
		return "";
	}
	
	public String loginCMCCiLearn(){
		httpclient = new DefaultHttpClient();
		String url = "http://cmuonline.chinamobile.com/ilearn/en/learner/jsp/hp_loginRedirect.jsp";
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair("loginType", "N"));
		nvps.add(new BasicNameValuePair("password", "cch119215277"));
		nvps.add(new BasicNameValuePair("siteName", "hb"));
		nvps.add(new BasicNameValuePair("username", "35032438"));
		nvps.add(new BasicNameValuePair("yzInput", randomNum));
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Cookie","oracle.ila.siteCookie=CBHRMB1i1s0$;JSESSIONID="+JSESSIONID+";cmccapp1cookie="+cmccapp1cookie);
		try{
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			HttpResponse response = httpclient.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			logger.log(Level.INFO, "statusCode : " + statusCode);
			//String retValue = this.Response2String(response);
			if(statusCode==302){
				for(Header h : response.getAllHeaders()){
					//logger.log(Level.INFO, "" + h.getName() + ":" + h.getValue());
					if(h.getName().equals("Location")){
						//http://cmuonline.chinamobile.com/ilearn/en/learner/jsp/hp_authenticate.jsp?key= 
						url = h.getValue();
						logger.log(Level.INFO, "url : " + url);
						HttpGet httpget = new HttpGet(url);
						httpget.setHeader("Cookie","oracle.ila.siteCookie=CBHRMB1i1s0$;JSESSIONID="+JSESSIONID+";cmccapp1cookie="+cmccapp1cookie);
						httpclient = new DefaultHttpClient();
						HttpResponse response1 = httpclient.execute(httpget);
//						JSESSIONID = this.getCookie(response1, "Set-Cookie", "JSESSIONID");
//						orclSiteCookie = this.getCookie(response1, "Set-Cookie", "oracle.ila.siteCookie");
//						logger.log(Level.INFO, "JSESSIONID = " + JSESSIONID);
//						logger.log(Level.INFO, "orclSiteCookie = " + orclSiteCookie);
						String retValue = this.Response2String(response1,"E:/10086key.txt");
						statusCode = response1.getStatusLine().getStatusCode();
						logger.log(Level.INFO, "statusCode123456 : " + statusCode);
						if(statusCode == 302){
							//http://cmuonline.chinamobile.com/ilearn/en/learner/jsp/hp_login_main.jsp
//							for(Header h1 : response1.getAllHeaders()){
//								if(h1.getName().equals("Location")){
									url = "http://cmuonline.chinamobile.com/ilearn/en/learner/jsp/hp_login_main.jsp";
									logger.log(Level.INFO, "url : " + url);
									httpget = new HttpGet(url);
									httpget.setHeader("Cookie","oracle.ila.siteCookie=CBHRMB1i1s0$;JSESSIONID="+JSESSIONID+";cmccapp1cookie="+cmccapp1cookie);
									httpclient = new DefaultHttpClient();
									HttpResponse response2 = httpclient.execute(httpget);
									//retValue = this.Response2String(response2);
//								}
//							}
						}
						//logger.log(Level.INFO, "retValue : " + retValue);
					}
				}
			}
			//String retValue = this.Response2String(response);
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{}
		return "true";
	}
	public String preCMCCLogin1(){
		//http://cmuonline.chinamobile.com/ilearn/en/learner/jsp/hp_login_ajaxServer.jsp
		httpclient = new DefaultHttpClient();
		String url = "http://cmuonline.chinamobile.com/ilearn/en/learner/jsp/hp_login_ajaxServer.jsp";
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair("type", "normalLogin"));
		nvps.add(new BasicNameValuePair("username", "35032438"));
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Cookie", "oracle.ila.siteCookie=CBHRMB1i1s0$;JSESSIONID="+JSESSIONID+";cmccapp1cookie="+cmccapp1cookie);
		try{
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			HttpResponse response = httpclient.execute(httpPost);
			String retValue = this.Response2String(response,"E:/10086ajaxServer.txt");
			if(response.getStatusLine().getStatusCode()==200){
//				JSESSIONID = this.getCookie(response, "Set-Cookie", "JSESSIONID");
//				cmccapp1cookie = this.getCookie(response, "Set-Cookie", "cmccapp1cookie");
//				logger.log(Level.INFO, "JSESSIONID = "+ JSESSIONID);
//				logger.log(Level.INFO, "cmccapp1cookie = "+ cmccapp1cookie);
				return "true1";
			}else{
				logger.log(Level.INFO,retValue);
			}
			
		}catch(Exception ex){
			
		}finally{}
		return "";
	}
	//
	public String preCMCCLogin0(){
		httpclient = new DefaultHttpClient();
		String url = "http://cmuonline.chinamobile.com/ilearn/en/learner/jsp/hp_login.jsp";
		
		HttpGet httpget = new HttpGet(url);
		httpget.setHeader("Cookie", "oracle.ila.siteCookie=CBHRMB1i1s0$;JSESSIONID="+JSESSIONID+";cmccapp1cookie="+cmccapp1cookie);
		try{
			HttpResponse response = httpclient.execute(httpget);
			String retValue = this.Response2String(response,"E:/10086hb_login.txt");
			if(response.getStatusLine().getStatusCode()==200){
				JSESSIONID = this.getCookie(response, "Set-Cookie", "JSESSIONID");
				cmccapp1cookie = this.getCookie(response, "Set-Cookie", "cmccapp1cookie");
				logger.log(Level.INFO, "JSESSIONID = "+ JSESSIONID);
				logger.log(Level.INFO, "cmccapp1cookie = "+ cmccapp1cookie);
				return "";
			}else{
				logger.log(Level.INFO,retValue);
			}
			
		}catch(Exception ex){
			
		}finally{}
		return "";
	}
	public String preCMCCLogin(){
		httpclient = new DefaultHttpClient();
		String url = "http://cmuonline.chinamobile.com/ilearn/en/learner/jsp/hp_login_ajaxServer.jsp";
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair("type", "randomCode"));
		HttpPost httpPost = new HttpPost(url);
		try{
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			HttpResponse response = httpclient.execute(httpPost);
			String retValue = this.Response2String(response,"E:/10086random.txt");
			if(response.getStatusLine().getStatusCode()==200){
				
				JSESSIONID = this.getCookie(response, "Set-Cookie", "JSESSIONID");
				cmccapp1cookie = this.getCookie(response, "Set-Cookie", "cmccapp1cookie");
				logger.log(Level.INFO, "JSESSIONID = "+ JSESSIONID);
				logger.log(Level.INFO, "cmccapp1cookie = "+ cmccapp1cookie);
				ObjectMapper map = new ObjectMapper();
				JsonNode node = null;
				node = map.readTree(retValue);
				randomNum = node.get("code").asText();
				return randomNum;
			}else{
				logger.log(Level.INFO,retValue);
			}
			
		}catch(Exception ex){
			
		}finally{}
		return "";
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
	private String Response2String(HttpResponse response,String filePath) throws IllegalStateException, IOException{
		int statusCode = response.getStatusLine().getStatusCode();
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
			log2File(filePath,b1);
            return new String(b1,"GBK");
		}else{
			HttpEntity entity = response.getEntity();
		    byte[] b = new byte[(int) entity.getContentLength()];
		    entity.getContent().read(b);
		    log2File(filePath,b);
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
