package me.training.reactivejava.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import me.training.reactivejava.entity.Employee;
import me.training.reactivejava.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static me.training.reactivejava.constants.EmployeeControllerConstants.EMPLOYEE_ENDPOINT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author Mukut bhattacharjee
 */
@Slf4j
@RestController
@RequestMapping(EMPLOYEE_ENDPOINT)
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Operation(description = "Get employee by name")
    @GetMapping(value = "/{name}", produces = APPLICATION_JSON_VALUE)
    public Mono<Employee> getEmployee(@PathVariable("name") String name) {
        log.info("Get employee by name {}", name);
        return employeeService.getEmployeeByName(name);
    }

    @Operation(description = "Get all employees")
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public Flux<Employee> getEmployees() {
        return employeeService.getAllEmployees();
    }


    @ResponseStatus(HttpStatus.CREATED)
    @Operation(description = "Save an employee")
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public Mono<Employee> saveEmployee(@RequestBody Mono<Employee> employee) {
        return employeeService.save(employee);
    }

    @Operation(description = "Update an employee")
    @PutMapping(value = "/{id}", consumes = APPLICATION_JSON_VALUE, produces =
            APPLICATION_JSON_VALUE)
    public Mono<Employee> updateEmployee(@PathVariable("id") String employeeId,
                                         @RequestBody Mono<Employee> employee) {
        return employeeService.update(employeeId, employee);
    }

    @Operation(description = "Delete an employee by id")
    @DeleteMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public Mono<Long> deleteEmployee(@PathVariable("id") Long employeeId) {
        return employeeService.deleteEmployee(employeeId);
    }


}
