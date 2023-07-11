package gunjoon98.scoreboard.domain.repository.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Getter
@RequiredArgsConstructor
public class TestProblemEntity {
    private final int testId;
    private final int number;
    private final String name;
    private final String level;
    private final String link;
    private final List<String> types;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestProblemEntity that = (TestProblemEntity) o;
        return testId == that.testId && number == that.number && Objects.equals(name, that.name)
                && Objects.equals(level, that.level) && Objects.equals(link, that.link) && listEquals(that.types);
    }
    public boolean listEquals(List<String> targetList) {
        if(types.size() != targetList.size()) return false;

        Map<String, Integer> map = new HashMap<>();
        for(String type : types) {
            map.put(type, 0);
        }
        for(String targetType : targetList) {
            if(!map.containsKey(targetType)) return false;
            map.put(targetType, map.get(targetType) + 1);
        }
        for(int value : map.values()) {
            if(value != 1) return false;
        }
        return true;
    }
}
