package pl.pjatk.SOZ_Gastro.Services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.pjatk.SOZ_Gastro.ObjectClasses.Category;
import pl.pjatk.SOZ_Gastro.ObjectClasses.Meal;
import pl.pjatk.SOZ_Gastro.ObjectClasses.Tabletop;
import pl.pjatk.SOZ_Gastro.Repositories.CategoryRepository;
import pl.pjatk.SOZ_Gastro.Repositories.MealRepository;
import pl.pjatk.SOZ_Gastro.Repositories.TabletopRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

//put table, put meal, put category

@Service
public class ManagementService {

    private final CategoryRepository categoryRepository;
    private final MealRepository mealRepository;
    private final TabletopRepository tabletopRepository;

    public ManagementService(CategoryRepository categoryRepository, MealRepository mealRepository, TabletopRepository tabletopRepository){

        this.categoryRepository=categoryRepository;
        this.mealRepository = mealRepository;
        this.tabletopRepository = tabletopRepository;
    }

    public Category addCategory (Category category){
        if (categoryRepository.existsByName(category.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "category with name" + category.getName() + "already exists");
        }
        return categoryRepository.save(category);
    }

    public Category updateCategory (Category category, long id){
        Category tmp = categoryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("no category with id: " + id));
        tmp.setName(category.getName());
        return categoryRepository.save(tmp);
    }

    public boolean deleteCategory (long id){
        if (!categoryRepository.existsById(id)) throw new NoSuchElementException("no category with id: " + id);
        Category tmp = categoryRepository.findById(id).orElseThrow();
        List<Meal> meals = mealRepository.findAllByCategoryName(tmp.getName());
        meals.forEach(meal -> meal.setCategory(getCategory(1L)));
        categoryRepository.deleteById(id);
        return true;
    }

    public Category getCategory (long id){
        return categoryRepository.findById(id).orElseThrow();
    }

    public List<Category> getCategoryList(){
        return categoryRepository.findAllByIdIsNotNull();
    }
    public Meal addMeal (Meal meal){
        return  mealRepository.save((meal));
    }

    public Meal updateMeal (Meal meal, long id){
        Meal tmp = mealRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("no meal with id: " + id));
        tmp.setName(meal.getName());
        tmp.setCategory(meal.getCategory());
        tmp.setPrice(meal.getPrice());
        return mealRepository.save(tmp);
    }

    public Meal updateMealPrice (long id, BigDecimal price){
        Meal tmp = mealRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("no meal with id: " + id));
        tmp.setPrice(price);
        return mealRepository.save(tmp);
    }

    public String updateManyMealPrice (Map<Long, BigDecimal> priceUpdates){
        priceUpdates.forEach((id,price) -> {
            updateMealPrice(id, price);
        });
        return "success";
    }

    public boolean deleteMeal(long id){
        if (!mealRepository.existsById(id)) throw new NoSuchElementException("no meal with id: " + id);
        mealRepository.deleteById(id);
        return true;
    }

    public List<Meal> getMealList(){
        return mealRepository.findAllByIdIsNotNull();
    }

    public List<Meal> getMealListByCategoryName(String categoryName){
        if (!categoryRepository.existsByName(categoryName)) throw new NoSuchElementException("no category with name: " + categoryName);
        return mealRepository.findAllByCategoryName(categoryName);
    }


    public Tabletop addTabletop(Tabletop tabletop){
        return tabletopRepository.save(tabletop);
    }

    public Tabletop updateTabletop (Tabletop tabletop, long id){
        Tabletop tmp = tabletopRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("no tableTop with id: " + id));
        tmp.setName(tabletop.getName());
        tmp.setAvailable(tabletop.isAvailable());
        return tabletopRepository.save(tmp);
    }

    public Tabletop getTabletop(Long id)
    {
        if (id == null)
        {
            throw new IllegalArgumentException("Tabletop id cannot be null");
        }
        return tabletopRepository.findById(id).
                orElseThrow(() -> new NoSuchElementException("no tableTop with id: " + id));
    }

    public Tabletop toggleAvailableTabletopById (long id){
        Tabletop tmp = tabletopRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("no tableTop with id: " + id));
        tmp.setAvailable(!tmp.isAvailable());
        return tabletopRepository.save(tmp);
    }

    public List<Tabletop> getTabletopList(){return tabletopRepository.findAllByIdIsNotNull();}

    public List<Tabletop> getAvailableTabletopList(){return tabletopRepository.findAllByIdIsNotNullAndIsAvailableTrue();}

}
