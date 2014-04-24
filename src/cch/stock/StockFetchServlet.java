package cch.stock;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cch.familyaccount.busi.ActionBusi;
import cch.familyaccount.model.DailyInfo;
import cch.reminder.busi.FeixinUtil;

public class StockFetchServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		ActionBusi actionBusi = new ActionBusi();
		DailyInfo model = new DailyInfo();
		model.setFee(11);
		model.setFeeDate(new Date());
		model.setFillDate(new Date());
		model.setIsPaid("0");
		model.setItemName("StockFetchServlet");
		model.setPCInfo("StockFetchServlet");
		model.setPersonName("StockFetchServlet");
		actionBusi.insert(model);
		
		FeixinUtil feixin = FeixinUtil.getInstance();
		feixin.sendMSM("test", "1330441788");
		
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(req, resp);
	}
	
}
