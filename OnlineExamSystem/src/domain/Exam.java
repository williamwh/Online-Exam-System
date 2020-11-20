package domain;

import java.sql.*;
import java.util.*;

import mappers.QuestionMapper;

public class Exam {

	private int examID;
	private String subjectCode;
	private String name;
	private String description;
	private int timeAllowed;
	private Boolean isClosed;
	private Boolean isPublished;
	private int totalMarks;
	private ExamDateRange examDateRange;
	private int ver;
	private String modifiedBy;
	private Timestamp modifiedTime;
	private List<Question> questions = null;

	public Exam(int examID, String subjectCode, String name, String description, int timeAllowed, Boolean isClosed,
			Boolean isPublished, int totalMarks, ExamDateRange examDateRange, int ver, String modifiedBy, Timestamp modifiedTime) {
		this.examID = examID;
		this.subjectCode = subjectCode;
		this.name = name;
		this.description = description;
		this.timeAllowed = timeAllowed;
		this.isClosed = isClosed;
		this.isPublished = isPublished;
		this.totalMarks = totalMarks;
		this.examDateRange = examDateRange;
		this.ver = ver;
		this.modifiedBy = modifiedBy;
		this.modifiedTime = modifiedTime;
	}

	public Exam(String subjectCode, String name, Boolean isPublished, int timeAllowed, String modifiedBy) {
		this.subjectCode = subjectCode;
		this.name = name;
		this.isPublished = isPublished;
		this.timeAllowed = timeAllowed;
		this.modifiedBy = modifiedBy;
	}
	
	public Exam(int examId, String name) {
		this.examID = examId;
		this.name = name;
	}

	public int getExamID() {
		return examID;
	}

	public String getSubjectCode() {
		return subjectCode;
	}

	public String getName() {
		return name;
	}

	public String getIsClosed() {
		String result = isClosed ? "Closed" : "Open";
		return result;
	}
	
	public Boolean getIsClosedVar() {
		return isClosed;
	}
	
	public void setIsClosedVar(Boolean isClosed) {
		this.isClosed = isClosed;
		if (isClosed) {
			this.examDateRange.setDateClosed(new Timestamp(System.currentTimeMillis()));
		} else {
			this.examDateRange.setDateClosed(null);
		}
	}
	
	public int getTotalMarks() {
		return totalMarks;
	}
	
	public String getDescription() {
		return description;
	}
	
    
	
	public int getTimeAllowed() {
		return timeAllowed;
	}
	
	
	public String getIsPublished() {
		String result = isPublished ? "Out of " + Integer.toString(totalMarks) : "UNPUBLISHED";
		return result;
	}
	
	public Boolean getIsPublishedVar() {
		return isPublished;
	}
	
	public void setIsPublishedVar(Boolean isPublished) {
		this.isPublished = isPublished;
		if (isPublished) {
			this.examDateRange.setDatePublished(new Timestamp(System.currentTimeMillis()));
		} else {
			this.examDateRange.setDatePublished(null);
		}
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	
	public List<Question> getQuestions() {
		if(this.questions == null) {
			this.questions = QuestionMapper.getAllQuestionsFromExam(this.examID);
		}
		return this.questions;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public void setTimeAllowed(int timeAllowed) {
		this.timeAllowed = timeAllowed;
	}
	
	public boolean canBeAnswered() {
		if(this.isClosed || !this.isPublished) {
			return false;
		}
		return true;
	}

	public ExamDateRange getExamDateRange() {
		return examDateRange;
	}
	
	public int getVer() {
		return this.ver;
	}
	
	public void setVer(int ver) {
		this.ver = ver;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}
	
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	
	public Timestamp getModifiedTime() {
		return this.modifiedTime;
	}
	
	public void setModifiedTime() {
		this.modifiedTime = new Timestamp(System.currentTimeMillis());
	}
	
}
