package com.ositel.ws.excel.demo.resource;

import java.util.List;

public class SheetModel {
	
	private String fileName;
	private List<String> headerColumn;
	private List<Line> linesValue;
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public List<String> getHeaderColumn() {
		return headerColumn;
	}

	public void setHeaderColumn(List<String> headerColumn) {
		this.headerColumn = headerColumn;
	}

	public List<Line> getLinesValue() {
		return linesValue;
	}

	public void setLinesValue(List<Line> linesValue) {
		this.linesValue = linesValue;
	}
}
