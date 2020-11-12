import React, { useState, useEffect } from 'react';
import { useHistory, useLocation } from 'react-router-dom';
import { connect } from 'react-redux';
import { doCreate, doUpdate } from './User.reducer';
import { profileOptions } from './UserService';
import { toastError } from '../../components/Toast';
import InputComponent from '../../components/Input/Input';
import SubmitButtonComponent from '../../components/Buttons/SubmitButton';
import SelectComponent from '../../components/Input/SelectInput'; 
import HeaderComponent from '../../components/Header';
import FooterComponent from '../../components/Footer';
import './style.css';

function UserPage(props) {
    const { loading, doCreate, doUpdate } = props;
    const navigation = useHistory();
    const { user, willCreate } = useLocation().state;
    const [name, setName] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPws] = useState('');
    const [confirmPassword, setConfPws] = useState('');
    const [profile, setProfile] = useState('');
    const [isDisabled, setIsDisabled] = useState(true);

    useEffect(() => {
        if(!willCreate && user){
            setName(user.name);
            setEmail(user.email);
            setProfile(user.profile[0]);
        }
    }, []);

    useEffect(() => {
        if(!isEmpty(email, password, confirmPassword, name, profile)){
            setIsDisabled(false);
        } else {
            setIsDisabled(true);
        }
    }, [email, password, confirmPassword, name, profile]);

    useEffect(() => {
        if(email !== '' && name !== '' && profile !== ''){
            setIsDisabled(false);
        } else {
            setIsDisabled(true);
        }
    }, [email, name, profile]);

    function resetState() {
        setName('');
        setEmail('');
        setPws('');
        setConfPws('');
        setProfile('');
    };

    function handleSaveNewUser(event) {
        event.preventDefault();
        if(verifyPasswords(password, confirmPassword)){
            doCreate(name, email, password, profile);
            resetState();
        } else { 
            toastError('Senhas nao batem !');
        }
    };

    function handleUpdateUser(event) {
        event.preventDefault();
        doUpdate(user.id, name, email, profile, navigation);
        resetState();
    };

    function verifyPasswords(psw, confiPsw) {
        if(psw === confiPsw){
            return true;
        } else {
            return false;
        }
    };

    function isEmpty(email, psw, confirmPsw, name, type) {
        if(psw !== '' || confirmPsw !== '' || email === '' || psw === '' 
        || name === '' || type === '' ){
            return true;
        } else {
            return false;
        }
    };

    return(
        <div className="container-user">
            <HeaderComponent />
            <div className="container-content">
                <main>
                    <form onSubmit={willCreate ? handleSaveNewUser : handleUpdateUser }>
                        <h1>Cadastrar um novo usuário</h1>
                        <fieldset>
                            <legend>
                                <h2>Credenciais</h2>
                                <span>
                                    Insira os dados do novo usuário
                                </span>
                            </legend>
                            <InputComponent
                                name="name"
                                label="Nome"
                                placeholder={"Digite um nome ..."}
                                disabled={false}
                                value={name}
                                onChange={(e) => setName(e.target.value)}
                            />
                            <InputComponent 
                                name="email"
                                label="Email"
                                type="email"
                                placeholder={"Digite um email ..."}
                                disabled={false}
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                            />
                            { willCreate 
                                ? <div className="container-input-group">
                                    <InputComponent 
                                        name="psw"
                                        label="Senha"
                                        type="password"
                                        placeholder={"Digite uma senha ..."}
                                        disabled={false}
                                        value={password}
                                        onChange={(e) => setPws(e.target.value)}
                                    />
                                    <InputComponent 
                                        name="confirmPsw"
                                        disabled={false}
                                        type="password"
                                        placeholder={"Confirme a senha ..."}
                                        label="Confirme a Senha"
                                        value={confirmPassword}
                                        onChange={(e) => setConfPws(e.target.value)}
                                    />
                                </div> 
                                : <></> 
                            }
                        </fieldset>
                        <fieldset>
                            <legend>
                                <h2>Tipagem</h2>
                                <span>
                                    Selecione o tipo do usupario
                                </span>
                            </legend>
                            <SelectComponent
                                name="types"
                                label="Tipo do usuário"
                                value={profile}
                                onChange={(e) => setProfile(e.target.value)}
                                options={profileOptions}
                            />
                        </fieldset>
                        <SubmitButtonComponent 
                            title={willCreate ? "Adicionar Usuário" : "Atualizar Usuário"}
                            loading={loading}
                            disabled={isDisabled}
                        />
                    </form>
                </main>
            </div>
            <FooterComponent />
        </div>
    );
};

const mapStateToProps = ({ userState }) => ({
    loading: userState.loading,
});

const mapDispatchToProps = {
    doCreate,
    doUpdate
};

export default connect(mapStateToProps, mapDispatchToProps)(UserPage);
