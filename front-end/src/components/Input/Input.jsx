import React from 'react';
import './inputStyle.css';

export default function Input(props) {
    const { label, name, type = "text", disabled, ...rest } = props;
    return(
        <div className="input-container">
            <label htmlFor={name}>{label}</label>
            <input 
                id={name}
                name={name}
                disabled={disabled}
                type={type}
                {...rest} 
            />
        </div>
    );
}