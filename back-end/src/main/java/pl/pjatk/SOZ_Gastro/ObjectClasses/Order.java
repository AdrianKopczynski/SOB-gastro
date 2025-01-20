package pl.pjatk.SOZ_Gastro.ObjectClasses;

import jakarta.persistence.*;
import pl.pjatk.SOZ_Gastro.Repositories.OrderMealRepository;

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
    @ManyToOne//(cascade = CascadeType.PERSIST)
    @JoinColumn(name="tabletop_id", nullable = false)
    private Tabletop tabletop;


    public Order()
    {
        createdAt = Instant.now();
        closedAt = null;
    }

    public Order(Tabletop tabletop)
    {
        this.tabletop = tabletop;
        createdAt = Instant.now();
        closedAt = null;
    }

    public Order(Long[] mealID, Tabletop tabletop, OrderMealRepository orderMealRepository, User user)
    {
        createdAt = Instant.now();
        closedAt = null;
        this.tabletop = tabletop;
        this.user = user;
        System.out.println(mealID.length);

        for (Long e : mealID)
        {
            System.out.println(this.id);
            OrderMeal orderMeal = new OrderMeal(e,this.id,"");
            orderMealRepository.save(orderMeal);
        }
    }

    public Order updateOrder(Long[] mealID, OrderMealRepository orderMealRepository)
    {
        for (Long e : mealID)
        {
            System.out.println(this.id);
            OrderMeal orderMeal = new OrderMeal(e,this.id,"");
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

   }
