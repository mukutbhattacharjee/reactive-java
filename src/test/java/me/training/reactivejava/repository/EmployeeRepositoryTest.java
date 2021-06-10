package me.training.reactivejava.repository;

import lombok.extern.slf4j.Slf4j;
import me.training.reactivejava.config.TestAppConfig;
import me.training.reactivejava.entity.Employee;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

/**
 * @author Mukut bhattacharjee
 */
@Import(TestAppConfig.class)
@DataR2dbcTest
@ActiveProfiles("test")
@Slf4j
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class EmployeeRepositoryTest {

    @Autowired
    EmployeeRepository employeeRepository;

    @BeforeEach
    void setUp() {
        List<Employee> employees = Arrays.asList(
                new Employee("test1_name", "test1_dep"),
                new Employee("test2_name", "test2_dep"));

        employeeRepository
                .deleteAll()
                .thenMany(Flux.fromIterable(employees))
                .flatMap(employeeRepository::save)
                .doOnNext((item -> {
                    log.info("Inserted Item is {}", item);
                }))
                .blockLast();
    }

    @Test
    @DisplayName("find_employee_by_name")
    void findByName() {
        Mono<Employee> employeeMono = employeeRepository.findByName("test1_name");
        StepVerifier.create(employeeMono)
                .expectSubscription()
                .expectNextMatches(employee -> StringUtils.equalsIgnoreCase(employee.getName(), "test1_name"))
                .verifyComplete();
    }

    @Test
    @DisplayName("find_all_employees")
    void findAll() {
        Flux<Employee> employeeMono = employeeRepository.findAll();
        StepVerifier.create(employeeMono)
                .expectSubscription()
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    @DisplayName("find_employee_by_id")
    void findById() {
        Mono<Employee> employeeMono = employeeRepository.findById("1");
        StepVerifier.create(employeeMono)
                .expectSubscription()
                .expectNextMatches(employee -> StringUtils.equalsIgnoreCase(employee.getName(), "test1_name"))
                .verifyComplete();
    }

    @Test
    @DisplayName("delete_by_id")
    void deleteById() {
        StepVerifier.create(employeeRepository.findAll())
                .expectSubscription()
                .expectNextCount(2)
                .verifyComplete();

        employeeRepository.deleteById(2L).block();

        StepVerifier.create(employeeRepository.findAll())
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete();
    }

}