package io.github.oguzhancevik.springbootpetclinic.service;

import io.github.oguzhancevik.springbootpetclinic.model.User;
import io.github.oguzhancevik.springbootpetclinic.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password(user.getPassword())
                .disabled(!user.isEnabled())
                .authorities(user.getAuthorities().stream().map(a -> a.getAuthority()).toArray(String[]::new)).build();
        return userDetails;
    }
}
