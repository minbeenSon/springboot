package com.study.reboard.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Comment_lec {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String comment;
    LocalDateTime created_date;
    Integer comment_num;
    Integer comment_parents;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @JoinColumn(name = "lecture_id")
    Lecture lecture;
}
