package burmalda.dev.dto;

public class UserResponse {
    private String login;
    private Long score;
    public UserResponse(String login, Long score){ this.login = login; this.score = score; }
    public String getLogin(){ return login; }
    public Long getScore(){ return score; }
}
