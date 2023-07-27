package p.jesse.accountor.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

import java.time.LocalDate;

@Entity
public class Expense extends Transaction {

    @ManyToOne
    private User user;

    public Expense(double amount, String description, LocalDate createdAt, Category category, User user) {
        super(amount, description, createdAt, category);
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
