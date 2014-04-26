package cch.task;

import cch.lottery.busi.LotteryBusi;
import cch.util.proxy.ProxyUtil;

public class KeepOnlineTask {
	public void doTask(){
		try{
			System.out.println("KeepOnline begin");
			ProxyUtil.go("http://easymouse.duapp.com/base64Encoding.html");
			System.out.println("KeepOnline end");
		}catch(Exception ex){
			System.out.println("KeepOnline:");
			System.out.println(ex.toString());
		}
		
	}
}
