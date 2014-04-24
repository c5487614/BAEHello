package cch.familyaccount;

import java.sql.ResultSet;
import java.util.List;

public interface ResultSetCallback<T>{
	List<T> processResultSet(ResultSet rs);
}
