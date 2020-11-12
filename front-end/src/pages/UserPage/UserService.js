import { decode } from 'jsonwebtoken';

export const getDataToke = (token) => {
    const _token = token.replace('Bearer ', '');
    const userDecode = decode( _token, { complete: true });
    const { sub, name, email, profiles } = userDecode.payload;
    return { id: sub, name, email, profiles, access_token: token };
}

export const setUserInLocalStorage = (user) => {
    localStorage.setItem('id', user.id);
    localStorage.setItem('email', user.email);
    localStorage.setItem('name', user.name);
    localStorage.setItem('profiles', JSON.stringify(user.profiles));
    localStorage.setItem('access_token', user.access_token);
};

export const getUserFromLocalStorage = () => {
    const user = {
        id: localStorage.getItem('id'),
        email: localStorage.getItem('email'),
        name: localStorage.getItem('name'),
        profiles: JSON.parse(localStorage.getItem('profiles')),
        access_token: localStorage.getItem('access_token')
    };
    return user;
};

export const clearUserFromLocalStorage = () => {
    localStorage.clear();
};

export const verifyUserProfile = (profileToVerify, profiles) => {
    return profiles.some(element => {
        return element.authority === profileToVerify;
    });
};

export const PROFILES = {
    TRIADOR:{default:'ROLE_TRIADOR', short: 'Triador'},
    ADMIN: {default:'ROLE_ADMIN', short: 'Admin'},
    FINALIZADOR: {default:'ROLE_FINALIZADOR', short: 'Finalizador'},
};

export const profileOptions = [
    { value: 'TRIADOR', label: 'Triador' },
    { value: 'FINALIZADOR', label: 'Finalizador' },
    { value: 'ADMIN', label: 'Admin' },
];