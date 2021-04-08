package pl.coderslab.charity.user;

import lombok.Getter;
import lombok.Setter;
import pl.coderslab.charity.model.Donation;
import pl.coderslab.charity.validation.EmailUnique;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;
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
    private String password;
    private int enabled;
    private String firstName;
    private String lastName;

    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @OneToMany
    private List<Donation> donation;
}
