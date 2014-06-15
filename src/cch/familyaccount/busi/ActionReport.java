package cch.familyaccount.busi;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import cch.familyaccount.model.DailyInfoReport;
import cch.util.ExcelUtil;

public class ActionReport {

	public XSSFWorkbook createReport(List<DailyInfoReport> list) throws FileNotFoundException, IOException{
		XSSFWorkbook  wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet();
		int i=0;
		for (DailyInfoReport dailyInfoReport : list) {
			XSSFRow row = sheet.createRow(i++);
			XSSFCell cell = row.createCell(0);
			ExcelUtil.SetCellValue(cell, dailyInfoReport.getItemName());
			cell = row.createCell(1);
			ExcelUtil.SetCellValue(cell, dailyInfoReport.getCount());
			cell = row.createCell(2);
			ExcelUtil.SetCellValue(cell, dailyInfoReport.getAvgFee());
			cell = row.createCell(2);
			ExcelUtil.SetCellValue(cell, dailyInfoReport.getTotalFee());
		}
		return wb;
	}
}
