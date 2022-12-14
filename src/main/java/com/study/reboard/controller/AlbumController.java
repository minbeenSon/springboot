package com.study.reboard.controller;

import com.study.reboard.entity.Album;
import com.study.reboard.service.AlbumService;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/album")
public class AlbumController {

    @Autowired
    private AlbumService albumService;

    @GetMapping("/list")
    public String albumList(@PageableDefault(page = 0, size = 6, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                            Authentication authentication, Model model) {
        Page<Album> list = albumService.albumList(pageable);
        String username = authentication.getName();

        int nowPage = list.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage -5, 1);
        int endPage = Math.min(list.getTotalPages(),nowPage + 5);

        model.addAttribute("list", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("username", username);
        return "/album/list";
    }

    @GetMapping("/write")
    public String albumWrite() {
        return "/album/write";
    }

    @PostMapping("/write/process")
    public String albumWriteProcess(Album album, MultipartFile file, Authentication authentication, Model model) throws Exception {
        String username = authentication.getName();
        albumService.albumWrite(album, username, file);

        model.addAttribute("message", "??????????????????");
        model.addAttribute("searchUrl", "/album/list");
        return "message";
    }
    //????????????
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> albumDownload(@PathVariable("id") Integer id) throws IOException {
        Album albumTemp = albumService.albumDetail(id);
        String albumPath = "C:/study/reboard/src/main/resources/static/album/";
        Path path = Paths.get(albumPath + albumTemp.getFilename());

        Resource resource = new InputStreamResource(Files.newInputStream(path));

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + path.getFileName() + "\"")
                .body(resource);
    }

    //??????
    @GetMapping("/delete")
    public String albumDelete(Integer id, Model model) {
        albumService.albumDelete(id);

        model.addAttribute("message", "??????????????????");
        model.addAttribute("searchUrl", "/album/list");
        return "message";
    }
}
