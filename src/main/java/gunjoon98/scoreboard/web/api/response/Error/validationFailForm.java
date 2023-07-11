package gunjoon98.scoreboard.web.api.response.Error;

import lombok.Getter;

import java.util.List;

@Getter
public class validationFailForm extends ErrorForm {
    private final List<FiledError> filedErrorList;
    private final ObjectError objectError;

    public validationFailForm(List<FiledError> filedErrorList, ObjectError objectError) {
        super(1000);
        this.filedErrorList = filedErrorList;
        this.objectError = objectError;
    }
}
