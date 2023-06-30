package gunjoon98.scoreboard.service.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
public class TestAttend {
    private final String userId;
    private final boolean isJoin;
}
