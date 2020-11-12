import React, { useEffect, useState } from 'react';
import { connect } from 'react-redux';
import { loadUserFinisher } from '../../pages/HomePage/Home.reducer';
import { doSaveUserInProcess } from '../../pages/ProcessSeemPage/Process.reducer';
import { Modal, FormCheck } from 'react-bootstrap';
import LoadingComponent from '../Loading';

function ModalComponent(props) {
    const { show, onClose, users, process, loadingProcess,
        loadUserFinisher, doSaveUserInProcess,
        loadingUsers } = props;
    const [ usersSelected, setUsersSelected ] = useState([]);
    const [ isDisabled, setIsDisabled ] = useState(false);

    useEffect(() => {
        setUsersSelected([]);
    }, [show])

    useEffect(() => {
        loadUserFinisher();
    }, []);

    useEffect(() => {
        if(usersSelected.length === 0 ){
            setIsDisabled(true);
        } else {
            setIsDisabled(false);
        }
    }, [usersSelected]);

    function renderEachUser() {
        return users.map( element => {
            return <FormCheck 
                    key={element.id}
                    type={"checkbox"}
                    id={element.id}
                    label={element.name}
                    value={element.id}
                    onClick={ (e) => handleUsersSelected(e.target.value)}
                />
        });
    };

    function handleUsersSelected(value) {
        const valueNumber = Number(value);
        if(usersSelected.some(element => element === valueNumber)){
            setUsersSelected(usersSelected.filter( element => element !== valueNumber ));
        } else {
            setUsersSelected([...usersSelected, valueNumber ]);
        }
    };

    function handleSetUsers() {
        doSaveUserInProcess(process, usersSelected);
        onClose();
    };

    return (
        <Modal 
            show={show} 
            onHide={onClose}
            size="lg"
        >
            <Modal.Header closeButton>
                <Modal.Title>Lista de usuários finalizadores</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                {
                    !loadingUsers
                    ? <div>
                        { renderEachUser() }
                    </div>
                    : <LoadingComponent 
                        variant="primary"
                    />
                }
            </Modal.Body>
            <Modal.Footer>
                <button 
                    onClick={onClose}
                    className={"btn-custom-cancel"}
                >
                    Fechar
                </button>
                <button 
                    className={isDisabled | loadingProcess 
                        ? "btn-custom-disabled" : "btn-custom-primary-lg" }
                    disabled={isDisabled | loadingProcess}
                    onClick={() => handleSetUsers()}
                >
                    { !loadingProcess 
                        ? "Designar users"
                        : <LoadingComponent 
                            size={'sm'}
                        /> }
                </button>
            </Modal.Footer>
        </Modal>
    );
};

const mapStateToProps = ({ dataState, processState }) => ({
    users: dataState.userFinisher,
    loadingProcess: processState.loading,
    loadingUsers: dataState.loadingUserFinisher
});

const mapDispatchToProps = {
    loadUserFinisher,
    doSaveUserInProcess
};

export default connect(mapStateToProps, mapDispatchToProps)(ModalComponent);