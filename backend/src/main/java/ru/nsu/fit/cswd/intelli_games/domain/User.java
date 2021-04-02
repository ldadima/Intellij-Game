package ru.nsu.fit.cswd.intelli_games.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.nsu.fit.cswd.intelli_games.model.UserRole;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Collection;
import java.util.Collections;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_user")
public class User implements UserDetails {
    @Id
    @SequenceGenerator(name = "seq", sequenceName = "user_seq", initialValue = 2)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @Column(name ="id")
    private Long id;
    @Column(name ="login", unique = true)
    private String login;
    @Column(name ="c_name")
    private String name;
    @Column(name ="hash_pass")
    private String password;
    @Column(name ="question")
    private String question;
    @Column(name ="answer")
    private String answer;
    @Column(name ="c_role")
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "team_id")
    private Team team;

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
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
