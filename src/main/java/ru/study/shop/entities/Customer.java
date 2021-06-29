package ru.study.shop.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "customer")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Customer {
    @Id
    @GeneratedValue(strategy =  GenerationType.AUTO)
    private Long id;

    private String firstName;
    private String lastName;
    private String login;
    private String email;

    public Customer(String firstName, String lastName, String login, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.login = login;
        this.email = email;
    }
}
