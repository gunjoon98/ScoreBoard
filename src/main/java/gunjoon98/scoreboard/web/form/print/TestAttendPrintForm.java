package gunjoon98.scoreboard.web.form.print;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TestAttendPrintForm {
    private final String userId;
    private final boolean isJoin;
}
