package com.study.reboard.service;

import com.study.reboard.entity.Comment_lec;
import com.study.reboard.entity.Lecture;
import com.study.reboard.entity.User;
import com.study.reboard.repository.Comment_lecRepository;
import com.study.reboard.repository.LectureRepository;
import com.study.reboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class Comment_lecService {
    @Autowired
    private Comment_lecRepository comment_lecRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LectureRepository lectureRepository;

    //Save
    public void comment_lecWrite(Comment_lec comment_lec, Integer lectureId, String username) {
        User user = userRepository.findByUsername(username);
        LocalDateTime dateTime = LocalDateTime.now();
        Lecture lecture = lectureRepository.findById(lectureId).get();

        comment_lec.setUser(user);
        comment_lec.setLecture(lecture);
        comment_lec.setCreated_date(dateTime);
        comment_lec.setComment_num(0);

        comment_lecRepository.save(comment_lec); //save
        comment_lec.setComment_parents(comment_lec.getId()); //'id' -> 'comment_parents'
        comment_lecRepository.save(comment_lec); //re-save
    }

    //Save(re-reply)
    public Comment_lec comment_lecReWrite(Comment_lec comment_lec, Integer lectureId,String username, Integer comment_id) {
        User user = userRepository.findByUsername(username);
        LocalDateTime dateTime = LocalDateTime.now();
        Lecture lecture = lectureRepository.findById(lectureId).get();
        Integer comment_num = comment_lecRepository.findByCommentParentsQuery(comment_id).orElse(0);

        comment_lec.setComment_num(comment_num + 1);
        comment_lec.setComment_parents(comment_id);
        comment_lec.setUser(user);
        comment_lec.setLecture(lecture);
        comment_lec.setCreated_date(dateTime);
        return comment_lecRepository.save(comment_lec);
    }

    //List
    public Page<Comment_lec> comment_lecList(Pageable pageable) {
        return comment_lecRepository.findByCommentList(pageable);
    }
}
