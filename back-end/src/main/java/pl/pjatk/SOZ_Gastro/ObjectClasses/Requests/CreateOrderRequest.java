package pl.pjatk.SOZ_Gastro.ObjectClasses.Requests;

import pl.pjatk.SOZ_Gastro.ObjectClasses.Tabletop;

public class CreateOrderRequest {
    private Long[] mealID;
    private Tabletop tabletop;

    // Getters and Setters
    public Long[] getMealID() {
        return mealID;
    }

    public void setMealID(Long[] mealID) {
        this.mealID = mealID;
    }

    public Tabletop getTabletop() {
        return tabletop;
    }

    public void setTabletop(Tabletop tabletop) {
        this.tabletop = tabletop;
    }
}
