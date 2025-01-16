package pl.pjatk.SOZ_Gastro.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pjatk.SOZ_Gastro.ObjectClasses.Requests.CreateOrderRequest;
import pl.pjatk.SOZ_Gastro.ObjectClasses.Order;
import pl.pjatk.SOZ_Gastro.ObjectClasses.Requests.UpdateOrderRequest;
import pl.pjatk.SOZ_Gastro.ObjectClasses.Tabletop;
import pl.pjatk.SOZ_Gastro.Services.OrderService;

@RestController
@RequestMapping("/order")
public class OrderController
{

    private final OrderService orderService;

    public OrderController(OrderService orderService) { this.orderService = orderService;}

    /// Podać jakoś listę zamówionych
    @PostMapping("/createOrder")
    public ResponseEntity<Order> createOrder(@RequestBody CreateOrderRequest createOrderRequest) {
        Long[] mealID = createOrderRequest.getMealID();
        Tabletop tabletop = createOrderRequest.getTabletop();

        Order order = orderService.addNewOrder(mealID, tabletop);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/getOrder/{id}")
    public ResponseEntity<Order> getOrderFromID(@PathVariable("id")int id)
    {
        Order order = orderService.findByID(id);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/closeOrder/{id}")
    public ResponseEntity<Order> closeOrder(@PathVariable("id")int id)
    {
        Order order = orderService.findByID(id);
        orderService.closeOrder(order);
        return ResponseEntity.ok(order);
    }

    @PatchMapping("/updateOrder/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable("id")int id, @RequestBody UpdateOrderRequest updateOrderRequest)
    {
        Long[] mealID = updateOrderRequest.getMealID();

        Order order = orderService.findByID(id);
        orderService.updateOrder(mealID, id);
        return ResponseEntity.ok(order);
    }

    /* TODO
        - Zamknięcie zamówienia
        - Dodanie pozycji do zamówienia (tylko jeśli dalej otwarte)

        Czy to wszystko?
     */

}
