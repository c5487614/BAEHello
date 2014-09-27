package cch.familyaccount;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
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
			getTop50(resp,req);
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
		}else if(action.equals("addFromMobile")){
			try{
				
				addDailyInfoM(req);
				responseSucc(resp);
			}catch(Exception ex){
				ex.printStackTrace();
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
	private void getTop50(HttpServletResponse resp,HttpServletRequest req) throws JsonGenerationException, JsonMappingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		ActionBusi actionBusi = new ActionBusi();
		String itemType = req.getParameter("itemType");
		String beginDate = req.getParameter("beginDate");
		String endDate = req.getParameter("endDate");
		List<DailyInfo> list = null;
		try {
			//modified by ChunhuiChen 2014-04-27
			//list = actionBusi.getTop50(itemType);
			//modified by ChunhuiChen 20141-06-07
			list = actionBusi.getTop50(itemType,beginDate,endDate);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.addJsonHead(resp);
		mapper.writeValue(resp.getOutputStream(), list);
	}
	private boolean addDailyInfoM(HttpServletRequest req) throws ParseException{
		DailyInfo model = new DailyInfo();
		model.setPersonName(req.getParameter("daily_name"));
		model.setItemName(req.getParameter("daily_item"));
		model.setFee(Double.parseDouble(req.getParameter("daily_money")));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		model.setFeeDate(sdf.parse(req.getParameter("daily_feeDate")));
		model.setFillDate(sdf.parse(req.getParameter("daily_fillDate")));
//		model.setPCInfo(req.getParameter("daily_PCInfo"));
		model.setPCInfo(req.getRemoteAddr());
		model.setIsPaid(req.getParameter("daily_isPaid"));
		model.setItemType(req.getParameter("daily_itemType"));
		model.setComment(req.getParameter("daily_comment"));
//		System.out.println("itemName:" + model.getItemName());
//		System.out.println("fee:" + model.getFee());
//		System.out.println("PersonName:" + model.getPersonName());
//		System.out.println("Comment:" + model.getComment());
		ActionBusi actionBusi = new ActionBusi();
		return actionBusi.insert(model);
	}
	//this function is used for add a list of daily info into the server database, added by Chunhui Chen 2014-09-27
	private boolean addDailyInfoMBash(HttpServletRequest req) throws IOException{
		ServletInputStream sis = req.getInputStream();
//		sis.read(b, off, len)
		return false;
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
		model.setComment("PC");
		ActionBusi actionBusi = new ActionBusi();
		return actionBusi.insert(model);
	}
	private boolean deleteDailyInfo(HttpServletRequest req){
		DailyInfo model = new DailyInfo();
		String[] ids = req.getParameter("IDs").split(",");
		ActionBusi actionBusi = new ActionBusi();
		for(String id : ids){
			model.setId(Integer.parseInt(id));
			//updated by ChunhuiChen 2014-05-12 not delete the record but set the isPaid column value to 2
			actionBusi.updateStatus(id);
			//actionBusi.delete(model);
		}
		return true;
	}
	private boolean updateDailyInfo(){
		return false;
	}
	
}
