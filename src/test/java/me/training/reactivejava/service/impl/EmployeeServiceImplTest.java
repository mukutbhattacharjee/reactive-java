package me.training.reactivejava.service.impl;

import lombok.extern.slf4j.Slf4j;
import me.training.reactivejava.config.TestAppConfig;
import me.training.reactivejava.entity.Employee;
import me.training.reactivejava.repository.EmployeeRepository;
import me.training.reactivejava.service.EmployeeService;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * @author Mukut bhattacharjee
 */
@Import(TestAppConfig.class)
@ActiveProfiles("test")
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class EmployeeServiceImplTest {

    @InjectMocks
    private final EmployeeService employeeService = new EmployeeServiceImpl();

    @Mock
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setUp() {
        Employee employee1 = new Employee("emp1","mock_dept");
        employee1.setId(1);
        Mockito.when(employeeRepository.findByName("emp1")).thenReturn(Mono.just(employee1));
        Mockito.when(employeeRepository.findById("1")).thenReturn(Mono.just(employee1));
        Mockito.when(employeeRepository.findById("1")).thenReturn(Mono.just(employee1));
        Mockito.when(employeeRepository.findAll()).thenReturn(Flux.just(employee1));
        Mockito.when(employeeRepository.save(Mockito.any())).thenReturn(Mono.just(employee1));
        Mockito.when(employeeRepository.deleteById(999L)).thenReturn(Mono.empty().then());
    }


    @Test
    void getEmployeeByName() {
        Mono<Employee> employeeMono = employeeService.getEmployeeByName("emp1");
        StepVerifier.create(employeeMono)
                .expectSubscription()
                .expectNextMatches(employee -> StringUtils.equalsIgnoreCase("emp1",employee.getName()))
                .verifyComplete();
    }

    @Test
    void getEmployeeById() {
        Mono<Employee> employeeMono = employeeService.getEmployeeById("1");
        StepVerifier.create(employeeMono)
                .expectSubscription()
                .expectNextMatches(employee -> StringUtils.equalsIgnoreCase("emp1",employee.getName()))
                .verifyComplete();
    }

    @Test
    void getAllEmployees() {
        Flux<Employee> employeeFlux = employeeService.getAllEmployees();
        StepVerifier.create(employeeFlux)
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void save() {
        Mono<Employee> employeeMono = employeeService.save(Mono.just(new Employee("emp1","dep")));
        StepVerifier.create(employeeMono)
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void update() {
        Mono<Employee> employeeMono = employeeService.update("1",Mono.just(new Employee("emp1",
                "dep")));
        StepVerifier.create(employeeMono)
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void deleteEmployee() {
        StepVerifier.create(employeeService.deleteEmployee(999L))
                .expectSubscription()
                .expectNext(999L)
                .verifyComplete();
    }
}