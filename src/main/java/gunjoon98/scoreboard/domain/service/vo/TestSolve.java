package gunjoon98.scoreboard.domain.service.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
public class TestSolve {
    private final String userId;
    private final int problemNumber;
    private final boolean isSolve;
    private final int tryCount;
}
