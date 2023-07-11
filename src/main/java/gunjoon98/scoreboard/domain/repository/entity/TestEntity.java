package gunjoon98.scoreboard.domain.repository.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode
public class TestEntity {
    private final int id;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    public TestEntity(int id, LocalDateTime startDate, LocalDateTime endDate) {
        this.id = id;
        this.startDate = startDate.withNano(0);
        this.endDate = endDate.withNano(0);
    }
}
