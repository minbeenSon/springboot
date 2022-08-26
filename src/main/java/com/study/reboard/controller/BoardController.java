package com.study.reboard.controller;

import com.study.reboard.entity.Board;
import com.study.reboard.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping("/list")
    public String boardList(Model model, @PageableDefault(page = 0, size = 4, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                            String searchType, String searchKeyword) {
        Page<Board> list = null;

        if(searchKeyword == "" || searchKeyword == null) {
            list = boardService.boardList(pageable);
        } else{
            if(searchType.equals("제목")) {
                list = boardService.boardSearchTitleList(pageable, searchKeyword);
            } else if(searchType.equals("내용")) {
                list = boardService.boardSearchContentList(pageable, searchKeyword);
            } else if(searchType.equals("제목+내용")) {
                list = boardService.boardSearchTitleAndContentList(pageable, searchKeyword, searchKeyword);
            }
        }

        int nowPage = list.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 5, 1);
        int endPage = Math.min(nowPage + 5, list.getTotalPages());

        model.addAttribute("list", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "/board/list";
    }

    @GetMapping("/write")
    public String boardWrite() {
        return "/board/write";
    }

    @PostMapping("/write/process")
    public String boardWriteProcess(Board board, Model model, Authentication authentication) {
        String username = authentication.getName();

        boardService.boardWrite(board, username);

        model.addAttribute("message", "게시물작성완료");
        model.addAttribute("searchUrl", "/board/list?id=6");
        return "message";
    }

    @GetMapping("/detail")
    public String boardDetail(Model model, Integer id, Authentication authentication) {
        String username = authentication.getName();

        model.addAttribute("board", boardService.boardDetail(id));
        model.addAttribute("username", username);
        return "/board/detail";
    }

    @GetMapping("/delete")
    public String boardDelete(Integer id, Model model) {
        boardService.boardDelete(id);

        model.addAttribute("message", "삭제하시겠습니까?");
        model.addAttribute("searchUrl", "/board/list");
        return "message2";
    }

    @GetMapping("/update")
    public String boardUpdate(Integer id, Model model) {
        model.addAttribute("board", boardService.boardDetail(id));
        return "/board/update";
    }

    @PostMapping("/update/process")
    public String boardUpdateProcess(Integer id, Board board, Model model, Authentication authentication) {
        Board boardTemp = boardService.boardDetail(id);
        boardTemp.setTitle(board.getTitle());
        boardTemp.setContent(board.getContent());
        String username = authentication.getName();

        boardService.boardWrite(boardTemp, username);

        model.addAttribute("message", "게시물수정완료");
        model.addAttribute("searchUrl", "/board/detail?id=" + id);
        return "message";
    }
}
