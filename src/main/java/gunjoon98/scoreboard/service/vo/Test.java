package gunjoon98.scoreboard.service.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
public class Test {
    private int id;
    private List<TestProblem> problemList;
    private List<TestSolve> solveList;
    private List<TestAttend> attendList;
}


