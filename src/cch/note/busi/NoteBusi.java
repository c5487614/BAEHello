package cch.note.busi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import cch.familyaccount.MySqlConn;
import cch.familyaccount.ResultSetCallback;
import cch.note.model.Note;


public class NoteBusi implements ResultSetCallback<Note>{

	
	public boolean insert(Note model) throws SQLException, UnsupportedEncodingException{
		String sql = "insert into `Note` (`NoteId`,`Author`,`WriteDate`,`Title`,`ViewCount`,`Region`) values ('%s','%s','%s','%s',%s,'%s')";
		MySqlConn<Note> conn = new MySqlConn<Note>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sql = String.format(sql, model.getNoteId(),model.getAuthor(),sdf.format(model.getWriteDate()),model.getTitle(),model.getViewCount(),model.getRegion());
		
		conn.executeSQL(sql);
		sql = "update `Note` set Content = ? where NoteId = ?";
		PreparedStatement stm = conn.getStatement(sql);
		//stm.setCharacterStream(parameterIndex, reader)
		
		InputStream in = new ByteArrayInputStream(model.getContent().getBytes("UTF-8")); 
		stm.setBlob(1, in);
		stm.setString(2, model.getNoteId());
		stm.execute();
		stm.close();
		return true;
	}
	public boolean editNote(Note model) throws SQLException, UnsupportedEncodingException{
		String sql = "update `Note` set Content = ?, title = ?,region = ? where NoteId = ?";
		MySqlConn<Note> conn = new MySqlConn<Note>();
		PreparedStatement stm = conn.getStatement(sql);
		InputStream in = new ByteArrayInputStream(model.getContent().getBytes("UTF-8"));
		stm.setBlob(1, in);
		stm.setString(2, model.getTitle());
		stm.setString(3, model.getRegion());
		stm.setString(4, model.getNoteId());
		stm.execute();
		stm.close();
		return true;
	}
	public Note getNote(Note model){
		String sql = "select * from `Note` where NoteId = '%s'";
		sql = String.format(sql, model.getNoteId());
		MySqlConn<Note> conn = new MySqlConn<Note>();
		List<Note> list = conn.ExecuteSQL(sql, this);
		if(list==null || list.size()==0){
			return null;
		}
		return list.get(0);
	}
	public List<Note> getAllNotes(){
		String sql = "select noteId,author,writeDate,title,region,viewCount,'' as content from `Note` order by writeDate DESC";
		MySqlConn<Note> conn = new MySqlConn<Note>();
		List<Note> list = conn.ExecuteSQL(sql, this);
		if(list==null || list.size()==0){
			return null;
		}
		return list;
	}
	public boolean deleteOneNote(String noteId){
		String sql = "delete from `Note` where noteId = '" + noteId +"'";
		MySqlConn<Note> conn = new MySqlConn<Note>();
		conn.executeSQL(sql);
		return true;
	}
	
	public boolean increaseVieCount(String noteId){
		String sql = "update `Note` set viewCount = viewCount + 1 where noteId = '" + noteId +"'";
		MySqlConn<Note> conn = new MySqlConn<Note>();
		conn.executeSQL(sql);
		return true;
	}
	@Override
	public List<Note> processResultSet(ResultSet rs) {
		// TODO Auto-generated method stub
		List<Note> list = new ArrayList<Note>();
		Logger logger = Logger.getLogger("name");
		try {
			while(rs.next()){
				Note model = new Note();
				model.setAuthor(rs.getString("author"));
				//model.setContent(rs.getString("content"));
				Blob blob = rs.getBlob("content");
				InputStream input = blob.getBinaryStream();
				//No buffer here
				byte[] b = new byte[(int) blob.length()];
				try {
					StringBuffer sb = new StringBuffer();
					input.read(b);
					sb.append(new String(b,"UTF-8"));
					model.setContent(sb.toString());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					logger.log(Level.INFO, e.getMessage());
				}
				model.setNoteId(rs.getString("noteId"));
				model.setRegion(rs.getString("region"));
				model.setTitle(rs.getString("title"));
				model.setViewCount(rs.getInt("viewCount"));
				model.setWriteDate(rs.getDate("writeDate"));
				//rs.getClob(columnIndex)
				list.add(model);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.log(Level.INFO, e.getMessage());
			e.printStackTrace();
		}
		return list;
	}

}
