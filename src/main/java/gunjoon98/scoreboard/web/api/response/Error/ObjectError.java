package gunjoon98.scoreboard.web.api.response.Error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ObjectError {
    private final String rejectedValue;
    private final boolean bindingFailure;
}
