package pl.pjatk.SOZ_Gastro.ObjectClasses;

import jakarta.persistence.*;

@Entity
@Table(name="categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public Category(Long id, String name) {
        validateNameOrThrowError(name);
        this.id = id;
        this.name = name;
    }

    public Category() {

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
}
