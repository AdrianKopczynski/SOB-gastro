package pl.pjatk.SOZ_Gastro.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pjatk.SOZ_Gastro.ObjectClasses.OrderMeal;
import pl.pjatk.SOZ_Gastro.ObjectClasses.Requests.CreateOrderRequest;
import pl.pjatk.SOZ_Gastro.ObjectClasses.Order;
import pl.pjatk.SOZ_Gastro.ObjectClasses.Requests.NoCommentRequest;
import pl.pjatk.SOZ_Gastro.ObjectClasses.Requests.NoCommentUpdate;
import pl.pjatk.SOZ_Gastro.ObjectClasses.Requests.UpdateOrderRequest;
import pl.pjatk.SOZ_Gastro.ObjectClasses.Tabletop;
import pl.pjatk.SOZ_Gastro.ObjectClasses.User;
import pl.pjatk.SOZ_Gastro.Services.OrderService;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController
{

    private final OrderService orderService;

    public OrderController(OrderService orderService) { this.orderService = orderService;}

    @PostMapping("/createOrder")
    public ResponseEntity<Order> createOrder(@RequestBody CreateOrderRequest createOrderRequest)
    {
        return ResponseEntity.ok(orderService.addNewOrder(createOrderRequest.getMealID(), createOrderRequest.getTabletop(), createOrderRequest.getUser(), createOrderRequest.getComment(), createOrderRequest.getComment2()));
    }
    @PostMapping("/createOrderNoComment")
    public ResponseEntity<Order> createOrderNoComment(@RequestBody NoCommentRequest noCommentRequest)
    {
        return ResponseEntity.ok(orderService.addNewOrderNoMealComment(noCommentRequest.getMealID(), noCommentRequest.getTabletop(), noCommentRequest.getUser(), noCommentRequest.getComment()));
    }

    @GetMapping("/getOrder/{id}")
    public ResponseEntity<Order> getOrderFromID(@PathVariable("id")Long id)
    {
        return ResponseEntity.ok(orderService.findByID(id));
    }

    @GetMapping("/getOrder")
    public ResponseEntity<List<Order>> getOrders()
    {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PostMapping("/closeOrder/{id}")
    public ResponseEntity<Order> closeOrder(@PathVariable("id")Long id)
    {
        return ResponseEntity.ok(orderService.closeOrder(orderService.findByID(id)));
    }

    @PatchMapping("/updateOrder/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable("id")Long id, @RequestBody UpdateOrderRequest updateOrderRequest)
    {
        return ResponseEntity.ok(orderService.updateOrder(updateOrderRequest.getMealID(), updateOrderRequest.getComment(), orderService.findByID(id)));
    }

    @PatchMapping("/updateOrderNoComment/{id}")
    public ResponseEntity<Order> updateOrderNoComment(@PathVariable("id")Long id, @RequestBody NoCommentUpdate noCommentUpdate)
    {
        return ResponseEntity.ok(orderService.updateOrderNoComment(noCommentUpdate.getMealID(), orderService.findByID(id)));
    }

    @GetMapping("/getOrderMeals/{id}")
    public ResponseEntity <List<OrderMeal>> getOrderMeals(@PathVariable("id")Long id)
    {
        return ResponseEntity.ok(orderService.getOrderMealById(id));
    }

    @DeleteMapping("/deleteOrder/{id}")
    public ResponseEntity<String> deleteOrderById(@PathVariable("id") Long id)
    {
        this.orderService.deleteOrderMealsByOrderId(id);
        this.orderService.deleteOrder(id);

        return ResponseEntity.ok().body("Order deleted");
    }

    @DeleteMapping("/deleteOrderMeals/{id}")
    public ResponseEntity<String> deleteOrderMealsByOrderId(@PathVariable("id") Long id)
    {
        this.orderService.deleteOrderMealsByOrderId(id);

        return ResponseEntity.ok().body("OrderMeals deleted");
    }


    @DeleteMapping("/deleteOrderMeal/{id}")
    public ResponseEntity<String> deleteOrderMealById(@PathVariable("id") Long id)
    {
        this.orderService.deleteOrderMeal(id);
        return ResponseEntity.ok().body("OrderMeal deleted");
    }

}
