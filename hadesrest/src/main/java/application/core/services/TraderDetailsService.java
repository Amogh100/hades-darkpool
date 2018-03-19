package application.core.services;

import application.core.repositories.TraderRepository;
import models.entities.Trader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class TraderDetailsService implements UserDetailsService{

    @Autowired
    private TraderRepository traderRepository;

    public TraderDetailsService(TraderRepository traderRepository) {
        this.traderRepository = traderRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Trader t = traderRepository.findByUsername(username);
        if(t == null){
            throw new UsernameNotFoundException(username);
        }
        return new User(t.getUsername(), t.getPassword(), new HashSet<>());
    }
}
