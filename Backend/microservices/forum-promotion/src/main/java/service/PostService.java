package service;

import dto.PostRequestDTO;
import dto.PostResponseDTO;

import java.util.List;

public interface PostService {
    PostResponseDTO createPost(PostRequestDTO dto);
    List<PostResponseDTO> getAllPosts();
    PostResponseDTO getPostById(String id);
    PostResponseDTO updatePost(String id, PostRequestDTO dto);
    void deletePost(String id);
}