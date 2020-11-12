import React from 'react';
import { Nav, Navbar} from 'react-bootstrap';
import { connect } from 'react-redux';
import { doLogout } from '../../pages/UserPage/User.reducer';
import { useHistory } from 'react-router-dom';
import './style.css';
import HeaderButton from '../Buttons/HeaderButton';
 
function HeaderComponent(props) {
    const { doLogout } = props;
    const navigation = useHistory();
    const { profile, name, indicator, children } = props;
 
    function handleDoLogout() {
        doLogout(navigation);
    }
 
    return( 
        <Navbar className="container-header">
            <Navbar.Brand className="container-logo">
                <h1>Usual Process</h1>
                <img alt='' src={require('../../images/logo-up.png')} />
            </Navbar.Brand>
 
            <Navbar.Collapse id="responsive-navbar-nav" className="container-info">
                <Nav>
                    <p>{indicator}</p>
                    {children}
                </Nav>
                <div className="profile">
                    <p>{profile && name ? <>{profile} | {name} </> : ''}</p>
                    {
                        profile ?
                        
                            <HeaderButton
                                
                                className={"btn-logout"}
                                action={handleDoLogout}
                                title={"Log out"}
                            /> : <></>
                    }
                </div>
            </Navbar.Collapse>
        </Navbar>
    );
};
 
const mapStateToProps = ({ userState }) => ({
    
});
 
const mapDispatchToProps = {
    doLogout,
};
 
export default connect(mapStateToProps, mapDispatchToProps)(HeaderComponent);