package stackmaster.devlog.backend.global.error;

import lombok.Getter;

@Getter
public class ErrorResponse {

    private final String message;
    private final int status;

    public ErrorResponse(ErrorCode errorCode) {
        this.message = errorCode.getMessage();
        this.status = errorCode.getStatus().value();
    }
}