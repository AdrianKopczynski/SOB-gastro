package pl.pjatk.SOZ_Gastro.ObjectClasses;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import pl.pjatk.SOZ_Gastro.Enums.MealTypes;

@Entity
@Table(name = "meals")
public class Meal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @Enumerated(EnumType.STRING)
    private MealTypes type;
    private String comment;

    //Tutaj ewentualnie dodać liste używanych produktów
}
