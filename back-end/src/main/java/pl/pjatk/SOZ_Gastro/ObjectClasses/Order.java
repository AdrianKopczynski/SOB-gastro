package pl.pjatk.SOZ_Gastro.ObjectClasses;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int userId;
    private Instant createdAt;
    private Instant closedAt;
    private String comment;
    private int tableID;    //ID stolika zamówienia



    //TODO Uzgodnić sposób przechowywania produktów w zamówieniu

    public Order()
    {
        createdAt = Instant.now();

    }

    public Order(int tableID)
    {
        createdAt = Instant.now();
        this.tableID = tableID;
    }

    public int getId() {
        return id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

   }
