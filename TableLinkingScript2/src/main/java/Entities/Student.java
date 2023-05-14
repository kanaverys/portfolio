package Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "students")
@Data
@AllArgsConstructor
@EqualsAndHashCode
public class Student {
    @Id
    int id;

    String name;

    int age;

    @Column(name = "registration_date")
    Date registrationDate;

    public Student() {
    }
}
