package com.exemple.forumpromotion.service;

import com.exemple.forumpromotion.dto.CommentaireRequestDTO;
import com.exemple.forumpromotion.dto.CommentaireResponseDTO;

import java.util.List;

public interface CommentaireService {
    CommentaireResponseDTO create(CommentaireRequestDTO dto);
    List<CommentaireResponseDTO> getAll();
    CommentaireResponseDTO getById(String id);
    CommentaireResponseDTO update(String id, CommentaireRequestDTO dto);
    void delete(String id);
}