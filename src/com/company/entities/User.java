package com.company.entities;

import com.company.enums.Gender;

import java.math.BigDecimal;
import java.util.List;

public class User {

    private Long id;
    private Integer age;
    private String name;
    private Gender gender;

    private BigDecimal salary;
    private List<Loan> loans;

    public User(Long id, Integer age, String name, Gender gender, BigDecimal salary, List<Loan> loans) {
        this.id = id;
        this.age = age;
        this.name = name;
        this.gender = gender;
        this.salary = salary;
        this.loans = loans;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public List<Loan> getLoans() {
        return loans;
    }

    public void setLoans(List<Loan> loans) {
        this.loans = loans;
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", age=" + age +
                ", name='" + name + '\'' +
                ", gender=" + gender +
                ", salary=" + salary +
                ", \nloans=" + loans;
    }
}
