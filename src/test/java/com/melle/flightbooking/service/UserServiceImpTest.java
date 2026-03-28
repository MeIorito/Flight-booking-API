package com.melle.flightbooking.service;

import com.melle.flightbooking.interfaces.UserService;
import com.melle.flightbooking.model.User;
import com.melle.flightbooking.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImpTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserServiceImp userService;

    @Test
    public void register_returnsRegisteredUser_whenRegistered(){
        int id = 1;
        User user = new User();
        user.setId(id);

        when(userRepository.save(user)).thenReturn(user);

        User result = userService.register(user);

        assertEquals(result.getId(), user.getId());
        verify(userRepository).save(user);
    }
}
