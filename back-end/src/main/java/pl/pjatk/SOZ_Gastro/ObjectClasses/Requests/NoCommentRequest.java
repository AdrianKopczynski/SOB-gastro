package pl.pjatk.SOZ_Gastro.ObjectClasses.Requests;

import pl.pjatk.SOZ_Gastro.ObjectClasses.Tabletop;
import pl.pjatk.SOZ_Gastro.ObjectClasses.User;

public class NoCommentRequest
{
    private Long[] mealID;
    private Long tabletop;
    private User user;
    private String comment;


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

    public Long getTabletop() {
        return tabletop;
    }

    public void setTabletop(Long tabletop) {
        this.tabletop = tabletop;
    }
}


