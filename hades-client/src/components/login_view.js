import React, { Component } from 'react';
import ReactSignupLoginComponent from 'react-signup-login-component';
import LoginController from '../controllers/LoginController';

const LoginView = (props) => {
    const loginWasClickedCallback = (data) => {
      console.log(data);
      alert('Login callback, see log on the console to see the data.');
    };
    const recoverPasswordWasClickedCallback = (data) => {
      console.log(data);
      alert('Recover password callback, see log on the console to see the data.');
    };

    return (
        <div>
            <ReactSignupLoginComponent
                title="Hades Darkpool Login"
                handleSignup={LoginController.signUp}
                handleLogin={loginWasClickedCallback}
                handleRecoverPassword={recoverPasswordWasClickedCallback}
            />
        </div>
    );
};
 
export default LoginView;