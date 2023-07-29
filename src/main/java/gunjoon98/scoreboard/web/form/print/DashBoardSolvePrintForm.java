package gunjoon98.scoreboard.web.form.print;

import gunjoon98.scoreboard.domain.repository.entity.PlatForm;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DashBoardSolvePrintForm {
    private final String userId;
    private final PlatForm platForm;
    private final int ProblemNumber;
    private final int tryCount;
    private final boolean isSolve;
}
