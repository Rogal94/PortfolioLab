package pl.coderslab.charity.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashSet;
import java.util.Set;

public class SpringDataUserDetailsService implements UserDetailsService {

    private UserService userService;

    @Autowired
    public void setUserRepository(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userService.findByEmail(username);

        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        boolean credentialsNonExpired = true;

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        user.getRoles().forEach(r -> grantedAuthorities.add(new SimpleGrantedAuthority(r.getName())));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), user.isEnabled(), user.isAccountNonExpired(),
                credentialsNonExpired, user.isAccountNonLocked(), grantedAuthorities);
    }
}
