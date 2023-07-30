package gunjoon98.scoreboard.web.ErrorHandler;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
@Getter
@RequiredArgsConstructor
public class ErrorResponse {
    final private String message;
    final private int errorCode;
}
