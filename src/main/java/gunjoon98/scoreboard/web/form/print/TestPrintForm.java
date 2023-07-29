package gunjoon98.scoreboard.web.form.print;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class TestPrintForm {
    private final int id;
    private final List<TestProblemPrintForm> problemList;
    private final List<TestSolvePrintForm> solveList;
    private final List<TestAttendPrintForm> attendList;
}
