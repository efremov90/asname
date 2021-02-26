import React from 'react';

import {Modal} from './Modal.js';
import {Error} from './Error.js';
import {HttpRequestCRUD, url} from './System.js';
import {permissions} from "./App";

var md5 = require('md5');

export class Login extends React.Component {
  constructor(props) {
    super(props);
    this.handleClick = this.handleClick.bind(this);
    this.handleInputChange = this.handleInputChange.bind(this);
    this.state = {
      auth:{account:"",password:""}
      , modalError: null
    }
  }
  handleInputChange(e) {
    let auth = this.state.auth;
    auth[e.target.name]=e.target.value;
    this.setState({auth:auth});
  }
  handleClick() {
    let text_error = null;
    if (!this.state.auth.account) text_error = "Введите логин.";
    else if (!this.state.auth.password) text_error = "Введите пароль.";
    if (text_error) this.setState({modalError:(<Modal title={"Ошибка"} onOK={()=>{this.setState({modalError:null});}}>
        <Error errorMessage={text_error}/>
      </Modal>)});
    else {
      let account = this.state.auth.account;
      let password = md5(this.state.auth.password);
      console.log('Login handleClick: '+password);
      let basic = "Basic ";
      let AuthorizationHeader = basic + btoa(account + ":" + password);
      let req = new HttpRequestCRUD();
      req.setURL(url + "/login");
      req.setContentType("text/plain;charset=UTF-8");
      req.setAuthorization(AuthorizationHeader);
      console.log('Login handleClick: '+AuthorizationHeader);
      let request = req.fetchLogin();
      request.then(
          () => {
            if (req.getStatus()) {
              this.props.onLogin(true);
            } else {
              text_error = req.getErrorMessage();
              this.setState({modalError:(<Modal title={"Ошибка"} onOK={()=>{this.setState({modalError:null});}}>
                  <Error errorMessage={text_error}/>
                </Modal>)});
            }
          }
      );
    }
  }
  render() {
    return (
      <div>
        <div className="Header">AS Name</div>
        <div className="Login" style={{padding: '15px 15px',border:'solid 1px #ddd', width:'200px'}}>
          <table style={{fontSize: 'medium'}}>
            <tr>
              <td style={{['textAlign']:'right'}}>Логин:</td>
              <td style={{width: '100%'}}>
                <input
                    style={{width: '100%', fontSize:'medium', padding:'5px'}}
                    name="account"
                    type="text"
                    required
                    minLength="5"
                    maxLength="10"
                    pattern="[A-Za-z0-9]"
                    autoFocus
                    onChange={this.handleInputChange}
                    value={this.state.auth.account}
                />
              </td>
            </tr>
            <tr>
              <td style={{['textAlign']:'right'}}>Пароль:</td>
              <td>
                <input
                    style={{width: '100%', fontSize:'medium', padding:'5px'}}
                    name="password"
                    type="password"
                    required
                    size="10"
                    minLength="5"
                    maxLength="10"
                    pattern="[A-Za-z0-9]"
                    onChange={this.handleInputChange}
                    value={this.state.auth.password}
                />
              </td>
            </tr>
          </table>
          <div>
            <button style={{marginTop:'5px', width:'100%', fontSize:'medium'}} onClick={this.handleClick}>Вход</button>
          </div>
        </div>
        <div className="Footer">EAA © (2020)</div>
        {this.state.modalError}
      </div>
    );
  }
}
