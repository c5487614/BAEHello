package cch.lottery.busi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

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
			httpPost.addHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		    httpPost.addHeader("Accept-Encoding", "gzip, deflate");
		    httpPost.addHeader("Cookie", "_lhc_uuid=sp_5295bb1a57bcc4.66649889; Hm_lvt_7b762fae745de11f6aafdc5eeebd1c43=1385544478,1385603645; Hm_lvt_9b75c2b57524b5988823a3dd66ccc8ca=1385544478,1385603645; _source=5555; _source_pid=0; _srcsig=b109a5da; Hm_lpvt_9b75c2b57524b5988823a3dd66ccc8ca=1385603922; Hm_lpvt_7b762fae745de11f6aafdc5eeebd1c43=1385603921; lehecai_request_control_stats=2");

		    response = httpclient.execute(httpPost);
		    System.out.println(response.getStatusLine());
		    HttpEntity entity = response.getEntity();
		    StringBuffer builder = new StringBuffer();
		    int responseCode = response.getStatusLine().getStatusCode();
		    if(responseCode == HttpURLConnection.HTTP_OK){
		    	InputStream content = entity.getContent();
	            BufferedReader reader = new BufferedReader(new InputStreamReader(content));
	            String line;
	            while((line = reader.readLine()) != null){
	                builder.append(line);
	            }
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
