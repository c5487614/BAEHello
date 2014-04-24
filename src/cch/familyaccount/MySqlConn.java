package cch.familyaccount;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
//import com.baidu.bae.api.util.BaeEnv;
import java.util.logging.Logger;
import java.util.logging.Level;
 
public class MySqlConn<T> {
	static String connStr = "jdbc:mysql://sqld.duapp.com:4050/eIiIjjxGVIxNJJMVEklh?user=92MilZM1hjzBQNpMTFQBzyZA&password=fDPGsgcCAWuUUUFt5LIYlGYxPDrDGOZn&characterEncoding=UTF8&connectTimeout=0";
	//static String connStr = "jdbc:mysql://localhost/baetest?user=root&password=CCH&characterEncoding=UTF8";
	public MySqlConn(){
		Logger logger = Logger.getLogger("name");
		
		Connection conn = null; 
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.log(Level.INFO, e.getMessage());
		}
		//connPool = new MysqlPooledConnection((com.mysql.jdbc.Connection) conn);
	}
	public List<T> ExecuteSQL(String sql,ResultSetCallback<T> callback){
		Logger logger = Logger.getLogger("name");
		Connection conn = null;
		List<T> list = null;
		try {
			//logger.log(Level.INFO, "conn begin");
			conn = DriverManager.getConnection(connStr);
			//logger.log(Level.INFO, "conn end");
			ResultSet rs = conn.createStatement().executeQuery(sql);
          
			list = callback.processResultSet(rs);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			
			logger.log(Level.INFO, e.getMessage());
			logger.log(Level.INFO, connStr);
		}finally{
			try {
				if(conn!=null){
					conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return list;
	}
	public void executeSQL(String sql){
		Logger logger = Logger.getLogger("name");
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(connStr);
          	
			conn.createStatement().execute(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.log(Level.INFO, e.getMessage());
			logger.log(Level.INFO, sql);
		}finally{
			try {
				if(conn!=null){
					conn.close();   
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public PreparedStatement getStatement(String sql){
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(connStr);
			return conn.prepareStatement(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String ExecuteCountSQL(String sql){
		Logger logger = Logger.getLogger("name");
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(connStr);
          	
			ResultSet rs = conn.createStatement().executeQuery(sql);
			if(rs.next()){
				return rs.getString(1);
			}
			return "";
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.log(Level.INFO, e.getMessage());
			logger.log(Level.INFO, sql);
		}finally{
			try {
				if(conn!=null){
					conn.close();   
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "";
	}
}