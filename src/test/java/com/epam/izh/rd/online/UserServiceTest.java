package com.epam.izh.rd.online;

import com.epam.izh.rd.online.entity.User;
import com.epam.izh.rd.online.exception.SimplePasswordException;
import com.epam.izh.rd.online.exception.UserAlreadyRegisteredException;
import com.epam.izh.rd.online.repository.IUserRepository;
import com.epam.izh.rd.online.repository.UserRepository;
import com.epam.izh.rd.online.service.CurrentUserManager;
import com.epam.izh.rd.online.service.IUserService;
import com.epam.izh.rd.online.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static com.epam.izh.rd.online.Providers.getUserWithNumberPassword;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserServiceTest {

    private IUserRepository userRepository;
    private IUserService userService;
    private Assert assertion = new Assert();

    @BeforeEach
    private void setup() {
        userRepository = new UserRepository();
        userService = new UserService(userRepository);
    }

    @ParameterizedTest
    @DisplayName("Тест метода IUserService.register(User user) кейс 1")
    @MethodSource("com.epam.izh.rd.online.Providers#testRegisterCase1")
    void testRegisterCase1(User user) {
        assertThrows(IllegalArgumentException.class,
                () -> userService.register(user),
                "Ошибка в заполнении полей"
        );
    }

    @Test
    @DisplayName("Тест метода IUserService.register(User user) кейс 2")
    void testRegisterCase2() throws UserAlreadyRegisteredException, SimplePasswordException {
        User user = Providers.getUser();

        userService.register(user);
        assertion.assertThrowsWithClassName("UserAlreadyRegisteredException", () -> userService.register(user),
                "Пользователь с логином " + user.getLogin() + " уже зарегистрирован");
    }

    @Test
    @DisplayName("Тест метода IUserService.register(User user) кейс 3")
    void testRegisterCase3() {
        User user = getUserWithNumberPassword();

        assertion.assertThrowsWithClassName("SimplePasswordException", () -> userService.register(user),
                "Пароль не соответствует требованиям безопасности"
        );
    }

    @ParameterizedTest
    @MethodSource("com.epam.izh.rd.online.Providers#testDelete")
    @DisplayName("Тест метода IUserService.delete(String login)")
    void testDelete(User user) {
        CurrentUserManager.setCurrentLoggedInUser(user);
        assertion.assertThrowsWithClassName("NotAccessException", () -> userService.delete("123"),
                "Недостаточно прав для выполнения операции");
    }
}
