package com.studentsystemapp.service.impl;

import java.util.List;
import java.util.Optional;

import com.studentsystemapp.model.entity.BaseUser;
import com.studentsystemapp.model.entity.CustomUserDetails;
import com.studentsystemapp.model.enums.UserRolesEnum;
import com.studentsystemapp.repo.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


public class UniversityUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public UniversityUserDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<BaseUser> optionalBaseUser = userRepository.findByUsername(username);


        return userRepository
                .findByUsername(username)
                .map(UniversityUserDetailsService::map)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found!"));
    }

    private static CustomUserDetails map(BaseUser userEntity) {
        return new CustomUserDetails(userEntity.getUsername(), userEntity.getPassword(), List.of(map(userEntity.getRole())),
                userEntity.getFirstName() + ' ' + userEntity.getLastName(), userEntity.getEnrollments(), userEntity.getTasks() );
    }

    private static GrantedAuthority map(UserRolesEnum userRoleEntity) {
        return new SimpleGrantedAuthority(
                "ROLE_" + userRoleEntity.name()
        );
    }
}
