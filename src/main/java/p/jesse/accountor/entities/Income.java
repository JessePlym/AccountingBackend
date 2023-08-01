package p.jesse.accountor.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Income extends Payment {

    private String source;

    @ManyToOne
    private User user;

    public Income(BigDecimal amount, String description, LocalDate createdAt, boolean isContinuous, Category category, String source, User user) {
        super(amount, description, createdAt, isContinuous, category);
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
