package com.example.demo.service;

import com.example.demo.dto.UserDto;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.UserRepository;
import com.example.demo.exception.NoSuchUserException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository repository;
    @Mock
    private UserMapper mapper;
    @InjectMocks
    private UserService service;

    @Test
    void getByIdReturnValidUserIfUserWithThisIdExist() {
        long id = 1;
        User user = new User(1, "Test");
        User expectedUser = new User(1, "Test");
        when(repository.findById(id)).thenReturn(Optional.of(user));

        User result = service.getById(id);

        assertNotNull(result);
        assertEquals(expectedUser, result);
    }

    @Test
    void getByIdThrowsRuntimeExceptionIfUserWithThisIdNotExist() {
        long id = 1;
        when(repository.findById(id)).thenReturn(Optional.empty());


        var runtimeException = assertThrows(NoSuchUserException.class,
                () -> service.getById(id));
        assertEquals("No such user", runtimeException.getMessage());
    }

    @Test
    void saveUserReturnUserIfSaveSuccess() {
        UserDto dtoToSave = new UserDto("Test");
        User expectedUser = new User(1, "Test");
        when(repository.save(expectedUser)).thenReturn(expectedUser);
        when(mapper.toUser(dtoToSave)).thenReturn(expectedUser);

        User result = service.saveUser(dtoToSave);

        assertEquals(expectedUser, result);

    }

    @Test
    void saveUserThrowsRuntimeExceptionIfUserDtoIsNull() {
        var runtimeException = assertThrows(RuntimeException.class,
                () -> service.saveUser(null));
        assertEquals("User cant be null", runtimeException.getMessage());
    }

    @Test
    void getAllReturnValidList() {
        List<User> users = List.of(new User(1, "Test"),
                new User(2, "Test2"));
        List<UserDto> userDto = List.of(new UserDto("Test"),
                new UserDto("Test"));
        when(repository.findAll()).thenReturn(users);
        when(mapper.toDtoList(users)).thenReturn(userDto);

        List<UserDto> result = service.getAll();

        assertNotNull(result);
        assertEquals(userDto, result);

    }

    @Test
    void saveUserShouldCallRepository() {
        User user = new User(1, "Test");
        UserDto dto = new UserDto("Test");
        when(mapper.toUser(dto)).thenReturn(user);

        service.saveUser(dto);

        verify(repository).save(user);
    }

}
