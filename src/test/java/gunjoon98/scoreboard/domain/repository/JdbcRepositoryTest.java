package gunjoon98.scoreboard.domain.repository;


import gunjoon98.scoreboard.domain.repository.entity.*;
import gunjoon98.scoreboard.domain.repository.entityform.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@SpringBootTest
class JdbcRepositoryTest {
    private final JdbcRepository jdbcRepository;
    private TestData testData;

    @Getter
    @RequiredArgsConstructor
    static class TestData {
        private final UserEntity userEntity;
        private final DashBoardEntity dashBoardEntity;
        private final DashBoardProblemEntity dashBoardProblemEntity1;
        private final DashBoardProblemEntity dashBoardProblemEntity2;
        private final DashBoardSolveEntity dashBoardSolveEntity1;
        private final DashBoardSolveEntity dashBoardSolveEntity2;
        private final DashBoardAttendEntity dashBoardAttendEntity;
    }

    @Autowired
    JdbcRepositoryTest(JdbcRepository jdbcRepository) {
        this.jdbcRepository = jdbcRepository;
    }

    @BeforeEach
    void DBInit() {
        UserEntity userEntity = new UserEntity("testUser", false);
        jdbcRepository.saveUserEntity(userEntity.getId());

        DashBoardEntityForm dashBoardEntityForm = new DashBoardEntityForm(LocalDateTime.now(), LocalDateTime.now().plusDays(1));
        int dashBoardEntityId = jdbcRepository.saveDashBoardEntity(dashBoardEntityForm);
        DashBoardEntity dashBoardEntity = new DashBoardEntity(dashBoardEntityId, dashBoardEntityForm.getStartDate(), dashBoardEntityForm.getEndDate());

        List<String> types = new ArrayList<>();
        types.add("구현");
        types.add("수학");
        DashBoardProblemEntity dashBoardProblemEntity1 = new DashBoardProblemEntity(dashBoardEntity.getId(), 1000, "A+B", "브론즈 V", "https://www.acmicpc.net/problem/1000", types);
        jdbcRepository.saveDashBoardProblemEntity(dashBoardProblemEntity1);
        DashBoardProblemEntity dashBoardProblemEntity2 = new DashBoardProblemEntity(dashBoardEntity.getId(), 1001, "A-B", "브론즈 V", "https://www.acmicpc.net/problem/1001", types);
        jdbcRepository.saveDashBoardProblemEntity(dashBoardProblemEntity2);

        DashBoardSolveEntity dashBoardSolveEntity1 = new DashBoardSolveEntity(userEntity.getId(), dashBoardEntity.getId(), dashBoardProblemEntity1.getNumber(), true, 3);
        jdbcRepository.saveDashBoardSolveEntity(dashBoardSolveEntity1);
        DashBoardSolveEntity dashBoardSolveEntity2 = new DashBoardSolveEntity(userEntity.getId(), dashBoardEntity.getId(), dashBoardProblemEntity2.getNumber(), false, 5);
        jdbcRepository.saveDashBoardSolveEntity(dashBoardSolveEntity2);

        DashBoardAttendEntity dashBoardAttendEntity = new DashBoardAttendEntity(userEntity.getId(), dashBoardEntity.getId());
        jdbcRepository.saveDashBoardAttendEntity(dashBoardAttendEntity);

        testData = new TestData(
                userEntity,
                dashBoardEntity,
                dashBoardProblemEntity1,
                dashBoardProblemEntity2,
                dashBoardSolveEntity1,
                dashBoardSolveEntity2,
                dashBoardAttendEntity);
    }

    @Test
    @Transactional
    void saveUserEntityTest() {
        //given
        UserEntity user = new UserEntity("test", false);

        //when
        jdbcRepository.saveUserEntity(user.getId());

        //then
        UserEntity result = jdbcRepository.findUserEntityById(user.getId());
        Assertions.assertThat(result).isEqualTo(user);
    }

    @Test
    @Transactional
    void deleteUserEntityTest() {
        //given
        String userId = "test";
        jdbcRepository.saveUserEntity(userId);

        //when
        jdbcRepository.deleteUserEntity(userId);

        //then
        Assertions.assertThat(jdbcRepository.findUserEntityById(userId).isRemove()).isEqualTo(true);
    }

    @Test
    @Transactional
    void saveDashBoardEntityTest() {
        //given
        DashBoardEntityForm dashBoardEntityForm = new DashBoardEntityForm(LocalDateTime.now(), LocalDateTime.now().plusDays(1));

        //when
        int dashboardId = jdbcRepository.saveDashBoardEntity(dashBoardEntityForm);

        //then
        DashBoardEntity dashBoardEntity = new DashBoardEntity(
                dashboardId,
                dashBoardEntityForm.getStartDate(),
                dashBoardEntityForm.getEndDate());
        DashBoardEntity result = jdbcRepository.findDashBoardEntityById(dashboardId);

        Assertions.assertThat(result).isEqualTo(dashBoardEntity);
    }


    @Test
    @Transactional
    void deleteDashBoardEntityTest() {
        //given
        UserEntity user = new UserEntity("test", false);
        jdbcRepository.saveUserEntity("test");

        DashBoardEntityForm dashBoardEntityForm = new DashBoardEntityForm(LocalDateTime.now(), LocalDateTime.now().plusDays(1));
        int dashboardId = jdbcRepository.saveDashBoardEntity(dashBoardEntityForm);

        List<String> types = new ArrayList<>();
        types.add("구현");
        types.add("수학");
        DashBoardProblemEntity dashBoardProblemEntity = new DashBoardProblemEntity(dashboardId, 1000, "A+B", "브론즈 V", "https://www.acmicpc.net/problem/1000", types);
        jdbcRepository.saveDashBoardProblemEntity(dashBoardProblemEntity);

        DashBoardSolveEntity dashBoardSolveEntity = new DashBoardSolveEntity(user.getId(), dashboardId, dashBoardProblemEntity.getNumber(), true, 3);
        jdbcRepository.saveDashBoardSolveEntity(dashBoardSolveEntity);

        DashBoardAttendEntity dashBoardAttendEntity = new DashBoardAttendEntity(user.getId(), dashboardId);
        jdbcRepository.saveDashBoardAttendEntity(dashBoardAttendEntity);

        //when
        jdbcRepository.deleteDashBoardEntity(dashboardId);

        //then
        Assertions.assertThatThrownBy(() -> jdbcRepository.findDashBoardEntityById(dashboardId))
                .isInstanceOf(EmptyResultDataAccessException.class);
        Assertions.assertThatThrownBy(() -> jdbcRepository.findDashBoardProblemEntityById(dashboardId, dashBoardProblemEntity.getDashBoardId()))
                .isInstanceOf(EmptyResultDataAccessException.class);
        Assertions.assertThatThrownBy(() -> jdbcRepository.findDashBoardSolveEntityById(user.getId(), dashboardId, dashBoardProblemEntity.getNumber()))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    @Transactional
    void findDashBoardEntityByRecentTest() {
        //given
        DashBoardEntityForm dashBoardEntityForm1 = new DashBoardEntityForm(LocalDateTime.now(), LocalDateTime.now().plusDays(1));
        jdbcRepository.saveDashBoardEntity(dashBoardEntityForm1);

        DashBoardEntityForm dashBoardEntityForm2 = new DashBoardEntityForm(LocalDateTime.now().minusDays(10), LocalDateTime.now().minusDays(1));
        jdbcRepository.saveDashBoardEntity(dashBoardEntityForm2);

        //when
        List<DashBoardEntity> result = jdbcRepository.findDashBoardEntityListByDesc(5);

        //then
        List<DashBoardEntity> dashBoardEntityList = new ArrayList<>();
        dashBoardEntityList.add(new DashBoardEntity(result.get(0).getId(), dashBoardEntityForm2.getStartDate(), dashBoardEntityForm2.getEndDate()));
        dashBoardEntityList.add(new DashBoardEntity(result.get(1).getId(), dashBoardEntityForm1.getStartDate(), dashBoardEntityForm1.getEndDate()));
        Assertions.assertThat(result).isEqualTo(dashBoardEntityList);
    }


    @Test
    @Transactional
    void saveDashBoardProblemEntityTest() {
        //given
        DashBoardEntityForm dashBoardEntityForm = new DashBoardEntityForm(LocalDateTime.now(), LocalDateTime.now().plusDays(1));
        int dashboardId = jdbcRepository.saveDashBoardEntity(dashBoardEntityForm);

        List<String> types = new ArrayList<>();
        types.add("구현");
        types.add("수학");

        DashBoardProblemEntity dashBoardProblemEntity = new DashBoardProblemEntity(
                dashboardId,
                1000,
                "A+B",
                "브론즈 V",
                "https://www.acmicpc.net/problem/1000",
                types);

        //when
        jdbcRepository.saveDashBoardProblemEntity(dashBoardProblemEntity);

        //then
        DashBoardProblemEntity result = jdbcRepository.findDashBoardProblemEntityById(dashboardId, dashBoardProblemEntity.getNumber());
        Assertions.assertThat(result).isEqualTo(dashBoardProblemEntity);
    }


    @Test
    @Transactional
    void deleteDashBoardProblemEntityTest() {
        //given
        DashBoardEntityForm dashBoardForm = new DashBoardEntityForm(LocalDateTime.now(), LocalDateTime.now().plusDays(1));
        int dashboardId = jdbcRepository.saveDashBoardEntity(dashBoardForm);

        List<String> types = new ArrayList<>();
        types.add("구현");
        types.add("수학");
        DashBoardProblemEntity dashBoardProblemEntity = new DashBoardProblemEntity(dashboardId, 1000, "A+B", "브론즈 V", "https://www.acmicpc.net/problem/1000", types);

        jdbcRepository.saveDashBoardProblemEntity(dashBoardProblemEntity);

        //when
        jdbcRepository.deleteDashBoardProblemEntity(dashboardId, dashBoardProblemEntity.getNumber());

        //then
        Assertions.assertThatThrownBy(() -> jdbcRepository.findDashBoardProblemEntityById(dashboardId, dashBoardProblemEntity.getNumber()))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    @Transactional
    void findDashBoardProblemEntityListTest() {
        //given
        DashBoardEntityForm dashBoardEntityForm = new DashBoardEntityForm(LocalDateTime.now(), LocalDateTime.now().plusDays(1));
        int dashboardId = jdbcRepository.saveDashBoardEntity(dashBoardEntityForm);

        List<String> types = new ArrayList<>();
        types.add("구현");
        types.add("수학");
        DashBoardProblemEntity dashBoardProblemEntity = new DashBoardProblemEntity(
                dashboardId,
                1000,
                "A+B",
                "브론즈 V",
                "https://www.acmicpc.net/problem/1000",
                types);

        List<DashBoardProblemEntity> list = new ArrayList<>();
        list.add(dashBoardProblemEntity);

        jdbcRepository.saveDashBoardProblemEntity(dashBoardProblemEntity);

        //when
        List<DashBoardProblemEntity> result = jdbcRepository.findDashBoardProblemEntityList(dashboardId);

        //then
        Assertions.assertThat(result).isEqualTo(list);
    }

    @Test
    @Transactional
    void saveDashBoardSolveEntityTest() {

    }
}



