import React from 'react';
import { BrowserRouter, Route } from 'react-router-dom';

import LoginPage from './pages/LoginPage';
import HomePage from './pages/HomePage';
import ProcessSeemPage from './pages/ProcessSeemPage';
import UserPage from './pages/UserPage';

export default function Routes() {
    return(
        <BrowserRouter>
            <Route path={'/'} exact component={LoginPage} />
            <Route path={'/home'} exact component={HomePage} />
            <Route path={'/process/:id'} exact component={ProcessSeemPage} />
            <Route path={'/process'} exact component={ProcessSeemPage} />
            <Route path={'/user'} exact component={UserPage} />
        </BrowserRouter>
    );
};