package com.restaurent.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.Data;

@NamedQuery(
		name = "User.findByEmailId", 
		query = "SELECT u FROM User u WHERE u.email = :email"
		)

@NamedQuery(
		name = "User.getAllUser",
		query = "SELECT new com.restaurent.wrapper.UserWrapper(u.id, u.name, u.phone, u.email, u.status) FROM User u WHERE u.role = 'user'"
		)


@NamedQuery(
	    name = "User.updateStatus",
	    query = "UPDATE User u SET u.status = :status WHERE u.id = :id"
	)

//JavaMailSender
//@NamedQuery(
//		name = "User.getAllAdmin",
//		query = "SELECT u.email FROM User u WHERE u.role = 'admin'"
//		)


@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "user")
public class User implements Serializable, UserDetails {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "role", nullable = false)
    private String role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !status.equalsIgnoreCase("expired");
    }

    @Override
    public boolean isAccountNonLocked() {
        return !status.equalsIgnoreCase("locked");
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !status.equalsIgnoreCase("credentials_expired");
    }

    @Override
    public boolean isEnabled() {
        return status.equalsIgnoreCase("active");
    }
}
