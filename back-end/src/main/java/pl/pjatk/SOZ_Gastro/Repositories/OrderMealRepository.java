package pl.pjatk.SOZ_Gastro.Repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.pjatk.SOZ_Gastro.ObjectClasses.OrderMeal;

import java.util.List;

public interface OrderMealRepository extends JpaRepository<OrderMeal, Long>
{
    public List<OrderMeal> findAllByOrderId(Long orderId);
    public void deleteById(Long id);

    @Modifying
    @Transactional
    @Query("DELETE FROM OrderMeal om WHERE om.orderId = :orderId")
    void deleteOrderMealsByOrderId(@Param("orderId") Long orderId);
}
