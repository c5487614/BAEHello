package cch.familyaccount.busi;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cch.familyaccount.MySqlConn;
import cch.familyaccount.ResultSetCallback;
import cch.familyaccount.model.DailyInfo;
import java.util.logging.Logger;
import java.util.logging.Level;
public class ActionBusi implements ResultSetCallback<DailyInfo> {
	
	public List<DailyInfo> getTop50(){
		MySqlConn<DailyInfo> conn = new MySqlConn<DailyInfo>();
		String sql = "select id,personName,itemName,fee,feeDate,fillDate,PCInfo,isPaid,payDate from action limit 50; ";
		//conn.executeSQL("set character_set_connection = utf8;set character_set_connection = utf8;");
		List<DailyInfo> list = conn.ExecuteSQL(sql, this);
		return list;
	}
	
	public boolean insert(DailyInfo model){
		String sql = "insert into `action` (`personName`,`itemName`,`fee`,`feeDate`,`fillDate`,`PCInfo`,`IsPaid`) values('%s','%s',%s,'%s','%s','%s','%s');";
		MySqlConn<DailyInfo> conn = new MySqlConn<DailyInfo>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sql = String.format(sql, model.getPersonName(),model.getItemName(),
				model.getFee(),sdf.format(model.getFeeDate()),sdf.format(model.getFillDate()),model.getPCInfo(),model.getIsPaid());
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