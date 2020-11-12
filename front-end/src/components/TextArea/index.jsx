import React from 'react';
import './style.css';

export default function TextArea(props) {
    const { name, label, ...rest} = props;
    return(
        <div className="container-textarea">
            <label htmlFor={name}>Parecer do usuário <strong>{label}</strong></label>
            <textarea id={name} {...rest} />
        </div>
    );
}