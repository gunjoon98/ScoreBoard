package gunjoon98.scoreboard.web.form.print;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TestSolvePrintForm {
    private final String userId;
    private final int problemNumber;
    private final boolean isSolve;
    private final int tryCount;
}
