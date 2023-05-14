package Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@EqualsAndHashCode
public class LinkedPurchaseCompositeKey implements Serializable {
    @Column(name = "course_id")
    int courseId;

    @Column(name = "student_id")
    int studentId;

    public LinkedPurchaseCompositeKey() {
    }
}
