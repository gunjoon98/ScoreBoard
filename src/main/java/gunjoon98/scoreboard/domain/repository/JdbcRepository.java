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
import java.util.List;

@Repository
@Slf4j
public class JdbcRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void saveUserEntity(String userId) {
        jdbcTemplate.update("insert into user(id, isRemove) values (?,?)", userId, false);
    }

    public void deleteUserEntity(String userId) {
        jdbcTemplate.update("update user set isRemove=? where id=?", true, userId);
    }

    public UserEntity findUserEntityById(String userId) {
        return jdbcTemplate.queryForObject("select * from user where id=?", (ResultSet rs, int rowNum) -> new UserEntity(
                rs.getString("id"),
                rs.getBoolean("isRemove")), userId);
    }

    public int saveDashBoardEntity(DashBoardEntityForm dashBoardEntityForm) {
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

        return generatedKeyHolder.getKey().intValue();
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

    public List<DashBoardEntity> findDashBoardEntityListByDesc(int printCount) {
        return jdbcTemplate.query("select * from dashboard order by id desc limit ?", (ResultSet rs, int rowNum) -> new DashBoardEntity(
                rs.getInt("id"),
                rs.getTimestamp("startDate").toLocalDateTime(),
                rs.getTimestamp("endDate").toLocalDateTime()), printCount);
    }

    public List<DashBoardEntity> findDashBoardEntityListByNext(int lastDashBoardEntityId, int printCount) {
        return jdbcTemplate.query("select * from dashboard where id < ? order by id desc limit ?", (ResultSet rs, int rowNum) -> new DashBoardEntity(
                rs.getInt("id"),
                rs.getTimestamp("startDate").toLocalDateTime(),
                rs.getTimestamp("endDate").toLocalDateTime()), lastDashBoardEntityId, printCount);

    }

    public void saveDashBoardProblemEntity(DashBoardProblemEntity dashBoardProblemEntity) {
        jdbcTemplate.update("insert into dashboardproblem(number, name, level, link, dashBoardId) values (?,?,?,?,?)",
                dashBoardProblemEntity.getNumber(),
                dashBoardProblemEntity.getName(),
                dashBoardProblemEntity.getLevel(),
                dashBoardProblemEntity.getLink(),
                dashBoardProblemEntity.getDashBoardId());

        for(String type : dashBoardProblemEntity.getTypes()) {
            jdbcTemplate.update("insert into dashboardproblemtype(dashboardId, problemNumber, problemType) values (?, ?, ?)",
                    dashBoardProblemEntity.getDashBoardId(), dashBoardProblemEntity.getNumber(), type);
        }
    }

    public void deleteDashBoardProblemEntity(int dashBoardId, int problemNumber) {
        jdbcTemplate.update("delete from dashboardproblem where dashBoardId=? and number=?", dashBoardId, problemNumber);
    }

    public DashBoardProblemEntity findDashBoardProblemEntityById(int dashBoardId, int problemNumber) {
        List<String> types = findDashBoardProblemEntityTypeList(dashBoardId, problemNumber);

        return jdbcTemplate.queryForObject("select * from dashboardproblem where dashBoardId=? and number=?", (ResultSet rs, int rowNum)-> new DashBoardProblemEntity(
                rs.getInt("dashBoardId"),
                rs.getInt("number"),
                rs.getString("name"),
                rs.getString("level"),
                rs.getString("link"),
                types), dashBoardId, problemNumber);
    }

    public List<DashBoardProblemEntity> findDashBoardProblemEntityList(int dashBoardId) {
        String sql = "select * from dashboardproblem where dashBoardId=?";
        return jdbcTemplate.query(sql, (ResultSet rs, int rowNum) -> {
            List<String> types = findDashBoardProblemEntityTypeList(dashBoardId, rs.getInt("number"));

            return new DashBoardProblemEntity(
                    rs.getInt("dashBoardId"),
                    rs.getInt("number"),
                    rs.getString("name"),
                    rs.getString("level"),
                    rs.getString("link"),
                    types);
        }, dashBoardId);
    }

    private List<String> findDashBoardProblemEntityTypeList(int dashBoardId, int problemNumber) {
        String sql = "select problemType from dashboardproblemtype where dashBoardId=? and problemNumber=?";
        return jdbcTemplate.query(sql, (ResultSet rs, int rowNum) ->
                rs.getString("problemType"), dashBoardId, problemNumber);
    }

    private List<String> findTestProblemEntityTypeList(int testId, int problemNumber) {
        String sql = "select problemType from testproblemtype where testId=? and problemNumber=?";
        return jdbcTemplate.query(sql, (ResultSet rs, int rowNum) ->
                rs.getString("problemType"), testId, problemNumber);
    }

    public void saveDashBoardSolveEntity(DashBoardSolveEntity solve) {
        jdbcTemplate.update("insert into dashboardsolve(userId, dashBoardId, problemNumber, isSolve, tryCount) values(?,?,?,?,?)",
                solve.getUserId(),
                solve.getDashBoardId(),
                solve.getProblemNumber(),
                solve.isIsSolve(),
                solve.getTryCount());
    }

    public void deleteDashBoardSolveEntity(String userId, int dashBoardId, int problemNumber) {
        jdbcTemplate.update("delete from dashboardsolve where userId=? and dashBoardId=? and problemNumber=?", userId, dashBoardId, problemNumber);
    }

    public DashBoardSolveEntity findDashBoardSolveEntityById(String userId, int dashBoardId, int problemNumber) {
        return jdbcTemplate.queryForObject("select * from dashboardsolve where userId=? and dashBoardId=? and problemNumber=?", (ResultSet rs, int rowNum) -> new DashBoardSolveEntity(
                rs.getString("userId"),
                rs.getInt("dashBoardId"),
                rs.getInt("problemNumber"),
                rs.getBoolean("isSolve"),
                rs.getInt("tryCount")), userId, dashBoardId, problemNumber);
    }

    public List<DashBoardSolveEntity> findDashBoardSolveEntityList(int dashBoardId) {
        return jdbcTemplate.query("select * from dashboardsolve where dashBoardId=?", (ResultSet rs, int rowNum) -> new DashBoardSolveEntity(
                rs.getString("userId"),
                rs.getInt("dashBoardId"),
                rs.getInt("problemNumber"),
                rs.getBoolean("isSolve"),
                rs.getInt("tryCount")), dashBoardId);
    }

    public void saveDashBoardAttendEntity(DashBoardAttendEntity dashBoardAttendEntity) {
        jdbcTemplate.update("insert into dashboardattend(userId, dashBoardId) values (?,?)",
                dashBoardAttendEntity.getUserId(), dashBoardAttendEntity.getDashBoardId());
    }

    public List<DashBoardAttendEntity> findDashBoardAttendEntityList(int dashBoardId) {
        return jdbcTemplate.query("select * from dashboardattend where dashBoardId=?", (ResultSet rs, int rowNum) -> new DashBoardAttendEntity(
                rs.getString("userId"),
                rs.getInt("dashBoardId")), dashBoardId);
    }

    public int saveTestEntity(TestEntityForm testEntityForm) {
        if(testEntityForm.getStartDate().isAfter(testEntityForm.getEndDate())) {
            throw new DataAccessException("startDate is behind endDate") { };
        }

        String sql = "insert into test(startDate, endDate, problemCount) values(?,?,?)";
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update((conn) -> {
            PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setTimestamp(1, Timestamp.valueOf(testEntityForm.getStartDate().withNano(0)));
            preparedStatement.setTimestamp(2, Timestamp.valueOf(testEntityForm.getEndDate().withNano(0)));
            preparedStatement.setInt(3, testEntityForm.getProblemCount());
            return preparedStatement;
        }, generatedKeyHolder);

        return generatedKeyHolder.getKey().intValue();
    }

    public void deleteTestEntity(int testId) {
        jdbcTemplate.update("delete from test where id=?", testId);
    }

    public TestEntity findTestEntityById(int testId) {
        return jdbcTemplate.queryForObject("select * from test where id=?", (ResultSet rs, int rowNum) -> new TestEntity(
                rs.getInt("id"),
                rs.getTimestamp("startDate").toLocalDateTime(),
                rs.getTimestamp("endDate").toLocalDateTime(),
                rs.getInt("problemCount")));
    }

    public List<TestEntity> findTestEntityListByDesc(int printCount) {
        return jdbcTemplate.query("select * from test order by id desc limit ?", (ResultSet rs, int rowNum) -> new TestEntity(
                rs.getInt("id"),
                rs.getTimestamp("startDate").toLocalDateTime(),
                rs.getTimestamp("endDate").toLocalDateTime(),
                rs.getInt("problemCount")), printCount);
    }

    public void saveTestProblemEntity(TestProblemEntity testProblemEntity) {
        jdbcTemplate.update("insert into testProblem(testId, number, name, level, link) values(?, ?, ?, ?, ?)",
                testProblemEntity.getTestId(),
                testProblemEntity.getNumber(),
                testProblemEntity.getName(),
                testProblemEntity.getLevel(),
                testProblemEntity.getLink());

        for(String type : testProblemEntity.getTypes()) {
            jdbcTemplate.update("insert into testProblemType(testId, problemNumber, problemType) values(?,?,?)",
                    testProblemEntity.getTestId(),
                    testProblemEntity.getNumber(),
                    type);
        }
    }

    public TestProblemEntity findTestProblemEntityById(int testId) {
        String sql = "select * from testproblem where testId = ?";
        return jdbcTemplate.queryForObject(sql, (ResultSet rs, int rowNum) -> {
            List<String> types = findTestProblemEntityTypeList(testId, rs.getInt("problemNumber"));

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

    public List<TestProblemEntity> findTestProblemEntityList(int testId) {
        String sql = "select * from testproblem where testId = ?";
        return jdbcTemplate.query(sql, (ResultSet rs, int rowNum) -> {
            List<String> types = findTestProblemEntityTypeList(testId, rs.getInt("problemNumber"));

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

    public List<TestSolveEntity> findTestSolveEntityList(String userId, int testId, int problemNumber) {
        return jdbcTemplate.query("select * from testsolve where userId=? and testId=? and problemNumber=?", (ResultSet rs, int rowNum) -> new TestSolveEntity(
                rs.getString("userId"),
                rs.getInt("testId"),
                rs.getInt("problemNumber"),
                rs.getBoolean("isSolve"),
                rs.getInt("tryCount")), userId, testId, problemNumber);
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
