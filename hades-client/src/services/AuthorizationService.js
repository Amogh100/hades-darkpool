
import axios from "axios";

/** 
 * This service manages the JWT token state,
 * by providing functionality for logging in, signing up (thus updating the token state)
 * and providing some utility methods for user data validation.
*/
export default class AuthorizationService {

    constructor(){
        this.signUp = this.signUp.bind(this);
        this.validatePasswordSignUp = this.validatePasswordSignUp.bind(this);
        this.login = this.login.bind(this);
        this.validTokenExists = this.validTokenExists.bind(this);
    }

    /**
     * @param data: object containing a username and password field
     * Logs in the user, and if succesfull updates the JWT token, routes
     * to the home page.
     */
    login = function(data) {
        const username = data.username;
        const password = data.password;
        axios.post("http://localhost:8080/user/signIn", {"username": username, "password": password}).then(
            response => {
                this.updateToken(response.data.message);
                Promise.resolve(response);
            }
        )
        window.location.replace("/");
    }
    
    /**
     * Signs a new user up, and if succesfull updates the JWT token
     * and routes to the home page.
     */
    signUp = function(data){
        const password = data.password;
        const passwordConfirmation = data.passwordConfirmation;
        if(this.validatePasswordSignUp(password, passwordConfirmation)){
            axios
              .post("http://localhost:8080/user/signUp", data)
              .then(response => {
                this.updateToken(response.data.message);
                Promise.resolve(response);
                window.location.replace("/");
              });
          }
          else {
              alert("Passwords don't match up!");
          }

    }

    /**
     * Gets token from localStorage
     */
    getToken = function() {
        return localStorage["jwt"];
    }

    /**
     * Check if token exists.
     * ToDo: expiration, refresh etc.
     */
    validTokenExists = function(){
        const token = this.getToken();
        return token !== undefined && token !== null; 
    }


    /**
     * Utility for making sure passwords match neccessary requirements
     * ToDo: password length/char requirements.
     */
    validatePasswordSignUp = function(password, passwordConfirmation){
        return passwordConfirmation === password;
    }
    
    /**
     * Updates the "jwt" field in localStorage
     * to @param token.
     */
    updateToken = function(token) {
        localStorage.setItem("jwt", token);
    }

}