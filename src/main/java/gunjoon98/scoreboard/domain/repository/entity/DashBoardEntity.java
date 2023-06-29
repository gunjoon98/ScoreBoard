package gunjoon98.scoreboard.domain.repository.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode
public class DashBoardEntity {
    private final int id;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    public DashBoardEntity(int id, LocalDateTime startDate, LocalDateTime endDate) {
        //localDatetime of java present until nanosecond, datetime of mysql present until microsecond
        this.id = id;
        this.startDate = startDate.withNano(0);
        this.endDate = endDate.withNano(0);
    }
}
