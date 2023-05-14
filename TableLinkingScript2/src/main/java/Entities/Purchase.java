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
@Table(name = "purchaselist")
@Data
@AllArgsConstructor
@EqualsAndHashCode
public class Purchase {
    @EmbeddedId
    PurchaseCompositeKey key;

    int price;

    @Column(name = "subscription_date")
    Date subscriptionDate;

    public Purchase() {
    }
}
