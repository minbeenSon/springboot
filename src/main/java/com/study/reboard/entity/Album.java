package com.study.reboard.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String title;
    String filename;
    String filetype;
    String filepath;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;


}
