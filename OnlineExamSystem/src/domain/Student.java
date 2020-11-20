package domain;

import java.util.List;

import mappers.StudentSubMapper;

public class Student extends User {

	private List<Subject> subjects = null;
	private StudentSubMapper studentSubMapper = new StudentSubMapper();
	
	public Student(String username, String email, String password) {
		super(username, email, password, "student");
	}
	
	public List<Subject> getSubjects(){
		if(this.subjects == null) {
			this.subjects = studentSubMapper.findSubjects(super.getUsername());
		}
		return this.subjects;
	}

}
