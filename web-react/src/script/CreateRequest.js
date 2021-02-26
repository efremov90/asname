import React from 'react';

import { ClientCode } from './Client.js';
import {ChoiceObject} from './ElementsWindow.js';
import {Modal} from './Modal.js';
import {Error} from './Error.js';
import {HttpRequestCRUD} from './System.js';
import {url} from './System.js';

export class Comment extends React.Component {
  constructor(props) {
    super(props);
    this.handleChange = this.handleChange.bind(this);
  }
  handleChange(e) {
    this.props.onChange(e.target.name, e.target.value);
  }
  render() {
    return (
      <>
        <textarea
          name="comment"
          rows="3"
          maxLength="150"
          style={{width:'250px'}}
          onChange={this.handleChange}
        ></textarea>
      </>
    );
  }
}

export class CreateRequest extends React.Component {
  constructor(props) {
    super(props);
    let createRequest = {
        clientCode:""
        ,comment:""
    }
    this.state = {modalError: null, isChange:false, createRequest:createRequest};
    this.handleInputChange = this.handleInputChange.bind(this);
    this.handleOK = this.handleOK.bind(this);
    props.setOK(this.handleOK);
    this.handleCancel = this.handleCancel.bind(this);
    props.setCancel(this.handleCancel);
    this.handleChoice = this.handleChoice.bind(this);
    this.props.setModalSize(370,0);
  }
  handleInputChange(name, value) {
    this.setState({isChange: true});
    let createRequest = this.state.createRequest;
    createRequest[name]=value;
    this.setState({createRequest:createRequest});
  }
  handleOK() {
    let text_error = null;
    if (!this.state.createRequest.clientCode) text_error = "Введите код клиента.";
    if (text_error) this.setState({modalError:(<Modal title={"Ошибка"} onOK={()=>{this.setState({modalError:null});}}>
      <Error errorMessage={text_error}/>
      </Modal>)});
    else {
      let createRequest = {
        request: {
          requestId: 0,
          clientCode: this.state.createRequest.clientCode,
          comment: this.state.createRequest.comment,
          lastUserAccountIdChangeRequestStatus: 0
        }
      }
      //alert('JSON');
      let json = JSON.stringify(createRequest);
      let req = new HttpRequestCRUD();
      req.setFetch(url + '/createRequest', json);
      let request = req.fetchJSON();
      request.then(
          () => {
            if (req.getStatus()) {
              this.props.onConfirmClose();
              this.props.onRefresh();
            } else {
              let text_error = req.getErrorMessage();
              let stackTrace = req.getStackTrace();
              this.setState({modalError:(<Modal title={"Ошибка"} onOK={()=>{this.setState({modalError:null});}}>
                  <Error errorMessage={text_error} stackTrace={stackTrace}/>
                </Modal>)});
            }
          }
      );
    }
  }
  handleCancel() {
    if (this.state.isChange) {
      this.setState({modalError:(<Modal title={"Предупреждение"} onOK={()=>{this.props.onConfirmClose();}} onCancel={()=>{this.setState({modalError:null});}}>
      <Error errorMessage={"Изменения не будут сохранены. Продолжить?"}/>
      </Modal>)});
    } else {
      this.props.onConfirmClose();
    }
  }
  handleChoice(items) {
    let clientCode = (items) ? items[0].clientCode : "";
    let createRequest = this.state.createRequest;
    createRequest.clientCode=clientCode;
    this.setState({createRequest:createRequest});
  }
  render() {
    return (
      <>
       <table>
          <tr>
            <td style={{['textAlign']:'right'}}>Код:</td>
            <td>
              <ClientCode onChange={this.handleInputChange} value={this.state.createRequest.clientCode}/>
              <ChoiceObject onOK={this.handleChoice}/>
            </td>
          </tr>
          <tr>
            <td style={{['textAlign']:'right'}}>Комментарий:</td>
            <td>
              <Comment onChange={this.handleInputChange}/>
            </td>
          </tr>
        </table>
        {this.state.modalError}
      </>
    );
  }
}