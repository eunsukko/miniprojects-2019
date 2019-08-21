package techcourse.fakebook.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.fakebook.domain.user.User;
import techcourse.fakebook.domain.user.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FriendshipServiceTest {
    @Autowired
    private FriendshipService friendshipService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private List<User> savedUsers;
    private List<Long> savedUserIds;

    private Long userId;
    private List<Long> friendIds;

    @BeforeEach
    void Setup() {
        유저_10명_추가();

    }

    private void 유저_10명_추가() {
        assertThat(userRepository).isNotNull();
        int numUsers = 10;
        List<User> users = generatesUsers(numUsers);

        savedUsers = users.stream()
                .map(userRepository::save)
                .collect(Collectors.toList());

        savedUserIds = savedUsers.stream()
                .mapToLong(User::getId)
                .boxed()
                .collect(Collectors.toList());
    }

    @Test
    void 유저_친구_추가_및_올바른지_조회() {
        // Arrange
        int userIndex = 0;
        List<Integer> friendIndexes = Arrays.asList(1, 9);
        유저_친구_초기화(userIndex, friendIndexes);

        // Act
        List<Long> foundFriendIds = friendshipService.findFriendIds(userId);

        // Assert
        assertThat(foundFriendIds.size()).isEqualTo(friendIndexes.size());
        friendIds.stream()
                .forEach(friendId -> assertThat(foundFriendIds.contains(friendId)).isTrue());

        for (Long friendId : friendIds) {
            List<Long> foundIds = friendshipService.findFriendIds(friendId);
            assertThat(foundIds).isEqualTo(Arrays.asList(userId));
        }
    }

    // 원래는 통합 테스트에서 해야하지 않을까?
    @Test
    void 유저삭제_친구정보도_삭제() {
        // Arrange
        int userIndex = 0;
        List<Integer> friendIndexes = Arrays.asList(1, 9);
        유저_친구_초기화(userIndex, friendIndexes);

        // Act
        userService.deleteById(userId);

        // Assert
        assertThat(friendshipService.findFriendIds(userId).isEmpty()).isTrue();
    }

    private void 유저_친구_초기화(int userIndex, List<Integer> friendIndexes) {
        userId = savedUserIds.get(userIndex);
        friendIds = friendIndexes.stream()
                .map(index -> savedUserIds.get(index))
                .collect(Collectors.toList());
        friendIds.stream()
                .forEach(friendId -> friendshipService.makeThemFriends(userId, friendId));
    }

    private <T> void printList(List<T> list) {
        list.stream().forEach(System.out::println);
    }

    private List<User> getUsersByIndexes(List<Integer> indexes) {
        return indexes.stream()
                .map(savedUsers::get)
                .collect(Collectors.toList());
    }

    private List<User> generatesUsers(int numUsers) {
        return IntStream.range(0, numUsers)
                .mapToObj(FriendshipServiceTest::newUser)
                .collect(Collectors.toList());
    }

    private static User newUser(int number) {
        String anyString = "xxx";

        return new User(
                String.format("email%d@hello.com", number),
                anyString,
                anyString,
                anyString,
                anyString,
                anyString,
                anyString
        );
    }
}