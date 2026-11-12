package com.exemple.forumpromotion.service.impI;
import com.exemple.forumpromotion.dto.CommentaireRequestDTO;
import com.exemple.forumpromotion.dto.CommentaireResponseDTO;
import com.exemple.forumpromotion.entity.Commentaire;
import com.exemple.forumpromotion.repository.CommentaireRepository;
import com.exemple.forumpromotion.service.CommentaireService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentaireServiceImpl implements CommentaireService {

    private final CommentaireRepository repository;

    @Override
    public CommentaireResponseDTO create(CommentaireRequestDTO dto) {
        Commentaire c = new Commentaire();
        BeanUtils.copyProperties(dto, c);
        c.setDateCommentaire(new Date());
        return toDto(repository.save(c));
    }

    @Override
    public List<CommentaireResponseDTO> getAll() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public CommentaireResponseDTO getById(String id) {
        return toDto(find(id));
    }

    @Override
    public CommentaireResponseDTO update(String id, CommentaireRequestDTO dto) {
        Commentaire c = find(id);
        BeanUtils.copyProperties(dto, c, "id", "dateCommentaire");
        return toDto(repository.save(c));
    }

    @Override
    public void delete(String id) {
        repository.delete(find(id));
    }

    private Commentaire find(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commentaire introuvable : " + id));
    }

    private CommentaireResponseDTO toDto(Commentaire c) {
        CommentaireResponseDTO dto = new CommentaireResponseDTO();
        BeanUtils.copyProperties(c, dto);
        return dto;
    }
}