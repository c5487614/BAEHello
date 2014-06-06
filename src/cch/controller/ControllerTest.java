package cch.controller;

import java.io.IOException;
import java.security.KeyManagementException;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cch.familyaccount.model.DailyInfoReport;
import cch.lottery.busi.LotteryBusi;
import cch.reminder.busi.FeixinUtil;
import cch.util.proxy.CMCCiLearn;
import cch.util.proxy.HB10086Util;
import cch.util.proxy.ProxyUtil;
import cch.util.url.URLUtil;

@Controller
@RequestMapping("/sp")
public class ControllerTest {
	
	@RequestMapping(value = "/hello1322.do",produces="application/json;charset=UTF-8")
    public @ResponseBody String hello1322() {
		return "{result:'的撒'}";
    }
	@RequestMapping(value = "/urlencode.do",produces="application/json;charset=UTF-8")
	public @ResponseBody String URLEncode(@RequestParam("url") String url,@RequestParam("encode") String encode) {
		//System.out.println(url);
		return URLUtil.encode(url,encode);
    }
	@RequestMapping(value = "/urldecode.do",produces="application/json;charset=UTF-8")
	public @ResponseBody String URLDecode(@RequestParam("url") String url,@RequestParam("encode") String encode) {
		//System.out.println(url);
		return URLUtil.decode(url,encode);
    }
	
	@RequestMapping(value = "/lottery.do",produces="application/json;charset=UTF-8")
    public @ResponseBody String lotteryUpdate() {
		LotteryBusi lotteryBusi = new LotteryBusi();
		//insert new -> predict -> store predict
		return lotteryBusi.insertLastResult();
    }
	@RequestMapping(value = "/doPredict.do",produces="application/json;charset=UTF-8")
    public @ResponseBody String doPredict() {
		//only should be called by hand
		LotteryBusi lotteryBusi = new LotteryBusi();
		//insert new -> predict -> store predict
		return String.valueOf (lotteryBusi.doPredict());
    }
	
	@RequestMapping(value = "/lotteryAnalysize.do",produces="application/json;charset=UTF-8")
    public @ResponseBody Map<String,Object> lotteryAnalysize() {
		LotteryBusi lotteryBusi = new LotteryBusi();
		Map<String,Object> map = lotteryBusi.analysize();
		Map<String,Object> mapPredictMatch = lotteryBusi.predictMatch();
		map.put("match", mapPredictMatch);
		return map;
    }
	
	@RequestMapping(value = "/proxy.do",produces="application/json;charset=UTF-8")
    public @ResponseBody String proxy(@RequestParam("url") String url) {
		try {
			String retValue = ProxyUtil.go(url);
			return retValue;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "{result:'的撒'}";
    }
	@RequestMapping(value = "/hbpreLogin.do",produces="application/json;charset=UTF-8")
    public @ResponseBody String hbpreLogin(@RequestParam("userId") String userId,
    		@RequestParam("password") String password,
    		@RequestParam("random") String random,
    		@RequestParam("jsessionid1") String jsessionid1,
    		@RequestParam("CW7YGvVXmx") String CW7YGvVXmx) throws KeyManagementException {
		System.setProperty("javax.net.ssl.trustStore", "D:/java/java7_045/JDK/jre/lib/security/jssecacerts");
		HB10086Util hb10086Util = HB10086Util.getInstance();
		hb10086Util.jsessionid1 = jsessionid1;
		hb10086Util.CW7YGvVXmx = CW7YGvVXmx;
		return "{jsessionid1:'"+hb10086Util.jsessionid1+"',CW7YGvVXmx:'"+hb10086Util.CW7YGvVXmx+"'}";
    }
	@RequestMapping(value = "/hbLogin.do",produces="application/json;charset=UTF-8")
    public @ResponseBody String hbLogin(@RequestParam("userId") String userId,
    		@RequestParam("password") String password,
    		@RequestParam("random") String random) throws KeyManagementException {
		
		//System.setProperty("javax.net.ssl.trustStore", "D:/java/java7_045/JDK/jre/lib/security/jssecacerts");
		HB10086Util hb10086Util = HB10086Util.getInstance();
		hb10086Util.login(userId, password, random);
		return "{result:'的撒'}";
    }
	@RequestMapping(value = "/hbAction.do",produces="application/json;charset=UTF-8")
    public @ResponseBody String hbAction(@RequestParam("url") String url,
    		@RequestParam("method") String method) throws KeyManagementException {
		HB10086Util hb10086Util = HB10086Util.getInstance();
		String retValue = hb10086Util.goURL(url,method);
		return retValue;
    }
	@RequestMapping(value = "/feixinLogin.do",produces="application/json;charset=UTF-8")
    public @ResponseBody String feixinLogin(@RequestParam("random") String random,
    		@RequestParam("ccpSession") String ccpSession) throws KeyManagementException {
		FeixinUtil feixin = FeixinUtil.getInstance();
		String retValue = feixin.Login(ccpSession, random);
		return retValue;
    }
	@RequestMapping(value = "/cmcciLearnLogin.do",produces="application/json;charset=UTF-8")
    public @ResponseBody String cmcciLearnLogin() throws KeyManagementException {
		CMCCiLearn cmcciLearn = CMCCiLearn.getInstance();
		cmcciLearn.preCMCCLogin0();
		String retValue = cmcciLearn.preCMCCLogin();
		retValue = retValue + ";" + cmcciLearn.preCMCCLogin1(); 
		retValue = retValue + ";" + cmcciLearn.loginCMCCiLearn(); 
		//cmcciLearn.getUserProfile();
		return "{result:'"+retValue+"'}";
    }
	@RequestMapping(value = "/cmccGetUserInfo.do",produces="application/json;charset=UTF-8")
    public @ResponseBody String cmccGetUserInfo() throws KeyManagementException {
		CMCCiLearn cmcciLearn = CMCCiLearn.getInstance();
		String retValue = cmcciLearn.getUserProfile();
		return "{result:'"+retValue+"'}";
    }
	@RequestMapping(value = "/cmccOpenClass.do",produces="application/json;charset=UTF-8")
    public @ResponseBody String cmccOpenClass() {
		CMCCiLearn cmcciLearn = CMCCiLearn.getInstance();
		String retValue = cmcciLearn.openClassRoom();
		return "{result:'"+retValue+"'}";
    }
	
	@RequestMapping(value = "/cmccLogout.do",produces="application/json;charset=UTF-8")
    public @ResponseBody String cmcclogout() {
		CMCCiLearn cmcciLearn = CMCCiLearn.getInstance();
		String retValue = cmcciLearn.logout();
		return "{result:'"+retValue+"'}";
    }
	
	@RequestMapping(value = "/exportDailyData.do",method=RequestMethod.POST,consumes="text/javascript",produces="application/json;charset=UTF-8")
    public @ResponseBody String exportDailyData(@RequestBody List<DailyInfoReport> list) {
		String retValue ="" + list.size();
		System.out.println("hello world");
		return "{result:'"+retValue+"'}";
    }
	
}
