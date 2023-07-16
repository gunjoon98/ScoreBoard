package gunjoon98.scoreboard.web.form.print;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class DashBoardPrintForm {
    private final int dashBoardId;
    private final List<DashBoardProblemPrintForm> problemList;
    private final List<DashBoardSolvePrintForm> solveList;
    private final List<DashBoardAttendPrintForm> attendList;
}
