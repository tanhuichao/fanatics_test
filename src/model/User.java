package model;

public class User {
    private int id;
    private String name;
    private String email;
    private Gender gender;
    private Status status;

    public User(int id, String name, String email, Gender gender, Status status) {
        this.id = id;
        this. name = name;
        this.email = email;
        this.gender = gender;
        this.status = status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Gender getGender() {
        return gender;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
