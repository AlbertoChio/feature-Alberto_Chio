package com.example.aplazo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	private UUID id;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(nullable = false)
	private String role;

	@OneToOne
	@JoinColumn(name = "customer_id", referencedColumnName = "id")
	private Customer customer;

	@Column(name = "created_at", nullable = false, updatable = false)
	private final Instant createdAt = Instant.now();

	@Builder.Default
	@Column(name = "updated_at", nullable = false)
	private Instant updatedAt = Instant.now();

	@PreUpdate
	public void preUpdate() {
		this.updatedAt = Instant.now();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
		List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		authorities.add(authority);
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return email;
	}
}