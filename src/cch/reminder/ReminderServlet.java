package cch.reminder;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cch.lottery.busi.LotteryBusi;
import cch.reminder.busi.FeixinUtil;

public class ReminderServlet extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		Logger logger =Logger.getLogger("name");
		String action = req.getParameter("action");
		//------------------------test
//		if(action!=null){
//			FeixinUtil.relogin();
//			return ;
//		}
		//------------------------test
		req.setCharacterEncoding("UTF-8");
		logger.log(Level.INFO, "ReminderServlet,doPost");
		logger.log(Level.INFO, req.getCharacterEncoding());
		logger.log(Level.INFO, req.getParameter("msgTxt"));
		String msgTxt = req.getParameter("msgTxt");
		String receivers = req.getParameter("receivers");
		//recievers
		//receivers
		logger.log(Level.INFO, msgTxt);
		//msgTxt = URLEncoder.encode(msgTxt, "UTF-8");
		//"1330441788"
		FeixinUtil feixin = FeixinUtil.getInstance();
//		feixin.sendMSM(msgTxt, receivers);
		//get lottery information 2013-11-28
		LotteryBusi lotteryBusi = new LotteryBusi();
		lotteryBusi.insertLastResult();
		lotteryBusi.doPredict();
		logger.log(Level.INFO, "ReminderServlet,doPost,end");
	}

}
