package pl.kubaf2k.langschoolappspring.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import pl.kubaf2k.langschoolappspring.repositories.UserRepository;

public class LangschoolUserDetailsService implements UserDetailsService {
    @Autowired private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByName(username);
        if (user.isEmpty()) throw new UsernameNotFoundException(username);
        return new LangschoolUserDetails(user.get(), user.get().getRoles());
    }
}
