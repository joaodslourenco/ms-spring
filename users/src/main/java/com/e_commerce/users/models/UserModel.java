package com.e_commerce.users.models;

import com.e_commerce.users.enums.ERole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_users")
@EqualsAndHashCode(of = "id")
public class UserModel implements Serializable, UserDetails {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private ERole role;

    @OneToOne
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private AddressModel address;

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == ERole.ADMIN) {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        }
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }


}
