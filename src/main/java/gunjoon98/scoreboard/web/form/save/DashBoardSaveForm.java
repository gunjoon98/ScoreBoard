package gunjoon98.scoreboard.web.form.save;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class DashBoardSaveForm {
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
}
