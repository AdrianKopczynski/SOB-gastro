package pl.pjatk.SOZ_Gastro.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pjatk.SOZ_Gastro.ObjectClasses.Meal;
import pl.pjatk.SOZ_Gastro.ObjectClasses.Tabletop;

import java.util.List;

public interface TabletopRepository extends JpaRepository<Tabletop, Long> {
    List<Tabletop> findAllByIdIsNotNull();

}
