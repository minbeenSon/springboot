package com.study.reboard.service;

import com.study.reboard.entity.Map;
import com.study.reboard.entity.User;
import com.study.reboard.repository.MapRepository;
import com.study.reboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MapService {
    @Autowired
    private MapRepository mapRepository;
    @Autowired
    private UserRepository userRepository;

    //Save
    public Map mapSave(Map map, String username) {
        User user = userRepository.findByUsername(username);
        map.setUser(user);
        return mapRepository.save(map);
    }

    //List
    public Page<Map> mapList(Pageable pageable, String username) {
        return mapRepository.findByUsernameQuery(username, pageable);
    }
}
