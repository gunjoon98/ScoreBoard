package gunjoon98.scoreboard.service;

import gunjoon98.scoreboard.domain.repository.JdbcRepository;
import gunjoon98.scoreboard.domain.repository.entity.DashBoardAttendEntity;
import gunjoon98.scoreboard.domain.repository.entity.DashBoardEntity;
import gunjoon98.scoreboard.domain.repository.entity.DashBoardProblemEntity;
import gunjoon98.scoreboard.domain.repository.entity.DashBoardSolveEntity;
import gunjoon98.scoreboard.service.vo.DashBoard;
import gunjoon98.scoreboard.service.vo.DashBoardAttend;
import gunjoon98.scoreboard.service.vo.DashBoardProblem;
import gunjoon98.scoreboard.service.vo.DashBoardSolve;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashBoardService {
    private final JdbcRepository jdbcRepository;
    private int printCount;
    public List<DashBoard> getDashBoardListByRecent() {
        List<DashBoard> result = new ArrayList<>();
        List<DashBoardEntity> dashBoardEntityList = jdbcRepository.findDashBoardEntityListByDesc(printCount);

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

    public List<DashBoard> getDashBoardListByNext(int lastDashBoardId) {
        List<DashBoard> result = new ArrayList<>();
        List<DashBoardEntity> dashBoardEntityList = jdbcRepository.findDashBoardEntityListByNext(lastDashBoardId, printCount);

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


}
