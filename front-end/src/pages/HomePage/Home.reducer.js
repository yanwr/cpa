import HttpRequest from '../../shared/httpRequest';
import { PROFILES, getUserFromLocalStorage } from '../UserPage/UserService';
import  { Logger } from '../../shared/log';
import { handleException } from '../../shared/helpFunction';

const Log = new Logger("HomePage.reducer.js");

export const ACTION_TYPES = {
    RESET_STATE: 'data/RESET_STATE',
    LOADING_DATA: 'data/LOADING_DATA',
    DATA_SUCCESS: 'data/DATA_SUCCESS',
    DATA_FAIL: 'data/DATA_FAIL',
    USERS_FINISHER_LOADING: 'data/USERS_FINISHER_LOADING',
    USERS_FINISHER_SUCCESS: 'data/USERS_FINISHER_SUCCESS',
    USERS_FINISHER_FAIL: 'data/USERS_FINISHER_FAIL'
};

const initialState = {
    loading: false,
    data:[],
    loadingUserFinisher: false,
    userFinisher: []
};

export default (state = initialState, action) => {
    switch (action.type) {
        case ACTION_TYPES.RESET_STATE:
            return initialState;
        case ACTION_TYPES.LOADING_DATA:
            return { ...state, loading: true };
        case ACTION_TYPES.USERS_FINISHER_LOADING:
            return { ...state, loadingUserFinisher: true };
        case ACTION_TYPES.DATA_SUCCESS:
            return { 
                ...state, 
                loading: false, 
                data: action.payload
            };
        case ACTION_TYPES.USERS_FINISHER_SUCCESS:
            return { 
                ...state,
                userFinisher: action.payload,
                loadingUserFinisher: false,
            };
        case ACTION_TYPES.DATA_FAIL:
            return { ...state, loading: false };
        case ACTION_TYPES.USERS_FINISHER_FAIL:
            return { ...state, loadingUserFinisher: false };
        default:
            return state;
    }
};

export const loadData = () => async (dispatch) => {
    dispatch({ type: ACTION_TYPES.LOADING_DATA });
    const user = getUserFromLocalStorage();
    let promise;
    const currentUserProfile = user.profiles[0].authority;
    if(currentUserProfile === PROFILES.TRIADOR.default){
        Log.info(`Try to load datas from ALL process`);
        promise = HttpRequest.get('/processes');
    } else if (currentUserProfile === PROFILES.FINALIZADOR.default) {
        Log.info(`Try to load datas from process`);
        promise = HttpRequest.get('/processes/no-opinion');
    } else {
        Log.info(`Try to load datas from users`);
        promise = HttpRequest.get('/users');
    }

    promise.then(
        response => {
            let { data } = response;
            Log.info('Data done: ', data);
            if(response.config.url === '/users'){
                data = dataFilteredById(data, user.id);
            }
            dispatch({
                type: ACTION_TYPES.DATA_SUCCESS,
                payload: data
            });
        }
    ).catch( 
        e => {
            Log.error(`Fail to load data: `, e);
            handleException(e);
            dispatch({ type: ACTION_TYPES.DATA_FAIL });
        }
    );
};

export const loadUserFinisher = () => async (dispatch) => {
    Log.info(`Try to load user finisher`);
    dispatch({ type: ACTION_TYPES.USERS_FINISHER_LOADING });
    try {
        const response = await HttpRequest.get('/users/finishers');
        const { data } = response;
        Log.info(`Load users finisher with success`);
        dispatch({
            type: ACTION_TYPES.USERS_FINISHER_SUCCESS,
            payload: data
        });
    } catch (e) {
        Log.error(`Fail to load users finisher: `, e);
        handleException(e);
        dispatch({ type: ACTION_TYPES.USERS_FINISHER_FAIL });
    }
};

export const refreshData = (IdDeleted) => (dispatch, getState) => {
    dispatch({ type: ACTION_TYPES.LOADING_DATA });
    try {
        Log.info(`Try to refresh data`);
        const { data } = getState().dataState;
        const dataFiltered = dataFilteredById(data, IdDeleted);
        Log.info(`Data refresh done. Data `, dataFiltered);
        dispatch({
            type: ACTION_TYPES.DATA_SUCCESS,
            payload: dataFiltered
        });
    } catch (e) {
        Log.info(`Fail to refresh data with idDeleted: ${IdDeleted}`);
        dispatch({ 
            type: ACTION_TYPES.DATA_FAIL
        });
    }
};

const dataFilteredById = (data, idDeleted) => {
    return data.filter( element =>  element.id !== Number(idDeleted));
};

export const resetState = () => (dispatch) => {
    Log.info(`Try to reset reducer state data`);
    dispatch({
        type: ACTION_TYPES.RESET_STATE
    });
    Log.info(`Reducer state data reseted done with success`);
};