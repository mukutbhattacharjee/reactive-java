package me.training.reactivejava.service.impl;

import lombok.extern.slf4j.Slf4j;
import me.training.reactivejava.entity.Employee;
import me.training.reactivejava.repository.EmployeeRepository;
import me.training.reactivejava.service.EmployeeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Mukut bhattacharjee
 */
@Slf4j
@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Mono<Employee> getEmployeeByName(String name) {
        return employeeRepository.findByName(name);
    }

    @Override
    public Mono<Employee> getEmployeeById(String id) {
        return employeeRepository.findById(id);
    }

    @Override
    public Flux<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Mono<Employee> save(Mono<Employee> employeeMono) {
        return employeeMono.flatMap(employee -> {
            log.info("Saving employee {}", employee);
            return employeeRepository.save(employee);
        }).log();
    }

    @Override
    public Mono<Employee> update(String employeeId, Mono<Employee> employeeMono) {
        return employeeMono.flatMap(employee ->
                employeeRepository.findById(employeeId).flatMap(e -> {
                    if (StringUtils.isNotBlank(employee.getDepartment())) {
                        e.setDepartment(employee.getDepartment());
                    }
                    if (StringUtils.isNotBlank(employee.getName())) {
                        e.setName(employee.getName());
                    }
                    log.info("Updating Employee {}", e);
                    return employeeRepository.save(e);
                })
        );
    }

    @Override
    public Mono<Long> deleteEmployee(Long id) {
        log.info("Deleting employee by id {}",id);
        return employeeRepository.deleteById(id).then(Mono.just(id));
    }

}
