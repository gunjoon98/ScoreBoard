package gunjoon98.scoreboard.domain.service;

import gunjoon98.scoreboard.domain.repository.JdbcRepository;
import gunjoon98.scoreboard.domain.repository.entity.*;
import gunjoon98.scoreboard.domain.repository.entityform.DashBoardEntityForm;
import gunjoon98.scoreboard.web.form.save.DashBoardProblemSaveForm;
import gunjoon98.scoreboard.web.form.save.DashBoardSaveForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final JdbcRepository jdbcRepository;

    @Transactional
    public void createDashBoard(DashBoardSaveForm dashBoardSaveForm) {
        DashBoardEntityForm dashBoardEntityForm = new DashBoardEntityForm(
                dashBoardSaveForm.getStartDate(),
                dashBoardSaveForm.getEndDate());
        DashBoardEntity dashBoardEntity = jdbcRepository.saveDashBoardEntity(dashBoardEntityForm);

        for(UserEntity userEntity : jdbcRepository.findActiveUserEntityList()) {
            jdbcRepository.saveDashBoardAttendEntity(new DashBoardAttendEntity(userEntity.getId(), dashBoardEntity.getId()));
        }
    }

    @Transactional
    public void deleteDashBoard(int dashBoardId) {
        jdbcRepository.deleteDashBoardEntity(dashBoardId);
    }

    @Transactional
    public void createAlgorithmProblem(DashBoardProblemSaveForm problemSaveForm) {
        DashBoardProblemEntity problemEntity = new DashBoardProblemEntity(
                problemSaveForm.getDashBoardId(),
                problemSaveForm.getNumber(),
                problemSaveForm.getName(),
                problemSaveForm.getLevel(),
                problemSaveForm.getLink(),
                problemSaveForm.getTypes());
        jdbcRepository.saveDashBoardProblemEntity(problemEntity);

        for(DashBoardAttendEntity attendEntity : jdbcRepository.findDashBoardAttendEntityList(problemEntity.getDashBoardId())) {
            jdbcRepository.saveDashBoardSolveEntity(new DashBoardSolveEntity(
                    attendEntity.getUserId(),
                    attendEntity.getDashBoardId(),
                    problemEntity.getNumber(),
                    false,
                    0));
        }
    }










}
