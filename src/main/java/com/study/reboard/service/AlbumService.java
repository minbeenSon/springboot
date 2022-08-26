package com.study.reboard.service;

import com.study.reboard.entity.Album;
import com.study.reboard.entity.User;
import com.study.reboard.repository.AlbumRepository;
import com.study.reboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Service
public class AlbumService {

    @Autowired
    private AlbumRepository albumRepository;
    @Autowired
    private UserRepository userRepository;

    //List
    public Page<Album> albumList(Pageable pageable) {
        return albumRepository.findAll(pageable);
    }

    //Save
    public void albumWrite(Album album, String username, MultipartFile file) throws Exception {
        User user = userRepository.findByUsername(username);

        String albumPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\album";
        UUID uuid = UUID.randomUUID(); //식별자
        String fileName = uuid + "_" + file.getOriginalFilename();

        File saveFile = new File(albumPath, fileName);
        file.transferTo(saveFile);

        album.setUser(user);
        album.setFilename(fileName);
        album.setFiletype(file.getContentType());
        album.setFilepath("/album/" + fileName);

        albumRepository.save(album);
    }
    //View
    public Album albumDetail(Integer id) {
        return albumRepository.findById(id).get();
    }

    //Delete
    public void albumDelete(Integer id) {
        albumRepository.deleteById(id);
    }
}
