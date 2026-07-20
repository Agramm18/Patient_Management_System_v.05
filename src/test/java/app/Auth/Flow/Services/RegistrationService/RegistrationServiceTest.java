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

    @Test
    void testUsernameIsToLongInRange() {
        RegistrationService service = new RegistrationService();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.validateUsername("JavaBackendDev2026Ax"));

        assertEquals("The Username can't be shorter than 5 or longer than 20 letters please try again", ex.getMessage());
    }

    @Test
    void testUsernameIsToShortInRange() {
        RegistrationService service = new RegistrationService();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.validateUsername("test"));

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
    void testEmailToShortInRange() {
        RegistrationService service = new RegistrationService();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.validateEmailAddress("exa@e"));

        assertEquals("It seems your Email is too short", ex.getMessage());
    }

    @Test
    void testEmailToLong() {
        RegistrationService service = new RegistrationService();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.validateEmailAddress("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@example.com"));

        assertEquals("The E-Mail can't be longer than 254 signs", ex.getMessage());
    }

    @Test
    void testEmailToLongInRange() {
        RegistrationService service = new RegistrationService();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.validateEmailAddress("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@example.com"));

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

    //Test Phone Number

    @Test
    void testValidPhoneNumber() {
        RegistrationService service = new RegistrationService();

        assertDoesNotThrow(() -> service.validatePhoneNumber("+491725231824"));
    }

    @Test
    void testEmptyPhoneNumber() {
        RegistrationService service = new RegistrationService();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.validatePhoneNumber(""));

        assertEquals("This field can't be empty please try again", ex.getMessage());
    }

    @Test
    void testToLongPhoneNumber() {
        RegistrationService service = new RegistrationService();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.validatePhoneNumber("+123456789521458745965445656554656"));

        assertEquals("Your Phone Number can't be longer or be equal than 15 please try again", ex.getMessage());
    }

    @Test
    void testToShortPhoneNumber() {
        RegistrationService service = new RegistrationService();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.validatePhoneNumber("+12"));

        assertEquals("Your Phone Number is to short your Phone Number can't be shorter than or be equal to 5", ex.getMessage());
    }

    @Test
    void testToShortPhoneNumberInRange() {
        RegistrationService service = new RegistrationService();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.validatePhoneNumber("+1234"));

        assertEquals("Your Phone Number is to short your Phone Number can't be shorter than or be equal to 5", ex.getMessage());
    }

    @Test
    void tessDoesNotContainPlus() {
        RegistrationService service = new RegistrationService();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.validatePhoneNumber("01725231824"));

        assertEquals("Invalid Format the Phone Number needs one + at the start", ex.getMessage());
    }

    @Test
    void testNumberContainsUpperLetters() {
        RegistrationService service = new RegistrationService();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.validatePhoneNumber("+4912752A31"));

        assertEquals("Invalid Format Your Phone Number can't contain Letters", ex.getMessage());
    }

    @Test
    void testNumberContainsLowerLetters() {
        RegistrationService service = new RegistrationService();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.validatePhoneNumber("+4912752a31"));

        assertEquals("Invalid Format Your Phone Number can't contain Letters", ex.getMessage());
    }

    @Test
    void testNumberContainsTwoPlus() {
        RegistrationService service = new RegistrationService();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.validatePhoneNumber("+491725213+899"));

        assertEquals("Invalid Format Your Phone Number can't contain other Special letters and more than 1 +", ex.getMessage());
    }

    @Test
    void testNumberContainsAnotherSpecialLetter() {
        RegistrationService service = new RegistrationService();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.validatePhoneNumber("+491725213!899"));

        assertEquals("Invalid Format Your Phone Number can't contain other Special letters and more than 1 +", ex.getMessage());
    }

    @Test
    void testNumberHasAnInvalidCountryCode() {
        RegistrationService service = new RegistrationService();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.validatePhoneNumber("+999123456789"));

        assertEquals("The Country Code in your Phone Number is Invalid", ex.getMessage());
    }

    @Test
    void testRegistrationStateY() {
        RegistrationService service = new RegistrationService();

        assertDoesNotThrow(() -> service.validateRegistrationState("y"));
    }

    @Test
    void testRegistrationStateN() {
        RegistrationService service = new RegistrationService();

        assertDoesNotThrow(() -> service.validateRegistrationState("n"));
    }

    @Test
    void testRegistrationStateEmpty() {
        RegistrationService service = new RegistrationService();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.validateRegistrationState(""));

        assertEquals("[ERROR] This field can't be empty", ex.getMessage());
    }

    @Test
    void testRegistrationStateIsNotYOrN() {
        RegistrationService service = new RegistrationService();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.validateRegistrationState("abc"));

        assertEquals("[ERROR] Only y or n are permitted", ex.getMessage());
    }

    @Test
    void testEmptyChangeValueString() {
        RegistrationService service = new RegistrationService();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.validateChangeValueString(""));

        assertEquals("[ERROR] This field can't be empty", ex.getMessage());
    }

    @Test
    void testValidNumberInput() {
        RegistrationService service = new RegistrationService();

        assertDoesNotThrow(() -> service.convertStringValueToNumber("1"));
    }

    @Test
    void testThrowsNumberFormatExceptionIfInputIsAString() {
        RegistrationService service = new RegistrationService();

        assertThrows(NumberFormatException.class, () -> service.convertStringValueToNumber("abc"));
    }

    @Test
    void testValidPasswordHash() {
        RegistrationService service = new RegistrationService();

        assertDoesNotThrow(() -> service.verifyCollectedPassword("TestPassword1234!!"));
    }

    @Test
    void testNullPasswordHash() {
        RegistrationService service = new RegistrationService();

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> service.verifyCollectedPassword(null));
        assertEquals("[ERROR] The Password can't be empty", ex.getMessage());
    }

    @Test
    void testBlankPasswordHash() {
        RegistrationService service = new RegistrationService();

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> service.verifyCollectedPassword(""));
        assertEquals("[ERROR] The Password can't be empty", ex.getMessage());
    }

    @Test
    void testPasswordHashWithOnylSpaces() {
        RegistrationService service = new RegistrationService();

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> service.verifyCollectedPassword("    "));
        assertEquals("[ERROR] The Password can't be empty", ex.getMessage());
    }
}