package application.core.controllers;

import application.core.repositories.TraderRepository;
import application.core.security.TokenManager;
import application.core.services.TraderDetailsService;
import jdk.nashorn.internal.objects.AccessorPropertyDescriptor;
import models.entities.Trader;
import models.messages.ApiMessage;
import models.messages.SignInRequest;
import models.messages.TraderInfoMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class TraderController {

    @Autowired
    private TraderDetailsService traderDetailsService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private TraderRepository traderRepository;

    @PostMapping(value = "/user/signIn")
    public ResponseEntity<ApiMessage> signIn(@RequestBody SignInRequest signInRequest){
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword());
        Authentication auth = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(auth);
        UserDetails details = traderDetailsService.loadUserByUsername(signInRequest.getUsername());
        String tokenString = tokenManager.createToken(details);
        Trader t = traderRepository.findByUsername(signInRequest.getUsername());
        return ResponseEntity.ok(new TraderInfoMessage(t.getId(), t.getAccount(),
                t.getUsername(),true,
                tokenString));    }

    @PostMapping(value = "/user/signUp")
    public ResponseEntity<ApiMessage> signUp(@RequestBody Trader trader) throws AuthenticationException {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(trader.getPassword());
        Trader newTrader = new Trader(trader.getUsername(), hashedPassword);
        try {
            traderDetailsService.save(newTrader);
            return ResponseEntity.ok(new TraderInfoMessage(newTrader.getId(), newTrader.getAccount(),
                    newTrader.getUsername(),true,
                                                            "User Succesfully Registered!"));
        } catch (Exception e){
            return ResponseEntity.ok(new ApiMessage(false, "Username already exists!"));
        }
    }
}
