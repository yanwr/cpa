import HttpRequest from '../../shared/httpRequest';
import { Logger } from '../../shared/log';
import { 
    getDataToke, 
    clearUserFromLocalStorage,
    setUserInLocalStorage } from './UserService';
import { resetState as resetStateFromData, refreshData } from '../HomePage/Home.reducer';
import { toastSuccess } from '../../components/Toast';
import { handleException } from '../../shared/helpFunction';

const Log = new Logger("User.reducer.js");

export const ACTION_TYPES = {
    RESET_STATE: 'session/RESET_STATE',
    LOADING: 'session/LOADING',
    LOGIN_SUCCESS: 'session/LOGIN_SUCCESS',
    LOGIN_FAIL: 'session/LOGIN_FAIL',
    CREATE_SUCCESS: 'session/CREATE_SUCCESS',
    CREATE_FAIL: 'session/CREATE_FAIL',
    UPDATE_SUCCESS: 'session/UPDATE_SUCCESS',
    UPDATE_FAIL: 'session/UPDATE_FAIL',
    DELETE_SUCCESS: 'session/DELETE_SUCCESS',
    DELETE_FAIL: 'session/DELETE_FAIL',
    LOGOUT_SUCCESS: 'session/LOGOUT_SUCCESS',
    LOGOUT_FAIL: 'session/LOGOUT_FAIL',
};

const initialState = {
    loading: false,
    user:{}
};

export default (state = initialState, action) => {
    switch (action.type) {
        case ACTION_TYPES.RESET_STATE:
            return initialState;
        case ACTION_TYPES.LOADING:
            return {...state, loading: true};
        case ACTION_TYPES.LOGIN_SUCCESS:
            const user = action.payload;
            return {...state, loading: false, user};
        case ACTION_TYPES.UPDATE_SUCCESS:
        case ACTION_TYPES.DELETE_SUCCESS:
        case ACTION_TYPES.CREATE_SUCCESS:
        case ACTION_TYPES.LOGOUT_SUCCESS:
        case ACTION_TYPES.LOGIN_FAIL:
        case ACTION_TYPES.CREATE_FAIL:
        case ACTION_TYPES.LOGOUT_FAIL:
        case ACTION_TYPES.UPDATE_FAIL:
        case ACTION_TYPES.DELETE_FAIL:
            return {...state, loading: false };
        default:
            return state;
    }
};

export const doLogin = (email, password, navigation) => async (dispatch) => {
    dispatch({ type: ACTION_TYPES.LOADING });
    try {
        Log.info(`Try to do login with email ${email}.`);
        const body = { 
            email,
            password
         };
        const response = await HttpRequest.post('/login', body);
        const token = response.headers.authorization;
        if (token) { 
            const user = getDataToke(token);
            Log.info(`User in session`, user);
            setUserInLocalStorage(user);
            dispatch({
                type: ACTION_TYPES.LOGIN_SUCCESS,
                payload: user
            });
            navigation.push('/home');
            Log.info(`Login done with success. userId: ${user.id}`);
        }
    } catch (e) {
        Log.error(`Fail to do login with email ${email}`, e);
        handleException(e);
        dispatch({ type: ACTION_TYPES.LOGIN_FAIL });
    }
};

export const doCreate = (name, email, password, profile ) => async (dispatch) =>{
    Log.info(`Try to do create user.`, { name, email, profile });
    dispatch({ type: ACTION_TYPES.LOADING });
    try {
        const body = { 
            email,
            name,
            password,
            profile
        };
        const response = await HttpRequest.post('/users', body);
        const user = response.data;
        Log.info(`create new user with success. User id: ${user.id}`);
        dispatch({ type: ACTION_TYPES.CREATE_SUCCESS });
        toastSuccess('Usuário cadastro com sucesso');
    } catch (e) {
        Log.error(`Fail to create a new user ${{name, email }}`, e);
        handleException(e);
        dispatch({ type: ACTION_TYPES.CREATE_FAIL });
    }
};

export const doUpdate = (id, name, email, profile, navigation) => async (dispatch) => {
    Log.info(`Try to do update user.`, { name, email, profile });
    dispatch({ type: ACTION_TYPES.LOADING });
    try {
        const body = { 
            email,
            name,
            profile
        };
        const response = await HttpRequest.put(`/users/${id}`, body);
        Log.info(`Update user ${id} with success.`);
        toastSuccess('Usuário alterado com sucesso');
        dispatch({ type: ACTION_TYPES.UPDATE_SUCCESS });
        navigation.push('/home');
    } catch (e) {
        Log.error(`Fail to update user ${id}`, e);
        handleException(e);
        dispatch({ type: ACTION_TYPES.UPDATE_FAIL });
    }
};

export const doDelete = (id) => async (dispatch, getState) => {
    Log.info(`Try to do delete user.`, id);
    dispatch({ type: ACTION_TYPES.LOADING });
    try {
        const response = await HttpRequest.delete(`/users/${id}`);
        Log.info(`Delete user ${id} with success.`);
        toastSuccess('Usuário deletado com sucesso');
        refreshData(id)(dispatch, getState);
        dispatch({ type: ACTION_TYPES.DELETE_SUCCESS });
    } catch (e) {
        Log.error(`Fail to delete new ${id}`, e);
        handleException(e);
        dispatch({ type: ACTION_TYPES.DELETE_FAIL });
    }
};

export const doLogout = (navigation) => (dispatch, getState) => {
    const { user } = getState().userState;
    Log.info(`Try to do logout from user ${user.id}`);
    dispatch({ type: ACTION_TYPES.LOADING });
    try { 
        clearUserFromLocalStorage();
        dispatch({ type: ACTION_TYPES.LOGOUT_SUCCESS });
        resetState()(dispatch);
        Log.info(`Logout done with success`);
        navigation.push('/');
    } catch(e) {
        Log.error(`Fail to do logout from user ${user.id}`, e);
        dispatch({ type: ACTION_TYPES.LOGOUT_FAIL });
    }
};

const resetState = () => (dispatch) => {
    Log.info(`Try to reset reducer states`);
    resetStateFromData()(dispatch);
    dispatch({
        type: ACTION_TYPES.RESET_STATE
    });
    Log.info(`Reducer state reseted done with success`);
};