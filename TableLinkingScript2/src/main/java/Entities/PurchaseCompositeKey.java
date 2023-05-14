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
public class PurchaseCompositeKey implements Serializable {
    @Column(name = "student_name")
    String studentName;

    @Column(name = "course_name")
    String courseName;

    public PurchaseCompositeKey() {
    }
}
