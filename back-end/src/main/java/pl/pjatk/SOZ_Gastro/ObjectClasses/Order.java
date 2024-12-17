package pl.pjatk.SOZ_Gastro.ObjectClasses;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;             //ID usera, który stworzył zamówienie
    private Instant createdAt;      //timestamp stworzenia zamówienia
    private Instant closedAt;       //timestamp zamknięcia zamówienia
                                    //Jeśli closedAt = 0; to zamówienie jest otwarte
    private String comment;
    @ManyToOne//(cascade = CascadeType.PERSIST)
    @JoinColumn(name="tabletop_id", nullable = false)
    private Tabletop tabletop;



    //TODO Uzgodnić sposób przechowywania produktów w zamówieniu

    public Order()
    {
        createdAt = Instant.now();
    }

    public Order(Tabletop tabletop)
    { this.tabletop = tabletop;
        createdAt = Instant.now();

    }

    public Order(Long[] mealID, Tabletop tabletop)
    {
        createdAt = Instant.now();
        this.tabletop = tabletop;
        System.out.println(mealID.length);
        /*
        for (Long e : mealID)
        {
            System.out.println("wejde");
            OrderMeal orderMeal = new OrderMeal(e,this.id,"");
        }*/
    }


    public Long getId() {
        return id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }


   }
