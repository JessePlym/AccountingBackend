package p.jesse.accountor.entities;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Expense extends Payment {

    @NotBlank
    private String receiver;

    public Expense(BigDecimal amount, String description, LocalDate createdAt, boolean isContinuous, Category category, String receiver, User user) {
        super(amount, description, createdAt, isContinuous, category, user);
        this.receiver = receiver;
    }

    public Expense() {}

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
}
