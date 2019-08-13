package techcourse.fakebook.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import techcourse.fakebook.service.dto.UserSignupRequest;
import techcourse.fakebook.service.dto.UserUpdateRequest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

class UserApiControllerTest extends ControllerTestHelper {
    @LocalServerPort
    private int port;

    @Test
    void 존재하는_유저_수정() {
        UserSignupRequest userSignupRequest = newUserSignupRequest();
        signup(userSignupRequest);
        Long userId = getId(userSignupRequest.getEmail());

        UserUpdateRequest userUpdateRequest = new UserUpdateRequest("updatedCoverUrl", "updatedIntroduction");

        given().
                port(port).
                contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).
                accept(MediaType.APPLICATION_JSON_UTF8_VALUE).
                body(userUpdateRequest).
        when().
                put("/api/users/" + userId).
        then().
                statusCode(200).
                body("coverUrl", equalTo(userUpdateRequest.getCoverUrl())).
                body("introduction", equalTo(userUpdateRequest.getIntroduction()));
    }
}