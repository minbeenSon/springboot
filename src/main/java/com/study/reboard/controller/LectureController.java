package com.study.reboard.controller;

import com.study.reboard.entity.Comment_lec;
import com.study.reboard.entity.Lecture;
import com.study.reboard.service.Comment_lecService;
import com.study.reboard.service.LectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/lecture")
public class LectureController {

    @Autowired
    private LectureService lectureService;

    @Autowired
    private Comment_lecService comment_lecService;

    @GetMapping("/list_and_view/{lectureId}")
    @Transactional
    public String lectureListAndView(@PathVariable Integer lectureId, Model model, Pageable pageable, Authentication authentication, Comment_lec comment_lec, String comment) {
        //side-nav
        Page<Lecture> list = lectureService.lectureList(pageable);
        //main
        Lecture lecture = lectureService.lectureDetail(lectureId);
        //view_count
        lecture.setView_count(lecture.getView_count() + 1);
        //date_format
        LocalDateTime createdDateTime = lecture.getCreated_date();
        String created_date = createdDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime modifiedDateTime = lecture.getModified_date();
        String modified_date = modifiedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        //comment
        Page<Comment_lec> comments = comment_lecService.comment_lecList(pageable);

        model.addAttribute("list", list);
        model.addAttribute("lecture", lecture);
        model.addAttribute("created_date", created_date);
        model.addAttribute("modified_date", modified_date);
        model.addAttribute("comments", comments);
        return "/lecture/list_and_view";
    }
    @PostMapping("/list_and_view")
    public String lectureListAndViewComment(Integer lectureId, Authentication authentication, Comment_lec comment_lec,
                                            Integer comment_id) {
        String username = authentication.getName();

        if(comment_id == null) {
            comment_lecService.comment_lecWrite(comment_lec, lectureId, username);
        } else  {
            comment_lecService.comment_lecReWrite(comment_lec,lectureId,username,comment_id);
        }

        return "redirect:/lecture/list_and_view/" + lectureId;
    }

    @GetMapping("/write")
    public String lectureWrite() {
        return "/lecture/write";
    }

    @PostMapping("/write/process")
    public String lectureWriteProcess(Lecture lecture, Authentication authentication, Model model) {
        String username = authentication.getName();

        lectureService.lectureWrite(lecture,username);

        model.addAttribute("message", "강의작성완료");
        model.addAttribute("searchUrl", "/lecture/list_and_view/6");
        return "message";
    }

    @GetMapping("/update")
    public String lectureUpdate(Integer id, Model model) {
        Lecture lecture = lectureService.lectureDetail(id);

        model.addAttribute("lecture", lecture);
        return "/lecture/update";
    }
    @PostMapping("/update/process")
    public String lectureUpdateProcess(Lecture lecture, Integer id,Authentication authentication, Model model) {
        Lecture lectureTemp = lectureService.lectureDetail(id);
        lectureTemp.setTitle(lecture.getTitle());
        lectureTemp.setContent(lecture.getContent());
        String username = authentication.getName();

        lectureService.lectureWrite(lectureTemp, username);

        model.addAttribute("message","강의수정완료");
        model.addAttribute("searchUrl", "/lecture/list_and_view/" + id);
        return "message";
    }
}
