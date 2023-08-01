package p.jesse.accountor.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

import java.time.LocalDate;

@Entity
public class Expense extends Payment {

    @ManyToOne
    private User user;

    public Expense(double amount, String description, LocalDate createdAt, boolean isContinuous, Category category, User user) {
        super(amount, description, createdAt, isContinuous, category);
        this.user = user;
    }

    public Expense() {}

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
