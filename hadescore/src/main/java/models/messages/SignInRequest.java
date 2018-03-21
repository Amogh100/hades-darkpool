package models.messages;

public class SignInRequest {
    private String username;
    private String password;

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public SignInRequest(String username, String password){
        this.username = username;
        this.password = password;
    }
}
