package gunjoon98.scoreboard.domain.repository.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode
public class TestEntity {
    private final int id;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final int problemCount;

    public TestEntity(int id, LocalDateTime startDate, LocalDateTime endDate, int problemCount) {
        this.id = id;
        this.startDate = startDate.withNano(0);
        this.endDate = endDate.withNano(0);
        this.problemCount = problemCount;
    }
}
