package com.studentsystemapp.service.impl;

import com.studentsystemapp.model.entity.BaseUser;
import com.studentsystemapp.model.enums.UserRolesEnum;
import com.studentsystemapp.repo.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UniversityUserDetailsServiceTest {

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private BaseUser mockUser;
    private UniversityUserDetailsService serviceToTest;

    @BeforeEach
    void setUp() {


        serviceToTest = new UniversityUserDetailsService(mockUserRepository);

    }

    @Test
    void testFindByValidUsername() {

        when(mockUser.getRole()).thenReturn(UserRolesEnum.STUDENT);
        when(mockUser.getUsername()).thenReturn("username");
        when(mockUser.getPassword()).thenReturn("password");
        when(mockUser.getRole()).thenReturn(UserRolesEnum.STUDENT);
        when(mockUserRepository.findByUsername("username")).thenReturn(Optional.of(mockUser));

        assertTrue(serviceToTest.loadUserByUsername("username").getAuthorities().contains(new SimpleGrantedAuthority(
                "ROLE_" + mockUser.getRole()
        )));

    }

    @Test
    void testFindByInvalidUsername() {
;
        assertThrows(UsernameNotFoundException.class, () -> serviceToTest.loadUserByUsername("invalidUsername"));

    }




}
