package cch.familyaccount;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cch.familyaccount.busi.ActionBusi;
import cch.familyaccount.model.DailyInfo;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class myServlet extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String action = req.getParameter("action");
		if(action==null){
			return ;
		}
		
		if(action.equals("getData")){
			String itemType = req.getParameter("itemType");
			getTop50(resp,itemType);
		}else if(action.equals("add")){
			try {
				if(addDailyInfo(req)){
					responseSucc(resp);
				}else{
					
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(action.equals("delete")){
			if(deleteDailyInfo(req)){
				responseSucc(resp);
			}
		}else if(action.equals("update")){
			
		}
		
		resp.getOutputStream().flush();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(req, resp);
	}
	private void addJsonHead(HttpServletResponse resp){
		resp.addHeader("Content-Type", "application/json");
	}
	
	private void responseSucc(HttpServletResponse resp) throws IOException{
		this.addJsonHead(resp);
		resp.getOutputStream().print("{success:true}");
	}
	private void getTop50(HttpServletResponse resp,String itemType) throws JsonGenerationException, JsonMappingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		ActionBusi actionBusi = new ActionBusi();
		List<DailyInfo> list = null;
		try {
			//modeified by ChunhuiChen 2014-04-27
			list = actionBusi.getTop50(itemType);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.addJsonHead(resp);
		mapper.writeValue(resp.getOutputStream(), list);
	}
	
	private boolean addDailyInfo(HttpServletRequest req) throws ParseException{
		DailyInfo model = new DailyInfo();
		model.setPersonName(req.getParameter("daily_name"));
		model.setItemName(req.getParameter("daily_item"));
		model.setFee(Double.parseDouble(req.getParameter("daily_money")));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		model.setFeeDate(sdf.parse(req.getParameter("daily_date")));
		model.setFillDate(new Date());
		model.setPCInfo(req.getRemoteAddr());
		model.setIsPaid("0");
		model.setItemType(req.getParameter("daily_type"));
		ActionBusi actionBusi = new ActionBusi();
		return actionBusi.insert(model);
	}
	private boolean deleteDailyInfo(HttpServletRequest req){
		DailyInfo model = new DailyInfo();
		String[] ids = req.getParameter("IDs").split(",");
		ActionBusi actionBusi = new ActionBusi();
		for(String id : ids){
			model.setId(Integer.parseInt(id));
			actionBusi.delete(model);
		}
		return 	true;
	}
	private boolean updateDailyInfo(){
		return false;
	}
	
}
