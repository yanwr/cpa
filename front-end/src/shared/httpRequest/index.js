import axios from "axios";

const HttpRequest = axios.create({
    baseURL: 'http://localhost:8080',
});

export default HttpRequest;