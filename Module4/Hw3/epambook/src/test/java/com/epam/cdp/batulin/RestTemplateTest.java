package com.epam.cdp.batulin;

import com.epam.cdp.batulin.dto.LoginDto;
import com.epam.cdp.batulin.dto.UserDto;
import com.epam.cdp.batulin.entity.Note;
import com.epam.cdp.batulin.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@WebIntegrationTest
public class RestTemplateTest {

    private static final String BASE_URL = "http://localhost:8080";
    static final RestTemplate template = new RestTemplate();
    private static final String NAME = "name";
    private static final String PASSWORD = "password";
    private static final String SET_COOKIE = "Set-Cookie";
    private static final String USERNAME = "username";
    private static final String TEST = "test";
    private static final String TEXT = "text";
    private static final String ADMIN = "admin";
    private static final String FRIEND = "friend";

    @BeforeClass
    public static void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("dd-MM-yyyy"));
        MappingJackson2HttpMessageConverter jsonMessageConverter = new MappingJackson2HttpMessageConverter();
        jsonMessageConverter.setObjectMapper(objectMapper);
        template.setMessageConverters(Collections.singletonList(jsonMessageConverter));
    }

    @Test
    public void testCreateUserThenLoginThenUpdateProfileAndAddFriend() {
        User user = new User(USERNAME, NAME, new Date());
        user.setPassword(PASSWORD);
        ResponseEntity<UserDto> responseEntity = createUser(user);
        UserDto userDto = responseEntity.getBody();

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.CREATED));
        assertThat(userDto.getUsername(), equalTo(user.getUsername()));
        assertThat(userDto.getName(), equalTo(user.getName()));

        ResponseEntity<LoginDto> loginResponse = login(user);
        assertTrue(loginResponse.getHeaders().containsKey(SET_COOKIE));

        UserDto updatedUser = updateUser(user);
        assertThat(updatedUser.getUsername(), equalTo(user.getUsername()));
        assertThat(updatedUser.getName(), equalTo(user.getName()));

        String friendUsername = FRIEND;
        ResponseEntity<List> addFriendResponse = addFriend(user, friendUsername);
        assertThat(addFriendResponse.getStatusCode(), equalTo(HttpStatus.CREATED));
        String username = ((LinkedHashMap) addFriendResponse.getBody().get(0)).get(USERNAME).toString();
        assertThat(username, equalTo(friendUsername));
    }

    @Test
    public void testCreateUserAndLoginThenAddNoteThenAddFriendAndAddNoteToHisTimeline() {
        User user = new User(TEST, "test name", new Date());
        user.setPassword(TEST);
        ResponseEntity<UserDto> responseEntity = createUser(user);
        UserDto userDto = responseEntity.getBody();
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.CREATED));
        assertThat(userDto.getUsername(), equalTo(user.getUsername()));
        assertThat(userDto.getName(), equalTo(user.getName()));

        ResponseEntity<LoginDto> loginResponse = login(user);
        assertTrue(loginResponse.getHeaders().containsKey(SET_COOKIE));

        Note note = new Note(TEXT);
        ResponseEntity<Note> noteResponseEntity = template.postForEntity(BASE_URL + "/user/{username}/timeline", getUserHttpEntity(user, note), Note.class, user.getUsername());
        assertThat(noteResponseEntity.getStatusCode(), equalTo(HttpStatus.CREATED));
        assertThat(noteResponseEntity.getBody(), equalTo(note));

        String friendUsername = "testFriend";
        ResponseEntity<List> addFriendResponse = addFriend(user, friendUsername);
        assertThat(addFriendResponse.getStatusCode(), equalTo(HttpStatus.CREATED));
        String username = ((LinkedHashMap) addFriendResponse.getBody().get(0)).get(USERNAME).toString();
        assertThat(username, equalTo(friendUsername));

        ResponseEntity<Note> noteResponseEntity1 = template.postForEntity(BASE_URL + "/user/{username}/friend/{friend}/timeline", getUserHttpEntity(user, note), Note.class, user.getUsername(), friendUsername);
        assertThat(noteResponseEntity1.getStatusCode(), equalTo(HttpStatus.CREATED));
        assertThat(noteResponseEntity1.getBody(), equalTo(note));
    }

    @Test
    public void testGetUserThenGetUsersTimelineThenGetUsersFriendsAsAdmin() {
        User user = new User("testUser", "test name", new Date());
        user.setPassword(TEST);
        ResponseEntity<UserDto> responseEntity = createUser(user);
        UserDto userDto = responseEntity.getBody();
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.CREATED));
        assertThat(userDto.getUsername(), equalTo(user.getUsername()));
        assertThat(userDto.getName(), equalTo(user.getName()));

        ResponseEntity<LoginDto> loginResponse = login(user);
        assertTrue(loginResponse.getHeaders().containsKey(SET_COOKIE));

        Note note = new Note(TEXT);
        ResponseEntity<Note> noteResponseEntity = template.postForEntity(BASE_URL + "/user/{username}/timeline", getUserHttpEntity(user, note), Note.class, user.getUsername());
        assertThat(noteResponseEntity.getStatusCode(), equalTo(HttpStatus.CREATED));
        assertThat(noteResponseEntity.getBody(), equalTo(note));

        String friendUsername = "anotherTestFriend";
        ResponseEntity<List> addFriendResponse = addFriend(user, friendUsername);
        assertThat(addFriendResponse.getStatusCode(), equalTo(HttpStatus.CREATED));
        String username = ((LinkedHashMap) addFriendResponse.getBody().get(0)).get("username").toString();
        assertThat(username, equalTo(friendUsername));

        User admin = new User(ADMIN, "Craig");
        admin.setPassword(ADMIN);
        ResponseEntity<UserDto> entity = template.exchange(BASE_URL + "/user/{username}", HttpMethod.GET, getUserHttpEntity(admin, null), UserDto.class, user.getUsername());
        UserDto entityBody = entity.getBody();
        assertThat(entityBody.getUsername(), equalTo(user.getUsername()));
        assertThat(entityBody.getName(), equalTo(user.getName()));

        ResponseEntity<List> noteResponse = template.exchange(BASE_URL + "/user/{username}/timeline", HttpMethod.GET, getUserHttpEntity(admin, null), List.class, user.getUsername());
        String noteText = ((LinkedHashMap) noteResponse.getBody().get(0)).get("noteText").toString();
        assertThat(noteText, equalTo(note.getNoteText()));

        ResponseEntity<List> friendResponse = template.exchange(BASE_URL + "/user/{username}/friend", HttpMethod.GET, getUserHttpEntity(admin, null), List.class, user.getUsername());
        String friendUsername1 = ((LinkedHashMap) friendResponse.getBody().get(0)).get(USERNAME).toString();
        assertThat(friendUsername1, equalTo(friendUsername));
    }

    private ResponseEntity<List> addFriend(User user, String friendUsername) {
        User friend = new User(friendUsername, NAME, new Date());
        friend.setPassword(FRIEND);
        UserDto userDto = createUser(friend).getBody();
        User createdFriend = new User(userDto.getUsername(), userDto.getName(), userDto.getDateOfBirth());

        HttpEntity<User> request = getUserHttpEntity(user, createdFriend);

        return template.postForEntity(BASE_URL + "/user/{username}/friend", request, List.class, user.getUsername());
    }

    private <T> HttpEntity<T> getUserHttpEntity(User user, Object body) {
        String plainCreds = user.getUsername() + ":" + user.getPassword();
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        MultiValueMap map = new HttpHeaders();
        map.put("Authorization", Arrays.asList("Basic " + base64Creds));
        return new HttpEntity<>((T) body, map);
    }

    private UserDto updateUser(User user) {
        user.setName("new name");
        HttpEntity<User> request = getUserHttpEntity(user, user);

        ResponseEntity<UserDto> exchange = template.exchange(BASE_URL + "/user/{username}", HttpMethod.PUT, request, UserDto.class, user.getUsername());

        return exchange.getBody();
    }

    private ResponseEntity<LoginDto> login(User user) {
        LoginDto loginDto = new LoginDto(user.getUsername(), user.getPassword());
        return template.postForEntity(BASE_URL + "/login", loginDto, LoginDto.class);
    }

    private ResponseEntity<UserDto> createUser(User user) {
        ResponseEntity<UserDto> response = template.postForEntity(BASE_URL + "/createUser", user, UserDto.class, Collections.EMPTY_MAP);
        return response;
    }
}