import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PurchaseCompositeKey  implements Serializable {
    @Column(name = "student_name", length = 45)
    public String studentName;

    @Column(name = "course_name")
    public String courseName;

    public PurchaseCompositeKey(String studentName, String courseName) {
        this.courseName = courseName;
        this.studentName = studentName;
    }

    public PurchaseCompositeKey() {
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PurchaseCompositeKey that = (PurchaseCompositeKey) o;
        return Objects.equals(courseName, that.courseName) && Objects.equals(studentName, that.studentName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseName, studentName);
    }
}
