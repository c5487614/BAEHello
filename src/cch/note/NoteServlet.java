package cch.note;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cch.note.busi.NoteBusi;
import cch.note.model.Note;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class NoteServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		Logger logger = Logger.getLogger("name");
		String action = req.getParameter("action");
		if(action.equals("add")){
			try {
				this.addNote(req);
				addJsonHead(resp);
				responseSucc(resp);
			} catch (SQLException e) {
				logger.log(Level.INFO, e.getMessage());
			}
			resp.getOutputStream().flush();
		}else if(action.equals("getOne")){
			this.getOneNote(req, resp);
		}else if(action.equals("getAll")){
			this.getAllNotes(resp);
		}else if(action.equals("deleteOne")){
			this.deleteOne(req);
			addJsonHead(resp);
			responseSucc(resp);
		}else if(action.equals("edit")){
			try {
				this.editNote(req);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.log(Level.INFO, e.getMessage());
			}
			addJsonHead(resp);
			responseSucc(resp);
		}else if(action.equals("increaseViewCount")){
			this.increaseViewCount(req);
			addJsonHead(resp);
			responseSucc(resp);
		}
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
	private boolean addNote(HttpServletRequest req) throws SQLException, UnsupportedEncodingException{
		Note model = new Note();
		model.setAuthor(req.getParameter("tbox_user"));
		model.setNoteId(java.util.UUID.randomUUID().toString());
		model.setRegion(req.getParameter("tbox_region"));
		model.setTitle(req.getParameter("tbox_title"));
		model.setViewCount(0);
		model.setWriteDate(new Date());
		String content = req.getParameter("tbox_content");
		model.setContent(content);
		NoteBusi noteBusi = new NoteBusi();
		return noteBusi.insert(model);
	}
	private void getOneNote(HttpServletRequest req,HttpServletResponse resp) throws JsonGenerationException, JsonMappingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		NoteBusi noteBusi = new NoteBusi();
		Note model = new Note();
		String noteId = req.getParameter("noteId");
		model.setNoteId(noteId);
		model = noteBusi.getNote(model);
		this.addJsonHead(resp);
		mapper.writeValue(resp.getOutputStream(), model);
	}
	private void getAllNotes(HttpServletResponse resp) throws JsonGenerationException, JsonMappingException, IOException{
		NoteBusi noteBusi = new NoteBusi();
		List<Note> list = noteBusi.getAllNotes();
		ObjectMapper mapper = new ObjectMapper();
		this.addJsonHead(resp);
		mapper.writeValue(resp.getOutputStream(), list);
	}
	private void deleteOne(HttpServletRequest req){
		NoteBusi noteBusi = new NoteBusi();
		String noteId = req.getParameter("noteId");
		noteBusi.deleteOneNote(noteId);
	}
	private boolean editNote(HttpServletRequest req) throws UnsupportedEncodingException, SQLException{
		Note model = new Note();
		//model.setAuthor(req.getParameter("tbox_user"));
		model.setNoteId(req.getParameter("tbox_noteId"));
		model.setRegion(req.getParameter("tbox_region"));
		model.setTitle(req.getParameter("tbox_title"));
		String content = req.getParameter("tbox_content");
		model.setContent(content);
		NoteBusi noteBusi = new NoteBusi();
		return noteBusi.editNote(model);
	}
	private boolean increaseViewCount(HttpServletRequest req){
		Note model = new Note();
		model.setNoteId(req.getParameter("noteId"));
		NoteBusi noteBusi = new NoteBusi();
		noteBusi.increaseVieCount(model.getNoteId());
		return true;
	}
}
