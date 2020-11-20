package domain;

import java.util.*;

import mappers.ExamMapper;
import mappers.InstructorSubMapper;
import mappers.StudentMapper;
import mappers.StudentSubMapper;

public class Subject {
	
	private String subjectCode;
    private String name;  
    //private Instructor coordinator;
    private List<Exam> exams = null;
    private List<Student> students = null;
    private List<Instructor> coordinators = null;
    
    public Subject(String subjectCode, String name) {
        this.subjectCode = subjectCode;
        this.name = name;
        //this.coordinator = coordinator;
    }

	public String getSubjectCode() {
		return subjectCode;
	}

	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/*public Instructor getCoordinator() {
		return this.coordinator;
	}

	public void setCoordinator(Instructor coordinator) {
		this.coordinator = coordinator;
	}*/
	
	public List<Exam> getExams(){
		if(this.exams == null) {
			this.exams = ExamMapper.getAllExams(this.subjectCode);
		}
		return this.exams;
	}
	
	public List<Student> getStudents(){
		if(this.students == null) {
			this.students = StudentSubMapper.findStudents(this.subjectCode);
		}
		return this.students;
	}
	
	public List<Instructor> getCoordinators(){
		if(this.coordinators == null) {
			this.coordinators = InstructorSubMapper.findInstructors(this.subjectCode);
		}
		return this.coordinators;
	}
}
