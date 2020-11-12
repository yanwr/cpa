import React from 'react';
import Routes from './routes';
import HtppRequest from './shared/httpRequest';
import { getUserFromLocalStorage } from './pages/UserPage/UserService';
import { Provider } from 'react-redux';
import { store } from './shared/redux/store';
import { ToastContainer } from 'react-toastify'
import './global.css';
import 'react-toastify/dist/ReactToastify.css';

HtppRequest.interceptors.request.use( function(config) {
    const { baseURL, url, data, headers } = config;
    const finalURL = baseURL + url;
    const currentBody = data || {};
    const user  = getUserFromLocalStorage();
    if(user && user.access_token){
        headers.Authorization = user.access_token;
    }
    console.log(`Do request ${finalURL} with body`, currentBody);
    return config;
}, function(error){
        const { baseURL, url, data } = error;
        const finalURL = baseURL + url; 
        const currentBody = data || {};
        console.log(`Fail request to ${finalURL} with body`, currentBody);
        return Promise.reject(error);
    }
);

HtppRequest.interceptors.response.use(function(res) {
    const { data, config } = res;
    const currentData = data || {};
    const { baseURL, url } = config;
    const finalURL = baseURL + url;
    console.log(`Request to ${finalURL} DONE with data`, currentData);
    return res;
}, function(error) {
    const { config, data } = error.response;
    const { baseURL, url } = config;
    const currentData = data || {};
    const finalURL = baseURL + url;
    console.log(`Fail response to ${finalURL} with data`, currentData);
    return Promise.reject(error);
});

function App() {
  return (
      <Provider store={store}>
        <Routes/>
        <ToastContainer
            position="bottom-right"
            autoClose={3000}
            hideProgressBar={false}
            newestOnTop={false}
            closeOnClick
            rtl={false}
            pauseOnFocusLoss
            draggable
            pauseOnHover
            limit={3}
        />
      </Provider>
  );
}

export default App;