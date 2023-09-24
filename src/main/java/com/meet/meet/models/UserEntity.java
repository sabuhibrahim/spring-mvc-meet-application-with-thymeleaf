package com.meet.meet.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String email;
    
    private String fullName;
    @Column(nullable = false)
    private String password;
    private String photo;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime joinedAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Relations

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
        name = "user_roles",
        joinColumns = {
            @JoinColumn(name = "user_id", referencedColumnName = "id")
        },
        inverseJoinColumns = {
            @JoinColumn(name = "role_id", referencedColumnName = "id")
        }
    )
    private List<Role> roles = new ArrayList<Role>();


    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
        name = "user_subscriptions",
        joinColumns = {
            @JoinColumn(name = "user_id", referencedColumnName = "id")
        },
        inverseJoinColumns = {
            @JoinColumn(name = "group_id", referencedColumnName = "id")
        }
    )
    private List<Group> subscriptions = new ArrayList<Group>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
        name = "user_participations",
        joinColumns = {
            @JoinColumn(name = "user_id", referencedColumnName = "id")
        },
        inverseJoinColumns = {
            @JoinColumn(name = "event_id", referencedColumnName = "id")
        }
    )
    private List<Event> participations = new ArrayList<Event>();

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Group> groups = new ArrayList<Group>();

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    private List<Notification> notifications = new ArrayList<Notification>();
}
