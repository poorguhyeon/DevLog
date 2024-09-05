package stackmaster.devlog.backend.domain.user.controller;

import stackmaster.devlog.backend.domain.user.dto.UserDto;
import stackmaster.devlog.backend.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Auth API")
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "회원가입 API")
    public ResponseEntity<Map<String, Object>> signUp(@RequestBody UserDto.SignUp signUpDto) {
        return userService.signUp(signUpDto);
    }

    @PostMapping("/signin")
    @Operation(summary = "로그인", description = "로그인 API")
    public ResponseEntity<Map<String, Object>> signIn(@RequestBody UserDto.SignIn signInDto) {
        return userService.signIn(signInDto);
    }
}
