package pl.pjatk.SOZ_Gastro.Services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.pjatk.SOZ_Gastro.Exceptions.UserNotFoundException;
import pl.pjatk.SOZ_Gastro.ObjectClasses.User;
import pl.pjatk.SOZ_Gastro.Repositories.UserRepository;
import pl.pjatk.SOZ_Gastro.Exceptions.BadRequestException;

import java.util.List;

@Service
public class UserService
{
    private final RestTemplate restTemplate;
    private final UserRepository userRepository;

    public UserService(RestTemplate restTemplate, UserRepository userRepository)
    {
        this.restTemplate = restTemplate;
        this.userRepository = userRepository;
    }

    public User createUser(User user) throws BadRequestException
    {

        if (user == null || user.getUsername() == null || user.getLoginPin() == null || user.getUserType() == null)
        {
            throw new BadRequestException("All credentials are required.");
        }

        if (userRepository.existsByUsername(user.getUsername()))
        {
            throw new BadRequestException("User with username " + user.getUsername() + " already exists.");
        }

        return userRepository.save(user);
    }

    public User getByUsername(String username) throws UserNotFoundException
    {
        if (username == null || username.trim().isEmpty())
        {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }

        return this.userRepository.findByUsername(username).orElseThrow(() ->
                new UserNotFoundException("User with username " + username + " not found"));
    }

    public User getByLoginPin(String loginPin) throws UserNotFoundException
    {
        if (loginPin == null)
        {
            throw new IllegalArgumentException("Login pin cannot be null");
        }
        return this.userRepository.findByLoginPin(loginPin).orElseThrow(() ->
                new UserNotFoundException("user with given login pin does net exist"));

    }

    public User getById(Long id) throws UserNotFoundException
    {
        if (id == null)
        {
            throw new IllegalArgumentException("Id cannot be null");
        }
        return this.userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
    }

    public void deleteUserById(Long id) throws UserNotFoundException
    {
        if (id == null)
        {
            throw new IllegalArgumentException("Id cannot be null");
        }
        getById(id);
        this.userRepository.deleteById(id);
    }

    public List<User>  getAll(){return this.userRepository.findAll();}

    public void deleteUserByUsername(String username) throws UserNotFoundException
    {
        if (username == null || username.trim().isEmpty())
        {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }

        getByUsername(username);

        this.userRepository.deleteByUsername(username);
    }
}
