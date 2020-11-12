import HttpRequest from '../../shared/httpRequest';
import  { Logger } from '../../shared/log';
import { toastSuccess } from '../../components/Toast';
import { handleException } from '../../shared/helpFunction';

const Log = new Logger("Process.reducer.js");

export const ACTION_TYPES = {
    RESET_STATE: 'process/RESET_STATE',
    LOADING: 'process/LOADING',
    CREATE_SUCCESS: 'process/CREATE_SUCCESS',
    CREATE_FAIL: 'process/CREATE_FAIL',
};

const initialState = {
    loading: false,
};

export default (state = initialState, action) => {
    switch (action.type) {
        case ACTION_TYPES.RESET_STATE:
            return initialState;
        case ACTION_TYPES.LOADING:
            return { ...state, loading: true };
        case ACTION_TYPES.CREATE_SUCCESS:
        case ACTION_TYPES.CREATE_FAIL:
            return { ...state, loading: false };
        default:
            return state;
    }
};

export const doCreateProcess = (name, description) => async (dispatch) => {
    dispatch({ type: ACTION_TYPES.LOADING });
    Log.info(`Try to create a new process`);
    try {
        const body = { description, name };
        const response = await HttpRequest.post('/processes', body);
        const { id } = response.data;
        dispatch({ type: ACTION_TYPES.CREATE_SUCCESS });
        Log.info(`Process ${id} resgister with success`);
        toastSuccess('Processo criado com sucesso');
    } catch (e) {
        Log.error(`Fail to create a new process.`, e);
        handleException(e);
        dispatch({ type: ACTION_TYPES.CREATE_FAIL });
    }
};

export const doSaveUserInProcess = (process, users) => async (dispatch) => {
    dispatch({ type: ACTION_TYPES.LOADING });
    Log.info(`Try to save users ${users} in process ${process.id}`);
    try {
        const body = { process: process.id, users };
        const response = await HttpRequest.post('/opinions', body);
        dispatch({ type: ACTION_TYPES.CREATE_SUCCESS });
        Log.info(`Users ${users} was set in process ${process.id} with success`);
        toastSuccess(`Usuários designados com sucesso`);
    } catch (e) {
        Log.error(`Fail to create a new process.`, e);
        handleException(e);
        dispatch({ type: ACTION_TYPES.CREATE_FAIL });
    }
};