package application.core.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import application.core.models.Trader;
import application.core.repositories.TraderRepository;

@RestController
@RequestMapping("/users")
public class TraderController {

    @Autowired
    private TraderRepository traderRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public TraderController(TraderRepository traderRepository,
                          BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.traderRepository = traderRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/sign-up")
    public void signUp(@RequestBody Trader newTrader) {
        newTrader.setPassword(bCryptPasswordEncoder.encode(newTrader.getPassword()));
        traderRepository.save(newTrader);
    }
}