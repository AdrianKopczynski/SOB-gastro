package pl.pjatk.SOZ_Gastro.Services;

import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;
import pl.pjatk.SOZ_Gastro.ObjectClasses.Order;
import pl.pjatk.SOZ_Gastro.ObjectClasses.Tabletop;
import pl.pjatk.SOZ_Gastro.Repositories.OrderMealRepository;
import pl.pjatk.SOZ_Gastro.Repositories.OrderRepository;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMealRepository orderMealRepository;
    public OrderService(OrderRepository orderRepository, OrderMealRepository orderMealRepository)
    {
        this.orderMealRepository = orderMealRepository;
        this.orderRepository = orderRepository;
    }

    ///Tablica mealID reprezentuje pozycje w zamówieniu
    public Order addNewOrder(Long[] mealID, Tabletop tabletop)
    {
        Order order = new Order(mealID, tabletop, orderMealRepository);
        printOrder(order);
        return orderRepository.save(order);
    }

    public Order updateOrder(Long[] mealID, int orderID)
    {
        Order order = findByID(orderID);
        order.updateOrder(mealID, orderMealRepository);
        return orderRepository.save(order);
    }

    public void printOrder(Order order)
    {
        System.out.println(order);
        //TODO sama funkcjonalnosć drukowania i połączenie z drukarką
    }

    public void closeOrder(Order order)
    {
        order.closeOrder();
    }

    public Order findByID(int id)
    {
        return orderRepository.findById(id).orElseThrow();
    }
}
