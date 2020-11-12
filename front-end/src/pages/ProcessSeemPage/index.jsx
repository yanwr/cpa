import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { useHistory, useLocation } from 'react-router-dom';
import { getUserFromLocalStorage, PROFILES } from '../UserPage/UserService';
import { loadSeem, doSaveSeem } from './Seem.reducer';
import { doCreateProcess } from './Process.reducer';
import HeaderComponent from '../../components/Header';
import FooterComponent from '../../components/Footer';
import InputComponent from '../../components/Input/Input';
import TextAreaComponent from '../../components/TextArea';
import SubmitButtonComponent from '../../components/Buttons/SubmitButton';
import './style.css';

function ProcessPage(props) {
    const navigation = useHistory();
    const { loadSeem, currentSeem, doSaveSeem,
        doCreateProcess, loadingSeem, loadingProcess } = props;
    const userInSession = getUserFromLocalStorage();
    const { process, profile, willCreate } = useLocation().state;
    const [seem, setSeem] = useState('');
    const [isDisabled, setIsDisabled] = useState(true);
    const [id, setId] = useState('');
    const [name, setName] = useState('');
    const [description, setDescription] = useState('');
    const isTriador = profile === PROFILES.TRIADOR.short;

    useEffect(() => {
        if(process){
            loadSeem(process);
        }
    }, []);

    useEffect(() => {
        if(!willCreate && process){
            setId(process.id);
            setName(process.name);
            setDescription(process.description);
        }
    }, []);

    useEffect(() => {
        if(seem === '' || seem === null){
            setIsDisabled(true);
        } else {
            setIsDisabled(false);
        }
    }, [seem]);

    useEffect(() => {
        if (willCreate) {
            if(name !== '' && description !== ''){
                setIsDisabled(false);
            } else {
                setIsDisabled(true);
            }
        }
    }, [name, description]);

    function handleActionSubmit(event) {
       event.preventDefault();
        if(willCreate){
            doCreateProcess(name, description);
            resetState();
        } else {
            debugger
            doSaveSeem(seem, process, navigation);
        }
    };

    function resetState() {
        setName('');
        setDescription('');
    };

    function renderEachSeem() {
        if (currentSeem.length === 0) {
            return <h5>Ainda sem parecer ...</h5>
        }
        const data = currentSeem.sort((a, b) => a.id - b.id );
        return data.map( element => {
           return <TextAreaComponent 
                    key={element.id}
                    name={`seem${element.id}`}
                    label={`${element.finisher.name}`}
                    value={ element.description 
                        ? element.description : "" }
                    disabled={true}
                />
        });
    };

    return(
        <div className="container-process">
            <HeaderComponent name={userInSession.name} profile={profile} />
            <div className="container-content">
                <main>
                    <form onSubmit={handleActionSubmit}>
                        <h1>
                            { isTriador && willCreate
                                ? "Crie um novo processo" 
                                : (isTriador && !willCreate 
                                    ? "Verifique o estado atual do processo"
                                    : "Adicione um parecer")
                            }
                        </h1>
                        <fieldset>
                            <legend>
                                <h2>Processo</h2>
                                <span>
                                    Visualize os dados do processo
                                </span>
                            </legend>
                            <div className="container-input-group">
                                <InputComponent
                                    name="id"
                                    label="ID do processo"
                                    disabled={true}
                                    value={id}
                                    placeholder={willCreate ? "Id automático" : ""}
                                />
                                <InputComponent
                                    name="name"
                                    label="Nome do processo"
                                    disabled={!willCreate}
                                    value={name}
                                    onChange={(e) => setName(e.target.value)}
                                />
                            </div>
                            <TextAreaComponent 
                                name="desc"
                                label="Descrição do processo"
                                disabled={!willCreate}
                                value={description}
                                onChange={(e) => setDescription(e.target.value)}
                            />
                        </fieldset>
                        <fieldset>
                        { !willCreate 
                            ? ( isTriador 
                                ? <>
                                    <legend>
                                        <h2>Parecer</h2>
                                        <span>
                                            { isTriador
                                            ? "Atual parecer desse processo"
                                            : "Adicione um parecer ao processo"
                                            }
                                        </span>
                                    </legend> 
                                    {renderEachSeem()}
                                </>
                                : <TextAreaComponent 
                                    name="seem"
                                    placeholder={"Escreva aqui ..." }
                                    value={seem}
                                    disabled={false}
                                    onChange={e => setSeem(e.target.value)}
                                /> ) 
                            : <></> }
                        </fieldset>
                        <SubmitButtonComponent
                            title={willCreate ? "Adicionar processo" : "Adicionar parecer"}
                            disabled={isDisabled}
                            loading={willCreate ? loadingProcess : loadingSeem}
                        /> 
                    </form>
                </main>
            </div>
            <FooterComponent />
        </div>
    );
};

const mapStateToProps = ({ seemState, processState }) => ({
    loadingSeem: seemState.loading,
    loadingProcess: processState.loading,
    currentSeem: seemState.currentSeem,
});

const mapDispatchToProps = {
    loadSeem,
    doSaveSeem,
    doCreateProcess
};

export default connect(mapStateToProps, mapDispatchToProps)(ProcessPage);