package gunjoon98.scoreboard.web.api.response.Error;

import lombok.Getter;

@Getter
public class ServerErrorForm extends ErrorForm {
    private final String message;
    public ServerErrorForm(String message) {
        super(500);
        this.message = message;
    }
}
