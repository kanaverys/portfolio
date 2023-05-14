import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class LinkedPurchaseCompositeKey implements Serializable{
    @Column(name = "student_id")
    public int firstValue;

    @Column(name = "course_id")
    public int secondValue;

    public LinkedPurchaseCompositeKey(int firstValue, int secondValue) {
        this.firstValue = firstValue;
        this.secondValue = secondValue;
    }

    public LinkedPurchaseCompositeKey() {
    }

    public int getFirstValue() {
        return firstValue;
    }

    public void setFirstValue(int firstValue) {
        this.firstValue = firstValue;
    }

    public int getSecondValue() {
        return secondValue;
    }

    public void setSecondValue(int secondValue) {
        this.secondValue = secondValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LinkedPurchaseCompositeKey that = (LinkedPurchaseCompositeKey) o;
        return firstValue == that.firstValue && secondValue == that.secondValue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstValue, secondValue);
    }
}