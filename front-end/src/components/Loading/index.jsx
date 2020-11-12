import React from 'react';
import { Spinner } from 'react-bootstrap';
export default function LoadingComponent(props) {
    const { ...values } = props;
    return (
        <div className="div-loading">
            <Spinner 
                {...values}
                animation={"border"}
                style={{ marginRight: 10 }}
            />
            Carregando ...
        </div>
    );  
};