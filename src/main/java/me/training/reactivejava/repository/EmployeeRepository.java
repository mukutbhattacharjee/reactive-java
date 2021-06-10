package me.training.reactivejava.repository;

import me.training.reactivejava.entity.Employee;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * @author Mukut bhattacharjee
 */
@Repository
public interface EmployeeRepository extends ReactiveCrudRepository<Employee, Long> {

    Mono<Employee> findByName(String name);
    Mono<Employee> findById(String id);

}
