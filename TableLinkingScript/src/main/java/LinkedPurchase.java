import javax.persistence.*;

@Entity
@Table(name = "linked_purchase_list")
public class LinkedPurchase {

    @EmbeddedId
    LinkedPurchaseCompositeKey id;

//    @Column(name = "student_id", insertable = false, updatable = false)
//    int studentId;
//
//    @Column(name = "course_id", insertable = false, updatable = false)
//    int courseId;

    public LinkedPurchaseCompositeKey getId() {
        return id;
    }

    public void setId(LinkedPurchaseCompositeKey id) {
        this.id = id;
    }

//    public int getStudentId() {
//        return studentId;
//    }
//
//    public void setStudentId(int studentId) {
//        this.studentId = studentId;
//    }
//
//    public int getCourseId() {
//        return courseId;
//    }
//
//    public void setCourseId(int courseId) {
//        this.courseId = courseId;
//    }

    //
//    @OneToOne
//    @JoinColumn(name = "student_name", insertable = false, updatable = false)
//    Student studentName;
//
//    @OneToOne
//    @JoinColumn(name = "course_name")
//    Course courseName;
//
//    int price;
//
//    @Id
//    @Column(name = "subscription_date", length = 24)
//    Timestamp subscriptionDate;
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        LinkedPurchase that = (LinkedPurchase) o;
//        return studentId == that.studentId && courseId == that.courseId && price == that.price && Objects.equals(studentName, that.studentName) && Objects.equals(courseName, that.courseName) && Objects.equals(subscriptionDate, that.subscriptionDate);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(studentId, courseId, studentName, courseName, price, subscriptionDate);
//    }
//
//    public int getStudentId() {
//        return studentId;
//    }
//
//    public void setStudentId(int studentId) {
//        this.studentId = studentId;
//    }
//
//    public int getCourseId() {
//        return courseId;
//    }
//
//    public void setCourseId(int courseId) {
//        this.courseId = courseId;
//    }
//
//    public Student getStudentName() {
//        return studentName;
//    }
//
//    public void setStudentName(Student studentName) {
//        this.studentName = studentName;
//    }
//
//    public Course getCourseName() {
//        return courseName;
//    }
//
//    public void setCourseName(Course courseName) {
//        this.courseName = courseName;
//    }
//
//    public int getPrice() {
//        return price;
//    }
//
//    public void setPrice(int price) {
//        this.price = price;
//    }
//
//    public Timestamp getSubscriptionDate() {
//        return subscriptionDate;
//    }
//
//    public void setSubscriptionDate(Timestamp subscriptionDate) {
//        this.subscriptionDate = subscriptionDate;
//    }
}