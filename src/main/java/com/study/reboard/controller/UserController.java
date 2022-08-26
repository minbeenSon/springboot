package com.study.reboard.controller;

import com.study.reboard.entity.User;
import com.study.reboard.repository.UserRepository;
import com.study.reboard.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/account")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/join")
    public String userJoin() {
        return "/account/join";
    }

    @PostMapping("/join/process")
    public String userJoinProcess(User user, Model model) {
        List<User> foundAll = userRepository.findAll();
        for(User all : foundAll) {
            //아이디중복확인
            if(user.getUsername().equals(all.getUsername())) {
                model.addAttribute("message", "중복된 아이디입니다.");
                model.addAttribute("searchUrl", "/account/join");
                return "message";
            }
        }
        userService.userJoin(user);
        model.addAttribute("message", "회원가입성공\n재로그인하세요.");
        model.addAttribute("searchUrl", "/");
        return "message";
    }

    @GetMapping("/login")
    public String userLogin() {return "/account/login";}

    @GetMapping("/management")
    public String userManagement(Model model, @PageableDefault(page = 0, size = 4, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                 String searchKeyword, String authorityType) {
        Page<User> list = null;

        if(searchKeyword == "" || searchKeyword == null) {
            list = userService.userList(pageable);
        } else {
            list = userService.userSearchList(pageable, searchKeyword);
        }

        int nowPage = list.getPageable().getPageNumber() + 1;
        int startPage = Math.max(1, nowPage - 5);
        int endPage = Math.min(nowPage + 5, list.getTotalPages());

        model.addAttribute("list", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "/account/management";
    }
    //권한변경
    @PostMapping("/management/authority")
    public String userAuthority(Integer id, String authorityType, Model model) {
        User userTemp = userService.userDetailById(id);

        userService.userAuthority(userTemp, authorityType, id);

        model.addAttribute("message", "권한변경완료");
        model.addAttribute("searchUrl", "/account/management");
        return "message";
    }

    @GetMapping("/delete")
    public String userDelete(Integer id, Model model) {
        userService.userDelete(id);

        model.addAttribute("searchUrl", "/account/management");
        return "message";
    }

    @GetMapping("/changePassword1")
    public String userChangePassword1(Model model, Authentication authentication) {
        String username = authentication.getName();

        model.addAttribute("username",username);
        return "/account/changePassword1";
    }
    @PostMapping("/changePassword1/process")
    public String userChangePassword1Process(Model model, String username, User user, Authentication authentication) {
        String nowUsername = authentication.getName();

        model.addAttribute("nowUsername", nowUsername);

        User userTemp = userService.useDetailByUsername(username);
        boolean confirm = passwordEncoder.matches(user.getPassword(), userTemp.getPassword());
        if(confirm) {
            return "/account/changePassword2";
        } else {
            model.addAttribute("message","본인인증에 실패했습니다.");
            model.addAttribute("searchUrl", "/");
            return "message";
        }
    }

    @GetMapping("/changePassword2")
    public String userChangePassword2() {
        return "/account/changePassword2";
    }

    @PostMapping("/changePassword2/process")
    public String userChangePassword2Process(Model model, String username, User user) {
        User userTemp = userService.useDetailByUsername(username);
        userTemp.setPassword(user.getPassword());

        userService.userPasswordUpdate(userTemp);

        model.addAttribute("message", "비밀번호변경완료\n재로그인하세요.");
        model.addAttribute("searchUrl", "/account/login?logout");
        return "message";
    }

}
