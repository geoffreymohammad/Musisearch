package com.musisearch.rest.model;

import java.sql.Date;


public class MetaData  {
	/* logging variables */
	protected int deleted;
	protected Date created;
	protected Date lastModified;
	protected String createdBy;
	protected Integer createdByLoginHistory;
	protected String lastModifiedBy;
	protected Integer lastModifiedByLoginHistory;
	protected String createdByUser;
	protected String lastModifiedByUser;
	protected String dataOwner;
	
	public MetaData() {

	}

	public int getDeleted() {
		return deleted;
	}

	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getCreatedByLoginHistory() {
		return createdByLoginHistory;
	}

	public void setCreatedByLoginHistory(Integer createdByLoginHistory) {
		this.createdByLoginHistory = createdByLoginHistory;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public Integer getLastModifiedByLoginHistory() {
		return lastModifiedByLoginHistory;
	}

	public void setLastModifiedByLoginHistory(Integer lastModifiedByLoginHistory) {
		this.lastModifiedByLoginHistory = lastModifiedByLoginHistory;
	}

	public String getCreatedByUser() {
		return createdByUser;
	}

	public void setCreatedByUser(String createdByUser) {
		this.createdByUser = createdByUser;
	}

	public String getLastModifiedByUser() {
		return lastModifiedByUser;
	}

	public void setLastModifiedByUser(String lastModifiedByUser) {
		this.lastModifiedByUser = lastModifiedByUser;
	}

	public String getDataOwner() {
		return dataOwner;
	}

	public void setDataOwner(String dataOwner) {
		this.dataOwner = dataOwner;
	}

	
	
}
