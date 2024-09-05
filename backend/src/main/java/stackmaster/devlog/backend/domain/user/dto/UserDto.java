package stackmaster.devlog.backend.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class UserDto {
    @Getter @Setter
    @AllArgsConstructor
    public static class SignIn {
        private String email;
        private String password;
    }

    @Getter @Setter
    @AllArgsConstructor
    public static class SignUp {
        private String email;
        private String name;
        private String password;
    }
}