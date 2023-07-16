package gunjoon98.scoreboard.web.form.print;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DashBoardSolvePrintForm {
    private final String userId;
    private final int ProblemNumber;
    private final int tryCount;
    private final boolean isSolve;
}
