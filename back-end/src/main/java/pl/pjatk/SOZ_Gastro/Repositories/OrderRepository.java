package pl.pjatk.SOZ_Gastro.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pjatk.SOZ_Gastro.ObjectClasses.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>
{
    public List<Order> findAllByUserId(Long id);

}
