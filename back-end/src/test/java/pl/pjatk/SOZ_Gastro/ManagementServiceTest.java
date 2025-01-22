package pl.pjatk.SOZ_Gastro;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import pl.pjatk.SOZ_Gastro.ObjectClasses.Category;
import pl.pjatk.SOZ_Gastro.ObjectClasses.Meal;
import pl.pjatk.SOZ_Gastro.ObjectClasses.Tabletop;
import pl.pjatk.SOZ_Gastro.Repositories.CategoryRepository;
import pl.pjatk.SOZ_Gastro.Repositories.MealRepository;
import pl.pjatk.SOZ_Gastro.Repositories.TabletopRepository;
import pl.pjatk.SOZ_Gastro.Services.ManagementService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class ManagementServiceTest {

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private MealRepository mealRepository;
    @Mock
    private TabletopRepository tabletopRepository;

    @InjectMocks
    private ManagementService managementService;

    @Test
    public void testGetTabletopList(){
        Tabletop tabletopF = new Tabletop(1L, "nameF", "colorF", 5, 6, 1);
        Tabletop tabletopT = new Tabletop(2L,"nameT","colorT", 7, 4, 2);
        when(tabletopRepository.findAllByIdIsNotNull())
                .thenReturn(Arrays.asList(tabletopF,tabletopT));

        List<Tabletop> result = managementService.getTabletopList();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(tabletopT.getName(),result.get(1).getName());
    }


    @Test
    public void testUpdateMealThrowsErrorWhenNoIdFound(){
        Category category1 = new Category(1L,"pizza");
        Meal meal1 = new Meal(1L,"roma",(BigDecimal.valueOf(25.54f)),category1);

        when(mealRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> managementService.updateMeal(meal1,1L));
    }

    @Test
    public void testUpdateCategoryThrowsErrorWhenNoIdFound(){
        Category category1 = new Category(1L,"pizza");

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> managementService.updateCategory(category1,1L));
    }

    @Test
    public void testUpdateMealChangedName(){
        Category category1 = new Category(1L,"pizza");
        Meal meal1 = new Meal(1L,"roma",(BigDecimal.valueOf(25.54f)),category1);
        Meal meal2 = new Meal(1L,"newRoma",(BigDecimal.valueOf(25.54f)),category1);

        when(mealRepository.findById(1L)).thenReturn(Optional.of(meal1));
        when(mealRepository.save(meal1)).thenReturn(meal1);

        meal1 = managementService.updateMeal(meal2,1L);

        assertEquals(meal1.getName(), meal2.getName());

    }

}
