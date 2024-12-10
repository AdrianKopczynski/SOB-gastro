package pl.pjatk.SOZ_Gastro.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundByUsernameException extends RuntimeException
{
    public UserNotFoundByUsernameException(String username)
    {
        super("User with username " + username + " not found");
    }
}