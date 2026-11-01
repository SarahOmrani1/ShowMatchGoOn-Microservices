package service;

import dto.CommentaireRequestDTO;
import dto.CommentaireResponseDTO;

import java.util.List;

public interface CommentaireService {
    CommentaireResponseDTO create(CommentaireRequestDTO dto);
    List<CommentaireResponseDTO> getAll();
    CommentaireResponseDTO getById(String id);
    CommentaireResponseDTO update(String id, CommentaireRequestDTO dto);
    void delete(String id);
}