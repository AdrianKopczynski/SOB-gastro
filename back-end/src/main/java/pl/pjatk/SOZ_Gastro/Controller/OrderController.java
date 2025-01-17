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

    @PostMapping("/createOrder")
    public ResponseEntity<Order> createOrder(@RequestBody CreateOrderRequest createOrderRequest)
    {
        return ResponseEntity.ok(orderService.addNewOrder(createOrderRequest.getMealID(), createOrderRequest.getTabletop()));
    }

    @GetMapping("/getOrder/{id}")
    public ResponseEntity<Order> getOrderFromID(@PathVariable("id")int id)
    {
        return ResponseEntity.ok(orderService.findByID(id));
    }

    @PostMapping("/closeOrder/{id}")
    public ResponseEntity<Order> closeOrder(@PathVariable("id")int id)
    {
        return ResponseEntity.ok(orderService.closeOrder(orderService.findByID(id)));
    }

    @PatchMapping("/updateOrder/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable("id")int id, @RequestBody UpdateOrderRequest updateOrderRequest)
    {
        return ResponseEntity.ok(orderService.updateOrder(updateOrderRequest.getMealID(), orderService.findByID(id)));
    }
}
