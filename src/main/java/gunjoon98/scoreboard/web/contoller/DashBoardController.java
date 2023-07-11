package gunjoon98.scoreboard.web.contoller;

import gunjoon98.scoreboard.domain.service.DashBoardService;
import gunjoon98.scoreboard.domain.service.vo.DashBoard;
import gunjoon98.scoreboard.domain.service.vo.Test;
import gunjoon98.scoreboard.web.api.response.print.DashBoardList;
import gunjoon98.scoreboard.web.api.response.print.InitPrintForm;
import gunjoon98.scoreboard.web.vaildation.vaildationException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class DashBoardController {
    private final DashBoardService dashBoardService;
    @GetMapping("/init")
    InitPrintForm getInitData() {
        List<DashBoard> dashBoardList = dashBoardService.getDashBoardListByRecent();
        List<Test> testList = dashBoardService.getTestListByRecent();
        return new InitPrintForm(dashBoardList, testList);
    }

    @GetMapping("/next_dashboard")
    DashBoardList getDashBoardListByNext(@RequestParam("dashBoardId") int lastDashBoardId) {
        return new DashBoardList(dashBoardService.getDashBoardListByNext(lastDashBoardId));
    }







    /*
    @GetMapping("/next_test")
    List<DashBoard> getTestListByNext(@RequestParam("testId") int lastTestId) {
        return new CommonForm();
    }
     */



}
