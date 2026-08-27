package burmalda.dev.Controllers;


import burmalda.dev.User;
import burmalda.dev.dto.UserResponse;
import burmalda.dev.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api")
public class GameController {
    private final UserService userService;

    public GameController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/score")
    public ResponseEntity<?> addScore(@RequestParam String login){
        try{
            User user = userService.incrementScore(login);
            return ResponseEntity.ok(new UserResponse(user.getUserName(), user.getCount()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body("Пользователь не найден");
        }
    }
}
