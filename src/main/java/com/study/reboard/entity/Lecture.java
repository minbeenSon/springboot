package com.study.reboard.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Lecture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @CreatedDate
    LocalDateTime created_date;
    LocalDateTime modified_date;
    String title;
    String content;
    @ColumnDefault("0")
    Integer view_count;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

}
