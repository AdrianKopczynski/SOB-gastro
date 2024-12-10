package pl.pjatk.SOZ_Gastro.Services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import pl.pjatk.SOZ_Gastro.Exceptions.UserNotFoundByIdException;
import pl.pjatk.SOZ_Gastro.Exceptions.UserNotFoundByUsernameException;
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

    public User getByUsername(String username) throws UserNotFoundByUsernameException
    {
        if (username == null || username.trim().isEmpty())
        {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }

        return this.userRepository.findByUsername(username).orElseThrow(()
                -> new UserNotFoundByUsernameException(username));
    }

    public User getById(Long id) throws UserNotFoundByIdException
    {
        if (id == null)
        {
            throw new IllegalArgumentException("Id cannot be null");
        }
        return this.userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundByIdException(id));
    }

    public void deleteUserById(Long id) throws UserNotFoundByIdException
    {
        if (id == null)
        {
            throw new IllegalArgumentException("Id cannot be null");
        }
        getById(id);
        this.userRepository.deleteById(id);
    }

    public List<User>  getAll(){return this.userRepository.findAll();}

    public void deleteUserByUsername(String username) throws UserNotFoundByUsernameException
    {
        if (username == null || username.trim().isEmpty())
        {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }

        getByUsername(username);

        this.userRepository.deleteByUsername(username);
    }
}
