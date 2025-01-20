package pl.pjatk.SOZ_Gastro.ObjectClasses.Requests;

import pl.pjatk.SOZ_Gastro.ObjectClasses.Tabletop;
import pl.pjatk.SOZ_Gastro.ObjectClasses.User;

public class CreateOrderRequest {
    private Long[] mealID;
    private Tabletop tabletop;
    private User user;

    // Getters and Setters
    public Long[] getMealID() {
        return mealID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
