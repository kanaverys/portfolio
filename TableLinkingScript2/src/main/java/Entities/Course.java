package Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Table(name = "courses")
@Data
@AllArgsConstructor
@EqualsAndHashCode
public class Course {
    @Id
    int id;

    String name;

    int duration;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "enum")
    CourseType type;

    String description;

    @Column(name = "teacher_id")
    int teacherId;

    @Column(name = "students_count")
    Integer studentsCount;

    int price;

    @Column(name = "price_per_hour")
    double pricePerHour;

    public Course() {
    }
}
