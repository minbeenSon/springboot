package com.study.reboard.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Lecture extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String title;
    String content;
    @ColumnDefault("0")
    Integer view_count;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

}
