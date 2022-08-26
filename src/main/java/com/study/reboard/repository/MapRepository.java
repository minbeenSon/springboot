package com.study.reboard.repository;

import com.study.reboard.entity.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MapRepository extends JpaRepository<Map, Integer> {

    @Query("select m from Map m where user_id = (select u.id from User u where u.username = ?1)")
    Page<Map> findByUsernameQuery(String username, Pageable pageable);
}
