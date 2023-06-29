package gunjoon98.scoreboard.domain.repository.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
public class NoticeEntity {
    private final int id;
    private final String title;
    private final String content;
    private final String type;
    private final LocalDateTime registerDate;
}
