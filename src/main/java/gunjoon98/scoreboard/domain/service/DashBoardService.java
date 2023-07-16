package gunjoon98.scoreboard.domain.service;

import gunjoon98.scoreboard.domain.repository.JdbcRepository;
import gunjoon98.scoreboard.domain.repository.entity.*;
import gunjoon98.scoreboard.web.form.print.*;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashBoardService {
    private final JdbcRepository jdbcRepository;
    static private final int printCount = 5;

    private List<DashBoardPrintForm> getDashBoardList(List<DashBoardEntity> dashBoardEntityList) {
        List<DashBoardPrintForm> result = new ArrayList<>();

        for(DashBoardEntity dashBoardEntity : dashBoardEntityList) {
            int dashBoardId = dashBoardEntity.getId();
            List<DashBoardProblemPrintForm> problemList = new ArrayList<>();
            List<DashBoardSolvePrintForm> solveList = new ArrayList<>();
            List<DashBoardAttendPrintForm> attendList = new ArrayList<>();

            List<DashBoardProblemEntity> dashBoardProblemEntityList = jdbcRepository.findDashBoardProblemEntityList(dashBoardId);
            for(DashBoardProblemEntity dashBoardProblemEntity : dashBoardProblemEntityList) {
                problemList.add(new DashBoardProblemPrintForm(
                        dashBoardProblemEntity.getNumber(),
                        dashBoardProblemEntity.getName(),
                        dashBoardProblemEntity.getLevel(),
                        dashBoardProblemEntity.getLink(),
                        dashBoardProblemEntity.getTypes()));
            }

            List<DashBoardSolveEntity> dashBoardSolveEntityList = jdbcRepository.findDashBoardSolveEntityList(dashBoardId);
            for(DashBoardSolveEntity dashBoardSolveEntity : dashBoardSolveEntityList) {
                solveList.add(new DashBoardSolvePrintForm(
                        dashBoardSolveEntity.getUserId(),
                        dashBoardSolveEntity.getProblemNumber(),
                        dashBoardSolveEntity.getTryCount(),
                        dashBoardSolveEntity.isSolve()));
            }

            List<DashBoardAttendEntity> dashBoardAttendEntityList = jdbcRepository.findDashBoardAttendEntityList(dashBoardId);
            for(DashBoardAttendEntity dashBoardAttendEntity : dashBoardAttendEntityList) {
                attendList.add(new DashBoardAttendPrintForm(
                        dashBoardAttendEntity.getUserId()
                ));
            }

            result.add(new DashBoardPrintForm(
                    dashBoardId,
                    problemList,
                    solveList,
                    attendList));
        }
        return result;
    }

    private List<TestPrintForm> getTestList(List<TestEntity> testEntityList) {
        List<TestPrintForm> result = new ArrayList<>();

        for(TestEntity testEntity : testEntityList) {
            int testId = testEntity.getId();
            List<TestProblemPrintForm> problemList = new ArrayList<>();
            List<TestSolvePrintForm> solveList = new ArrayList<>();
            List<TestAttendPrintForm> attendList = new ArrayList<>();

            List<TestProblemEntity> testProblemEntityList = jdbcRepository.findTestProblemEntityList(testId);
            for(TestProblemEntity testProblemEntity : testProblemEntityList) {
                problemList.add(new TestProblemPrintForm(
                        testProblemEntity.getNumber(),
                        testProblemEntity.getName(),
                        testProblemEntity.getLevel(),
                        testProblemEntity.getLink(),
                        testProblemEntity.getTypes()));
            }

            List<TestSolveEntity> testSolveEntityList = jdbcRepository.findTestSolveEntityList(testId);
            for(TestSolveEntity testSolveEntity : testSolveEntityList) {
                solveList.add(new TestSolvePrintForm(
                        testSolveEntity.getUserId(),
                        testSolveEntity.getProblemNumber(),
                        testSolveEntity.isSolve(),
                        testSolveEntity.getTryCount()
                ));
            }

            List<TestAttendEntity> testAttendEntityList = jdbcRepository.findTestAttendEntityList(testId);
            for(TestAttendEntity testAttendEntity : testAttendEntityList) {
                attendList.add(new TestAttendPrintForm(
                        testAttendEntity.getUserId(),
                        testAttendEntity.isJoin()));
            }

            result.add(new TestPrintForm(
                    testId,
                    problemList,
                    solveList,
                    attendList));
        }
        return result;
    }
    public List<DashBoardPrintForm> getDashBoardListByRecent() {
        List<DashBoardEntity> dashBoardEntityList = jdbcRepository.findDashBoardEntityListByRecent(printCount);
        return getDashBoardList(dashBoardEntityList);
    }

    public List<DashBoardPrintForm> getDashBoardListByNext(int lastDashBoardId) {
        List<DashBoardEntity> dashBoardEntityList = jdbcRepository.findDashBoardEntityListByNext(lastDashBoardId, printCount);
        return getDashBoardList(dashBoardEntityList);
    }

    public List<TestPrintForm> getTestListByRecent() {
        List<TestEntity> testEntityList = jdbcRepository.findTestEntityListByRecent(printCount);
        return getTestList(testEntityList);
    }

    public List<TestPrintForm> getTestListByNext(int lastTestId) {
        List<TestEntity> testEntityList = jdbcRepository.findTestEntityListByNext(lastTestId, printCount);
        return getTestList(testEntityList);
    }

    public boolean CheckDashBoardId(int dashBoardId) {
        try {
            jdbcRepository.findDashBoardEntityById(dashBoardId);
        }
        catch (EmptyResultDataAccessException e) {
            return false;
        }
        return true;
    }
}
