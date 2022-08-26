package com.study.reboard.service;

import com.study.reboard.entity.Lecture;
import com.study.reboard.entity.User;
import com.study.reboard.repository.LectureRepository;
import com.study.reboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class LectureService {
    @Autowired
    private LectureRepository lectureRepository;
    @Autowired
    private UserRepository userRepository;

    //List
    public Page<Lecture> lectureList(Pageable pageable) {
        return lectureRepository.findAll(pageable);
    }

    //View(Lecture)
    public Lecture lectureDetail(Integer id) {
        Lecture lecture = lectureRepository.findById(id).get();
        lecture.setContent(lecture.getContent());
        return lecture;
    }

    //Save
    public Lecture lectureWrite(Lecture lecture, String username) {
        User user = userRepository.findByUsername(username);
        lecture.setUser(user);
        lecture.setContent(lecture.getContent());
        return lectureRepository.save(lecture);
    }
}
