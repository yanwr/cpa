import { toast } from 'react-toastify';

export function toastSuccess(msg) {
   return toast.info(msg);
};

export function toastError(msg) {
    return toast.error(msg);  
};