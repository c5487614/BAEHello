package cch.util.proxy;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

public class ProxyUtil {
	public static String go(String url) throws ClientProtocolException, IOException{
		Logger logger = Logger.getLogger("name");
		logger.log(Level.INFO, url);
		HttpClient httpclient = new DefaultHttpClient();
		String strUrl = url;
		HttpGet httpGet = new HttpGet(strUrl);
		HttpPost httpPost = new HttpPost(strUrl);
		httpPost.setHeader("Charset", "GBK");
		HttpResponse response = httpclient.execute(httpGet);
		int statusCode = response.getStatusLine().getStatusCode();
		logger.log(Level.INFO, "" + statusCode);
		if(statusCode==200){
			for(Header h : response.getAllHeaders()){
				logger.log(Level.INFO, "" + h.getName() + ":" + h.getValue());
				if(h.getName().equals("Location")){
					strUrl = h.getValue();
					return go(strUrl);
				}
			}
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
            return new String(b1,"UTF8");
		}else if(statusCode==302){
			for(Header h : response.getAllHeaders()){
				logger.log(Level.INFO, "" + h.getName() + ":" + h.getValue());
				if(h.getName().equals("Location")){
					strUrl = h.getValue();
					return go(strUrl);
				}
			}
			
		}else{
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
            return statusCode + new String(b1,"GBK");
		}
		return "";
	}
}
