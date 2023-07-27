package p.jesse.accountor.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

import java.time.LocalDate;

@Entity
public class Income extends Transaction {

    private String source;

    @ManyToOne
    private User user;

    public Income(double amount, String description, LocalDate createdAt, Category category, String source, User user) {
        super(amount, description, createdAt, category);
        this.source = source;
        this.user = user;
    }

    public Income() {}

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
