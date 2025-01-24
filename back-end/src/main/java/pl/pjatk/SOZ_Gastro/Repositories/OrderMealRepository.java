package pl.pjatk.SOZ_Gastro.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pjatk.SOZ_Gastro.ObjectClasses.OrderMeal;

import java.util.List;

public interface OrderMealRepository extends JpaRepository<OrderMeal, Long>
{
    public List<OrderMeal> findAllByOrderId(Long orderId);
    public void deleteById(Long id);
}
