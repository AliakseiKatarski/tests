package com.example.demo.service;

import com.example.demo.dto.UserDto;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.UserRepository;
import com.example.demo.exception.NoSuchUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;


    public User getById(long id) {
        return repository.findById(id)
                .orElseThrow(NoSuchUserException::new);
    }

    public User saveUser(UserDto userDto) {
        if (userDto == null) {
            throw new RuntimeException("User cant be null");
        }
        return repository.save(mapper.toUser(userDto));
    }

    public List<UserDto> getAll() {
        return mapper.toDtoList(repository.findAll());
    }
}
