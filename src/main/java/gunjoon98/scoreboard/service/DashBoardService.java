package gunjoon98.scoreboard.service;

import gunjoon98.scoreboard.domain.repository.JdbcRepository;
import gunjoon98.scoreboard.domain.repository.entity.*;
import gunjoon98.scoreboard.service.vo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashBoardService {
    private final JdbcRepository jdbcRepository;
    private int printCount;

    private List<DashBoard> getDashBoardList(List<DashBoardEntity> dashBoardEntityList) {
        List<DashBoard> result = new ArrayList<>();

        for(DashBoardEntity dashBoardEntity : dashBoardEntityList) {
            int dashBoardId = dashBoardEntity.getId();
            List<DashBoardProblem> problemList = new ArrayList<>();
            List<DashBoardSolve> solveList = new ArrayList<>();
            List<DashBoardAttend> attendList = new ArrayList<>();

            List<DashBoardProblemEntity> dashBoardProblemEntityList = jdbcRepository.findDashBoardProblemEntityList(dashBoardId);
            for(DashBoardProblemEntity dashBoardProblemEntity : dashBoardProblemEntityList) {
                problemList.add(new DashBoardProblem(
                        dashBoardProblemEntity.getNumber(),
                        dashBoardProblemEntity.getName(),
                        dashBoardProblemEntity.getLevel(),
                        dashBoardProblemEntity.getLink(),
                        dashBoardProblemEntity.getTypes()));
            }

            List<DashBoardSolveEntity> dashBoardSolveEntityList = jdbcRepository.findDashBoardSolveEntityList(dashBoardId);
            for(DashBoardSolveEntity dashBoardSolveEntity : dashBoardSolveEntityList) {
                solveList.add(new DashBoardSolve(
                        dashBoardSolveEntity.getUserId(),
                        dashBoardSolveEntity.getProblemNumber(),
                        dashBoardSolveEntity.getTryCount(),
                        dashBoardSolveEntity.isIsSolve()));
            }

            List<DashBoardAttendEntity> dashBoardAttendEntityList = jdbcRepository.findDashBoardAttendEntityList(dashBoardId);
            for(DashBoardAttendEntity dashBoardAttendEntity : dashBoardAttendEntityList) {
                attendList.add(new DashBoardAttend(
                        dashBoardAttendEntity.getUserId()
                ));
            }

            result.add(new DashBoard(
                    dashBoardId,
                    problemList,
                    solveList,
                    attendList));
        }
        return result;
    }

    private List<Test> getTestList(List<TestEntity> testEntityList) {
        List<Test> result = new ArrayList<>();

        for(TestEntity testEntity : testEntityList) {
            int testId = testEntity.getId();
            List<TestProblem> problemList = new ArrayList<>();
            List<TestSolve> solveList = new ArrayList<>();
            List<TestAttend> attendList = new ArrayList<>();

            List<TestProblemEntity> testProblemEntityList = jdbcRepository.findTestProblemEntityList(testId);
            for(TestProblemEntity testProblemEntity : testProblemEntityList) {
                problemList.add(new TestProblem(
                        testProblemEntity.getNumber(),
                        testProblemEntity.getName(),
                        testProblemEntity.getLevel(),
                        testProblemEntity.getLink(),
                        testProblemEntity.getTypes()));
            }

            List<TestSolveEntity> testSolveEntityList = jdbcRepository.findTestSolveEntityList(testId);
            for(TestSolveEntity testSolveEntity : testSolveEntityList) {
                solveList.add(new TestSolve(
                        testSolveEntity.getUserId(),
                        testSolveEntity.getProblemNumber(),
                        testSolveEntity.isSolve(),
                        testSolveEntity.getTryCount()
                ));
            }

            List<TestAttendEntity> testAttendEntityList = jdbcRepository.findTestAttendEntityList(testId);
            for(TestAttendEntity testAttendEntity : testAttendEntityList) {
                attendList.add(new TestAttend(
                        testAttendEntity.getUserId(),
                        testAttendEntity.isJoin()));
            }

            result.add(new Test(
                    testId,
                    problemList,
                    solveList,
                    attendList));
        }
        return result;
    }
    public List<DashBoard> getDashBoardListByRecent() {
        List<DashBoardEntity> dashBoardEntityList = jdbcRepository.findDashBoardEntityListByDesc(printCount);
        return getDashBoardList(dashBoardEntityList);
    }

    public List<DashBoard> getDashBoardListByNext(int lastDashBoardId) {
        List<DashBoardEntity> dashBoardEntityList = jdbcRepository.findDashBoardEntityListByNext(lastDashBoardId, printCount);
        return getDashBoardList(dashBoardEntityList);
    }

    public List<Test> getTestListByRecent() {
        List<TestEntity> testEntityList = jdbcRepository.findTestEntityListByDesc(printCount);
        return getTestList(testEntityList);
    }

    public List<Test> getTestListByNext(int lastTestId) {
        List<TestEntity> testEntityList = jdbcRepository.findTestEntityListByNext(lastTestId, printCount);
        return getTestList(testEntityList);
    }
}
