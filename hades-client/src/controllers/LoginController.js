import axios from "axios";

function LoginController() {}


LoginController.signUp = function(data) {
    if(LoginController.validatePasswordSignUp(data)){
      axios
        .post("http://localhost:8080/user/signUp", data)
        .then(response => LoginController.signUpHandler(response.data));
    }
    else {
        alert("Passwords don't match up!");
    }
}


LoginController.login = function(data){
    axios.post("http://localhost:8080/user/signIn", data)
         .then(response => LoginController.loginHandler(response.data));
}

LoginController.validatePasswordSignUp = function(data) {
    return data.password == data.passwordConfirmation;
}

LoginController.signUpHandler = function(responseData) {
    if(responseData.success !== true){
        alert("Something went wrong!" + responseData.message);
    }
    else {
        //Set jwt token
        console.log("Setting jwt token!");
        sessionStorage.setItem("jwt", responseData.message);
        //Route
    }
}
// LoginController.loginHandler = function(responseData) {
//     if(responseData.success !== true){
//         alert("Something went wrong!" + responseData.message);
//     }
//     else {
//         console.log("Setting jwt token!");
//         sessionStorage.setItem("jwt", responseData.message);
//         return new UserDetails()
//     }
// }


export default LoginController;