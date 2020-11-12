import React, { useState } from 'react';
import { PROFILES } from '../../pages/UserPage/UserService';
import * as Icon from 'react-bootstrap-icons';
import { useHistory } from 'react-router-dom';
import ModalComponent from '../Modal';
import LoadingComponent from '../Loading';
import './style.css';
import { render } from '@testing-library/react';

export default function TableComponent(props) {
    const { data, profile, deleteUser, loading } = props;
    const navigation = useHistory();
    const [show, setShow] = useState(false);
    const [process, setProcess] = useState({});
    const isFinal = profile === PROFILES.FINALIZADOR.short;
    const isTriador = profile === PROFILES.TRIADOR.short;
    const isAdmin = profile === PROFILES.ADMIN.short;

    function handleCloseModal() { setShow(false) };

    function handleShowModal(element) {
        setProcess(element);
        setShow(true);
    };

    function goTo(to, data) {
        navigation.push(to, data);
    };

    function handleActionBtnCell(element) {
        if (isFinal) {
            const data = {
                process: element,
                profile,
                willCreate: false
            };
            goTo(`/process/${element.id}`, data);
        } else if (isTriador) {
            handleShowModal(element);
        }
    };

    function renderColumns() {
        return <>
            <th></th>
            <th>Id</th>
            <th>Nome</th>
            {
                isAdmin
                    ? <th>Descrição</th>
                    : <th>Tipo</th>
            }
        </>
    };

    function renderBtnCell(element) {
        return (
            <>
                <button
                    className="btn-custom-primary"
                    onClick={() => handleActionBtnCell(element)}
                >
                    {isFinal
                        ? 'Add Parecer'
                        : 'Designar Usuario'
                    }
                </button>
                {
                    isTriador
                        ? <button type="button"
                            className="btn-custom-primary btn-eye"
                            onClick={() => goTo(`/process/${element.id}`, { process: element, profile })}
                        >
                            <Icon.Eye size="32" />
                        </button>
                        : <></>
                }
            </>
        );
    };

    function renderRows() {
        if( isAdmin && data.length === 0){
            return <tr>
                <td colSpan={4}>
                    Nenhum usuário no momento ...
                </td>
            </tr>
        } else if(isFinal && data.length === 0 || isTriador && data.length === 0) {
            return <tr>
                <td colSpan={4}>
                    Nenhum processo no momento ...
                </td>
            </tr>
        } else {
            return data.map(element => (
                <tr key={element.id}>
                    {
                        isAdmin
                            ? <><td className="first-row">
                                <div>
                                    <button
                                        className="btn-custom-primary"
                                        onClick={() => goTo('/user', { user: element, isNew: false })}
                                    >
                                        Atualizar
                                    </button>
                                    <button
                                        className="btn-custom-primary"
                                        onClick={() => deleteUser(element.id)}
                                    >
                                        Excluir
                                    </button>
                                </div>
                            </td>
                                <td className="id-column">
                                    {element.id}
                                </td>
                                <td className="name-column">
                                    {element.name}
                                </td>
                                <td className="profile-column">
                                    {element.profile}
                                </td>
                            </>
                            : <><td className="first-row">
                                {renderBtnCell(element)}
                            </td>
                                <td>{element.id}</td>
                                <td>{element.name}</td>
                                <td className="desc-column"><div className="overflow ellipsis">{element.description}</div></td>
                            </>
                    }
                </tr>
            ));
        }
    };

    if (loading) {
        return <LoadingComponent
            variant="dark"
            as={'span'}
            size={'sm'}
        />
    }
    return (
        <table className="container-table">
            <thead>
                {renderColumns()}
            </thead>
            <tbody>
                {renderRows()}
            </tbody>
            {
                isTriador
                    ? <ModalComponent
                        show={show}
                        onClose={handleCloseModal}
                        process={process}
                    /> : <></>
            }
        </table>
    );
}