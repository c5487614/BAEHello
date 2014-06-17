package cch.lottery.busi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import cch.util.proxy.HB10086Util;

public class LotteryUtil {
	private static LotteryUtil uniqueInstance = null;
	
	HttpClient httpclient = null;
	Logger logger = null;
	private LotteryUtil(){
		httpclient = new DefaultHttpClient();
		logger = Logger.getLogger("name");
	}
	public static LotteryUtil getInstance(){
		if(uniqueInstance == null){
			uniqueInstance = new LotteryUtil();
		}
		return uniqueInstance;
	}
	public String getLastResult(){
		HttpResponse response = null;
		try{
			String strUrl = "http://baidu.lecai.com/lottery/ajax_get_stats_omit.php?lottery_type=50&play_type=5002";
			HttpPost httpPost = new HttpPost(strUrl);
			httpPost.addHeader("Host", "baidu.lecai.com");
			httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0");
			httpPost.addHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		    httpPost.addHeader("Accept-Encoding", "deflate");
		    httpPost.addHeader("Referer", "http://baidu.lecai.com/lottery/ssq/");
		    httpPost.addHeader("deflate", "deflate");
		    httpPost.addHeader("Cookie", "_adwc=110406678%23http%253A%252F%252Fbaidu.lecai.com%252Flottery%252Fssq%252F; _adwp=110406678.6895533860.1402989814.1402989814.1402991875.2; _adwt=1402991875; _adwlh=110406678%23http%253A%252F%252Fbaidu.lecai.com%252Flottery%252Fssq%252F; _adwtc=t; _adwr=110406678%23directinto; _lhc_uuid=sp_539fecf73098e3.23734118; Hm_lvt_6c5523f20c6865769d31a32a219a6766=1402989818; Hm_lpvt_6c5523f20c6865769d31a32a219a6766=1402991875; Hm_lvt_9b75c2b57524b5988823a3dd66ccc8ca=1402989818; Hm_lpvt_9b75c2b57524b5988823a3dd66ccc8ca=1402991875; _adwb=110406678; lehecai_request_control_stats=2");

		    response = httpclient.execute(httpPost);
		    System.out.println(response.getStatusLine());
		    HttpEntity entity = response.getEntity();
		    StringBuffer builder = new StringBuffer();
		    int responseCode = response.getStatusLine().getStatusCode();
		    if(responseCode == HttpURLConnection.HTTP_OK){
		    	InputStream content = entity.getContent();
	            BufferedReader reader = new BufferedReader(new InputStreamReader(content,"utf-8"));
	            String line;
	            while((line = reader.readLine()) != null){
	                builder.append(line);
	            }
//		    	HB10086Util hb10086Util = HB10086Util.getInstance();
//		    	return hb10086Util.Response2String(response);
		    }
		    return builder.toString();
		}catch(Exception e){
			e.printStackTrace();
			logger.log(Level.INFO, e.getMessage());
		}finally{
			try {
				response.getEntity().getContent().close();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				logger.log(Level.INFO, e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
}
