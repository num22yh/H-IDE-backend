package org.example.backend.chat;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.backend.login.User;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name="massages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name="user_id",nullable = false)
    private User user;
    public Message() {
        this.timestamp = LocalDateTime.now();
    }
}