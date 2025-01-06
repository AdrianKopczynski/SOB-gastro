package pl.pjatk.SOZ_Gastro.ObjectClasses;

import jakarta.persistence.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@Entity
@Table(name = "meals")
public class Meal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToOne
    @JoinColumn(name="category_id", nullable = false)
    private Category category;
    private BigDecimal price;


    public Meal(Long id, String name, BigDecimal price, Category category) {
        validateNameOrThrowError(name);
        this.id = id;
        this.name = name;
        this.price = price;
        this.category=category;
    }

    public Meal() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        validateNameOrThrowError(name);
        this.name = name;
    }

    public void validateNameOrThrowError(String name){
        if (InputStringMysqlValidator.containsForbiddenCharacters(name)) {
            throw new IllegalArgumentException("name contains forbidden characters " + name);
        }
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
