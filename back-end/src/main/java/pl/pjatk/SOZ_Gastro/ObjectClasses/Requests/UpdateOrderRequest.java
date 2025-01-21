package pl.pjatk.SOZ_Gastro.ObjectClasses.Requests;

import pl.pjatk.SOZ_Gastro.ObjectClasses.Tabletop;
public class UpdateOrderRequest {
    private Long[] mealID;
    private String[] comment;

    public String[] getComment() {
        return comment;
    }

    public void setComment(String[] comment) {
        this.comment = comment;
    }

    // Getters and Setters
    public Long[] getMealID() {
        return mealID;
    }

    public void setMealID(Long[] mealID) {
        this.mealID = mealID;
    }
}
