  
import { combineReducers } from 'redux';

import userState from '../../pages/UserPage/User.reducer';
import dataState from '../../pages/HomePage/Home.reducer';
import seemState from '../../pages/ProcessSeemPage/Seem.reducer';
import processState from '../../pages/ProcessSeemPage/Process.reducer';

const appReducer = combineReducers({
    userState,
    dataState,
    seemState,
    processState
});

const routerReducer = (state, action) => {
    return appReducer(state, action);
};

export default routerReducer;