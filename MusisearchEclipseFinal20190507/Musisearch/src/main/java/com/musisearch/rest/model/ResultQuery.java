package com.musisearch.rest.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ResultQuery {
	private boolean errorResult;
	private String errorMessage;
	private int integerResult;
	private Double doubleResult;
	private BigDecimal bigDecimalResult;
	private String stringResult;
	private Object object;
	private ArrayList<Object> lstObject;

	
	public boolean isErrorResult() {
		return errorResult;
	}

	public void setErrorResult(boolean errorResult) {
		this.errorResult = errorResult;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public int getIntegerResult() {
		return integerResult;
	}

	public void setIntegerResult(int integerResult) {
		this.integerResult = integerResult;
	}

	public Double getDoubleResult() {
		return doubleResult;
	}

	public void setDoubleResult(Double doubleResult) {
		this.doubleResult = doubleResult;
	}

	public BigDecimal getBigDecimalResult() {
		return bigDecimalResult;
	}

	public void setBigDecimalResult(BigDecimal bigDecimalResult) {
		this.bigDecimalResult = bigDecimalResult;
	}

	public String getStringResult() {
		return stringResult;
	}

	public void setStringResult(String stringResult) {
		this.stringResult = stringResult;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public ArrayList<Object> getLstObject() {
		return lstObject;
	}

	public void setLstObject(ArrayList<Object> lstObject) {
		this.lstObject = lstObject;
	}

	
}