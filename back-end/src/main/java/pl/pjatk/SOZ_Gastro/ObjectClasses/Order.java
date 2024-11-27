package pl.pjatk.SOZ_Gastro.ObjectClasses;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.time.Instant;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Instant orderTime;
    private String tempOrder;   //tymczasowo zamówienie
    private int tableID;    //ID stolika zamówienia

    //TODO Uzgodnić sposób przechowywania produktów w zamówieniu

    public Order()
    {
        orderTime = Instant.now();

    }

    public Order(int tableID)
    {
        orderTime = Instant.now();
        this.tableID = tableID;
    }

    public int getId() {
        return id;
    }

    public Instant getOrderTime() {
        return orderTime;
    }

    public String getTempOrder() {
        return tempOrder;
    }

    public void setTempOrder(String tempOrder) {
        this.tempOrder = tempOrder;
    }
}
