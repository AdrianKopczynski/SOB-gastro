package pl.pjatk.SOZ_Gastro.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pjatk.SOZ_Gastro.ObjectClasses.OrderMeal;

public interface OrderMealRepository extends JpaRepository<OrderMeal, Long> {
}
