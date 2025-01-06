package pl.pjatk.SOZ_Gastro.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pjatk.SOZ_Gastro.ObjectClasses.Category;
import pl.pjatk.SOZ_Gastro.ObjectClasses.Meal;
import pl.pjatk.SOZ_Gastro.ObjectClasses.Tabletop;
import pl.pjatk.SOZ_Gastro.Services.ManagementService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/management")
public class ManagementController {
    private final ManagementService managementService;

    public ManagementController(ManagementService managementService){this.managementService = managementService;}


    @PostMapping("/addCategory")
    public ResponseEntity<Category> addCategory(@RequestBody Category category){
        return ResponseEntity.ok(managementService.addCategory(category));
    }

    @PutMapping("/updateCategory/{id}")
    public ResponseEntity<Category> updateCategory(@RequestBody Category category, @PathVariable long id){
        return ResponseEntity.ok(managementService.updateCategory(category,id));
    }
    @GetMapping("/getCategoryList")
    public ResponseEntity<List<Category>> getCategoryList(){
        return ResponseEntity.ok(managementService.getCategoryList());
    }

    @PostMapping("/addMeal")
    public ResponseEntity<Meal> addMeal(@RequestBody Meal meal){
        return ResponseEntity.ok(managementService.addMeal(meal));
    }

    @PutMapping("/updateMeal/{id}")
    public ResponseEntity<Meal> updateMeal(@RequestBody Meal meal, @PathVariable long id){
        return ResponseEntity.ok(managementService.updateMeal(meal,id));
    }

    @PutMapping("/updateManyMealPrices")
    public ResponseEntity<String> updateManyMealPrices(@RequestBody Map<Long, BigDecimal> priceUpdates){
        return ResponseEntity.ok(managementService.updateManyMealPrice(priceUpdates));
    }

    @DeleteMapping("/deleteMeal/{id}")
    public ResponseEntity<Boolean> deleteMeal(@PathVariable long id){
        return ResponseEntity.ok(managementService.deleteMeal(id));
    }

    @GetMapping("/getMealList")
    public ResponseEntity<List<Meal>> getMealList(){
        return ResponseEntity.ok(managementService.getMealList());
    }

    @GetMapping("getMealListByCategoryName/{categoryName}")
    public ResponseEntity<List<Meal>> getMealListByCategoryName(@PathVariable String categoryName){
        return ResponseEntity.ok(managementService.getMealListByCategoryName(categoryName));
    }

    @PostMapping("/addTabletop")
    public ResponseEntity<Tabletop> addTabletop(@RequestBody Tabletop tabletop){
        return ResponseEntity.ok(managementService.addTabletop(tabletop));
    }

    @PutMapping("/updateTabletop/{id}")
    public ResponseEntity<Tabletop> updateTabletop(@RequestBody Tabletop tabletop, @PathVariable long id){
        return ResponseEntity.ok(managementService.updateTabletop(tabletop,id));
    }
    @PutMapping("/toggleAvailableTabletopById/{id}")
    public ResponseEntity<Tabletop> toggleAvailableTabletopById(@PathVariable long id){
        return ResponseEntity.ok(managementService.toggleAvailableTabletopById(id));
    }

    @GetMapping("/getTabletopList")
    public ResponseEntity<List<Tabletop>> getTabletopList() {return ResponseEntity.ok(managementService.getTabletopList());}

    @GetMapping("/getAvailableTabletopList")
    public ResponseEntity<List<Tabletop>> getAvailableTabletopList() {return ResponseEntity.ok(managementService.getAvailableTabletopList());}
}
