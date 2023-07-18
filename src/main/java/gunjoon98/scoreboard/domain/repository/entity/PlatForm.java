package gunjoon98.scoreboard.domain.repository.entity;

public enum PlatForm {
    BAEKJOON(0), PROGRAMMERS(1), SW_EXPERT_ACADEMY(2), JUNGOL(3);
    private final int value;
    PlatForm (int value) { this.value = value; }
    public int getValue() { return value; }

    public static PlatForm valueOf(int value) {
        for(PlatForm platForm : PlatForm.values()) {
            if(platForm.getValue() == value) return platForm;
        }
        return null;
    }
}
