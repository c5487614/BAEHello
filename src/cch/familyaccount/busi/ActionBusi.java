package cch.familyaccount.busi;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import cch.familyaccount.MySqlConn;
import cch.familyaccount.ResultSetCallback;
import cch.familyaccount.model.DailyInfo;
public class ActionBusi implements ResultSetCallback<DailyInfo> {
	
	public List<DailyInfo> getTop50(){
		MySqlConn<DailyInfo> conn = new MySqlConn<DailyInfo>();
		String sql = "select id,personName,itemName,fee,feeDate,fillDate,PCInfo,isPaid,payDate,itemType,comment from action where isPaid = '0' limit 150; ";
		//conn.executeSQL("set character_set_connection = utf8;set character_set_connection = utf8;");
		List<DailyInfo> list = conn.ExecuteSQL(sql, this);
		return list;
	}
	//created by Chunhui Chen 2014-04-27
	public List<DailyInfo> getTop50(String itemType){
		if(itemType==null||itemType.trim().equals("")){
			return this.getTop50();
		}
		MySqlConn<DailyInfo> conn = new MySqlConn<DailyInfo>();
		String sql = "select id,personName,itemName,fee,feeDate,fillDate,PCInfo,isPaid,payDate,itemType,comment from action where isPaid = '0' and itemType='"+itemType+"'  limit 150; ";
		List<DailyInfo> list = conn.ExecuteSQL(sql, this);
		return list;
	}
	public List<DailyInfo> getTop50(String itemType,String beginDate,String endDate){
		
		String sql = "select id,personName,itemName,fee,feeDate,fillDate,PCInfo,isPaid,payDate,itemType,comment from action where isPaid = '0' and itemType='"+itemType+"' and feeDate>='"+beginDate+"' and feeDate<='"+endDate+"' ; ";
		MySqlConn<DailyInfo> conn = new MySqlConn<DailyInfo>();
		List<DailyInfo> list = conn.ExecuteSQL(sql, this);
		System.out.println(list.size());
		System.out.println(sql);
		return list;
	}
	
	public boolean insert(DailyInfo model){
		String sql = "insert into `action` (`personName`,`itemName`,`fee`,`feeDate`,`fillDate`,`PCInfo`,`IsPaid`,`itemType`,`comment`) values('%s','%s',%s,'%s','%s','%s','%s','%s','%s');";
		MySqlConn<DailyInfo> conn = new MySqlConn<DailyInfo>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sql = String.format(sql, model.getPersonName(),model.getItemName(),
				model.getFee(),sdf.format(model.getFeeDate()),sdf.format(model.getFillDate()),model.getPCInfo(),
				model.getIsPaid(),model.getItemType(),model.getComment());
		//conn.executeSQL("set character_set_connection = utf8;set character_set_connection = utf8;");
		conn.executeSQL(sql);
		return true;
	}
	public boolean update(DailyInfo model){
		String sql = "Update `action` set personName = '%s', itemName = '%s', fee = %s, feeDate = '%s', fillDate = '%s', PCInfo = '%s', IsPaid = '%s' where `id` = %s";
		MySqlConn<DailyInfo> conn = new MySqlConn<DailyInfo>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sql = String.format(sql, model.getPersonName(),model.getItemName(),model.getFee(),sdf.format(model.getFeeDate()),sdf.format(model.getFillDate()),model.getPCInfo(),model.getIsPaid(),model.getId());
		conn.executeSQL(sql);
		return true;
		
	}
	public boolean delete(DailyInfo model){
		String sql ="delete from `action` where id = %s";
		sql = String.format(sql, model.getId());
		MySqlConn<DailyInfo> conn = new MySqlConn<DailyInfo>();
		conn.executeSQL(sql);
		return true;
	}
	public boolean updateStatus(String id){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date now = new Date();
		//IsPaid = 2 means that this record is been deleted
		String sql = "Update action set isPaid='2', PayDate='" + sdf.format(now) + "' Where id=" + id;
		MySqlConn<DailyInfo> conn = new MySqlConn<DailyInfo>();
		conn.executeSQL(sql);
		return true;
	}
	public boolean confirmPaid(String id){
		String sql = "";
		return true;
	}
	@Override
	public List<DailyInfo> processResultSet(ResultSet rs) {
		// TODO Auto-generated method stub
		Logger logger = Logger.getLogger("name");
		List<DailyInfo> list = new ArrayList<DailyInfo>();
		try {
			while(rs.next()){
				DailyInfo model = new DailyInfo();
				model.setFee(rs.getDouble("fee"));
				model.setFeeDate(rs.getDate("feeDate"));
				model.setFillDate(rs.getDate("fillDate"));
				model.setId(rs.getInt("id"));
				model.setIsPaid(rs.getString("isPaid"));
				model.setItemName(rs.getString("itemName"));
				try{
					model.setPayDate(rs.getDate("payDate"));
				}
				catch(SQLException e){
					
				}
				model.setPCInfo(rs.getString("pCInfo"));
				model.setPersonName(rs.getString("personName"));
				model.setItemType(rs.getString("itemType"));
				model.setComment(rs.getString("comment"));
				list.add(model);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			logger.log(Level.INFO, e.getMessage());
		}
		return list;
	}
}
