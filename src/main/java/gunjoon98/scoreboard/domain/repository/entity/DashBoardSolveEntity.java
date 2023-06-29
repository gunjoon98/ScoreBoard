package gunjoon98.scoreboard.domain.repository.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
public class DashBoardSolveEntity {
    private final String userId;
    private final int dashBoardId;
    private final int problemNumber;
    private final boolean IsSolve;
    private final int tryCount;
}
