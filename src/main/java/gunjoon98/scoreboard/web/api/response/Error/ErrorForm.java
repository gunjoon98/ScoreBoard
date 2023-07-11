package gunjoon98.scoreboard.web.api.response.Error;

import lombok.Getter;

@Getter
public class ErrorForm {
    private final int errorCode;
    public ErrorForm(int errorCode) {
        this.errorCode = errorCode;
    }
}
