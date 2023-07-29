package gunjoon98.scoreboard.domain.repository.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.*;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
public class DashBoardProblemEntity {
    private final int dashBoardId;
    private final PlatForm platForm;
    private final int number;
    private final String name;
    private final String level;
    private final String link;
    private final Set<String> types;
}
