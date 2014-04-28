package cch.task;

import java.util.logging.Level;
import java.util.logging.Logger;

import cch.lottery.busi.LotteryBusi;

public class LotteryTask {
	public void doTask(){
		try{
			
			
			Logger logger = Logger.getLogger("name");
			logger.log(Level.INFO, "Lottery begin");
			LotteryBusi lotteryBusi = new LotteryBusi();
			lotteryBusi.insertLastResult();
			lotteryBusi.doPredict();
			logger.log(Level.INFO, "Lottery end");
		}catch(Exception ex){
			
		}
		
	}
}
