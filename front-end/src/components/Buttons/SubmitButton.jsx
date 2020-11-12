import React from 'react';
import LoadingComponent from '../../components/Loading';
import './submitButtonStyle.css';

export default function SubmitButton(props) {
    const { title, disabled, loading} = props;
    const btnStyle = disabled ? "btn-submit-disabled" : "btn-submit";
    return(
        <button
            type="submit"
            className={btnStyle}
            disabled={disabled}
        >
            {
                loading
                ? <LoadingComponent 
                    as={'span'} 
                    size={'sm'}    
                />
                : title
            }
        </button>
    );
}