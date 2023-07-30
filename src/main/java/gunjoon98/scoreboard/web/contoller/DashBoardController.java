package gunjoon98.scoreboard.web.contoller;

import gunjoon98.scoreboard.domain.service.DashBoardService;
import gunjoon98.scoreboard.web.ErrorHandler.ParameterException;
import gunjoon98.scoreboard.web.form.print.DashBoardPrintForm;
import gunjoon98.scoreboard.web.form.print.TestPrintForm;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class DashBoardController {
    private final DashBoardService dashBoardService;

    @GetMapping("/dashboard-list")
    public List<DashBoardPrintForm> getDashBoard() {
        return dashBoardService.getDashBoardListByRecent();
    }

    @GetMapping("/dashboard/next")
    public List<DashBoardPrintForm> getNextDashBoard(Integer lastDashboardId) {
        if (lastDashboardId == null) {
            throw new ParameterException();
        }

        if (!dashBoardService.ExistDashBoard(lastDashboardId)) {
            throw new ParameterException();
        }

        return dashBoardService.getDashBoardListByNext(lastDashboardId);
    }

    @GetMapping("/test-list")
    public List<TestPrintForm> getTest() {
        return dashBoardService.getTestListByRecent();
    }

    @GetMapping("/test/next")
    public List<TestPrintForm> getNextTest(Integer lastTestId) {
        if (lastTestId == null) {
            throw new ParameterException();
        }

        if (!dashBoardService.ExistTest(lastTestId)) {
            throw new ParameterException();
        }

        return dashBoardService.getTestListByNext(lastTestId);
    }
}
