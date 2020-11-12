import { applyMiddleware, createStore } from 'redux';
import ReduxThunk from 'redux-thunk';
import Reducers from '../index';

export const store = createStore(Reducers, applyMiddleware(
    ReduxThunk
));