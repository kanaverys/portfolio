import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "purchaselist")
public class Purchase implements Serializable {
    @EmbeddedId
    PurchaseCompositeKey id;

    @JoinColumn(name = "student_name", referencedColumnName = "name", insertable = false, updatable = false)
    @OneToOne(cascade = CascadeType.ALL)
    Student student;

    int price;

    @Column(name = "subscription_date", length = 32)
    Timestamp date;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "course_name", referencedColumnName = "name", insertable = false, updatable = false)
    Course course;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Purchase purchase = (Purchase) o;
        return price == purchase.price && Objects.equals(student, purchase.student) && Objects.equals(date, purchase.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(student, price, date);
    }

    public PurchaseCompositeKey getId() {
        return id;
    }

    public void setId(PurchaseCompositeKey id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student studentName) {
        this.student = studentName;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
