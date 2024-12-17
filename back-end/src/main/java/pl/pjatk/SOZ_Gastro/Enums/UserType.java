package pl.pjatk.SOZ_Gastro.Enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "role of the user") public enum UserType
{
    Admin("Administrator"), Inventory("Magazyn"), Cashier("Kasjer");
    private String name;

    UserType(String name)
    {
        this.name = name;
    }


}
