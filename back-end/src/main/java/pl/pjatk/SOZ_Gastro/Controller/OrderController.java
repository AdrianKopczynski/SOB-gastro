package pl.pjatk.SOZ_Gastro.Controller;

import org.apache.coyote.BadRequestException;
import org.aspectj.weaver.ast.Or;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pjatk.SOZ_Gastro.ObjectClasses.Order;
import pl.pjatk.SOZ_Gastro.ObjectClasses.Tabletop;
import pl.pjatk.SOZ_Gastro.ObjectClasses.User;
import pl.pjatk.SOZ_Gastro.Services.OrderService;
import pl.pjatk.SOZ_Gastro.Services.UserService;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController
{

    private final OrderService orderService;

    public OrderController(OrderService orderService) { this.orderService = orderService;}

    /// Podać jakoś listę zamówionych
    @PostMapping("/createOrder")
    public ResponseEntity<Order> createOrder(@RequestBody Long[] mealID, Tabletop tabletop)
    {
        Order order = orderService.addNewOrder(mealID,tabletop);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/getorder/{id}")
    public ResponseEntity<Order> getOrderFromID(@PathVariable("id")int id)
    {
        Order order = orderService.findByID(id);
        return ResponseEntity.ok(order);
    }



    /* TODO
        - Stworzenie zamówienia
        - Zamknięcie zamówienia
        - Dodanie pozycji do zamówienia (tylko jeśli dalej otwarte)

        Czy to wszystko?
     */

}
