package gunjoon98.scoreboard.domain.service.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
public class DashBoard {
    private final int id;
    private final List<DashBoardProblem> problemList;
    private final List<DashBoardSolve> solveList;
    private final List<DashBoardAttend> attendList;
}




