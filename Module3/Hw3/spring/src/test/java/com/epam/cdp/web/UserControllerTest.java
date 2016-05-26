package com.epam.cdp.web;

import com.epam.cdp.facade.BookingFacade;
import com.epam.cdp.model.User;
import com.epam.cdp.model.impl.UserImpl;
import com.epam.cdp.web.controller.UserController;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static com.epam.cdp.TestUtils.APPLICATION_JSON_UTF8;
import static com.epam.cdp.TestUtils.convertObjectToJsonBytes;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerTest {
    @Mock
    BookingFacade facade;

    UserController userController;

    @Before
    public void setUp() {
        facade = mock(BookingFacade.class);
        userController = new UserController(facade);
    }

    @Test
    public void whenGetUserByIdThenUserIsReturnedInJson() throws Exception {
        User user = new UserImpl(1, "name", "email");
        when(facade.getUserById(anyLong())).thenReturn(user);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        mockMvc.perform(get("/users/{id}", user.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));

        verify(facade, times(1)).getUserById(anyLong());
        verifyNoMoreInteractions(facade);
    }

    @Test
    public void whenGetUserByEmailThenUserIsReturnedInJson() throws Exception {
        User user = new UserImpl(1, "name", "email");
        when(facade.getUserByEmail(anyString())).thenReturn(user);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        mockMvc.perform(get("/users/email/{email}", user.getEmail()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));

        verify(facade, times(1)).getUserByEmail(anyString());
        verifyNoMoreInteractions(facade);
    }

    @Test
    public void whenGetUsersByNameThenUsersAreReturnedInJson() throws Exception {
        String name = "name";
        User user = new UserImpl(1, name, "email");
        User user1 = new UserImpl(2, name, "xx@dx.com");
        when(facade.getUsersByName(anyString(), anyInt(), anyInt())).thenReturn(Arrays.asList(user, user1));
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        mockMvc.perform(get("/users/name/{name}", user.getName()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[0].name", is(user.getName())))
                .andExpect(jsonPath("$[1].name", is(user1.getName())));

        verify(facade, times(1)).getUsersByName(anyString(), anyInt(), anyInt());
        verifyNoMoreInteractions(facade);
    }

    @Test
    public void whenCreateUserThenUserIsCreatedAndReturnedInJson() throws Exception {
        User user = new UserImpl(1, "name", "email");
        when(facade.createUser(anyObject())).thenReturn(user);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        mockMvc.perform(post("/users/").
                content(convertObjectToJsonBytes(user)).
                contentType(APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));

        verify(facade, times(1)).createUser(anyObject());
        verifyNoMoreInteractions(facade);
    }

    @Test
    public void whenUpdateUserThenUserIsUpdatedAndReturnedInJson() throws Exception {
        User user = new UserImpl(1, "name", "email");
        when(facade.updateUser(anyObject())).thenReturn(user);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        mockMvc.perform(put("/users/").
                content(convertObjectToJsonBytes(user)).
                contentType(APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));

        verify(facade, times(1)).updateUser(anyObject());
        verifyNoMoreInteractions(facade);
    }

    @Test
    public void whenDeleteUserThenEventIsDeletedAndStatusIs200() throws Exception {
        when(facade.deleteUser(anyLong())).thenReturn(true);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        mockMvc.perform(delete("/users/1").
                contentType(APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isOk());

        verify(facade, times(1)).deleteUser(anyLong());
        verifyNoMoreInteractions(facade);
    }

    @Test
    public void whenDeleteUserAndThereIsNoUserThenStatusNotFoundIsSent() throws Exception {
        when(facade.deleteUser(anyLong())).thenReturn(false);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        mockMvc.perform(delete("/users/1").
                contentType(APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isNotFound());

        verify(facade, times(1)).deleteUser(anyLong());
        verifyNoMoreInteractions(facade);
    }
}
