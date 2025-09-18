package com.project.TaskManger.security.user;

import jakarta.persistence.*;

import java.util.Collection;
import java.util.List;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user",uniqueConstraints = @UniqueConstraint(name = "user_unique_email",columnNames = "email"))
public class User implements UserDetails {

  @Id
  @GeneratedValue
  @Column(name = "id")
  private Integer id;

  @Column(name = "first_name")
  private String firstname;
  private String lastname;
  @Column(name = "email")
  @Email
  private String email;
  private String password;

  @Enumerated(EnumType.STRING)
  private Role role;

  public User(String firstname, String lastname, String email, String password, Role role) {
    this.firstname = firstname;
    this.lastname = lastname;
    this.email = email;
    this.password = password;
    this.role = role;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return role.getAuthority();
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
