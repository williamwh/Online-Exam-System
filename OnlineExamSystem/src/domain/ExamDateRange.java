package domain;

import java.sql.Timestamp;

public class ExamDateRange {
	private Timestamp dateCreated;
	private Timestamp datePublished;
	private Timestamp dateStart;
	private Timestamp dateClosed;
	
	public ExamDateRange(Timestamp dateCreated, Timestamp datePublished, Timestamp dateStart, Timestamp dateClosed) {
		this.dateCreated = dateCreated;
		this.datePublished = datePublished;
		this.dateStart = dateStart;
		this.dateClosed = dateClosed;
	}
	
	public Timestamp getDateCreated() {
		return dateCreated;
	}
	
	 public Timestamp getDatePublished() {
			return datePublished;
	}
	 
	public void setDatePublished(Timestamp datePublished) {
		this.datePublished = datePublished;
	}
	
	public Timestamp getDateStart() {
		return dateStart;
	}
	
	public void setDateStart(Timestamp dateStart) {
		this.dateStart = dateStart;
	}
	
	public Timestamp getDateClosed() {
		return dateClosed;
	}
	
	public void setDateClosed(Timestamp dateClosed) {
		this.dateClosed = dateClosed;
	}
}
