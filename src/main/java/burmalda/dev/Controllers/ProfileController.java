package burmalda.dev.Controllers;

import burmalda.dev.dto.ChangeLoginRequest;
import burmalda.dev.dto.ChangePasswordRequest;
import burmalda.dev.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    public ProfileController(UserService userService, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PatchMapping("/login")
    public ResponseEntity<?> changeLogin(@RequestParam String curLogin, @RequestBody ChangeLoginRequest request){
        if(userService.findByUserName(request.getNewLogin()).isPresent()){
            return ResponseEntity.badRequest().body("Такой логин уже занят");
        }

        userService.updateUserName(curLogin, request.getNewLogin());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/password")
    public ResponseEntity<?> changePassword(@RequestParam String login, @RequestBody ChangePasswordRequest request){
        return userService.findByUserName(login)
                .map(u -> {
                    if(!passwordEncoder.matches(request.getOldPassword(), u.getPassword())){
                        return ResponseEntity.badRequest().body("Неверный пароль");
                    }

                    userService.updateUserPassword(login, passwordEncoder.encode(request.getNewPassword()));
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.status(404).body("Пользователь не найден"));
    }
}
