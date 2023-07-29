package gunjoon98.scoreboard.web.contoller;

import gunjoon98.scoreboard.domain.service.DashBoardService;
import gunjoon98.scoreboard.web.form.print.DashBoardPrintForm;
import gunjoon98.scoreboard.web.form.save.DashBoardSaveForm;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class DashBoardController {
    private final DashBoardService dashBoardService;

    @GetMapping("/dashboard-list")
    public List<DashBoardPrintForm> GetDashBoard() {
        return dashBoardService.getDashBoardListByRecent();
    }

    @GetMapping("/next")
    public List<DashBoardPrintForm> GetNextDashBoard(Integer lastDashboardId) {
        //throw new RuntimeException();
        return dashBoardService.getDashBoardListByNext(lastDashboardId);
    }
}
