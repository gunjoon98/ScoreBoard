package gunjoon98.scoreboard.web.api.response.print;

import gunjoon98.scoreboard.domain.service.vo.DashBoard;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class DashBoardList {
    private final List<DashBoard> dashBoardList;
}
