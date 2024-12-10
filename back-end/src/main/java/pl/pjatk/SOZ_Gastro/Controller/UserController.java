package pl.pjatk.SOZ_Gastro.Controller;

import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pjatk.SOZ_Gastro.ObjectClasses.User;
import pl.pjatk.SOZ_Gastro.Services.UserService;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController
{

    private final UserService userService;

    public UserController(UserService userService)
    {
        this.userService = userService;
    }
    //Getters
    @GetMapping("/getAllUsers")
    public ResponseEntity<List<User>> getAll()
    {
        return ResponseEntity.ok().body(this.userService.getAll());
    }

    @GetMapping("/getUserById/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id)
    {
        return ResponseEntity.ok().body(this.userService.getById(id));
    }

    @GetMapping("/getUserByPin/{loginPin}")
    public ResponseEntity<User> getUserByLoginPin(@PathVariable("loginPin") String loginPin)
    {
        return ResponseEntity.ok().body(this.userService.getByLoginPin(loginPin));
    }

    @GetMapping("/getUserByUsername/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable("username") String username)
    {
        return ResponseEntity.ok().body(this.userService.getByUsername(username));
    }

    //Posts
    @PostMapping("/createUser")
    public ResponseEntity<User> createUser(@RequestBody User user)
    {
        return ResponseEntity.ok().body(this.userService.createUser(user));
    }

    //Deletes
    @DeleteMapping("/deleteUserByUsername/{username}")
    public ResponseEntity<String> deleteUserByUsername(@PathVariable("username") String username) {
        this.userService.deleteUserByUsername(username);
        return ResponseEntity.ok().body("User deleted");
    }

    @DeleteMapping("/deleteUserById/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable("id") Long id) {
        this.userService.deleteUserById(id);
        return ResponseEntity.ok().body("User deleted");
    }


}
