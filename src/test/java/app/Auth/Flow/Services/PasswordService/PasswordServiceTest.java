package app.Auth.Flow.Services.PasswordService;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PasswordServiceTest {

    //Test if the program runs via the Terminal
    @Test
    void shouldRunViaTerminal() {

        assertNull(System.console());

        PasswordService service = new PasswordService();

        RuntimeException ex = assertThrows(RuntimeException.class, service::plainPWSD);

        assertEquals("[WARNING] Please run the program only in the Terminal", ex.getMessage());
    }

    //Test if the exception for the blank password works
    @Test
    void cantBeBlank() {
        PasswordService service = new PasswordService();
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> service.validatePWSD("".toCharArray()));

        assertEquals("[ERROR] Your Password can't be empty!", ex.getMessage());
    }

    //Test if the exception for the password length works
    @Test
    void shouldBeTenLettersLong() {
        PasswordService service = new PasswordService();

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> service.validatePWSD("t1A!".toCharArray()));

        assertEquals("[ERROR] Your password must bee at least 10 letters long", ex.getMessage());
    }

    //Test if the exception for the Uppercase letter works
    @Test
    void shouldContainUpperCase() {

        PasswordService service = new PasswordService();

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> service.validatePWSD("testpwsd123!".toCharArray()));

        assertEquals("[ERROR] Please note that your Password need to contain Uppercase Letters to be valid", ex.getMessage());
    }

    //Test if the exception for lowercase letter works
    @Test
    void ShouldContainLowerCase() {
        PasswordService service = new PasswordService();

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> service.validatePWSD("TESTPWSD123!".toCharArray()));

        assertEquals("[ERROR] Please note that your Password need to contain Lowercase Letters to be valid", ex.getMessage());
    }

    //Test if the exception for numbers works
    @Test
    void shouldContainNumbers() {
        PasswordService service = new PasswordService();

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> service.validatePWSD(("TestPwsdTestCase!".toCharArray())));

        assertEquals("[ERROR] Please note that your Password need to contain Numbers to be valid", ex.getMessage());
    }

    //Test if the exception for the special Letters works
    @Test
    void shouldContainSpecialLetter() {
        PasswordService service = new PasswordService();

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> service.validatePWSD("TestPwsd123".toCharArray()));

        assertEquals("[ERROR] Please note that your Password need to contain a Special Letter (e.g. !%$§§%&/) to be valid", ex.getMessage());
    }

    //Test Successfully password input
    @Test
    void validPasswordTest() {
        PasswordService service = new PasswordService();

        assertDoesNotThrow(() -> service.validatePWSD("PasswordTest2026!!".toCharArray()));
    }

    //Test if the same password works
    @Test
    void testSamePasswordInput() {
        PasswordService service = new PasswordService();

        assertDoesNotThrow(() -> service.validateRetypedPassword("Password2025!!".toCharArray(), "Password2025!!".toCharArray()));
    }

    //Test if the exception works if the retype is empty
    @Test
    void testEmptyRetype() {
        PasswordService service = new PasswordService();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.validateRetypedPassword("Password2025!!".toCharArray(), "".toCharArray()));

        assertEquals("[ERROR] The verification password can't be empty and must be equal to the password from before", ex.getMessage());
    }

    //Test if the exception for the retype works if the retype is not the same
    @Test
    void testWrongRetypeException() {
        PasswordService service = new PasswordService();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.validateRetypedPassword("Password2025!!".toCharArray(), "Password2026!!".toCharArray()));

        assertEquals("[ERROR] The verification password can't be empty and must be equal to the password from before", ex.getMessage());
    }

    
}
