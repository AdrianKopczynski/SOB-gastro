package pl.pjatk.SOZ_Gastro.ObjectClasses.Requests;

public class NoCommentUpdate
{
    private Long[] mealID;
    private String comment;


    // Getters and Setters
    public Long[] getMealID() {
        return mealID;
    }
    public String getComment(){return comment;}

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setMealID(Long[] mealID) {
        this.mealID = mealID;
    }
}
