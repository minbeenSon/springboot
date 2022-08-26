package com.study.reboard.controller;

import com.study.reboard.entity.Map;
import com.study.reboard.service.MapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/travel")
public class TravelController {
    @Autowired
    private MapService mapService;

    @GetMapping()
    public String travel(Pageable pageable, Model model, Authentication authentication) {
        String username = authentication.getName();
        Page<Map> list = mapService.mapList(pageable, username);

        model.addAttribute("list", list);
        return "/travel/travel";
    }

    @PostMapping()
    public String travelSave(Map map, Authentication authentication) {
        String username = authentication.getName();

        mapService.mapSave(map,username);
        return "redirect:/travel";
    }
}
