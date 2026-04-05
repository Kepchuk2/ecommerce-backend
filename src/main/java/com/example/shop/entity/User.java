package com.example.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
    @SequenceGenerator(name = "users_seq", sequenceName = "users_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Cart cart;

    @OneToMany(mappedBy = "user")
    private List<Order> orders = new ArrayList<>();

    public  User(String email, String password, Role role) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email must not be blank");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password must not be blank");
        }
        if (role == null) {
            throw new IllegalArgumentException("Role must not be null");
        }

        this.email = email;
        this.password = password;
        this.role = role;
    }

    public void changePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password must not be blank");
        }
        this.password = password;
    }

    public void changeRole(Role role) {
        if (role == null) {
            throw new IllegalArgumentException("Role must not be null");
        }
        this.role = role;
    }

    public void assignCart(Cart cart) {
        this.cart = cart;
        cart.assignUser(this);
    }

    public void addOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order must not be null");
        }

        orders.add(order);
        order.assignUser(this);
    }
}
