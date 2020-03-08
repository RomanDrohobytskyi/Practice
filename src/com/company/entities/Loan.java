package com.company.entities;

import java.math.BigDecimal;
import java.util.Date;

public class Loan {

    private Long id;
    private Date start;
    private Date due;
    private BigDecimal amount;
    private BigDecimal balance;

    private Bank bank;

    public Loan(Long id, Date start, Date due, BigDecimal amount, BigDecimal balance, Bank bank) {
        this.id = id;
        this.start = start;
        this.due = due;
        this.amount = amount;
        this.balance = balance;
        this.bank = bank;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getDue() {
        return due;
    }

    public void setDue(Date due) {
        this.due = due;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    @Override
    public String toString() {
        return id +
                ", star=" + start +
                ", due=" + due +
                ", amount=" + amount +
                ", balance=" + balance +
                ", \nbank=" + bank;
    }
}
