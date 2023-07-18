package gunjoon98.scoreboard.domain.repository.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.*;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
public class TestProblemEntity {
    private final int testId;
    private final int number;
    private final String name;
    private final String level;
    private final String link;
    private final Set<String> types;
}
