package me.training.reactivejava.controller;

import lombok.extern.slf4j.Slf4j;
import me.training.reactivejava.constants.EmployeeControllerConstants;
import me.training.reactivejava.entity.Employee;
import me.training.reactivejava.service.EmployeeService;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

/**
 * @author Mukut bhattacharjee
 */
@Slf4j
@ActiveProfiles("test")
@WebFluxTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class EmployeeControllerTest {

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    public void init(){
        var employee1 = new Employee("emp1","dep1");
        employee1.setId(1);
        List<Employee> employees = Arrays.asList(
                employee1,
                new Employee("emp2","dep2"),
                new Employee("emp3","dep3")
        );
        var save = new Employee("save","save");
        save.setId(4);

        var update = new Employee("update","save");;
        update.setId(4);

        Mockito.when(employeeService.getEmployeeByName("emp1")).thenReturn(Mono.just(employee1));
        Mockito.when(employeeService.getEmployeeByName("404")).thenReturn(Mono.empty());
        Mockito.when(employeeService.getEmployeeById("404")).thenReturn(Mono.empty());
        Mockito.when(employeeService.getAllEmployees()).thenReturn(Flux.fromIterable(employees));
        Mockito.when(employeeService.save(Mockito.any())).thenReturn(Mono.just(save));
        Mockito.when(employeeService.update(Mockito.any(),Mockito.any())).thenReturn(Mono.just(update));
        Mockito.when(employeeService.deleteEmployee(Mockito.any())).thenReturn(Mono.just(999L));

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("Get all employees")
    void getEmployees() {
        Flux<Employee> employeeFlux = webTestClient
                .get()
                .uri(EmployeeControllerConstants.EMPLOYEE_ENDPOINT)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Employee.class)
                .getResponseBody();

        StepVerifier.create(employeeFlux)
                .expectSubscription()
                .expectNextMatches(employee -> StringUtils.equalsIgnoreCase(employee.getName(),"emp1"))
                .expectNextMatches(employee -> StringUtils.equalsIgnoreCase(employee.getName(),"emp2"))
                .expectNextMatches(employee -> StringUtils.equalsIgnoreCase(employee.getName(),"emp3"))
                .verifyComplete();
    }

    @Test
    @DisplayName("Get employee by name")
    void getEmployee() {

        Mono<Employee> employeeMono = webTestClient
                .get()
                .uri(EmployeeControllerConstants.EMPLOYEE_ENDPOINT.concat("/{name}"),"emp1")
                .exchange()
                .expectStatus().isOk()
                .returnResult(Employee.class)
                .getResponseBody().last().log();

        StepVerifier.create(employeeMono)
                .expectSubscription()
                .expectNextMatches(employee -> StringUtils.equalsIgnoreCase(employee.getName(),"emp1"))
                .verifyComplete();
    }

    @Test
    void testSaveEmployee() {
        webTestClient
                .post()
                .uri(EmployeeControllerConstants.EMPLOYEE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(new Employee("save","save")),Employee.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo(4);

    }

    @Test
    void updateEmployee() {
        webTestClient
                .put()
                .uri(EmployeeControllerConstants.EMPLOYEE_ENDPOINT.concat("/{id}"),4)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(new Employee("update","save")),Employee.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(4)
                .jsonPath("$.name").isEqualTo("update")
                .jsonPath("$.department").isEqualTo("save");
    }

    @Test
    void deleteEmployee() {
        webTestClient
                .delete()
                .uri(EmployeeControllerConstants.EMPLOYEE_ENDPOINT.concat("/{id}"),4)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Long.class)
                .consumeWith(
                        res -> {
                            Assertions.assertNotNull(res);
                            Assertions.assertEquals(999L,res.getResponseBody());
                        }

                );
    }
}