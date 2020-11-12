import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { useHistory } from 'react-router-dom';
import { 
    PROFILES, 
    getUserFromLocalStorage, 
    verifyUserProfile } from '../UserPage/UserService';
import { doDelete } from '../UserPage/User.reducer';
import { loadData } from './Home.reducer';
import HeaderComponent from '../../components/Header';
import TableComponent from '../../components/Table';
import FooterComponent from '../../components/Footer';
import HeaderButtonComponent from '../../components/Buttons/HeaderButton';
import './style.css';

function HomePage(props) {
    const { doDelete, loadData, data, loadingData } = props;
    const userInSession = getUserFromLocalStorage();
    const navigation = useHistory();
    const [currentProfile, setCurrentProfile] = useState('');
    const [indicator, setIndicator] = useState('Processos');

    function goTo(to, data = {}) {
        navigation.push(to, data);
    };

    useEffect(() => {
        if(verifyUserProfile(PROFILES.ADMIN.default, userInSession.profiles)){
            setCurrentProfile(PROFILES.ADMIN.short);
            setIndicator('Usuários');
        } else if(verifyUserProfile(PROFILES.FINALIZADOR.default, userInSession.profiles)){
            setCurrentProfile(PROFILES.FINALIZADOR.short);
            setIndicator('Seus Processos');
        } else {
            setCurrentProfile(PROFILES.TRIADOR.short);
            setIndicator('Processos');
        }
        loadData();
    }, []);


    function renderHeaderButton() {
        if (currentProfile === PROFILES.ADMIN.short) {
            return <HeaderButtonComponent 
                        title={"Add user"} 
                        action={() => goTo('/user', { willCreate: true })}
                    />
        } else if (currentProfile === PROFILES.TRIADOR.short) {
            return <HeaderButtonComponent 
                        title={"Add processo"} 
                        action={() => goTo('/process', { willCreate: true, profile: currentProfile })}
                    />
        } else {
            return <></>
        }
    };

    return(
        <div className="container-home">
            <HeaderComponent indicator={indicator} name={userInSession.name} profile={currentProfile} >
                { renderHeaderButton() }
            </HeaderComponent>
            <main>
                <TableComponent 
                    data={data} 
                    profile={currentProfile}
                    deleteUser={(id) => doDelete(id)}
                    loading={loadingData}
                />
            </main>
            <FooterComponent />
        </div>
    );
};

const mapStateToProps = ({ dataState }) => ({
    loadingData: dataState.loading,
    data: dataState.data
});

const mapDispatchToProps = {
    doDelete,
    loadData
};

export default connect(mapStateToProps, mapDispatchToProps)(HomePage);