package comp5216.sydney.edu.au.aieatsapp;

public class customerIdentityFormat {
    public String userName;
    public String userPassword;
    public String userEmail;
    public String userIdentityName;
    public String userIdentityPassword;

    public customerIdentityFormat(String userName, String userPassword, String userEmail, String userIdentityName, String userIdentityPassword) {
        this.userName = userName;
        this.userPassword = userPassword;
        this.userEmail = userEmail;
        this.userIdentityName = userIdentityName;
        this.userIdentityPassword = userIdentityPassword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserIdentityName() {
        return userIdentityName;
    }

    public void setUserIdentityName(String userIdentityName) {
        this.userIdentityName = userIdentityName;
    }

    public String getUserIdentityPassword() {
        return userIdentityPassword;
    }

    public void setUserIdentityPassword(String userIdentityPassword) {
        this.userIdentityPassword = userIdentityPassword;
    }
}
