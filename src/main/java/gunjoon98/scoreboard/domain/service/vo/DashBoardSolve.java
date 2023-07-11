package gunjoon98.scoreboard.domain.service.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
public class DashBoardSolve {
    private final String userId;
    private final int ProblemNumber;
    private final int tryCount;
    private final boolean isSolve;
}
