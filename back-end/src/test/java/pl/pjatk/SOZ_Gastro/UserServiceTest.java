package pl.pjatk.SOZ_Gastro;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.pjatk.SOZ_Gastro.Enums.UserType;
import pl.pjatk.SOZ_Gastro.Exceptions.BadRequestException;
import pl.pjatk.SOZ_Gastro.Exceptions.UserNotFoundException;
import pl.pjatk.SOZ_Gastro.ObjectClasses.Tabletop;
import pl.pjatk.SOZ_Gastro.ObjectClasses.User;
import pl.pjatk.SOZ_Gastro.Repositories.UserRepository;
import pl.pjatk.SOZ_Gastro.Services.UserService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;


    @InjectMocks
    private UserService userService;

//    @Test
//    public void testGetUserList(){
//        User user = new User(1L, "nameA", "1222", UserType.Admin, true );
//        User user1 = new User(2L, "nameB", "12223", UserType.Cashier, false );
//        when(userRepository.findAllByIdIsNotNull())
//                .thenReturn(Arrays.asList(user, user1));
//
//        List<User> result = userService.getAll();
//
//        assertNotNull(result);
//        assertEquals(2, result.size());
//        assertEquals(user.getUsername(),result.get(1).getUsername());
//    }
//
//
//	@Test
//	public void testGetUserById()
//	{
//        User user = new User(1L, "name", "1111", UserType.Admin, true );
//        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//
//        User result = userService.getById(1L);
//        assertNotNull(result);
//        assertEquals(Long.valueOf(1L), result.getId());
//	}
//
//    @Test
//    public void testGetUserByUsername()
//    {
//        User user = new User(1L, "name", "1111", UserType.Admin, true );
//        when(userRepository.findByUsername("name")).thenReturn(Optional.of(user));
//
//        User result = userService.getByUsername("name");
//        assertNotNull(result);
//        assertEquals("name", result.getUsername());
//    }
//
//    @Test
//    public void testThrowBadRequestWhenUserWIthGivenUsernameExists()
//    {
//        User user = new User(1L, "name", "1111", UserType.Admin, true );
//        when(userRepository.existsByUsername("name")).thenReturn(true);
//
//        BadRequestException thrown = assertThrows(BadRequestException.class,()
//                        -> userService.createUser(user),
//                "Expected createUser to throw, but it didn't");
//
//        assertTrue(thrown.getMessage().contains("User with username " + user.getUsername() + " already exists."));
//    }
//
//
//    @Test
//    public void testUpdateUserUsername()
//    {
//        User user = new User(1L, "nameAB", "12232", UserType.Cashier, true);
//
//        when(userRepository.existsByUsername("newUsername")).thenReturn(true);
//
//        userService.updateUsername("nameAB", "newUsername");
//
//        verify(userRepository).existsByUsername("newUsername");
//
//        assertEquals("newUsername", user.getUsername());
//    }
//
//    @Test
//    public void testDeleteUserByUsername()
//    {
//        User user = new User(1L, "nameABC", "1232", UserType.Cashier, true);
//
//        when(userRepository.existsByUsername("nameABC")).thenReturn(false);
//
//        userService.deleteUserByUsername("nameABC");
//
//        verify(userRepository).existsByUsername("nameABC");
//
//        verify(userRepository, never()).deleteByUsername("nameABC");
//    }
}
