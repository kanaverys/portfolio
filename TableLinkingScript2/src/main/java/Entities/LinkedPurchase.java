package Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "linked_purchase_list")
@Data
@AllArgsConstructor
@EqualsAndHashCode
public class LinkedPurchase {
    @EmbeddedId
    LinkedPurchaseCompositeKey id;

    PurchaseCompositeKey key;

    int price;

    @Column(name = "subscription_date")
    Date subscriptionDate;

    public LinkedPurchase(Purchase purchase) {
        this.price = purchase.getPrice();
        this.subscriptionDate = purchase.getSubscriptionDate();

        this.key = new PurchaseCompositeKey();
        this.key.courseName = purchase.key.courseName;
        this.key.studentName = purchase.key.studentName;
    }

    public LinkedPurchase() {
    }
}
