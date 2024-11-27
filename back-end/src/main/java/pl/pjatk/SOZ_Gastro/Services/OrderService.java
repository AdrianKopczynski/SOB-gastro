package pl.pjatk.SOZ_Gastro.Services;

import org.springframework.stereotype.Service;
import pl.pjatk.SOZ_Gastro.ObjectClasses.Order;
import pl.pjatk.SOZ_Gastro.Repositories.OrderRepository;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    public OrderService(OrderRepository orderRepository)
    {
        this.orderRepository = orderRepository;
    }

    public Order addNewOrder(String tempName, int tableID)
    {
        Order order = new Order(tableID);
        printOrder(order);
        return orderRepository.save(order);
    }

    public void printOrder(Order order)
    {
        //TODO sama funkcjonalnosć drukowania i połączenie z drukarką
    }

    public void closeOrder(Order order)
    {
        //TODO zamykanie zamówienia, co to wgl znaczy w kontekscie naszej aplikacji?
    }
}
