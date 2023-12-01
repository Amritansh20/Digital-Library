package com.personalProject.libraryManagementSystem.repository;

import com.personalProject.libraryManagementSystem.modals.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
public class CacheRepository {

    @Autowired
    RedisTemplate redisTemplate;

    private static  String user_prefix = "USER::";
    public User getUserByContact(String contact) {
        return (User) redisTemplate.opsForValue().get(user_prefix+contact);
    }

    public void insertDataByContact(User user) {
        redisTemplate.opsForValue().set(user_prefix+user.getContact(),user,10, TimeUnit.MINUTES);
    }
}
