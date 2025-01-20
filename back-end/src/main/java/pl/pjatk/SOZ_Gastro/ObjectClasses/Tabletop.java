package pl.pjatk.SOZ_Gastro.ObjectClasses;

import jakarta.persistence.*;

@Entity
@Table(name = "tabletops")
public class Tabletop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String color;

    private float x;

    private float y;

    private int size;

    public Tabletop(Long id, String name, String color, float x, float y, int size) {
        validateNameOrThrowError(name);
        this.id = id;
        this.name = name;
        this.color = color;
        this.x = x;
        this.y = y;
        this.size = size;
    }

    public Tabletop() {

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

    public String getColor() {
        return color;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getSize() {
        return size;

    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
