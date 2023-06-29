package gunjoon98.scoreboard.domain.repository.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
public class TestSolveEntity {
    private final String userId;
    private final int testId;
    private final int problemNumber;
    private final boolean isSolve;
    private final int tryCount;
}
