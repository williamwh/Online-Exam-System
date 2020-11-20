package mappers;

import domain.User;
import domain.User.Role;

public class UserMapper {

	private StudentMapper studentMapper;
	private InstructorMapper instructorMapper;
	private AdminMapper adminMapper;
	
	public UserMapper() {
		this.studentMapper = new StudentMapper();
		this.instructorMapper = new InstructorMapper();
		this.adminMapper = new AdminMapper();
	}
	
	public User find(String username) {
		User result = null;
		result = studentMapper.find(username);
		if(result!=null) return result;
		result = instructorMapper.find(username);
		if(result!=null) return result;
		result = adminMapper.find(username);
		return result;
	}
	
	// Check for validity of sign-up needs to be done in business logic (NOT HERE). ALSO THIS NEEDS TO BE DELEGATED TO
	// STUDENTMAPPER/INSTRUCTORMAPPER - work should not be done here.
	public String insert(User user) {
		if(user.getRole().equals(Role.Students)) {
			return studentMapper.insert(user);
		}
		else if(user.getRole().equals(Role.Instructors)) {
			return instructorMapper.insert(user);
		}
		else if(user.getRole().equals(Role.Admins)) {
			return adminMapper.insert(user);
		}
		return "Invalid Role.";
	}
	
	public User findWithPassword(String username, String password) {
		User result = null;
		result = studentMapper.findWithPassword(username, password);
		if(result!=null) return result;
		result = instructorMapper.findWithPassword(username, password);
		if(result!=null) return result;
		result = adminMapper.findWithPassword(username, password);
		return result;
	}
	
}
