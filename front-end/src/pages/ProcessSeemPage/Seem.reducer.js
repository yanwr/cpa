import HttpRequest from '../../shared/httpRequest';
import  { Logger } from '../../shared/log';
import { toastSuccess } from '../../components/Toast';
import { handleException } from '../../shared/helpFunction';

const Log = new Logger("Seem.reducer.js");

export const ACTION_TYPES = {
    RESET_STATE: 'seem/RESET_STATE',
    LOADING: 'seem/LOADING',
    SEEM_SUCCESS: 'seem/SEEM_SUCCESS',
    SEEM_FAIL: 'seem/SEEM_FAIL',
    STORE_SEEM_SUCCESS: 'seem/STORE_SEEM_SUCCESS',
    STORE_SEEM_SUCCESS: 'seem/SEEM_FAIL',
};

const initialState = {
    loading: false,
    currentSeem: []
};

export default (state = initialState, action) => {
    switch (action.type) {
        case ACTION_TYPES.RESET_STATE:
            return initialState;
        case ACTION_TYPES.LOADING:
            return { ...state, loading: true };
        case ACTION_TYPES.SEEM_SUCCESS:
            return { 
                ...state,
                loading: false,
                currentSeem: action.payload
            };
        case ACTION_TYPES.SEEM_FAIL:
            return { ...state, loading: false };
        default:
            return state;
    }
};

export const doSaveSeem = (seem, process, navigation) => async (dispatch) => {
    dispatch({ type: ACTION_TYPES.LOADING });
    debugger
    Log.info(`Try to save seem on process ${process.id}`);
    try {
        const { opinions } = process;
        const body = { description: seem };
        const response = await HttpRequest.put(`/opinions/${opinions[0].id}`, body);
        Log.info(`Save seem on process ${process.id} done`);
        toastSuccess(`Parecer salvo com sucesso`);
        navigation.push('/home');
    } catch (e) {
        Log.error(`Fail to save seem on process ${process.id}`);
        handleException(e);
    } finally {
        dispatch({ type: ACTION_TYPES.SEEM_FAIL });
    }
}

export const loadSeem = (process) => async (dispatch) => {
    Log.info(`Try to load seem from process ${process.id}`);
    try {
        if(!process.opinions || process.opinions.length === 0 ){
            Log.info(`New process don\'t have seem yet`);
            dispatch({
                type: ACTION_TYPES.SEEM_SUCCESS,
                payload: []
            });
            return;
        }
        dispatch({
            type: ACTION_TYPES.SEEM_SUCCESS,
            payload: process.opinions
        });
    } catch (e) {
        Log.error(`Fail to load seem from process ${process.id}`);
        handleException(e);
        dispatch({ type: ACTION_TYPES.SEEM_FAIL });
    }
};