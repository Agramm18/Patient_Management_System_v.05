package app.Auth.Flow.Services.RegistrationService;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RegistrationServiceTest {

    //Testing Username
    @Test
    void testValidUsername() {
        RegistrationService service = new RegistrationService();

        assertDoesNotThrow(() -> service.validateUsername("agramm"));
    }

    @Test
    void testBlankUsername() {
        RegistrationService service = new RegistrationService();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.validateUsername(""));

        assertEquals("This field can't be empty please try again", ex.getMessage());
    }

    @Test
    void testUsernameIsToShort() {
        RegistrationService service = new RegistrationService();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.validateUsername("ex"));

        assertEquals("The Username can't be shorter than 5 or longer than 20 letters please try again", ex.getMessage());
    }

    @Test
    void testUsernameIsToLong() {
        RegistrationService service = new RegistrationService();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.validateUsername("UserNameExampleIsToLongSoItCan'tBeValidateBecauseItsToLong"));

        assertEquals("The Username can't be shorter than 5 or longer than 20 letters please try again", ex.getMessage());
    }

    //Test EmailAddress

    @Test
    void testValidEmail() {
        RegistrationService service = new RegistrationService();

        assertDoesNotThrow(() -> service.validateEmailAddress("alexgramm@outlook.de"));
    }

    @Test
    void testEmailIsBlank() {
        RegistrationService service = new RegistrationService();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.validateEmailAddress(""));

        assertEquals("This field can't be empty please try again", ex.getMessage());
    }

    @Test
    void testEmailToShort() {
        RegistrationService service = new RegistrationService();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.validateEmailAddress("ex@4"));

        assertEquals("It seems your Email is too short", ex.getMessage());
    }

    @Test
    void testEmailToLong() {
        RegistrationService service = new RegistrationService();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.validateEmailAddress("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@example.com"));

        assertEquals("The E-Mail can't be longer than 254 signs", ex.getMessage());
    }

    @Test
    void testContainsAT() {
        RegistrationService service = new RegistrationService();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.validateEmailAddress("exampleexample.domainname.com"));

        assertEquals("Invalid Email Format It seems the @ sign is missing", ex.getMessage());
    }

    @Test
    void testBlankContentBeforeAt() {
        RegistrationService service = new RegistrationService();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.validateEmailAddress("@example.com"));

        assertEquals("Invalid Email Format The Content before the @ can't be empty", ex.getMessage());
    }
    @Test
    void testEmptyDomain() {
        RegistrationService service = new RegistrationService();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.validateEmailAddress("testExampleEmailAddress@"));

        assertEquals("Invalid Email Format The Domain can't be Empty", ex.getMessage());
    }

    @Test
    void testNonExistentDotInEmail() {
        RegistrationService service = new RegistrationService();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.validateEmailAddress("example@testcom"));

        assertEquals("Invalid Email Format the Domain must contain at least one .", ex.getMessage());
    }

    @Test
    void testNoneExistentTLDInEmail() {
        RegistrationService service = new RegistrationService();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.validateEmailAddress("example@test."));

        assertEquals("Invalid Email Format the E-Mail does not contain a TLD-Domain like .com, .org etc.", ex.getMessage());
    }

    @Test
    void testEmptyDomainName() {
        RegistrationService service = new RegistrationService();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.validateEmailAddress("example@.org"));

        assertEquals("Invalid Email Format The Domain Name can't be empty", ex.getMessage());
    }
}
