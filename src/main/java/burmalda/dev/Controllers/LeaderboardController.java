package burmalda.dev.Controllers;

import burmalda.dev.dto.UserResponse;
import burmalda.dev.services.UserService;
import org.apache.coyote.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class LeaderboardController {
    private final UserService userService;

    public LeaderboardController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/leaderboard")
    public List<UserResponse> leaderboard(){
        return userService.getLeaderBoard()
                .stream()
                .map(u -> new UserResponse(u.getUserName(), u.getCount()))
                .toList();
    }
}
