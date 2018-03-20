package models.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import models.entities.Account;

public class TraderInfoMessage extends ApiMessage {

    @JsonProperty("traderId")
    private long traderId;

    @JsonProperty("account")
    private Account account;

    @JsonProperty("username")
    private String username;

    public TraderInfoMessage(long traderId, Account account, String username, boolean successful, String message){
        super(successful, message);
        this.traderId = traderId;
        this.account = account;
        this.username = username;
    }

}
