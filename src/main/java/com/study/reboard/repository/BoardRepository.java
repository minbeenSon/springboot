package com.study.reboard.repository;

import com.study.reboard.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {
    Page<Board> findByTitleContaining(Pageable pageable, String title);
    Page<Board> findByContentContaining(Pageable pageable, String content);
    Page<Board> findByTitleOrContentContaining(Pageable pageable, String title, String content);
}

