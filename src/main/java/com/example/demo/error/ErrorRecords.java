package com.example.demo.error;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ErrorRecords {
	
    private String fileName;  
    private Long recordNumber;  
    private String errorCode;  
    private String errorClassificationName;  
    private String errorDescription;  
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date errorDate;  
    
    
	public ErrorRecords() {
	}
	
	public ErrorRecords(String fileName, Long recordNumber, String errorCode, String errorClassificationName,
			String errorDescription, Date errorDate) {
		this.fileName = fileName;
		this.recordNumber = recordNumber;
		this.errorCode = errorCode;
		this.errorClassificationName = errorClassificationName;
		this.errorDescription = errorDescription;
		this.errorDate = errorDate;
	}

	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Long getRecordNumber() {
		return recordNumber;
	}
	public void setRecordNumber(Long recordNumber) {
		this.recordNumber = recordNumber;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorClassificationName() {
		return errorClassificationName;
	}
	public void setErrorClassificationName(String errorClassificationName) {
		this.errorClassificationName = errorClassificationName;
	}
	public String getErrorDescription() {
		return errorDescription;
	}
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}
	public Date getErrorDate() {
		return errorDate;
	}
	public void setErrorDate(Date errorDate) {
		this.errorDate = errorDate;
	}
	@Override
	public String toString() {
		return "ErrorRecords [fileName=" + fileName + ", recordNumber=" + recordNumber + ", errorCode="
				+ errorCode + ", errorClassificationName=" + errorClassificationName + ", errorDescription="
				+ errorDescription + ", errorDate=" + errorDate + "]";
	}

    


}
