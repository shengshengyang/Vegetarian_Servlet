package bean;

public class User {
	
	private int uid;
    private String email;
    private String password;
    private String username;
    private String status;
    
    public User() {}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
    
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
    
	@Override
	public String toString() {
		return "User[uid=" + uid + ",email=" + email + ",password=" + password + 
				",username=" + username + ",status=" + status + "]";  
	}
	
}
