package gunjoon98.scoreboard.domain.repository;

import gunjoon98.scoreboard.domain.repository.entityform.*;
import gunjoon98.scoreboard.domain.repository.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
@Slf4j
public class JdbcRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public UserEntity saveUserEntity(String userId, String passWord) {
        jdbcTemplate.update("insert into user(id, passWord, isRemove) values (?, ?, ?)", userId, passWord, false);
        return new UserEntity(userId, passWord,false);
    }

    public void deleteUserEntity(String userId) {
        jdbcTemplate.update("update user set isRemove = ? where id = ?", true, userId);
    }

    public UserEntity findUserEntityById(String userId) {
        return jdbcTemplate.queryForObject("select * from user where id=?", (ResultSet rs, int rowNum) -> new UserEntity(
                rs.getString("id"),
                rs.getString("passWord"),
                rs.getBoolean("isRemove")), userId);
    }

    public List<UserEntity> findActiveUserEntityList() {
        return jdbcTemplate.query("select * from user where isRemove = ?", (ResultSet rs, int rowNum) -> new UserEntity(
                rs.getString("id"),
                rs.getString("passWord"),
                rs.getBoolean("isRemove")), false);
    }

    public DashBoardEntity saveDashBoardEntity(DashBoardEntityForm dashBoardEntityForm) {
        if(dashBoardEntityForm.getStartDate().isAfter(dashBoardEntityForm.getEndDate())) {
            throw new DataAccessException("startDate is behind endDate") { };
        }

        String sql = "insert into dashboard(startDate, endDate) values (?,?)";
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update((conn) -> {
            PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setTimestamp(1, Timestamp.valueOf(dashBoardEntityForm.getStartDate()));
            preparedStatement.setTimestamp(2, Timestamp.valueOf(dashBoardEntityForm.getEndDate()));
            return preparedStatement;
        }, generatedKeyHolder);

        return new DashBoardEntity(generatedKeyHolder.getKey().intValue(), dashBoardEntityForm.getStartDate(), dashBoardEntityForm.getEndDate());
    }

    public void deleteDashBoardEntity(int dashBoardId) {
        jdbcTemplate.update("delete from dashboard where id=?", dashBoardId);
    }

    public DashBoardEntity findDashBoardEntityById(int dashBoardId) {
        return jdbcTemplate.queryForObject("select * from dashboard where id=?", (ResultSet rs, int rowNum) -> new DashBoardEntity(
                rs.getInt("id"),
                rs.getTimestamp("startDate").toLocalDateTime(),
                rs.getTimestamp("endDate").toLocalDateTime()), dashBoardId);
    }

    public List<DashBoardEntity> findDashBoardEntityRecentList(int printCount) {
        return jdbcTemplate.query("select * from dashboard order by id desc limit ?", (ResultSet rs, int rowNum) -> new DashBoardEntity(
                rs.getInt("id"),
                rs.getTimestamp("startDate").toLocalDateTime(),
                rs.getTimestamp("endDate").toLocalDateTime()), printCount);
    }

    public List<DashBoardEntity> findDashBoardEntityNextList(int lastDashBoardEntityId, int printCount) {
        return jdbcTemplate.query("select * from dashboard where id < ? order by id desc limit ?", (ResultSet rs, int rowNum) -> new DashBoardEntity(
                rs.getInt("id"),
                rs.getTimestamp("startDate").toLocalDateTime(),
                rs.getTimestamp("endDate").toLocalDateTime()), lastDashBoardEntityId, printCount);
    }

    public void saveDashBoardProblemEntity(DashBoardProblemEntity dashBoardProblemEntity) {
        jdbcTemplate.update("insert into dashboardproblem(dashBoardId, platForm, number, name, level, link) values (?, ?, ?, ?, ?, ?)",
                dashBoardProblemEntity.getDashBoardId(),
                dashBoardProblemEntity.getPlatForm().getValue(),
                dashBoardProblemEntity.getNumber(),
                dashBoardProblemEntity.getName(),
                dashBoardProblemEntity.getLevel(),
                dashBoardProblemEntity.getLink());

        for(String type : dashBoardProblemEntity.getTypes()) {
            jdbcTemplate.update("insert into dashboardproblemtype(dashboardId, problemPlatForm, problemNumber, problemType) values (?, ?, ?, ?)",
                    dashBoardProblemEntity.getDashBoardId(), dashBoardProblemEntity.getPlatForm().getValue(), dashBoardProblemEntity.getNumber(), type);
        }
    }

    public void deleteDashBoardProblemEntity(int dashBoardId, PlatForm platForm, int number) {
        jdbcTemplate.update("delete from dashboardproblem where dashBoardId=? and platForm=? and number=?", dashBoardId, platForm.getValue(), number);
    }

    public DashBoardProblemEntity findDashBoardProblemEntityById(int dashBoardId, PlatForm platForm, int number) {
        Set<String> types = findDashBoardProblemEntityTypeList(dashBoardId, platForm, number);

        return jdbcTemplate.queryForObject("select * from dashboardproblem where dashBoardId=? and platForm=? and number=?", (ResultSet rs, int rowNum) -> new DashBoardProblemEntity(
                rs.getInt("dashBoardId"),
                PlatForm.valueOf(rs.getInt("platForm")),
                rs.getInt("number"),
                rs.getString("name"),
                rs.getString("level"),
                rs.getString("link"),
                types), dashBoardId, platForm.getValue(), number);
    }

    public List<DashBoardProblemEntity> findDashBoardProblemEntityList(int dashBoardId) {
        String sql = "select * from dashboardproblem where dashBoardId=?";
        return jdbcTemplate.query(sql, (ResultSet rs, int rowNum) -> {
            Set<String> types = findDashBoardProblemEntityTypeList(dashBoardId, PlatForm.valueOf(rs.getInt("platForm")), rs.getInt("number"));

            return new DashBoardProblemEntity(
                    rs.getInt("dashBoardId"),
                    PlatForm.valueOf(rs.getInt("platForm")),
                    rs.getInt("number"),
                    rs.getString("name"),
                    rs.getString("level"),
                    rs.getString("link"),
                    types);
        }, dashBoardId);
    }

    private Set<String> findDashBoardProblemEntityTypeList(int dashBoardId, PlatForm problemPlatForm, int problemNumber) {
        String sql = "select problemType from dashboardproblemtype where dashBoardId=? and problemPlatForm=? and problemNumber=?";
        return new HashSet<>(jdbcTemplate.query(sql, (ResultSet rs, int rowNum) ->
                rs.getString("problemType"), dashBoardId, problemPlatForm.getValue(), problemNumber));
    }

    private Set<String> findTestProblemEntityTypeList(int testId, int problemNumber) {
        String sql = "select problemType from testproblemtype where testId=? and problemNumber=?";
        return new HashSet<>(jdbcTemplate.query(sql, (ResultSet rs, int rowNum) ->
                rs.getString("problemType"), testId, problemNumber));
    }

    public void saveDashBoardSolveEntity(DashBoardSolveEntity solve) {
        jdbcTemplate.update("insert into dashboardsolve(userId, dashBoardId, problemPlatForm, problemNumber, isSolve, tryCount) values(?, ?, ?, ?, ?, ?)",
                solve.getUserId(),
                solve.getDashBoardId(),
                solve.getProblemPlatForm().getValue(),
                solve.getProblemNumber(),
                solve.isSolve(),
                solve.getTryCount());
    }

    public void deleteDashBoardSolveEntity(String userId, int dashBoardId, PlatForm problemPlatForm, int problemNumber) {
        jdbcTemplate.update("delete from dashboardsolve where userId=? and dashBoardId=? and problemPlatForm=? and problemNumber=?",
                userId, dashBoardId, problemPlatForm.getValue(), problemNumber);
    }

    public DashBoardSolveEntity findDashBoardSolveEntityById(String userId, int dashBoardId, PlatForm problemPlatForm, int problemNumber) {
        return jdbcTemplate.queryForObject("select * from dashboardsolve where userId=? and dashBoardId=? and problemPlatForm=? and problemNumber=?", (ResultSet rs, int rowNum) -> new DashBoardSolveEntity(
                rs.getString("userId"),
                rs.getInt("dashBoardId"),
                PlatForm.valueOf(rs.getInt("problemPlatForm")),
                rs.getInt("problemNumber"),
                rs.getBoolean("isSolve"),
                rs.getInt("tryCount")), userId, dashBoardId, problemPlatForm.getValue(), problemNumber);
    }

    public List<DashBoardSolveEntity> findDashBoardSolveEntityList(int dashBoardId) {
        return jdbcTemplate.query("select * from dashboardsolve where dashBoardId=?", (ResultSet rs, int rowNum) -> new DashBoardSolveEntity(
                rs.getString("userId"),
                rs.getInt("dashBoardId"),
                PlatForm.valueOf(rs.getInt("problemPlatForm")),
                rs.getInt("problemNumber"),
                rs.getBoolean("isSolve"),
                rs.getInt("tryCount")), dashBoardId);
    }

    public void saveDashBoardAttendEntity(DashBoardAttendEntity dashBoardAttendEntity) {
        jdbcTemplate.update("insert into dashboardattend(userId, dashBoardId) values (?, ?)",
                dashBoardAttendEntity.getUserId(), dashBoardAttendEntity.getDashBoardId());
    }

    public DashBoardAttendEntity findDashBoardAttendEntityById(String userId, int dashBoardId) {
        return jdbcTemplate.queryForObject("select * from dashboardattend where userId=? and dashBoardId=?", (ResultSet rs, int rowNum) -> new DashBoardAttendEntity(
                rs.getString("userId"),
                rs.getInt("dashBoardId")), userId, dashBoardId);
    }

    public List<DashBoardAttendEntity> findDashBoardAttendEntityList(int dashBoardId) {
        return jdbcTemplate.query("select * from dashboardattend where dashBoardId=?", (ResultSet rs, int rowNum) -> new DashBoardAttendEntity(
                rs.getString("userId"),
                rs.getInt("dashBoardId")), dashBoardId);
    }

    public TestEntity saveTestEntity(TestEntityForm testEntityForm) {
        if(testEntityForm.getStartDate().isAfter(testEntityForm.getEndDate())) {
            throw new DataAccessException("startDate is behind endDate") { };
        }

        String sql = "insert into test(startDate, endDate) values(?,?)";
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update((conn) -> {
            PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setTimestamp(1, Timestamp.valueOf(testEntityForm.getStartDate()));
            preparedStatement.setTimestamp(2, Timestamp.valueOf(testEntityForm.getEndDate()));
            return preparedStatement;
        }, generatedKeyHolder);

        return new TestEntity(generatedKeyHolder.getKey().intValue(), testEntityForm.getStartDate(), testEntityForm.getEndDate());
    }

    public void deleteTestEntity(int testId) {
        jdbcTemplate.update("delete from test where id=?", testId);
    }

    public TestEntity findTestEntityById(int testId) {
        return jdbcTemplate.queryForObject("select * from test where id=?", (ResultSet rs, int rowNum) -> new TestEntity(
                rs.getInt("id"),
                rs.getTimestamp("startDate").toLocalDateTime(),
                rs.getTimestamp("endDate").toLocalDateTime()), testId);
    }

    public List<TestEntity> findTestEntityyRecentList(int printCount) {
        return jdbcTemplate.query("select * from test order by id desc limit ?", (ResultSet rs, int rowNum) -> new TestEntity(
                rs.getInt("id"),
                rs.getTimestamp("startDate").toLocalDateTime(),
                rs.getTimestamp("endDate").toLocalDateTime()), printCount);
    }

    public List<TestEntity> findTestEntityNextList(int lastTestEntityId, int printCount) {
        return jdbcTemplate.query("select * from test where id < ? order by id desc limit ?", (ResultSet rs, int rowNum) -> new TestEntity(
                rs.getInt("id"),
                rs.getTimestamp("startDate").toLocalDateTime(),
                rs.getTimestamp("endDate").toLocalDateTime()), lastTestEntityId, printCount);
    }

    public void saveTestProblemEntity(TestProblemEntity testProblemEntity) {
        jdbcTemplate.update("insert into testProblem(testId, number, name, level, link) values(?, ?, ?, ?, ?)",
                testProblemEntity.getTestId(),
                testProblemEntity.getNumber(),
                testProblemEntity.getName(),
                testProblemEntity.getLevel(),
                testProblemEntity.getLink());

        for(String type : testProblemEntity.getTypes()) {
            jdbcTemplate.update("insert into testProblemType(testId, problemNumber, problemType) values(?, ?, ?)",
                    testProblemEntity.getTestId(),
                    testProblemEntity.getNumber(),
                    type);
        }
    }

    public TestProblemEntity findTestProblemEntityById(int testId, int problemNumber) {
        String sql = "select * from testproblem where testId = ? and number = ?";
        return jdbcTemplate.queryForObject(sql, (ResultSet rs, int rowNum) -> {
            Set<String> types = findTestProblemEntityTypeList(testId, problemNumber);

            return new TestProblemEntity(
                    rs.getInt("testId"),
                    rs.getInt("number"),
                    rs.getString("name"),
                    rs.getString("level"),
                    rs.getString("link"),
                    types
            );
        }, testId, problemNumber);
    }

    public List<TestProblemEntity> findTestProblemEntityList(int testId) {
        String sql = "select * from testproblem where testId = ?";
        return jdbcTemplate.query(sql, (ResultSet rs, int rowNum) -> {
                Set<String> types = findTestProblemEntityTypeList(testId, rs.getInt("problemNumber"));

            return new TestProblemEntity(
                    rs.getInt("testId"),
                    rs.getInt("number"),
                    rs.getString("name"),
                    rs.getString("level"),
                    rs.getString("link"),
                    types
            );
        }, testId);
    }

    public void saveTestSolveEntity(TestSolveEntity solve) {
        jdbcTemplate.update("insert into testsolve(userId, testId, problemNumber, isSolve, tryCount) values(?,?,?,?,?)",
                solve.getUserId(),
                solve.getTestId(),
                solve.getProblemNumber(),
                solve.isSolve(),
                solve.getTryCount());
    }

    public void deleteTestSolveEntity(String userId, int testId, int problemNumber) {
        jdbcTemplate.update("delete from testsolve where userId=? and testId=? and problemNumber=?", userId, testId, problemNumber);
    }

    public TestSolveEntity findTestSolveEntityById(String userId, int testId, int problemNumber) {
        return jdbcTemplate.queryForObject("select * from testsolve where userId=? and testId=? and problemNumber=?", (ResultSet rs, int rowNum) -> new TestSolveEntity(
                rs.getString("userId"),
                rs.getInt("testId"),
                rs.getInt("problemNumber"),
                rs.getBoolean("isSolve"),
                rs.getInt("tryCount")), userId, testId, problemNumber);
    }

    public List<TestSolveEntity> findTestSolveEntityList(int testId) {
        return jdbcTemplate.query("select * from testsolve where testId=?", (ResultSet rs, int rowNum) -> new TestSolveEntity(
                rs.getString("userId"),
                rs.getInt("testId"),
                rs.getInt("problemNumber"),
                rs.getBoolean("isSolve"),
                rs.getInt("tryCount")), testId);
    }

    public void saveTestAttendEntity(TestAttendEntity testAttend) {
        jdbcTemplate.update("insert into testattend(userId, testId, isJoin) values (?,?,?)",
                testAttend.getUserId(), testAttend.getTestId(), testAttend.isJoin());
    }

    public List<TestAttendEntity> findTestAttendEntityList(int testId) {
        return jdbcTemplate.query("select * from testattend where testId=?", (ResultSet rs, int rowNum) -> new TestAttendEntity(
                rs.getString("userId"),
                rs.getInt("testId"),
                rs.getBoolean("isJoin")), testId);
    }
}
