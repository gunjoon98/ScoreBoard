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

import java.sql.ResultSet;
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
        private final TestEntity testEntity;
        private final TestProblemEntity testProblemEntity1;
        private final TestProblemEntity testProblemEntity2;
        private final TestSolveEntity testSolveEntity1;
        private final TestSolveEntity testSolveEntity2;
        private final TestAttendEntity testAttendEntity;
    }

    @Autowired
    JdbcRepositoryTest(JdbcRepository jdbcRepository) {
        this.jdbcRepository = jdbcRepository;
    }

    @BeforeEach
    void DBInit() {
        UserEntity userEntity = jdbcRepository.saveUserEntity("testUser", "123456");

        List<String> types = new ArrayList<>();
        types.add("구현");
        types.add("수학");

        DashBoardEntity dashBoardEntity = jdbcRepository.saveDashBoardEntity(new DashBoardEntityForm(LocalDateTime.now(), LocalDateTime.now().plusDays(1)));

        DashBoardProblemEntity dashBoardProblemEntity1 = new DashBoardProblemEntity(dashBoardEntity.getId(), "baekjoon", "1000, "A+B", "브론즈 V", "https://www.acmicpc.net/problem/1000", types);
        jdbcRepository.saveDashBoardProblemEntity(dashBoardProblemEntity1);
        DashBoardProblemEntity dashBoardProblemEntity2 = new DashBoardProblemEntity(dashBoardEntity.getId(), 1001, "A-B", "브론즈 V", "https://www.acmicpc.net/problem/1001", types);
        jdbcRepository.saveDashBoardProblemEntity(dashBoardProblemEntity2);

        DashBoardSolveEntity dashBoardSolveEntity1 = new DashBoardSolveEntity(userEntity.getId(), dashBoardEntity.getId(), dashBoardProblemEntity1.getNumber(), true, 3);
        jdbcRepository.saveDashBoardSolveEntity(dashBoardSolveEntity1);
        DashBoardSolveEntity dashBoardSolveEntity2 = new DashBoardSolveEntity(userEntity.getId(), dashBoardEntity.getId(), dashBoardProblemEntity2.getNumber(), false, 5);
        jdbcRepository.saveDashBoardSolveEntity(dashBoardSolveEntity2);

        DashBoardAttendEntity dashBoardAttendEntity = new DashBoardAttendEntity(userEntity.getId(), dashBoardEntity.getId());
        jdbcRepository.saveDashBoardAttendEntity(dashBoardAttendEntity);

        TestEntity testEntity = jdbcRepository.saveTestEntity(new TestEntityForm(LocalDateTime.now(), LocalDateTime.now().plusDays(+1)));

        TestProblemEntity testProblemEntity1 = new TestProblemEntity(testEntity.getId(), 1000, "A+B", "브론즈 V", "https://www.acmicpc.net/problem/1000", types);
        jdbcRepository.saveTestProblemEntity(testProblemEntity1);
        TestProblemEntity testProblemEntity2 = new TestProblemEntity(testEntity.getId(), 1001, "A-B", "브론즈 V", "https://www.acmicpc.net/problem/1001", types);
        jdbcRepository.saveTestProblemEntity(testProblemEntity2);

        TestSolveEntity testSolveEntity1 = new TestSolveEntity(userEntity.getId(), testEntity.getId(), testProblemEntity1.getNumber(), false, 5);
        jdbcRepository.saveTestSolveEntity(testSolveEntity1);
        TestSolveEntity testSolveEntity2 = new TestSolveEntity(userEntity.getId(), testEntity.getId(), testProblemEntity2.getNumber(), true, 5);
        jdbcRepository.saveTestSolveEntity(testSolveEntity2);

        TestAttendEntity testAttendEntity = new TestAttendEntity(userEntity.getId(), testEntity.getId(), true);
        jdbcRepository.saveTestAttendEntity(testAttendEntity);

        testData = new TestData(
                userEntity,
                dashBoardEntity,
                dashBoardProblemEntity1,
                dashBoardProblemEntity2,
                dashBoardSolveEntity1,
                dashBoardSolveEntity2,
                dashBoardAttendEntity,
                testEntity,
                testProblemEntity1,
                testProblemEntity2,
                testSolveEntity1,
                testSolveEntity2,
                testAttendEntity);
    }

    @Test
    @Transactional
    void saveUserEntityTest() {
        //given
        String userId = "test";

        //when
        UserEntity result = jdbcRepository.saveUserEntity(userId);

        //then
        UserEntity expectation = new UserEntity(userId, false);
        Assertions.assertThat(result).isEqualTo(expectation);
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
    void findActiveUserEntityListTest() {
        //given

        //when
        List<UserEntity> result = jdbcRepository.findActiveUserEntityList();

        //then
        Assertions.assertThat(result.get(0)).isEqualTo(testData.userEntity);
    }

    @Test
    @Transactional
    void saveDashBoardEntityTest() {
        //given
        DashBoardEntityForm dashBoardEntityForm = new DashBoardEntityForm(LocalDateTime.now(), LocalDateTime.now().plusDays(1));

        //when
        DashBoardEntity result = jdbcRepository.saveDashBoardEntity(dashBoardEntityForm);

        //then
        DashBoardEntity expectation = new DashBoardEntity(
                result.getId(),
                dashBoardEntityForm.getStartDate(),
                dashBoardEntityForm.getEndDate());
        Assertions.assertThat(result).isEqualTo(expectation);
    }

    @Test
    @Transactional
    void deleteDashBoardEntityTest() {
        //given

        //when
        jdbcRepository.deleteDashBoardEntity(testData.getDashBoardEntity().getId());

        //then
        int dashBoardId = testData.getDashBoardEntity().getId();
        Assertions.assertThatThrownBy(() -> jdbcRepository.findDashBoardEntityById(dashBoardId)).isInstanceOf(EmptyResultDataAccessException.class);
        Assertions.assertThat(jdbcRepository.findDashBoardProblemEntityList(dashBoardId).size()).isEqualTo(0);
        Assertions.assertThat(jdbcRepository.findDashBoardSolveEntityList(dashBoardId).size()).isEqualTo(0);
        Assertions.assertThat(jdbcRepository.findDashBoardAttendEntityList(dashBoardId).size()).isEqualTo(0);
    }

    @Test
    @Transactional
    void findDashBoardEntityByRecentTest() {
        //given
        DashBoardEntity dashBoardEntity = jdbcRepository.saveDashBoardEntity(new DashBoardEntityForm(LocalDateTime.now(), LocalDateTime.now().plusDays(1)));

        //when
        List<DashBoardEntity> result = jdbcRepository.findDashBoardEntityListByRecent(5);

        //then
        List<DashBoardEntity> expectation = new ArrayList<>();
        expectation.add(dashBoardEntity);
        expectation.add(testData.dashBoardEntity);
        Assertions.assertThat(result).isEqualTo(expectation);
    }

    @Test
    @Transactional
    void findDashBoardEntityByNextTest() {
        //given
        DashBoardEntity dashBoardEntity = jdbcRepository.saveDashBoardEntity(new DashBoardEntityForm(LocalDateTime.now(), LocalDateTime.now().plusDays(1)));

        //when
        List<DashBoardEntity> result = jdbcRepository.findDashBoardEntityListByNext(dashBoardEntity.getId(), 5);

        //then
        List<DashBoardEntity> expectation = new ArrayList<>();
        expectation.add(testData.dashBoardEntity);
        Assertions.assertThat(result).isEqualTo(expectation);
    }

    @Test
    @Transactional
    void saveDashBoardProblemEntityTest() {
        List<String> types = new ArrayList<>();
        types.add("브루트포스");
        types.add("백트래킹");

        DashBoardProblemEntity dashBoardProblemEntity = new DashBoardProblemEntity(
                testData.getDashBoardEntity().getId(),
                1050,
                "TEST",
                "브론즈 V",
                "https://TEST",
                types);

        //when
        jdbcRepository.saveDashBoardProblemEntity(dashBoardProblemEntity);

        //then
        DashBoardProblemEntity result = jdbcRepository.findDashBoardProblemEntityById(testData.getDashBoardEntity().getId(), dashBoardProblemEntity.getNumber());
        Assertions.assertThat(result).isEqualTo(dashBoardProblemEntity);
    }


    @Test
    @Transactional
    void deleteDashBoardProblemEntityTest() {
        //given

        //when
        jdbcRepository.deleteDashBoardProblemEntity(testData.getDashBoardProblemEntity1().getDashBoardId(), testData.getDashBoardProblemEntity1().getNumber());

        //then
        Assertions.assertThatThrownBy(() -> jdbcRepository.findDashBoardProblemEntityById(testData.getDashBoardProblemEntity1().getDashBoardId(), testData.getDashBoardProblemEntity1().getNumber()))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    @Transactional
    void findDashBoardProblemEntityListTest() {
        //given
        List<DashBoardProblemEntity> expectation = new ArrayList<>();
        expectation.add(testData.dashBoardProblemEntity1);
        expectation.add(testData.dashBoardProblemEntity2);

        //when
        List<DashBoardProblemEntity> result = jdbcRepository.findDashBoardProblemEntityList(testData.getDashBoardEntity().getId());

        //then
        Assertions.assertThat(result).isEqualTo(expectation);
    }

    @Test
    @Transactional
    void saveDashBoardSolveEntityTest() {
        //given
        List<String> types = new ArrayList<>();
        types.add("구현");
        types.add("수학");
        DashBoardProblemEntity dashBoardProblemEntity = new DashBoardProblemEntity(
                testData.getDashBoardEntity().getId(),
                1050,
                "TEST",
                "브론즈 V",
                "https://TEST",
                types);
        jdbcRepository.saveDashBoardProblemEntity(dashBoardProblemEntity);
        DashBoardSolveEntity dashBoardSolveEntity = new DashBoardSolveEntity(testData.userEntity.getId(), testData.getDashBoardEntity().getId(), dashBoardProblemEntity.getNumber(), true, 3);

        //when
        jdbcRepository.saveDashBoardSolveEntity(dashBoardSolveEntity);

        //then
        Assertions.assertThat(jdbcRepository.findDashBoardSolveEntityById(testData.userEntity.getId(), testData.getDashBoardEntity().getId(), dashBoardProblemEntity.getNumber())).isEqualTo(dashBoardSolveEntity);
    }

    @Test
    @Transactional
    void deleteDashBoardSolveEntityTest() {
        //given

        //when
        jdbcRepository.deleteDashBoardSolveEntity(testData.getDashBoardSolveEntity1().getUserId(), testData.getDashBoardSolveEntity1().getDashBoardId(),
                testData.getDashBoardSolveEntity1().getProblemNumber());

        //then
        Assertions.assertThatThrownBy(() -> jdbcRepository.findDashBoardSolveEntityById(testData.userEntity.getId(), testData.getDashBoardEntity().getId(),
                        testData.getDashBoardProblemEntity1().getNumber())).isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    @Transactional
    void saveDashBoardAttendEntityTest() {
        //given
        UserEntity userEntity = jdbcRepository.saveUserEntity("test");

        //when
        DashBoardAttendEntity dashBoardAttendEntity = new DashBoardAttendEntity(userEntity.getId(), testData.getDashBoardEntity().getId());
        jdbcRepository.saveDashBoardAttendEntity(dashBoardAttendEntity);

        //then
        Assertions.assertThat(jdbcRepository.findDashBoardAttendEntityById(userEntity.getId(), testData.getDashBoardEntity().getId())).isEqualTo(dashBoardAttendEntity);
    }

    @Test
    @Transactional
    void findDashBoardAttendEntityList() {
        //given
        List<DashBoardAttendEntity> expectation = new ArrayList<>();
        expectation.add(testData.getDashBoardAttendEntity());

        //when
        List<DashBoardAttendEntity> result = jdbcRepository.findDashBoardAttendEntityList(testData.getDashBoardEntity().getId());

        //then
        Assertions.assertThat(result).isEqualTo(expectation);
    }

    @Test
    @Transactional
    void saveTestEntityTest() {
        //given
        TestEntityForm testEntityForm = new TestEntityForm(LocalDateTime.now(), LocalDateTime.now().plusDays(1));

        //when
        TestEntity result = jdbcRepository.saveTestEntity(new TestEntityForm(testEntityForm.getStartDate(), testEntityForm.getEndDate()));

        //then
        TestEntity expectation = new TestEntity(
                result.getId(),
                testEntityForm.getStartDate(),
                testEntityForm.getEndDate()
        );
        Assertions.assertThat(result).isEqualTo(expectation);
    }
    @Test
    @Transactional
    void deleteTestEntityTest() {
        //given

        //when
        jdbcRepository.deleteTestEntity(testData.getTestEntity().getId());

        //then
        int testId = testData.getTestEntity().getId();
        Assertions.assertThatThrownBy(() -> jdbcRepository.findTestEntityById(testId)).isInstanceOf(EmptyResultDataAccessException.class);
        Assertions.assertThat(jdbcRepository.findTestProblemEntityList(testId).size()).isEqualTo(0);
        Assertions.assertThat(jdbcRepository.findTestSolveEntityList(testId).size()).isEqualTo(0);
        Assertions.assertThat(jdbcRepository.findTestAttendEntityList(testId).size()).isEqualTo(0);
    }

    @Test
    @Transactional
    void findTestEntityListByRecentTest() {
        //given
        TestEntity testEntity = jdbcRepository.saveTestEntity(new TestEntityForm(LocalDateTime.now(), LocalDateTime.now().plusDays(1)));

        //when
        List<TestEntity> result = jdbcRepository.findTestEntityListByRecent(5);

        //then
        List<TestEntity> expectation = new ArrayList<>();
        expectation.add(testEntity);
        expectation.add(testData.testEntity);
        Assertions.assertThat(result).isEqualTo(expectation);
    }

    @Test
    @Transactional
    void findTestEntityListByNextTest() {
        //given
        TestEntity testEntity = jdbcRepository.saveTestEntity(new TestEntityForm(LocalDateTime.now(), LocalDateTime.now().plusDays(1)));

        //when
        List<TestEntity> result = jdbcRepository.findTestEntityListByNext(testEntity.getId(), 5);

        //then
        List<TestEntity> expectation = new ArrayList<>();
        expectation.add(testData.testEntity);
        Assertions.assertThat(result).isEqualTo(expectation);
    }

    @Test
    @Transactional
    void saveTestProblemEntityTest() {
        //given
        List<String> types = new ArrayList<>();
        types.add("브루트포스");
        types.add("백트래킹");

        TestProblemEntity testProblemEntity = new TestProblemEntity(
                testData.getTestEntity().getId(),
                1050,
                "TEST",
                "브론즈 V",
                "https://TEST",
                types);

        //when
        jdbcRepository.saveTestProblemEntity(testProblemEntity);

        //then
        TestProblemEntity result = jdbcRepository.findTestProblemEntityById(testData.getTestEntity().getId(), testProblemEntity.getNumber());
        Assertions.assertThat(result).isEqualTo(testProblemEntity);
    }








}





