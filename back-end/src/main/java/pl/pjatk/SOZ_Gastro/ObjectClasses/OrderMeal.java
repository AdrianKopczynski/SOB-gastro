package pl.pjatk.SOZ_Gastro.ObjectClasses;

import jakarta.persistence.*;

@Entity
@Table(name= "OrderMeal")
public class OrderMeal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long mealId;

    private Long orderId;
    private String comment;

    public OrderMeal(Long mealId, Long orderId, String comment)
    {
        this.mealId = mealId;
        this.orderId = orderId;
        this.comment = comment;
    }

    public OrderMeal(){}
    public Long getId() {
        return id;
    }

    public Long getMealId() {
        return mealId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setMealId(long id)
    {
        this.mealId = id;
    }
}
