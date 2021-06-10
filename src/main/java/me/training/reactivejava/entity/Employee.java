package me.training.reactivejava.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author Mukut bhattacharjee
 */

@ToString
@NoArgsConstructor
@Data
@Table("employee")
public class Employee {

    @Id
    private Integer id;
    private String name;
    private String department;

    public Employee(String name, String department) {
        this.name = name;
        this.department = department;
    }
}
