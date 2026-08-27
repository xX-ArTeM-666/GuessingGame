package burmalda.dev.Controllers;


import burmalda.dev.User;
import burmalda.dev.dto.AuthRequest;
import burmalda.dev.dto.UserResponse;
import burmalda.dev.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthController(UserService userService, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest request){
        if(request.getLogin() == null || request.getLogin().length() < 3){
            return ResponseEntity.badRequest().body("Логин должен быть не короче 3 символов");
        }

        if(request.getPassword() == null || request.getPassword().length() < 4){
            return ResponseEntity.badRequest().body("Пароль должен быть не короче 4 символов");
        }

        if(userService.findByUserName(request.getLogin()).isPresent()){
            return ResponseEntity.badRequest().body("Такой логин уже занят");
        }

        User user = new User(request.getLogin(), passwordEncoder.encode(request.getPassword()));
        userService.saveUser(user);

        return ResponseEntity.ok(new UserResponse(user.getUserName(), user.getCount()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request){
        return userService.findByUserName(request.getLogin())
                .filter(u -> passwordEncoder.matches(request.getPassword(), u.getPassword()))
                .<ResponseEntity<?>>map(u -> ResponseEntity.ok(new UserResponse(u.getUserName(), u.getCount())))
                .orElse(ResponseEntity.status(401).body("Неверный логин или пароль"));
    }
}
