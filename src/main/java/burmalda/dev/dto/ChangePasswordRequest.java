package burmalda.dev.dto;

public class ChangePasswordRequest {
    private String oldPassword;
    private String newPassword;
    public String getOldPassword(){ return oldPassword; }
    public void setOldPassword(String p){ this.oldPassword = p; }
    public String getNewPassword(){ return newPassword; }
    public void setNewPassword(String p){ this.newPassword = p; }
}
