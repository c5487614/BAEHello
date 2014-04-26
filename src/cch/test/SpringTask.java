package cch.test;

import cch.lottery.busi.LotteryBusi;
import cch.lottery.busi.LotteryUtil;

public class SpringTask {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		LotteryUtil lotteryUtil = LotteryUtil.getInstance();
//		String result = lotteryUtil.getLastResult();
//		System.out.println(result);
		
		LotteryBusi lotteryBusi = new LotteryBusi();
		String result = lotteryBusi.getMaxNumIndex();
		System.out.println(result);
	}

}
