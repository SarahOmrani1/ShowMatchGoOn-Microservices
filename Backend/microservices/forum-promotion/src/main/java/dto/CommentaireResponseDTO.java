package dto;

import lombok.Data;

import java.util.Date;

@Data
public class CommentaireResponseDTO {
    private String id;
    private String contenu;
    private String postId;
    private String authorUsername;
    private Date dateCommentaire;
}