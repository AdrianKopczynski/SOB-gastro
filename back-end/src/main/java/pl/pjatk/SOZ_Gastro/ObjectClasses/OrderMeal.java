package pl.pjatk.SOZ_Gastro.ObjectClasses;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class OrderMeal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int mealId;

    private int orderId;
    private String comment;

    public OrderMeal( int id, int mealId, int orderId, String comment)
    {
        this.id = id;
        this.mealId = mealId;
        this.orderId = orderId;
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public int getMealId() {
        return mealId;
    }

    public int getOrderId() {
        return orderId;
    }
}
