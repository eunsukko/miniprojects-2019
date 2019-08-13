package techcourse.fakebook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import techcourse.fakebook.domain.UserRepository;
import techcourse.fakebook.service.dto.UserSignupRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ControllerTestHelper {
    protected Long newUserRequestId = 1l;

    @Autowired
    protected WebTestClient webTestClient;

    @Autowired
    protected UserRepository userRepository;


    protected UserSignupRequest newUserSignupRequest() {
        return new UserSignupRequest(String.format("email%d@hello.com", newUserRequestId++),
                "Password!1",
                "name",
                "gender",
                "coverUrl",
                "birth",
                "introduction");
    }

    protected WebTestClient.ResponseSpec signup(UserSignupRequest userSignupRequest) {
        return webTestClient.post().uri("/users")
                .body(BodyInserters.fromFormData("email", userSignupRequest.getEmail())
                        .with("password", userSignupRequest.getPassword())
                        .with("name", userSignupRequest.getName())
                        .with("gender", userSignupRequest.getGender())
                        .with("coverUrl", userSignupRequest.getCoverUrl())
                        .with("birth", userSignupRequest.getBirth())
                        .with("introduction", userSignupRequest.getIntroduction())
                )
                .exchange();
    }

    protected Long getId(String email) {
        return userRepository.findByEmail(email).get().getId();
    }
}
