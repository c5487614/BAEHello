package cch.util;

import org.apache.poi.ss.usermodel.Cell;

public class ExcelUtil {
	public static void  SetCellValue(Cell c, Object value){
		if(value instanceof Double||value instanceof Float||value instanceof Integer){
			double value1 = Double.parseDouble(value.toString()) ;
			c.setCellValue(value1);
		}else if(value instanceof String){
			String value1 = value.toString();
			c.setCellValue(value1);
		}else{
			String value1 = value.toString();
			c.setCellValue(value1);
		}
	}
}
