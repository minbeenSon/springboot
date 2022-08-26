package com.study.reboard.service;

import com.study.reboard.entity.Role;
import com.study.reboard.entity.User;
import com.study.reboard.repository.RoleRepository;
import com.study.reboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    //Save
    public User userJoin(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());

        user.setPassword(encodedPassword);
        user.setEnabled(true);
        Role role = new Role();
        role.setId(1);
        user.getRoles().add(role);
        return userRepository.save(user);
    }

    //UpdatePassword
    public  User userPasswordUpdate(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }

    //List
    public Page<User> userList(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    //ListBySearchedUser
    public  Page<User> userSearchList(Pageable pageable, String username) {
        return userRepository.findByUsernameContaining(pageable, username);
    }

    //Delete
    public void userDelete(Integer id) {
        userRepository.deleteById(id);
    }

    //ViewById
    public User userDetailById(Integer id) {
        return userRepository.findById(id).get();
    }

    //ViewByUsername
    public User useDetailByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    //UpdateAuthority(권한)
    public User userAuthority(User user,String authorityType,Integer id) {
        //USER 인 경우
        if(authorityType == "ROLE_USER" || authorityType.equals("ROLE_USER")) {
            Role role = new Role();
            role.setId(1);
            user.getRoles().add(role);
            Role r1 = roleRepository.getById(2);
            if(user.getRoles().contains(r1)) {
                user.getRoles().remove(r1);
            }
            userRepository.save(user);
        //MANAGER 인 경우
        } else if(authorityType == "ROLE_MANAGER" || authorityType.equals("ROLE_MANAGER")) {
            Role role = new Role();
            role.setId(2);
            user.getRoles().add(role);
            Role r1 = roleRepository.getById(1);
            if(user.getRoles().contains(r1)) {
                user.getRoles().remove(r1);
            }
            userRepository.save(user);
        }
        return userRepository.save(user);
    }
}
