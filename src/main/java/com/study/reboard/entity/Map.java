package com.study.reboard.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Map {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    Double lat;
    Double lng;
    String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
}
