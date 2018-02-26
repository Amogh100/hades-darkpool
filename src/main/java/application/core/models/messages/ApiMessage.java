package application.core.models.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApiMessage {

    @JsonProperty("success")
    private boolean success;
    @JsonProperty("message")
    private String message;

    public ApiMessage(boolean success, String message){
        this.success = success;
        this.message = message;
    }

    public String getMessage(){
        return message;
    }

    public void setMessage(String newMessage){
        this.message = newMessage;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean getSuccess(){
        return this.success;
    }

}
