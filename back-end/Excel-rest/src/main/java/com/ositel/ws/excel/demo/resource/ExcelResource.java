package com.ositel.ws.excel.demo.resource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.impl.common.IOUtil;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;


@Path("/uploadExcelFile")
@Produces(MediaType.APPLICATION_JSON)
public class ExcelResource {
	
	@Context
	private ServletContext applicationScope; 
	

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public ResourceMessage uploadFile(@FormDataParam("file") InputStream uploadedInputStream, @FormDataParam("file") FormDataContentDisposition fileDetail) {
		ResourceMessage rm = new ResourceMessage();
		if (uploadedInputStream == null || fileDetail == null) {
			rm.setMessage("Error in upload operation");
			return rm;
		}
		String fileName = fileDetail.getFileName();
		byte[] byteBinaryExel = null;
		try {
			byteBinaryExel = IOUtils.toByteArray(uploadedInputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		applicationScope.setAttribute(fileName, byteBinaryExel);
		rm.setMessage("Upload operation done");
		return rm;
	}
	
	@GET
	@Path("/search/{excelFileName}")
	public SheetModel searchExcelFile(@PathParam("excelFileName")  String excelFileName) {
		XSSFRow row;
		SheetModel sm = new SheetModel();
		List<Line> lines = new ArrayList<>();
		boolean firstIter = true;
		ResourceMessage rm = new ResourceMessage();
		int lineCount = 1;
		byte[] byteBinaryExel = (byte[])applicationScope.getAttribute(excelFileName);
		InputStream binayExcel = new ByteArrayInputStream(byteBinaryExel);

		if(binayExcel == null) {
			rm.setMessage("The file specifiyed is not uploded yet");
	        return sm;
		}
		sm.setFileName(excelFileName);
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(binayExcel);
			Iterator<Row>  rowIterator = workbook.getSheetAt(0).iterator();
			 while (rowIterator.hasNext()) {
				 row = (XSSFRow) rowIterator.next();
				 if(firstIter) {
					 List<String> headers = RowToList(row);
					 sm.setHeaderColumn(headers);
					 firstIter = false;
				 }
				 else {
					 Line line = new Line();
					 line.setLineNumber(lineCount);
					 line.setValues(RowToList(row));
					 lines.add(line);
					 lineCount++;
				 }

			 }
			 sm.setLinesValue(lines);
			return sm;
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return sm;
	}
	
	@GET
	@Path("/update/{excelFileName}/{col}/{row}/{cellvalue}")
	public SheetModel updateExcelFile(@PathParam("excelFileName")  String excelFileName,
			@PathParam("pcol") int pcol,
			@PathParam("prow") int prow,
			@PathParam("cellvalue") String cellvalue) {
		XSSFRow row;
		SheetModel sm = new SheetModel();
		List<Line> lines = new ArrayList<>();
		boolean firstIter = true;
		ResourceMessage rm = new ResourceMessage();
		int lineCount = 1;
		byte[] byteBinaryExel = (byte[])applicationScope.getAttribute(excelFileName);
		InputStream binayExcel = new ByteArrayInputStream(byteBinaryExel);

		if(binayExcel == null) {
			rm.setMessage("The file specifiyed is not uploded yet");
	        return sm;
		}
		sm.setFileName(excelFileName);
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(binayExcel);
			changeCellValue(workbook,cellvalue,pcol,prow);
			Iterator<Row>  rowIterator = workbook.getSheetAt(0).iterator();
			 while (rowIterator.hasNext()) {
				 row = (XSSFRow) rowIterator.next();
				 if(firstIter) {
					 List<String> headers = RowToList(row);
					 sm.setHeaderColumn(headers);
					 firstIter = false;
				 }
				 else {
					 Line line = new Line();
					 line.setLineNumber(lineCount);
					 line.setValues(RowToList(row));
					 lines.add(line);
					 lineCount++;
				 }

			 }
			 sm.setLinesValue(lines);
			return sm;
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return sm;
	}
	private List<String> RowToList(XSSFRow row){
		List<String> resultList = new ArrayList<>();
		 Iterator<Cell> cellIterator = row.cellIterator();
		 while ( cellIterator.hasNext()) {
	            Cell cell = cellIterator.next();
	            
	            switch (cell.getCellType()) {
	               case Cell.CELL_TYPE_NUMERIC:
	            	  resultList.add(new Double(cell.getNumericCellValue()).toString());
	                  break;
	               
	               case Cell.CELL_TYPE_STRING:
	            	  resultList.add(cell.getStringCellValue());
	                  break;
	            }
		 }
		return resultList;
	}
	
	private void changeCellValue(XSSFWorkbook wb, String cellValue,int pcol, int prow) {
		XSSFRow row;
		Iterator<Row>  rowIterator = wb.getSheetAt(0).iterator();
		 while (rowIterator.hasNext()) {
			 row = (XSSFRow) rowIterator.next();
			 if(prow == row.getRowNum()) {
				 Iterator<Cell> cellIterator = row.cellIterator();
				 while ( cellIterator.hasNext()) {
					 Cell cell = cellIterator.next();
					 if(pcol == cell.getColumnIndex()) {
						 cell.setCellValue(cellValue);
					 }
				 }
			 }
		 }
	}
	
}
