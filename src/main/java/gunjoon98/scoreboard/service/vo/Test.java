package gunjoon98.scoreboard.service.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
public class Test {
    private final int id;
    private final List<TestProblem> problemList;
    private final List<TestSolve> solveList;
    private final List<TestAttend> attendList;
}


