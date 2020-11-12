import React from 'react';
import './headerButtonStyle.css';

export default function HeaderButton(props) {
    const { title, action, className = "btn-custom-primary" } = props;
    return (
        <button
            className={className}
            onClick={() => action()}
        >
            {title}
        </button>
    );
}