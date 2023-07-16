package gunjoon98.scoreboard.web.form.save;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class DashBoardProblemSaveForm {
    private final int dashBoardId;
    private final int number;
    private final String name;
    private final String level;
    private final String link;
    private final List<String> types;
}
