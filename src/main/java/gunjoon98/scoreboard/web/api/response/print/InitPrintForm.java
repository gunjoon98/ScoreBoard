package gunjoon98.scoreboard.web.api.response.print;

import gunjoon98.scoreboard.domain.service.vo.DashBoard;
import gunjoon98.scoreboard.domain.service.vo.Test;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class InitPrintForm {
    private final List<DashBoard> dashBoardList;
    private final List<Test> testList;
}
