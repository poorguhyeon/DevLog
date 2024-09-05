package stackmaster.devlog.backend.domain.user.service;

import stackmaster.devlog.backend.domain.user.dto.UserDto;
import stackmaster.devlog.backend.domain.user.entity.User;
import stackmaster.devlog.backend.domain.user.repository.UserRepository;
import stackmaster.devlog.backend.global.error.ErrorCode;
import stackmaster.devlog.backend.global.exception.BusinessException;
import stackmaster.devlog.backend.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public ResponseEntity<Map<String, Object>> signUp(UserDto.SignUp signUpDto) {
        if (userRepository.existsByEmail(signUpDto.getEmail())) {
            throw new BusinessException(ErrorCode.EMAIL_DUPLICATION);
        }

        User user = new User();
        user.setEmail(signUpDto.getEmail());
        user.setName(signUpDto.getName());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));

        userRepository.save(user);

        Map<String, Object> response = new HashMap<>();
        response.put("status", 201);
        response.put("message", "회원가입이 완료되었습니다.");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public ResponseEntity<Map<String, Object>> signIn(UserDto.SignIn signInDto) {
        User user = userRepository.findByEmail(signInDto.getEmail())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(signInDto.getPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_PASSWORD);
        }

        String accessToken = jwtTokenProvider.createAccessToken(user.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail());

        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "로그인이 성공적으로 되었습니다.");
        response.put("accessToken", accessToken);
        response.put("refreshToken", refreshToken);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
