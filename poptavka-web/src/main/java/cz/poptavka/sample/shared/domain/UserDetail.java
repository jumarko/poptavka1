package cz.poptavka.sample.shared.domain;

import java.io.Serializable;

public class UserDetail implements Serializable {

    private String email;
    private String password;

    public UserDetail() {
    }

    public UserDetail(String email, String password) {
        this.email = email;
        this.password = password;
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

}
