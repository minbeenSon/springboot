package com.study.reboard.service;

import com.study.reboard.entity.Board;
import com.study.reboard.entity.User;
import com.study.reboard.repository.BoardRepository;
import com.study.reboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BoardService {
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private UserRepository userRepository;

    //Save
    public Board boardWrite(Board board, String username) {
        User user = userRepository.findByUsername(username);
        board.setUser(user);
        return boardRepository.save(board);
    }

    //List
    public Page<Board> boardList(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    //ListBySearchedTitle
    public Page<Board> boardSearchTitleList(Pageable pageable, String title) {
        return boardRepository.findByTitleContaining(pageable, title);
    }
    //ListBySearchedContent
    public Page<Board> boardSearchContentList(Pageable pageable, String content) {
        return boardRepository.findByContentContaining(pageable, content);
    }
    //ListBySearchedTitleAndContent
    public Page<Board> boardSearchTitleAndContentList(Pageable pageable, String title, String content) {
        return boardRepository.findByTitleOrContentContaining(pageable, title, content);
    }

    //View
    public Board boardDetail(Integer id) {
        return boardRepository.findById(id).get();
    }

    //Delete
    public void boardDelete(Integer id) {
        boardRepository.deleteById(id);
    }
}
