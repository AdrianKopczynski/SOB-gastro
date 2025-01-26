package pl.pjatk.SOZ_Gastro.Services;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.pjatk.SOZ_Gastro.Enums.UserType;
import pl.pjatk.SOZ_Gastro.Exceptions.UserNotFoundException;
import pl.pjatk.SOZ_Gastro.ObjectClasses.Order;
import pl.pjatk.SOZ_Gastro.ObjectClasses.User;
import pl.pjatk.SOZ_Gastro.Repositories.OrderMealRepository;
import pl.pjatk.SOZ_Gastro.Repositories.OrderRepository;
import pl.pjatk.SOZ_Gastro.Repositories.UserRepository;
import pl.pjatk.SOZ_Gastro.Exceptions.BadRequestException;

import java.util.List;

@Service
public class UserService
{
    private final RestTemplate restTemplate;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderMealRepository orderMealRepository;

    public UserService(RestTemplate restTemplate, UserRepository userRepository, OrderMealRepository orderMealRepository, OrderRepository orderRepository)
    {
        this.restTemplate = restTemplate;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.orderMealRepository = orderMealRepository;

    }

    public User createUser(User user) throws BadRequestException {

        if (user == null || user.getUsername() == null || user.getLoginPin() == null) {
            throw new BadRequestException("Username and pin are required");
        }

        String trimmedUsername = user.getUsername().trim();
        String trimmedLoginPin = user.getLoginPin().trim();

        user.setUsername(trimmedUsername);
        user.setLoginPin(trimmedLoginPin);

        if (userRepository.existsByUsername(trimmedUsername)) {
            throw new BadRequestException("User with username " + trimmedUsername + " already exists.");
        }

        if (userRepository.existsByLoginPin(trimmedLoginPin)) {
            throw new BadRequestException("Try another PIN");
        }

        return userRepository.save(user);
    }

    public User getByUsername(String username) throws UserNotFoundException
    {
        if (username == null || username.trim().isEmpty())
        {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }

        return userRepository.findByUsername(username).orElseThrow(() ->
                new UserNotFoundException("User with username " + username + " not found"));
    }

    public User getByLoginPin(String loginPin) throws UserNotFoundException
    {
        if (loginPin == null)
        {
            throw new IllegalArgumentException("Login pin cannot be null");
        }
        return userRepository.findByLoginPin(loginPin).orElseThrow(() ->
                new UserNotFoundException("user with given login pin does net exist"));

    }

    public User getById(Long id) throws UserNotFoundException
    {
        if (id == null)
        {
            throw new IllegalArgumentException("Id cannot be null");
        }
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
    }

    @Transactional
    public void deleteUserById(Long id) throws UserNotFoundException {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }

        User userToDelete = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));

        User defaultUser = userRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("Default user with id 1 not found"));

        List<Order> orders = orderRepository.findAllByUserId(id);

        for (Order order : orders) {
            order.setUser(defaultUser);
            orderRepository.save(order);
        }

        userRepository.deleteById(id);
    }

    public List<User>  getAll(){return userRepository.findAllByIdIsNotNull();}

    public void deleteUserByUsername(String username) throws UserNotFoundException
    {
        if (username == null || username.trim().isEmpty())
        {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }

        getByUsername(username);

        userRepository.deleteByUsername(username);
    }

    public User updateUsername(String username, String newUsername) throws UserNotFoundException {

        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (newUsername == null || newUsername.trim().isEmpty()) {
            throw new IllegalArgumentException("New username cannot be null or empty");
        }

        String trimmedUsername = username.trim();
        String trimmedNewUsername = newUsername.trim();

        User user = getByUsername(trimmedUsername);
        user.setUsername(trimmedNewUsername);

        return userRepository.save(user);
    }
    public User updateLoginPin(String loginPin, String newLoginPin) throws UserNotFoundException
    {
        if (loginPin == null)
        {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (newLoginPin == null)
        {
            throw new IllegalArgumentException("New login pin cannot be null");
        }

        User user = getByLoginPin(loginPin);
        user.setLoginPin(newLoginPin);

        return userRepository.save(user);

    }

    public User updateUserType(String username, UserType userType) throws UserNotFoundException
    {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (userType == null) {
            throw new IllegalArgumentException("User type cannot be null");
        }

        User user = getByUsername(username);
        user.setUserType(userType);

        return userRepository.save(user);
    }

    public User updateEnabled(String username, boolean enabled) throws UserNotFoundException
    {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }

        User user = getByUsername(username);
        user.setEnabled(enabled);

        return userRepository.save(user);
    }
}
