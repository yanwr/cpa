import React from 'react';
import './selectStyle.css';

export default function SelectInput(props) {
    const { label, name, options ,...rest } = props;
    
    function renderOptions() {
        return options.map(element => (
             <option 
                 key={element.value}
                 value={element.value}
             >{element.label}</option>
        ));
     };
 
    return(
        <div className="input-container">
            <label htmlFor={name}>{label}</label>
            <select 
                id={name}
                name={name}
                value=""
                {...rest}
            >
                <option value="" disabled hidden>Selecione uma opção</option>
                { renderOptions() }
            </select>
        </div>
    );
}
