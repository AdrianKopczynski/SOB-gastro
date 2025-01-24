package pl.pjatk.SOZ_Gastro.ObjectClasses;

import jakarta.persistence.*;
import pl.pjatk.SOZ_Gastro.Repositories.OrderMealRepository;
import pl.pjatk.SOZ_Gastro.Repositories.OrderRepository;

import java.time.Instant;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;             //ID usera, który stworzył zamówienie
    private Instant createdAt;      //timestamp stworzenia zamówienia
    private Instant closedAt;       //timestamp zamknięcia zamówienia
                                    //Jeśli closedAt = Null; to zamówienie jest otwarte
    private String comment;
//    @ManyToOne//(cascade = CascadeType.PERSIST)
//    @JoinColumn(name="tabletop_id", nullable = false)
    private Long tabletop;


    public Order()
    {
        createdAt = Instant.now();
        closedAt = null;
    }

//    public Order(Tabletop tabletop)
//    {
//        this.tabletop = tabletop;
//        createdAt = Instant.now();
//        closedAt = null;
//    }
    public Order(Long[] mealID,  Long tabletop, OrderMealRepository orderMealRepository, OrderRepository orderRepository,User user, String comment)
    {
        createdAt = Instant.now();
        closedAt = null;
        this.tabletop = tabletop;
        this.user = user;
        this.comment = comment;
        orderRepository.save(this);

        for (int i = 0; i < mealID.length; i++) {
            OrderMeal orderMeal = new OrderMeal(mealID[i], this.id);
            orderMealRepository.save(orderMeal);
        }
    }
    public Order(Long[] mealID, Long tabletop, OrderMealRepository orderMealRepository, OrderRepository orderRepository,User user, String comment, String[] comment2)
    {
        createdAt = Instant.now();
        closedAt = null;
        this.tabletop = tabletop;
        this.user = user;
        this.comment = comment;
      orderRepository.save(this);

        for (int i = 0; i < mealID.length; i++) {
            OrderMeal orderMeal = new OrderMeal(mealID[i], this.id, comment2[i]);
            orderMealRepository.save(orderMeal);
        }

//        for (Long e : mealID)
//        {
//            System.out.println(this.id);
//            OrderMeal orderMeal = new OrderMeal(e,this.id,comment2[e]);
//            orderMealRepository.save(orderMeal);
//        }
    }

    public Order updateOrder(Long[] mealID, OrderMealRepository orderMealRepository, String[] comment)
    {

        for (int i = 0; i < mealID.length; i++) {
            OrderMeal orderMeal = new OrderMeal(mealID[i], this.id, comment[i]);
            orderMealRepository.save(orderMeal);
        }

        return this;
    }

    public Order updateOrderNoComment(Long[] mealID, OrderMealRepository orderMealRepository)
    {

        for (int i = 0; i < mealID.length; i++) {
            OrderMeal orderMeal = new OrderMeal(mealID[i], this.id);
            orderMealRepository.save(orderMeal);
        }

        return this;
    }


    public Long getId() {
        return id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public boolean isClosed()
    {
        return (closedAt != null);
    }

    public void closeOrder()
    {
        closedAt = Instant.now();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTabletop() {
        return tabletop;
    }

    public void setTabletop(Long tabletop) {
        this.tabletop = tabletop;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Instant getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(Instant closedAt) {
        this.closedAt = closedAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
