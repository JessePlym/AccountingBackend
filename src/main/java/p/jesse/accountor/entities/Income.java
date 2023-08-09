package p.jesse.accountor.entities;

import jakarta.persistence.Entity;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Income extends Payment {

    private String source;

    public Income(BigDecimal amount, String description, LocalDate createdAt, boolean isContinuous, Category category, User user, String source) {
        super(amount, description, createdAt, isContinuous, category, user);
        this.source = source;
    }

    public Income() {}

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

}
