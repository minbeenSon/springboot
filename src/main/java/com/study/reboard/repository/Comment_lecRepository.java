package com.study.reboard.repository;

import com.study.reboard.entity.Comment_lec;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Comment_lecRepository extends JpaRepository<Comment_lec, Integer> {
    @Query("select MAX(c.comment_num) from Comment_lec c where c.comment_parents = ?1")
    Optional<Integer> findByCommentParentsQuery(Integer comment_parents);

    @Query("select c from Comment_lec c order by comment_parents, comment_num")
    Page<Comment_lec> findByCommentList(Pageable pageable);
}
