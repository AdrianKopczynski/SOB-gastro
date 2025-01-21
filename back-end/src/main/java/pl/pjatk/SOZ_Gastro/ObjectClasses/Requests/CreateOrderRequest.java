package pl.pjatk.SOZ_Gastro.ObjectClasses.Requests;

import pl.pjatk.SOZ_Gastro.ObjectClasses.Tabletop;
import pl.pjatk.SOZ_Gastro.ObjectClasses.User;

public class CreateOrderRequest {
    private Long[] mealID;
    private Tabletop tabletop;
    private User user;
    private String comment;
    private String[] comment2;

    public String[] getComment2() {
        return comment2;
    }

    public void setComment2(String[] comment2) {
        this.comment2 = comment2;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

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
