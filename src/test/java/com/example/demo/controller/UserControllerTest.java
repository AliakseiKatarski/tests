package com.example.demo.controller;

import com.example.demo.dto.UserDto;
import com.example.demo.entity.User;
import com.example.demo.exception.NoSuchUserException;
import com.example.demo.exception.handler.GlobalExceptionHandler;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = {UserController.class, GlobalExceptionHandler.class})
@AutoConfigureMockMvc(printOnlyOnFailure = false)
public class UserControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private UserService userService;
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Test
    @SneakyThrows
    void getAllShouldReturnAllUsers() {
        List<UserDto> list = List.of(new UserDto("Test"),
                new UserDto("Test2"),
                new UserDto("Test3"));
        when(userService.getAll()).thenReturn(list);
        mvc.perform(get("/api/v1/users"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                [
                                {"name": "Test"},
                                {"name": "Test2"},
                                {"name": "Test3"}
                                ]
                                """));
    }

    @Test
    void getByIdReturnValidUserDto() throws Exception {
        User user = new User(1, "Test");
        long id = 1;
        when(userService.getById(id)).thenReturn(user);

        mvc.perform(get("/api/v1/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""               
                        {
                        "id": 1,
                         "name": "Test"
                        }   
                        """))
                .andReturn();

    }

    @Test
    void shouldThrowsException() throws Exception {
        long id = 1;
        when(userService.getById(id)).thenThrow(new NoSuchUserException());

        Exception resolvedException = mvc.perform(get("/api/v1/users/" + id))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResolvedException();
        assertAll(() -> {
            assertTrue(resolvedException instanceof NoSuchUserException);
            assertEquals("No such user", resolvedException.getMessage());
        });

    }

    @Test
    void saveReturnValidUser() throws Exception {
        User user = new User(1, "Test");
        UserDto dto = new UserDto("Test");
        when(userService.saveUser(dto)).thenReturn(user);

        mvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$.id").value(1));


    }
}