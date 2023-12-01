package com.personalProject.libraryManagementSystem.service;

import com.personalProject.libraryManagementSystem.modals.User;
import com.personalProject.libraryManagementSystem.repository.CacheRepository;
import com.personalProject.libraryManagementSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private CacheRepository cacheRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String contact) throws UsernameNotFoundException {
        // redis
        User user = cacheRepository.getUserByContact(contact);
        if(user != null){
            return  user;
        }
        // check from db
        user =userRepository.findByContact(contact);
        if(user == null){
            throw new UsernameNotFoundException("User is not there in the system");
        }
        // put the data in cache
        cacheRepository.insertDataByContact(user);
        return user;

    }
}
