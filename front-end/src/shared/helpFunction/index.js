import { toastError } from '../../components/Toast';

export function handleException(e) {
    const { status, data } = e.response;
    debugger
    if(status && data.message){
        toastError(data.message);
    } else {
        toastError('Oppss, houve um erro de conexão com o servidor. Tente novamente, por favor.');
    }
};