package ru.study.shop.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(strategy =  GenerationType.AUTO)
    private Long id;

    private String firstName;
    private String lastName;
    private String login;
    private String email;

    public Customer() {
    }

    public Customer(String firstName, String lastName, String login, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.login = login;
        this.email = email;
    }
}
