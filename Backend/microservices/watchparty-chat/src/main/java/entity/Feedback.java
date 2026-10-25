package entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "feedbacks")
public class Feedback {

    @Id
    private String id;
    private int note;
    private String commentaire;
    private Date dateFeedback;
    private String clientId;
    private String watchPartyId;

    public Feedback() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public int getNote() { return note; }
    public void setNote(int note) { this.note = note; }

    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }

    public Date getDateFeedback() { return dateFeedback; }
    public void setDateFeedback(Date dateFeedback) { this.dateFeedback = dateFeedback; }

    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }

    public String getWatchPartyId() { return watchPartyId; }
    public void setWatchPartyId(String watchPartyId) { this.watchPartyId = watchPartyId; }
}