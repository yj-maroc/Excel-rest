package com.ositel.ws.excel.demo.resource;

import java.util.List;

public class Line{
	
	private int lineNumber;
	private List<String> values;
	
	public int getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	public List<String> getValues() {
		return values;
	}
	public void setValues(List<String> values) {
		this.values = values;
	}
	
	
}