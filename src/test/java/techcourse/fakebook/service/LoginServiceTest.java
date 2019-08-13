package techcourse.fakebook.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import techcourse.fakebook.controller.utils.SessionUser;
import techcourse.fakebook.domain.User;
import techcourse.fakebook.domain.UserRepository;
import techcourse.fakebook.exception.NotFoundUserException;
import techcourse.fakebook.exception.NotMatchPasswordException;
import techcourse.fakebook.service.dto.LoginRequest;
import techcourse.fakebook.service.utils.encryptor.Encryptor;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(SpringExtension.class)
class LoginServiceTest {
    @InjectMocks
    private LoginService loginService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Encryptor encryptor;

    @Test
    void login_틀린_이메일() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("notExist@email.com", "Password1!");
        given(userRepository.findByEmail(loginRequest.getEmail())).willReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundUserException.class, () -> loginService.login(loginRequest));
    }

    @Test
    void login_틀린_비밀번호() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("exist@email.com", "WrongPassword1!");
        User user = mock(User.class);
        given(userRepository.findByEmail(loginRequest.getEmail()))
                .willReturn(Optional.of(user));
        given(encryptor.isMatch(loginRequest.getPassword(), user.getEncryptedPassword()))
                .willReturn(false);

        // Act & Assert
        assertThrows(NotMatchPasswordException.class, () -> loginService.login(loginRequest));
    }

    @Test
    void login_올바른_이메일_및_비밀번호() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("exist@email.com", "Password1!");
        User user = mock(User.class);
        given(userRepository.findByEmail(loginRequest.getEmail()))
                .willReturn(Optional.of(user));
        given(encryptor.isMatch(loginRequest.getPassword(), user.getEncryptedPassword()))
                .willReturn(true);
        SessionUser expectedSessionUser = new SessionUser(1l, "name", "coverUrl");
        given(user.getId()).willReturn(expectedSessionUser.getId());
        given(user.getName()).willReturn(expectedSessionUser.getName());
        given(user.getCoverUrl()).willReturn(expectedSessionUser.getCoverUrl());

        // Act
        SessionUser sessionUser = loginService.login(loginRequest);

        // Assert
        assertThat(sessionUser).isEqualTo(expectedSessionUser);
    }

}