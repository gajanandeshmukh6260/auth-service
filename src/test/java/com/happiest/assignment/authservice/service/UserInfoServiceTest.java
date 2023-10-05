package com.happiest.assignment.authservice.service;

import com.happiest.assignment.authservice.entity.UserInfo;
import com.happiest.assignment.authservice.repository.UserInfoRepository;
import com.happiest.assignment.authservice.services.UserInfoService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class UserInfoServiceTest {

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    private UserInfoRepository repository;

    @InjectMocks
    private UserInfoService userDetailsService;

    @BeforeEach
    public void setUp(){
        userDetailsService=new UserInfoService();
    }

    @Test
    public void testLoadUserByUsername() {
        String username = "Gajanan";
        UserInfo userInfo = new UserInfo();
        userInfo.setName(username);
        userInfo.setPassword("password");
        userInfo.setRoles("ROLE_USER");

        when(repository.findByName(username)).thenReturn(Optional.of(userInfo));
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_USER")));

    }

    @Test
    public void testAddUser(){
        UserInfo userInfo = new UserInfo();
        userInfo.setName("user");
        userInfo.setPassword("password");

        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        userDetailsService.addUser(userInfo);
        verify(passwordEncoder).encode("password");
        verify(repository).save(userInfo);
    }

}
