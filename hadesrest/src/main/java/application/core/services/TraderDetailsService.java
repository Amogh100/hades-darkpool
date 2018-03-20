package application.core.services;

import application.core.repositories.TraderRepository;
import models.entities.Trader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service("traderDetailsService")
public class TraderDetailsService implements UserDetailsService{

    @Autowired
    private TraderRepository traderRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Trader t = traderRepository.findByUsername(username);
        if(t == null){
            throw new UsernameNotFoundException(username);
        }
        return new User(t.getUsername(), t.getPassword(), new HashSet<>());
    }

    public void save(Trader newTrader) {
        if(traderRepository.findByUsername(newTrader.getUsername()) != null){
            throw new IllegalArgumentException("Duplicate username");
        }
        traderRepository.save(newTrader);
    }

}
