package pl.pjatk.SOZ_Gastro.Services;

import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;
import pl.pjatk.SOZ_Gastro.ObjectClasses.Order;
import pl.pjatk.SOZ_Gastro.ObjectClasses.Tabletop;
import pl.pjatk.SOZ_Gastro.Repositories.OrderRepository;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    public OrderService(OrderRepository orderRepository)
    {
        this.orderRepository = orderRepository;
    }

    ///Tablica mealID reprezentuje pozycje w zamówieniu
    public Order addNewOrder(Long[] mealID, Tabletop tabletop)
    {
        Order order = new Order(mealID, tabletop);
        printOrder(order);
        return orderRepository.save(order);
    }

    public void printOrder(Order order)
    {
        System.out.println(order);
        //TODO sama funkcjonalnosć drukowania i połączenie z drukarką
    }

    public void closeOrder(Order order)
    {
        //TODO zamykanie zamówienia, czyli dodanie czasu zakończenia
    }

    public Order findByID(int id)
    {
        return orderRepository.findById(id).orElseThrow();
    }
}
