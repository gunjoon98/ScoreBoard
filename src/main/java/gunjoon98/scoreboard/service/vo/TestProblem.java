package gunjoon98.scoreboard.service.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
public class TestProblem {
    private final int number;
    private final String name;
    private final String level;
    private final String link;
    private final List<String> types;
}
