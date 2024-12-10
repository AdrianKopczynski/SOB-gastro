package pl.pjatk.SOZ_Gastro.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundByIdException extends RuntimeException
{
    public UserNotFoundByIdException(Long id)
    {
        super("User with id " + id + " not found");
    }
}