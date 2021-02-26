import React from 'react';

import {MainForm} from './MainForm.js';
import {Login} from './Login.js';
import {getCookie, HttpRequestCRUD, url} from "./System";
import {Modal} from "./Modal";
import {Error} from "./Error";

export let permissions = new Set();
// export let permissions = new Set();
// permissions.add("REQUESTS_VIEW");
// permissions.add("CLIENTS_VIEW");
// permissions.add("REPORTS_VIEW");
// permissions.add("REPORT_GENERATE_REQUESTS_DETAILED");
// permissions.add("REPORT_GENERATE_REQUESTS_CONSOLIDATED");

export class App extends React.Component {
    constructor(props) {
        super(props);
        this.handleLogin = this.handleLogin.bind(this);
        this.handleGetPermissions = this.handleGetPermissions.bind(this);
        this.state = {
            isLogin:null
            , modalError:null
        }
    }
    handleGetPermissions() {
        let reqPerm = new HttpRequestCRUD();
        reqPerm.setMethod("GET");
        reqPerm.setFetch(url + "/getPermissions");
        let requestPerm = reqPerm.fetchJSON();
        requestPerm.then(
            () => {
                if (reqPerm.getStatus() && !reqPerm.getErrorMessage() && !reqPerm.getStackTrace()) {
                    let data = reqPerm.getData();
                    permissions = new Set(data.permissions);
                    this.setState({isLogin:true});
                } else {
                    let text_error = reqPerm.getErrorMessage();
                    this.setState({modalError:(<Modal title={"Ошибка"} onOK={()=>{this.setState({modalError:null});}}>
                            <Error errorMessage={text_error}/>
                        </Modal>)});
                }
            }
        );
        console.log(`handleGetPermissions permissions.has('REQUESTS_VIEW') ${permissions.has('REQUESTS_VIEW')}`);
        console.log('handleGetPermissions finish');
    }
    componentDidMount() {
        console.log('App componentDidMount');
        let req = new HttpRequestCRUD();
        req.setURL(url + "/login");
        req.setContentType("text/plain;charset=UTF-8");
        let request = req.fetchLogin();
        request.then(
            () => {
                if (req.getStatus()) {
                    this.handleGetPermissions();
                } else {
                    this.setState({isLogin:false});
                }
            }
        );
    }
    handleLogin(isLogin) {
        console.log('App handleLogin');
        if (isLogin) document.location.reload();
        this.setState({isLogin:isLogin});
    }
    render() {
        return <>
            {(this.state.isLogin) ? <MainForm/> : (!(this.state.isLogin==null) ? <Login onLogin={this.handleLogin}/> : null)}
            {this.state.modalError}
            </>;
    }
}