package cch.lottery.busi;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import cch.familyaccount.MySqlConn;
import cch.familyaccount.ResultSetCallback;
import cch.lottery.model.Lottery;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LotteryBusi implements ResultSetCallback<Lottery>{

	/**
	 * @param args
	 */
	public boolean isDuplicate(String numIndex){
		String sql = "select * from `Lottery` where `numIndex` = '"+numIndex+"'";
		MySqlConn<Lottery> conn = new MySqlConn<Lottery>();
		List<Lottery> list = conn.ExecuteSQL(sql, this);
		if(list==null || list.size()==0){
			return false;
		}
		if(list.size()>=1){
			return true;
		}
		return false;
	}
	public String insertLastResult(){
		LotteryUtil lotteryUtil = LotteryUtil.getInstance();
		Logger logger = Logger.getLogger("name");
		String json = lotteryUtil.getLastResult();
		//logger.log(Level.INFO, json);
		ObjectMapper map = new ObjectMapper();
		JsonNode node = null;
		try {
			node = map.readTree(json);
			String ballsStr = node.get("data").get("last_result").asText();
			String numIndex = node.get("data").get("phase").asText();
			//check if the result is in the database 
			if(this.isDuplicate(numIndex)){
				System.out.println("already exist!");
				return "already exist!";
			}
			String[] balls = ballsStr.split(",");
			if(balls.length==7){
				//Added by Chunhui Chen 2013-12-25
				//remove 0 before e.g. 09->9
				for(int i = 0;i<balls.length;i++ ){
					balls[i] = Integer.parseInt(balls[i])+"";
				}
				Lottery model = new Lottery();
				model.setNumIndex(numIndex);
				model.setBall1(balls[0]);
				model.setBall2(balls[1]);
				model.setBall3(balls[2]);
				model.setBall4(balls[3]);
				model.setBall5(balls[4]);
				model.setBall6(balls[5]);
				model.setBall7(balls[6]);
				String sql = "insert into `Lottery`(`NumIndex`,`Ball1`,`Ball2`,`Ball3`,`Ball4`,`Ball5`,`Ball6`,`Ball7`) values('%s','%s','%s','%s','%s','%s','%s','%s')";
				sql = String.format(sql, model.getNumIndex(),model.getBall1(),model.getBall2(),model.getBall3(),model.getBall4(),model.getBall5(),model.getBall6(),model.getBall7());
				MySqlConn<Lottery> conn = new MySqlConn<Lottery>();
				conn.executeSQL(sql);
				
				return sql;
			}else{
				logger.log(Level.INFO, "Ball data wrong, less than 7!");
				return "Ball data wrong, less than 7!";
			}
			
			
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			logger.log(Level.INFO, e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.log(Level.INFO, e.getMessage());
			e.printStackTrace();
		}
		return String.valueOf(false);
	}
	public int getCount(String num){
		String sqlWhere = "Ball1='%s' or Ball2='%s' or Ball3='%s' or Ball4='%s' or Ball5='%s' or Ball6='%s' "; 
		String sql = "select count(*) from Lottery where " + String.format(sqlWhere,num,num,num,num,num,num);
		MySqlConn<Lottery> conn = new MySqlConn<Lottery>();
		String result = conn.ExecuteCountSQL(sql);
		if(result.equals("")){
			return -1;
		}
		return Integer.parseInt(result);
	}
	public String getMaxNumIndex(){
		String sql="select max(numIndex) from Lottery";
		MySqlConn<Lottery> conn = new MySqlConn<Lottery>();
		String result = conn.ExecuteCountSQL(sql);
		return result;
	}
	public boolean doPredict(){
		//do predict and store the predict data
		Map<String,Object> AnalysizeMap = this.analysize();
		String predictNumIndex = (String) AnalysizeMap.get("predictNumIndex");
		if(this.isPredictDuplicate(predictNumIndex)==false){
			this.insertPredict(AnalysizeMap);
			return true;
		}
		return false;
	}
	public int getBlueBallCount(String num){
		String sqlWhere = "Ball7= '%s' "; 
		String sql = "select count(*) from Lottery where " + String.format(sqlWhere,num);
		MySqlConn<Lottery> conn = new MySqlConn<Lottery>();
		String result = conn.ExecuteCountSQL(sql);
		if(result.equals("")){
			return -1;
		}
		return Integer.parseInt(result);
	}
	public boolean isPredictDuplicate(String numIndex){
		String sql = "select * from `LotteryPredict` where `numIndex` = '"+numIndex+"'";
		MySqlConn<Lottery> conn = new MySqlConn<Lottery>();
		List<Lottery> list = conn.ExecuteSQL(sql, this);
		if(list==null || list.size()==0){
			return false;
		}
		if(list.size()>=1){
			return true;
		}
		return false;
	}
	public boolean insertPredict(Map<String,Object> map){
		Map<String,Integer> redPredict = (Map<String, Integer>) map.get("redPredict");
		Map<String,Integer> bluePredict = (Map<String, Integer>) map.get("bluePredict");
		String sql = "insert into `LotteryPredict`(`NumIndex`,`Ball1`,`Ball2`,`Ball3`,`Ball4`,`Ball5`,`Ball6`,`Ball7`,`Ball8`) values('%s','%s','%s','%s','%s','%s','%s','%s','%s')";
		sql = String.format(sql, map.get("predictNumIndex"),redPredict.get("1"),redPredict.get("2"),redPredict.get("3"),
				redPredict.get("4"),redPredict.get("5"),redPredict.get("6"),
				bluePredict.get("1"),bluePredict.get("2"));
		MySqlConn<Lottery> conn = new MySqlConn<Lottery>();
		conn.executeSQL(sql);
		return true;
	}
	public Map<String,Object> analysize(){
		int i = 0;
		Map<String,Integer> redMap = new HashMap<String,Integer>();
		int[][] reds = new int[32][2];
		for(i=1;i!=33;i++){
			int iCount = this.getCount(i+"");
			reds[i-1][0] = i;
			reds[i-1][1] = iCount;
			redMap.put(i+"", iCount);
		}
		for(i=0;i!=32;i++){
			for(int j=0;j!=32;j++){
				if(reds[i][1]<reds[j][1]){
					int tempi,tempj;
					tempi = reds[i][0];
					tempj = reds[i][1];
					reds[i][0] = reds[j][0];
					reds[i][1] = reds[j][1];
					reds[j][0] = tempi;
					reds[j][1] = tempj;
				}
			}
		}
		Map<String,Integer> redPredictMap = new HashMap<String,Integer>();
		for(i=0;i!=7;i++){
			redPredictMap.put((i+1)+"", reds[i][0]);
		}
		
		int[][] blues = new int[16][2];
		Map<String,Integer> blueMap = new HashMap<String,Integer>();
		for(i=1;i!=17;i++){
			int iCount = this.getBlueBallCount(i+"");
			blues[i-1][0] = i;
			blues[i-1][1] = iCount;
			blueMap.put(i+"", iCount);
		}
		for(i=0;i!=16;i++){
			for(int j=0;j!=16;j++){
				if(blues[i][1]<blues[j][1]){
					int tempi,tempj;
					tempi = blues[i][0];
					tempj = blues[i][1];
					blues[i][0] = blues[j][0];
					blues[i][1] = blues[j][1];
					blues[j][0] = tempi;
					blues[j][1] = tempj;
				}
			}
		}
		Map<String,Integer> bluePredictMap = new HashMap<String,Integer>();
		for(i=0;i!=2;i++){
			bluePredictMap.put((i+1)+"", blues[i][0]);
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("red", redMap);
		map.put("blue", blueMap);
		map.put("redPredict", redPredictMap);
		map.put("bluePredict", bluePredictMap);
		String predictNumIndex = this.getMaxNumIndex();
		predictNumIndex = Integer.valueOf(predictNumIndex) + 1 + "";
		map.put("predictNumIndex", predictNumIndex);
		
		return map;
	}
	public Map<String,Object> predictMatch(){
		Map<String,Object> map = new HashMap<String,Object>();
		String sql = "select * from `LotteryPredict` order by `NumIndex` DESC limit 100";
		MySqlConn<Lottery> conn = new MySqlConn<Lottery>();
		List<Lottery> listPre = conn.ExecuteSQL(sql, this);
		map.put("predictHistory", listPre);
		
		List<Lottery> listOpen = new ArrayList<Lottery>();
		for(Lottery model : listPre){
			sql = "select * from `Lottery` where `NumIndex` = '" + model.getNumIndex() + "'";
			List<Lottery> listOpened = conn.ExecuteSQL(sql, this);
			if(listOpened==null||listOpened.size()==0){
				continue;
			}else{
				listOpen.add(listOpened.get(0));
			}
		}
		map.put("openHistory", listOpen);
		return map;
	}
	private static String removeZero(String value){
		if(value.trim().equals("")){
			return "";
		}
		int i = Integer.parseInt(value);
		return String.valueOf(i);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		LotteryBusi lotteryBusi = new LotteryBusi();
//		lotteryBusi.insertLastResult();
		
		
	}
	@Override
	public List<Lottery> processResultSet(ResultSet rs) {
		// TODO Auto-generated method stub
		List<Lottery> list = new ArrayList<Lottery>();
		Logger logger = Logger.getLogger("name");
		try {
			while(rs.next()){
				Lottery model = new Lottery();
				model.setNumIndex(rs.getString("NumIndex"));
				model.setBall1(rs.getString("Ball1"));
				model.setBall2(rs.getString("Ball2"));
				model.setBall3(rs.getString("Ball3"));
				model.setBall4(rs.getString("Ball4"));
				model.setBall5(rs.getString("Ball5"));
				model.setBall6(rs.getString("Ball6"));
				model.setBall7(rs.getString("Ball7"));
				list.add(model);
			}
			return list;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.log(Level.INFO, e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

}
//code used to insert data into lottery for excel
//try {
//	InputStream inp = new FileInputStream("E:\\MyDoc\\test.xls");
//	Workbook wb = WorkbookFactory.create(inp);
//    Sheet sheet = wb.getSheetAt(0);
//    Row row = sheet.getRow(4);
//    Cell cell = row.getCell(2);
//    System.out.println(cell.getStringCellValue());
//    MySqlConn<Lottery> conn = new MySqlConn<Lottery>();
//    for(int i = 1442;i>0;i--){
//    	row = sheet.getRow(i);
//    	row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
//    	row.getCell(2).setCellType(Cell.CELL_TYPE_STRING);
//    	row.getCell(3).setCellType(Cell.CELL_TYPE_STRING);
//    	row.getCell(4).setCellType(Cell.CELL_TYPE_STRING);
//    	row.getCell(5).setCellType(Cell.CELL_TYPE_STRING);
//    	row.getCell(6).setCellType(Cell.CELL_TYPE_STRING);
//    	row.getCell(7).setCellType(Cell.CELL_TYPE_STRING);
//    	row.getCell(8).setCellType(Cell.CELL_TYPE_STRING);
//    	
//    	String numIndex =  row.getCell(0).getStringCellValue();
//    	String ball1 = removeZero(row.getCell(2));
//    	String ball2 = removeZero(row.getCell(3));
//    	String ball3 = removeZero(row.getCell(4));
//    	String ball4 = removeZero(row.getCell(5));
//    	String ball5 = removeZero(row.getCell(6));
//    	String ball6 = removeZero(row.getCell(7));
//    	String ball7 = removeZero(row.getCell(8));
//	    	
//    	if(numIndex.trim().equals("")||ball1.trim().equals("")){
//    		continue;
//    	}
//    	Lottery model = new Lottery();
//    	
//    	model.setNumIndex(numIndex);
//		model.setBall1(ball1);
//		model.setBall2(ball2);
//		model.setBall3(ball3);
//		model.setBall4(ball4);
//		model.setBall5(ball5);
//		model.setBall6(ball6);
//		model.setBall7(ball7);
//		String sql = "insert into `Lottery`(`NumIndex`,`Ball1`,`Ball2`,`Ball3`,`Ball4`,`Ball5`,`Ball6`,`Ball7`) values('%s','%s','%s','%s','%s','%s','%s','%s')";
//		sql = String.format(sql, model.getNumIndex(),model.getBall1(),model.getBall2(),model.getBall3(),model.getBall4(),model.getBall5(),model.getBall6(),model.getBall7());
//		
//		conn.executeSQL(sql);
//    }
//} catch (Exception e) {
//	// TODO Auto-generated catch block
//	e.printStackTrace();
//}finally{
//	
//}


//code used to insert data into lottery for excel
//InputStream inp;
//try {
//	inp = new FileInputStream("E:\\MyDoc\\2013shuangseqiu.xls");
//	Workbook wb = WorkbookFactory.create(inp);
//    Sheet sheet = wb.getSheetAt(0);
//    MySqlConn<Lottery> conn = new MySqlConn<Lottery>();
//    for(int i = 1;i<139;i++){
//    	Row row = sheet.getRow(i);
//    	row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
//    	row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
//    	
//    	String numIndex = row.getCell(0).getStringCellValue();
//    	String ballsStr = row.getCell(1).getStringCellValue();
//    	//System.out.println(numIndex+","+ballsStr);
//    	String[] balls = ballsStr.split("#")[0].split(",");
//    	String blueBall = ballsStr.split("#")[1];
//    	Lottery model = new Lottery();
//    	String ball1 = removeZero(balls[0]);
//    	String ball2 = removeZero(balls[1]);
//    	String ball3 = removeZero(balls[2]);
//    	String ball4 = removeZero(balls[3]);
//    	String ball5 = removeZero(balls[4]);
//    	String ball6 = removeZero(balls[5]);
//    	String ball7 = removeZero(blueBall);
//    	//System.out.println(ball7);
//    	model.setNumIndex(numIndex);
//		model.setBall1(ball1);
//		model.setBall2(ball2);
//		model.setBall3(ball3);
//		model.setBall4(ball4);
//		model.setBall5(ball5);
//		model.setBall6(ball6);
//		model.setBall7(ball7);
//		String sql = "insert into `Lottery`(`NumIndex`,`Ball1`,`Ball2`,`Ball3`,`Ball4`,`Ball5`,`Ball6`,`Ball7`) values('%s','%s','%s','%s','%s','%s','%s','%s')";
//		sql = String.format(sql, model.getNumIndex(),model.getBall1(),model.getBall2(),model.getBall3(),model.getBall4(),model.getBall5(),model.getBall6(),model.getBall7());
//		conn.executeSQL(sql);
//    }
//    
//} catch (Exception e) {
//	// TODO Auto-generated catch block
//	e.printStackTrace();
//}

//private static String removeZero(Cell cell){
//String value = cell.getStringCellValue();
//if(value.trim().equals("")){
//	return "";
//}
//int i = Integer.parseInt(value);
//return String.valueOf(i);
//}
