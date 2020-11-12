export class Logger {
    fileName = 'FILE_NAME';

    constructor(fileName){
        this.fileName = fileName;
    }

    info = (message, ...optionalParams) => {
        console.info(message, ...optionalParams);
    }
    debug = (message, ...optionalParams) => {
        console.debug(message, ...optionalParams);
    };
    warn = (message, ...optionalParams) => {
        console.warn(message, ...optionalParams);
    };
    error = (message, ...optionalParams) => {
        console.error(message, ...optionalParams);
    };
}