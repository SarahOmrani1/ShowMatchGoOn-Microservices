package com.exemple.forumpromotion.service.impI;

import com.exemple.forumpromotion.dto.PostRequestDTO;
import com.exemple.forumpromotion.dto.PostResponseDTO;
import com.exemple.forumpromotion.entity.Post;
import com.exemple.forumpromotion.repository.PostRepository;
import com.exemple.forumpromotion.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository repository;

    @Override
    public PostResponseDTO createPost(PostRequestDTO dto) {
        Post p = new Post();
        BeanUtils.copyProperties(dto, p);
        p.setDatePublication(new Date());
        return toDto(repository.save(p));
    }

    @Override
    public List<PostResponseDTO> getAllPosts() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public PostResponseDTO getPostById(String id) {
        return toDto(find(id));
    }

    @Override
    public PostResponseDTO updatePost(String id, PostRequestDTO dto) {
        Post p = find(id);
        BeanUtils.copyProperties(dto, p, "id", "datePublication");
        return toDto(repository.save(p));
    }

    @Override
    public void deletePost(String id) {
        repository.delete(find(id));
    }

    private Post find(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post introuvable : " + id));
    }

    private PostResponseDTO toDto(Post p) {
        PostResponseDTO dto = new PostResponseDTO();
        BeanUtils.copyProperties(p, dto);
        return dto;
    }
}