export let url = "http://localhost:8081/asname";

export class HttpRequestCRUD {
    constructor() {
        this.response = null;
        this.authorization = null;
        this.data = null;
        this.status = false;
        this.form = null;
        this.url = url;
        this.errorMessage = null;
        this.stackTrace = null;
        this.options = {
            method: "POST",
            headers: {
                "Content-Type": "application/json;charset=utf-8"
                , "AUTH_STATUS_CODE": null
                , "Content-Disposition": null
                , "Origin": "http://localhost:3000"
            },
            mode: "cors",
            credentials: "include",
            body: undefined
        }
    }

    setURL(url) {
        this.url = url;
    }

    setFetch(url, body) {
        this.url = url;
        this.options.body = body;
    }

    setMethod(method) {
        this.options.method = method;
    }

    setContentType(contentType) {
        this.options.headers["Content-Type"] = contentType;
    }

    setBody(body) {
        this.options.body = body;
    }

    setAuthorization(authorization) {
        this.options.headers.Authorization = authorization;
    }

    getAuthorization() {
        return this.authorization;
    }


    setForm(form) {
        this.form = form;
    }

    getForm() {
        return this.form;
    }

    setOK(ok) {
        this.ok = ok;
    }

    getStatus() {
        return this.status;
    }

    getErrorMessage() {
        return this.errorMessage;
    }

    getStackTrace() {
        return this.stackTrace;
    }

    getData() {
        return this.data;
    }

    getResponse() {
        return this.response;
    }

    fetch(type) {
        console.log('System fetch');
        return fetch(this.url, this.options).then(
            response => {
                this.response = response;
                if (response.ok) {
                    console.log('System fetch: OK');
                    for (let [key, value] of response.headers) {
                        console.log(`заголовок ${key} = ${value}`);
                    }
                    if (type=='Login' || type=='Simple') {
                        console.log('System fetch: type==\'Login\' || type==\'Simple\'');
                        this.status = true;
                        // return true;
                    }
                    else if (type=='JSON') return response.json();
                    else if (type=='Blob') return response.blob();
                } else {
                    // if (type=='Login') {
                        console.log('System fetch: Login !OK');
                        if (response.status == 401) {
                            console.log('System fetch: Login 401');
                            console.log('response.headers.get(\'AUTH_STATUS_CODE\')==\'S00001\'): '+response.headers.get('AUTH_STATUS_CODE'));
                            console.log('getCookie("AUTH_STATUS_CODE"): '+getCookie("AUTH_STATUS_CODE"));
                            // перебрать все заголовки
                            for (let [key, value] of response.headers) {
                                console.log(`${key} = ${value}`);
                            }
                            // if (response.headers.get('AUTH_STATUS_CODE')=='S00001') this.errorMessage = "Неверный логин или пароль.";
                            // else if (response.headers.get('AUTH_STATUS_CODE')=='S00002') this.errorMessage = "Сессия истекла.";
                            // else if (response.headers.get('AUTH_STATUS_CODE')=='S00003') this.errorMessage = "Отсутствует Authorization заголовок.";
                            // else this.errorMessage = "Ошибка аутентификации.";
                            if (getCookie("AUTH_STATUS_CODE")=='S00001') this.errorMessage = "Неверный логин или пароль.";
                            else if (getCookie("AUTH_STATUS_CODE")=='S00002') this.errorMessage = "Сессия истекла.";
                            else if (getCookie("AUTH_STATUS_CODE")=='S00003') this.errorMessage = "Отсутствует Authorization заголовок.";
                            else this.errorMessage = "Ошибка аутентификации.";
                        } else {
                            console.log('System fetch: Login Other');
                            // this.errorMessage = "Ошибка аутентификации. HTTP-статус " + response.status + "\n" + response.statusText;
                            this.errorMessage = "HTTP-статус " + response.status + "\n" + response.statusText;
                        }
                        // return true;
                    // }
                    // else this.errorMessage = "HTTP-статус " + response.status + "\n" + response.statusText;
                }
            }
        ).then(r => {
            console.log('System fetch: then');
                if (type!='Login' && type!='Simple') {
                    if (r.error == null) {
                        console.log('System fetch: then r.error == null');
                        this.status = true;
                        this.data = r;
                    } else {
                        console.log('System fetch: then else');
                        this.status = false;
                        this.errorMessage = r.error.errorMessage;
                        this.stackTrace = r.error.stackTrace;
                    }
                }
            }
        ).catch(e => {
            console.log('System fetch: catch');
            this.status = false;
            this.errorMessage = "Сервер не доступен \n" + e;
        });
    }

    fetchLogin() {
        console.log('System fetchLogin');
        return this.fetch('Login');
    }

    fetchBlob() {
        console.log('System fetchBlob');
        return this.fetch('Blob');
    }

    fetchJSON() {
        console.log('System fetchJSON');
        return this.fetch('JSON');
    }

    fetchSimple() {
        console.log('System fetchSimple');
        return this.fetch('Simple');
    }
}

export function formatDate(milliseconds) {
    let d = new Date(milliseconds);
    let month;
    if (d.getMonth() + 1 < 10) {
        month = '0' + (d.getMonth() + 1).toString();
    } else {
        month = (d.getMonth() + 1).toString();
    }
    let date;
    if (d.getDate() < 10) {
        date = '0' + d.getDate().toString();
    } else {
        date = d.getDate().toString();
    }
    return d.getFullYear().toString() + '-' + month + '-' + date;
}

export function formatTime(milliseconds) {
    let d = new Date(milliseconds);
    let minutes;
    if (d.getMinutes() < 10) {
        minutes = '0' + d.getMinutes().toString();
    } else {
        minutes = (d.getMinutes() + 1).toString();
    }
    let seconds;
    if (d.getSeconds() < 10) {
        seconds = '0' + d.getSeconds().toString();
    } else {
        seconds = d.getSeconds().toString();
    }
    return minutes + ':' + seconds;
}

export function dateTimeJSONToView(dtString, round) {
    //alert('dateJSONToView');
    let dtJSON = new Date(Date.parse(dtString));
    if (dtJSON && round) {
        let dateTime = '';
        let date;
        if (dtJSON.getDate() < 10) {
            date = '0' + dtJSON.getDate().toString();
        } else {
            date = dtJSON.getDate().toString();
        }
        let month;
        if (dtJSON.getMonth() + 1 < 10) {
            month = '0' + (dtJSON.getMonth() + 1).toString();
        } else {
            month = (dtJSON.getMonth() + 1).toString();
        }
        dateTime = date + '.' + month + '.' + dtJSON.getFullYear().toString();
        if (round == 'dd') {
            return dateTime;
        }
        let hours;
        if (dtJSON.getHours() < 10) {
            hours = '0' + dtJSON.getHours().toString();
        } else {
            hours = dtJSON.getHours().toString();
        }
        dateTime = dateTime + ' ' + hours;
        if (round == 'hh') {
            return dateTime;
        }
        let minutes;
        if (dtJSON.getMinutes() < 10) {
            minutes = '0' + dtJSON.getMinutes().toString();
        } else {
            minutes = dtJSON.getMinutes().toString();
        }
        dateTime = dateTime + ':' + minutes;
        if (round == 'mm') {
            return dateTime;
        }
        let seconds;
        if (dtJSON.getSeconds() < 10) {
            seconds = '0' + dtJSON.getSeconds().toString();
        } else {
            seconds = dtJSON.getSeconds().toString();
        }
        dateTime = dateTime + ':' + seconds;
        if (round == 'ss') {
            return dateTime;
        }
    } else {
        return '';
    }
}

export function getCookie(name) {
    console.log('System getCookie');
    console.log('System getCookie document.cookie: '+document.cookie);
    // var matches = document.cookie.match(new RegExp(
    //     '(?:^|\s)' + name.replace(/([.$?*+\\\/{}|()\[\]^])/g, '\\$1') + '=(.*?)(?:;|$)'
    // ));
    var matches = document.cookie.match(new RegExp(
        name + '=(.*?)(?:;|$)'
    ));
    console.log('System getCookie matches: '+matches);
    return matches ? decodeURIComponent(matches[1]) : undefined;
}

function setCookie(name, value, options = {}) {

    options = {
        path: '/',
        // при необходимости добавьте другие значения по умолчанию
        ...options
    };

    if (options.expires instanceof Date) {
        options.expires = options.expires.toUTCString();
    }

    let updatedCookie = encodeURIComponent(name) + "=" + encodeURIComponent(value);

    for (let optionKey in options) {
        updatedCookie += "; " + optionKey;
        let optionValue = options[optionKey];
        if (optionValue !== true) {
            updatedCookie += "=" + optionValue;
        }
    }

    document.cookie = updatedCookie;
}

function deleteCookie(name) {
    setCookie(name, "", {
        'max-age': -1
    })
}

function cross_download(url, fileName) {
    let req = new XMLHttpRequest();
    req.open("GET", url, true);
    req.responseType = "blob";
    let __fileName = fileName;
    req.onload = function (event) {
        let blob = req.response;
        req.getResponseHeader()
        let contentType = req.getResponseHeader("content-type");
        if (window.navigator.msSaveOrOpenBlob) {
            // Internet Explorer
            window.navigator.msSaveOrOpenBlob(new Blob([blob], {type: contentType}), fileName);
        } else {
            let link = document.createElement('a');
            document.body.appendChild(link);
            link.download = __fileName;
            link.href = window.URL.createObjectURL(blob);
            link.click();
            document.body.removeChild(link); //remove the link when done
        }
    };
    req.send();
}

export function downloadReport(reportId,error) {
    let getReport = {
        reportId: reportId
    }
    let json = JSON.stringify(getReport);
    let req = new HttpRequestCRUD();
    req.setFetch(url + '/getReport', json);
    // req.setContentType("application/json;charset=UTF-8")
    req.setContentType("application/vnd.oasis.opendocument.text;charset=UTF-8")
    let request = req.fetchBlob();
    request.then(
        () => {
            if (req.getStatus()) {
                let link = document.createElement('a');
                document.body.appendChild(link);
                // link.download = (decodeURI(req.getResponse()
                //     .headers.get('Content-Disposition')
                //     .match("(?<=(filename=\")).*?(?=(\"))")[0])).replace(/\+/g, " ");
                // link.href = window.URL.createObjectURL(req.getData());
                link.click();
                document.body.removeChild(link); //remove the link when done
            } else {
                let text_error = req.getErrorMessage();
                let stackTrace = req.getStackTrace();
                error(text_error,stackTrace);
            };
        }
    );
}