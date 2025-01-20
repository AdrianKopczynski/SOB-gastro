package pl.pjatk.SOZ_Gastro;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.pjatk.SOZ_Gastro.Exceptions.UserNotFoundException;
import pl.pjatk.SOZ_Gastro.ObjectClasses.User;
import pl.pjatk.SOZ_Gastro.Services.UserService;

@SpringBootTest
class SozGastroApplicationTests {

	@Autowired
	private UserService userService;
	@Test
	void contextLoads() {
	}



}
