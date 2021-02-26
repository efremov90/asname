import React from 'react';

import { ClientCode } from './Client.js';
import { ClientName } from './Client.js';
import { Addres } from './Client.js';
import { CloseDate } from './Client.js';
import {Modal} from './Modal.js';
import {Error} from './Error.js';
import {HttpRequestCRUD} from "./System";

export class ClientDopoffice extends React.Component {
  constructor(props) {
    super(props);
    this.handleInputChange = this.handleInputChange.bind(this);
    this.handleOK = this.handleOK.bind(this);
    props.setOK(this.handleOK);
    this.handleCancel = this.handleCancel.bind(this);
    props.setCancel(this.handleCancel);
    let client = {
        clientCode:""
        ,clientName:""
        ,address:""
        ,closeDate:""
    }
    let propsClient = null;
    if (this.props.client) propsClient=this.props.client.dopoffice;
    if (propsClient) client={
        clientCode:propsClient.clientCode
        ,clientName:propsClient.clientName
        ,address:propsClient.address
        ,closeDate:propsClient.closeDate
    } 
    this.state = {
      client: client
      , modalError: null
      , isChange:false
    };
  }
  handleInputChange(name, value) {
    this.setState({isChange: true});
    let client = this.state.client;
    client[name]=value;
    this.setState({client:client});
  }
  handleOK() {
    let text_error = null;
    if (!this.state.client.clientCode) text_error = "Введите код клиента.";
    else if (!this.state.client.clientName) text_error = "Введите наименование клиента.";
    if (text_error) this.setState({modalError:(<Modal title={"Ошибка"} onOK={()=>{this.setState({modalError:null});}}>
      <Error errorMessage={text_error}/>
      </Modal>)});
    else {
      let client = {
        dopoffice: {
          id: 0,
          clientCode: this.state.client.clientCode,
          clientName: this.state.client.clientName,
          address: this.state.client.address,
          closeDate: this.state.client.closeDate
        }
      }
      let json = JSON.stringify(client);
      let req = new HttpRequestCRUD();
      req.setFetch("http://localhost:8081/asname/mvc" + (this.props.editMode ? '/editClient' : '/createClient'), json);
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
  render() {
    let client = this.state.client;
    return (
      <>
        <table>
          <tr>
            <td style={{['textAlign']:'right'}}>Код:</td>
            <td>
              <ClientCode editMode={this.props.editMode} onChange={this.handleInputChange} value={client.clientCode}/>
            </td>
          </tr>
          <tr>
            <td style={{['textAlign']:'right'}}>Наименование:</td>
            <td>
              <ClientName onChange={this.handleInputChange} value={client.clientName}/>
            </td>
          </tr>
          <tr>
            <td style={{['textAlign']:'right'}}>Адрес:</td>
            <td>
              <Addres onChange={this.handleInputChange} value={client.address}/>
            </td>
          </tr>
          <tr>
            <td style={{['textAlign']:'right'}}>Дата закрытия:</td>
            <td>
              <CloseDate onChange={this.handleInputChange} value={client.closeDate}/>
            </td>
          </tr>
        </table>
        {this.state.modalError}
      </>
    );
  }
}
