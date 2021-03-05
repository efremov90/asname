let url = "http://localhost:8081/asname";

class HttpRequestCRUD {
    constructor() {
        this.response = null;
        this.data = null;
        this.status = false;
        this.form = null;
        this.url = url;
        this.options = {
            method: "POST",
            headers: {
                "Content-Type": "application/json;charset=utf-8"
            },
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

    getData() {
        return this.data;
    }

    getResponse() {
        return this.response;
    }

    fetchBlob() {
        return fetch(this.url, this.options).then(
            response => {
                this.response = response;
                if (response.ok) {
                    return response.blob();
                } else {
                    let errorForm = new ModalError();
                    errorForm.setErrorMessage(
                        "HTTP-статус " + response.status + "\n" +
                        response.statusText
                    );
                    if (this.form) errorForm.show(this.form);
                }
            }
        ).then(r => {
                if (r.error == null) {
                    this.status = true;
                    this.data = r;
                } else {
                    let errorForm = new ModalError();
                    errorForm.setErrorMessage(r.error.errorMessage);
                    errorForm.setStackTrace(r.error.stackTrace);
                    errorForm.show(this.form);
                }
            }
        ).catch(e => {
            let errorForm = new ModalError();
            errorForm.setErrorMessage(
                "Сервер не доступен \n" +
                e
            );
            // errorForm.setStackTrace(r.error.stackTrace);
            errorForm.show(this.form);
        })
    }

    fetchJSON() {
        return fetch(this.url, this.options).then(
            response => {
                if (response.ok) {
                    return response.json();
                } else {
                    let errorForm = new ModalError();
                    errorForm.setErrorMessage(
                        "HTTP-статус " + response.status + "\n" +
                        response.statusText
                    );
                    if (this.form) errorForm.show(this.form);
                }
            }
        ).then(r => {
                if (r.error == null) {
                    this.status = true;
                    this.data = r;
                } else {
                    let errorForm = new ModalError();
                    errorForm.setErrorMessage(r.error.errorMessage);
                    errorForm.setStackTrace(r.error.stackTrace);
                    errorForm.show(this.form);
                }
            }
        ).catch(e => {
            let errorForm = new ModalError();
            errorForm.setErrorMessage(
                "Сервер не доступен \n" +
                e
            );
            // errorForm.setStackTrace(r.error.stackTrace);
            errorForm.show(this.form);
        })
    }

    fetchHTML() {
        // alert('fetchHTML');
        return fetch(this.url, this.options).then(
            response => {
                if (response.ok) {
                    this.status = true;
                    return response.text();
                } else {
                    let errorForm = new ModalError();
                    errorForm.setErrorMessage(
                        "HTTP-статус " + response.status + "\n" +
                        response.statusText
                    );
                    if (this.form) errorForm.show(this.form);
                }
            }
        ).then(r => {
                this.data = (new DOMParser()).parseFromString(r, 'text/html').querySelector('div');
            }
        ).catch(e => {
            let errorForm = new ModalError();
            errorForm.setErrorMessage(
                "Сервер не доступен \n" +
                e
            );
            errorForm.show(this.form);
        })
    }

    fetchJS() {
        // alert('fetchJS');
        let url = this.url;
        if (url) {
            // alert('fetchJS');
            return new Promise(function (resolve, reject) {
                let script = document.createElement('script');
                script.src = url;
                script.type = 'text/javascript';
                script.onload = () => resolve(script);
                script.onerror = () => {
                    let errorForm = new ModalError();
                    errorForm.setErrorMessage(
                        "Сервер не доступен"
                    );
                    errorForm.show(this.form);
                }
                document.head.append(script);
            });
        }
    }
}

function formatDate(milliseconds) {
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

function formatTime(milliseconds) {
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

function dateTimeJSONToView(dtString, round) {
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

function getCookie(name) {
    var matches = document.cookie.match(new RegExp(
        '(?:^|\s)' + name.replace(/([.$?*+\\\/{}|()\[\]^])/g, '\\$1') + '=(.*?)(?:;|$)'
    ));
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

function downloadReport(reportId, form) {
    let getReport = {
        reportId: reportId
    }
    let json = JSON.stringify(getReport);
    let req = new HttpRequestCRUD();
    req.setFetch(url + '/getReport', json);
    // req.setContentType("application/json;charset=UTF-8")
    req.setContentType("application/vnd.oasis.opendocument.text;charset=UTF-8");
    req.setForm(form);
    let request = req.fetchBlob();
    request.then(
        () => {
            if (req.getStatus()) {
                let link = document.createElement('a');
                document.body.appendChild(link);
                link.download = (decodeURI(req.getResponse()
                    .headers.get('Content-Disposition')
                    .match("(?<=(filename=\")).*?(?=(\"))")[0])).replace(/\+/g, " ");
                link.href = window.URL.createObjectURL(req.getData());
                link.click();
                document.body.removeChild(link); //remove the link when done
            }
        }
    );
}