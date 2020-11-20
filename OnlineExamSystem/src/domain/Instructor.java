package domain;

public class Instructor extends User {

	public Instructor(String username, String email, String password) {
		super(username, email, password, "instructor");
	}
}
