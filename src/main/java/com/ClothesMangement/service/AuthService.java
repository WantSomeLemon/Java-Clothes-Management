package com.ClothesMangement.service;

import com.ClothesMangement.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    
    public User Login(String username,String password);
    public User register(User user);
    public User authenticate(String username, String password);
}
