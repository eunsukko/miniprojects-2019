package techcourse.fakebook.service.dto;

public class UserRequest {
    private String email;
    private String password;
    private String name;
    private String gender;
    private String coverUrl;
    private String birth;
    private String introduction;

    public UserRequest(String email, String password, String name, String gender, String coverUrl, String birth, String introduction) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.coverUrl = coverUrl;
        this.birth = birth;
        this.introduction = introduction;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public String getBirth() {
        return birth;
    }

    public String getIntroduction() {
        return introduction;
    }
}
