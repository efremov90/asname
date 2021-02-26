import React from 'react';

import { ButtonBar } from './ElementsWindow.js';
import { ClientDopoffice } from './ClientDopoffice.js';
import { ClientSelfservice } from './ClientSelfservice.js';

class ClientType extends React.Component {
  constructor(props) {
    super(props);
    this.handleChange = this.handleChange.bind(this);
  }
  handleChange(e) {
    this.props.onChangeClientType(e.target.value);
  }
  render() {
    return (
      <>
        Тип клиента: <select onChange={this.handleChange} disabled={(this.props.editMode) ? "disabled" : null} value={this.props.value}>
          <option value="DOPOFFICE">ВСП</option>
          <option value="SELFSERVICE">УС</option>
        </select>
      </>
    );
  }
}

export class ClientCode extends React.Component {
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
        <input
          name="clientCode"
          type="text"
          required
          size="10"
          maxLength="10"
          pattern="[A-Za-z0-9]"
          onChange={this.handleChange}
          disabled={(this.props.editMode) ? "disabled" : null}
          value={(this.props.value) ? this.props.value : ""}
        />
      </>
    );
  }
}

export class ClientName extends React.Component {
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
          name="clientName"
          rows="2"
          required
          maxLength="50"
          style={{width:'250px'}}
          onChange={this.handleChange}
          value={(this.props.value) ? this.props.value : ""}
        ></textarea>
      </>
    );
  }
}

export class Addres extends React.Component {
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
          name="address"
          rows="3"
          maxLength="150"
          style={{width:'250px'}}
          onChange={this.handleChange}
          value={(this.props.value) ? this.props.value : ""}
        ></textarea>
      </>
    );
  }
}

export class CloseDate extends React.Component {
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
        <input
          name="closeDate"
          type="date"
          onChange={this.handleChange}
          value={(this.props.value) ? this.props.value : ""}
        />
      </>
    );
  }
}

export class Client extends React.Component {
  constructor(props) {
    super(props);
    this.handleChangeClientType = this.handleChangeClientType.bind(this);
    let clientType;
    if (!this.props.editMode) clientType='DOPOFFICE';
    if (this.props.editMode) {
      if (this.props.client.dopoffice) clientType='DOPOFFICE';
      if (this.props.client.selfservice) clientType='SELFSERVICE';
    };
    this.state = { clientType: clientType};
    //alert('Client constructor');
    if (clientType == 'DOPOFFICE') this.props.setModalSize(380,240);
    if (clientType == 'SELFSERVICE') this.props.setModalSize(380,275);
  }
  handleChangeClientType(clientType) {
    this.setState({ clientType: clientType });
    //alert(clientType);
    if (clientType == 'DOPOFFICE') this.props.setModalSize(380,240);
    if (clientType == 'SELFSERVICE') this.props.setModalSize(380,275);
  }
  render() {
    let editMode = (this.props.editMode) ? true : false;
    let client = (this.props.client) ? this.props.client : null;
    return (
      <>
        <ButtonBar>
          <ClientType editMode={editMode} onChangeClientType={this.handleChangeClientType} value={this.state.clientType}/>
        </ButtonBar>
        {this.state.clientType == 'DOPOFFICE' ? <ClientDopoffice editMode={editMode} client={client} setOK={this.props.setOK} setCancel={this.props.setCancel} onConfirmClose={this.props.onConfirmClose} onRefresh={this.props.onRefresh}/> : null}
        {this.state.clientType == 'SELFSERVICE' ? <ClientSelfservice editMode={editMode} client={client} setOK={this.props.setOK} setCancel={this.props.setCancel} onConfirmClose={this.props.onConfirmClose} onRefresh={this.props.onRefresh}/> : null}
      </>
    );
  }
}