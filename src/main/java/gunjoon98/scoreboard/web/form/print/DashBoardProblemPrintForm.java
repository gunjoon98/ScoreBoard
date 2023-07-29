package gunjoon98.scoreboard.web.form.print;

import gunjoon98.scoreboard.domain.repository.entity.PlatForm;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Getter
@RequiredArgsConstructor
public class DashBoardProblemPrintForm {
    private final PlatForm platForm;
    private final int number;
    private final String name;
    private final String level;
    private final String link;
    private final Set<String> types;
}
