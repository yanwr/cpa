import React, { useState } from 'react';
import { connect } from 'react-redux';
import { useHistory } from 'react-router-dom';
import { doLogin } from '../UserPage/User.reducer';
import HeaderComponent from '../../components/Header';
import FooterComponent from '../../components/Footer';
import SumbitButtonComponent from '../../components/Buttons/SubmitButton';
import { toastError } from '../../components/Toast';
import './style.css';

function LoginPage(props) {
    const navigation = useHistory();
    const email = useHandleFormInput('');
    const password = useHandleFormInput('');
    const { loading, doLogin } = props;

    function handleDoLogin(event) {
        event.preventDefault();
        if(email.value === '' && password.value === ''){
            toastError('Campos vazio !');
        } else {
            doLogin(email.value, password.value, navigation);
        }
    };

    return(
        <div className="container-login">
            <HeaderComponent/>
            <main>
                <h1>LOGIN</h1>
                <form onSubmit={handleDoLogin}>
                    <p>Email</p>
                    <input 
                        {...email}
                        type="text"
                        placeholder="Insira seu email"
                    />
                    <p>Senha</p>
                    <input 
                        {...password}
                        type="password"
                        placeholder="Insira sua senha"
                    />
                    <SumbitButtonComponent 
                        title={"Logar"}
                        loading={loading} 
                        disabled={loading}
                    />
                </form>
            </main>
            <FooterComponent />
        </div>
    );
};

function useHandleFormInput(initialValue) {
    const [value, setValue] = useState(initialValue);
    function handleOnChange(value) {
        setValue(value.target.value);
    };
    return {
        value,
        onChange: handleOnChange,
    };
};

const mapStateToProps = ({ userState }) => ({
    loading: userState.loading,
});

const mapDispatchToProps = {
    doLogin,
};

export default connect(mapStateToProps, mapDispatchToProps)(LoginPage);