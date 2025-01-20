package pl.pjatk.SOZ_Gastro.Services;

import org.aspectj.weaver.ast.Or;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.pjatk.SOZ_Gastro.ObjectClasses.Order;
import pl.pjatk.SOZ_Gastro.ObjectClasses.Tabletop;
import pl.pjatk.SOZ_Gastro.ObjectClasses.User;
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
    public Order addNewOrder(Long[] mealID, Tabletop tabletop, User user)
    {
        Order order = new Order(mealID, tabletop, orderMealRepository, user);
        return orderRepository.save(order);
    }

    public Order updateOrder(Long[] mealID, Order order)
    {
        if(order.isClosed())
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "cannot update closed order with id " + order.getId());
        }
        order.updateOrder(mealID, orderMealRepository);
        return orderRepository.save(order);
    }

    public void printOrder(Order order)
    {
        System.out.println(order);
        //TODO sama funkcjonalnosć drukowania i połączenie z drukarką
    }

    public Order closeOrder(Order order)
    {
        order.closeOrder();
        return order;
    }

    public Order findByID(int id)
    {
        return orderRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                "order with id " + id +" doesnt exist"));
    }
}
