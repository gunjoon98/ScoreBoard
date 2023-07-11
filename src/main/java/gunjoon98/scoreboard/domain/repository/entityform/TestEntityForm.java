package gunjoon98.scoreboard.domain.repository.entityform;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TestEntityForm {
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    public TestEntityForm(LocalDateTime startDate, LocalDateTime endDate) {
        this.startDate = startDate.withNano(0);
        this.endDate = endDate.withNano(0);
    }
}
