package my.notinhas.project.services.impl;

import my.notinhas.project.dtos.PostDTO;
import my.notinhas.project.entities.Posts;
import my.notinhas.project.services.IPostService;
import org.springframework.stereotype.Service;

@Service
public class PostService implements IPostService {

    @Override
    public Posts saveUsers(PostDTO postDTO) {
        return null;
    }
}