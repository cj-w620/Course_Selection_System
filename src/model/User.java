package model;

import java.io.Serializable;

public abstract class User implements Serializable {
    private static final long serialVersionUID = 1L;
    
    protected String userId;
    protected String username;
    protected String password;
    protected String role;
    
    public User(String userId, String username, String password, String role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
    }
    
    
    public String getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getRole() { return role; }
    
    //密码校验
    public boolean authenticate(String password) {
        return this.password.equals(password);
    }
    
    public abstract void showMenu();
}