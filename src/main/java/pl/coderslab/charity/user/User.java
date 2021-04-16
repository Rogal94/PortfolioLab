package pl.coderslab.charity.user;

import lombok.Getter;
import lombok.Setter;
import pl.coderslab.charity.validation.EmailUnique;
import pl.coderslab.charity.validation.ValidPassword;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 60)
    @NotBlank(message = "CAN NOT BE EMPTY!")
    @EmailUnique(message = "EMAIL IS ALREADY EXIST!")
    private String email;
    @NotBlank(message = "CAN NOT BE EMPTY!")
    @ValidPassword(message = "PASSWORD: min 8 chars, 1 uppercase letter, 1 lowercase letter, 1 special char, 1 number!")
    private String password;
    private boolean enabled;
    private boolean accountNonLocked;
    private boolean accountNonExpired;
    private String firstName;
    private String lastName;

    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;
}
