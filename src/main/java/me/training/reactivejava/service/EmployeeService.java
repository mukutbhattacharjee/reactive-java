package me.training.reactivejava.service;

import me.training.reactivejava.entity.Employee;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Mukut bhattacharjee
 */
public interface EmployeeService {

    Mono<Employee> getEmployeeByName(String name);

    Mono<Employee> getEmployeeById(String id);

    Flux<Employee> getAllEmployees();

    Mono<Employee> save(Mono<Employee> employeeMono);

    Mono<Employee> update(String employeeId, Mono<Employee> employeeMono);

    Mono<Long> deleteEmployee(Long id);

}
