import React, { Component } from 'react';
import ReactSignupLoginComponent from 'react-signup-login-component';
import AuthorizationService from '../services/AuthorizationService';

/**
 * Login uses the ReactSignUpLoginComponent which is a form for
 * entering sign in/up functionality along with recoverPassword functionality.
 */

class Login extends Component {
    constructor(props){
        super(props);
        this.auth = new AuthorizationService();
    }

    componentWillMount(){
        /**
         * If there is already a valid token route to the main app
         */
        if(this.auth.validTokenExists()){
            window.location.replace("/");
        }
    }

    render() {
        return (
         <div>
            <ReactSignupLoginComponent
                title="Hades Darkpool Login"
                handleSignup={this.auth.signUp}
                handleLogin={this.auth.login}
                handleRecoverPassword={this.auth.recoverPassword}
            />
        </div>
        )
    }
}

export default Login;