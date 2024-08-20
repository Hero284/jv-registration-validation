package core.basesyntax.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import core.basesyntax.db.Storage;
import core.basesyntax.exception.RegistrationException;
import core.basesyntax.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RegistrationServiceImplTest {
    private static RegistrationService registrationService;

    @BeforeEach
    void setUp() {
        registrationService = new RegistrationServiceImpl();
        Storage.people.clear();
    }

    @Test
    public void register_existingLogin_notOk() {
        User user = new User();
        user.setLogin("Good login");
        user.setPassword("Good password");
        user.setAge(19);

        registrationService.register(user);

        User newUser = new User();
        newUser.setLogin("Good login");
        newUser.setPassword("Good password");
        newUser.setAge(19);

        assertThrows(RegistrationException.class,
                () -> registrationService.register(newUser),
                "Expected register() to throw, but it didn't");
    }

    @Test
    public void register_shortLogin_notOK() {
        User user = new User();
        user.setLogin("short");
        user.setPassword("Good password");
        user.setAge(19);

        assertThrows(RegistrationException.class,
                () -> registrationService.register(user),
                "Login is too short");
    }

    @Test
    public void register_shortPassword_notOK() {
        User user = new User();
        user.setLogin("Good login");
        user.setPassword("short");
        user.setAge(19);
        assertThrows(RegistrationException.class,
                () -> registrationService.register(user),
                "Password is too short");
    }

    @Test
    public void register_LowerAge_notOK() {
        User user = new User();
        user.setLogin("Good login");
        user.setPassword("Good password");
        user.setAge(17);
        assertThrows(RegistrationException.class,
                () -> registrationService.register(user),
                "Age is too small");
    }

    @Test
    public void register_NegativeAge_notOK() {
        User user = new User();
        user.setLogin("Good login");
        user.setPassword("Good password");
        user.setAge(-1);
        assertThrows(RegistrationException.class,
                () -> registrationService.register(user),
                "Negative age is not allowed");
    }

    @Test
    public void register_ExactAge_OK() {
        User user = new User();
        user.setLogin("Good login");
        user.setPassword("Good password");
        user.setAge(18);
        registrationService.register(user);
        assertEquals(18, user.getAge());
    }

    @Test
    public void register_validData_success() {
        User user = new User();
        user.setLogin("Good login");
        user.setPassword("Good password");
        user.setAge(19);

        registrationService.register(user);

        assertNotNull(user);
        assertEquals(1, Storage.people.size());
        assertEquals("Good login", Storage.people.getFirst().getLogin());
    }
}

