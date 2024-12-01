package pl.pjatk.SOZ_Gastro.Models;

import jakarta.persistence.*;

@Entity
@Table(name="user")
public class User
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;
    private String password;
    @Column(name = "user_type", columnDefinition = "enum('Admin', 'Inventory', 'Cashier' ")
    @Enumerated(EnumType.STRING)
    private UserType userType;

    public User(Integer id, String username, String password, UserType userType)
    {
        this.id = id;
        this.username = username;
        this.password = password;
        this.userType = userType;
    }
    public User(){}

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public Integer getId() {
        return id;
    }

    public UserType getUserType() {
        return userType;
    }

    public String getUsername() {
        return username;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}