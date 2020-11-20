package domain;

public class User {

	private String username;
	private String email;
	private String password;
	private Role role;
	
	public enum Role {
		Students, Instructors, Admins
	};
	
	public User(String username, String email, String password, String role) {
		this.username = username;
		this.email = email;
		this.password = password;
		if(role.equals("student")) {
			this.role = Role.Students;
		}
		else if(role.equals("instructor")){
			this.role = Role.Instructors;
		}
		else if (role.equals("admin")) {
			this.role = Role.Admins;
		}
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public Role getRole() {
		return this.role;
	}
}
