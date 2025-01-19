package pl.pjatk.SOZ_Gastro.ObjectClasses.Requests;

import pl.pjatk.SOZ_Gastro.ObjectClasses.Tabletop;
public class UpdateOrderRequest {
    private Long[] mealID;

    // Getters and Setters
    public Long[] getMealID() {
        return mealID;
    }

    public void setMealID(Long[] mealID) {
        this.mealID = mealID;
    }
}
