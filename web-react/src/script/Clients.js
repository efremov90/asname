import React from 'react';

import {ButtonBar} from './ElementsWindow.js';
import {TitleForm} from './ElementsWindow.js';
import {Client} from './Client.js';
import {Modal} from './Modal.js';
import {Grid} from './Grid.js';
import {formatDate, HttpRequestCRUD, url} from "./System";
import {permissions} from "./App.js";

class ClearFilter extends React.Component {
  constructor(props) {
    super(props);
    this.handleClick = this.handleClick.bind(this);
  }
  handleClick() {
    this.props.onClearFilter();
  }
  render() {
    return <button onClick={this.handleClick}>Очистить</button>;
  }
}

class Search extends React.Component {
  constructor(props) {
    super(props);
    this.handleClick = this.handleClick.bind(this);
    props.setRefresh(this.handleClick);
    this.state = {modalError: null};
  }
  handleClick() {
    let search = {
      clientType: this.props.searchParams.clientType
    };
    let json = JSON.stringify(search);
    let req = new HttpRequestCRUD();
    req.setFetch("http://localhost:8081/asname/mvc" + "/getClients", json);
    let request = req.fetchJSON();
    request.then(
        () => {
          if (req.getStatus()) {
            let data = req.getData();
            // alert('Requests Search handleClick: '+data.items.length);
            this.props.onSearch(data.items);
          }
        }
    );
  }
  render() {
    return <><button onClick={this.handleClick}>Поиск</button>
    {this.state.modalError}</>;
  }
}

class CreateClientButton extends React.Component {
  constructor(props) {
    super(props);
    this.state = {modal: null};
    this.handleShow = this.handleShow.bind(this);
    this.handleSetOK = this.handleSetOK.bind(this);
    this.handleOK = this.handleOK.bind(this);
    this.handleSetCancel = this.handleSetCancel.bind(this);
    this.handleCancel = this.handleCancel.bind(this);
    this.handleConfirmClose = this.handleConfirmClose.bind(this);
    this.setModalSize = this.setModalSize.bind(this);
    this.state={showModal:false,size:null}
  }
  handleShow() {
    this.setState({showModal:true});
  }
  handleSetOK(func) {
    this.setState({onOK:func});
  }
  handleOK() {
    this.state.onOK();
  }
  handleSetCancel(func) {
    this.setState({onCancel:func});
  }
  handleCancel() {
    this.state.onCancel();
  }
  handleConfirmClose() {
    this.setState({showModal: false});
  }
  setModalSize(width,height) {
    //alert('Clients CreateClientButton setShowSize');
    this.setState({size:{width:width,height:height}});
  }
  render() {
    let modal = <Modal title={"Создание клиента"} onOK={this.handleOK} onCancel={this.handleCancel} size={this.state.size}>
      <Client setOK={this.handleSetOK} setCancel={this.handleSetCancel} onConfirmClose={this.handleConfirmClose} setModalSize={this.setModalSize}/>
      </Modal>;
    return <>
      {permissions.has("CLIENTS_CREATE") ? <button onClick={this.handleShow}>Создать</button> : null}
    {(this.state.showModal) ? modal : null}
    </>;
  }
}

class EditClientButton extends React.Component {
  constructor(props) {
    super(props);
    this.state = {modal: null};
    this.handleShow = this.handleShow.bind(this);
    this.handleSetOK = this.handleSetOK.bind(this);
    this.handleOK = this.handleOK.bind(this);
    this.handleSetCancel = this.handleSetCancel.bind(this);
    this.handleCancel = this.handleCancel.bind(this);
    this.handleConfirmClose = this.handleConfirmClose.bind(this);
    this.setModalSize = this.setModalSize.bind(this);
    this.state={showModal:false,size:null}
  }
  handleShow() {
    let client = {
      clientCode: this.props.clientCode
    };

    let json = JSON.stringify(client);
    //alert(json);
    let req = new HttpRequestCRUD();
    // req.setFetch(url + "/getClient", json);
    req.setFetch("http://localhost:8081/asname/mvc" + "/getClient", json);
    let request = req.fetchJSON();
    request.then(
        () => {
          if (req.getStatus()) {
            let data = req.getData();
            this.setState({
              client:data
              , showModal:true
            });
          }
        }
    );
  }
  handleSetOK(func) {
    this.setState({onOK:func});
  }
  handleOK() {
    this.state.onOK();
  }
  handleSetCancel(func) {
    this.setState({onCancel:func});
  }
  handleCancel() {
    this.state.onCancel();
  }
  handleConfirmClose() {
    this.setState({showModal:false});
  }
  setModalSize(width,height) {
    //alert('Clients CreateClientButton setShowSize');
    this.setState({size:{width:width,height:height}});
  }
  render() {
    //let data = {clientDopoffice:{clientCode:"1001",clientName:"N 1001",address:"A 1001",closeDate:"2020-01-20"}}
    // let data = {clientSelfservice:{clientCode:"2001",clientName:"N 2001",address:"A 2001",atmType:"TERMINAL",closeDate:"2020-01-20"}}
    let modal = <Modal title={"Редактирование клиента"} onOK={this.handleOK} onCancel={this.handleCancel} size={this.state.size}>
      <Client editMode={true} client={this.state.client} setOK={this.handleSetOK} setCancel={this.handleSetCancel} onRefresh={this.props.onRefresh} onConfirmClose={this.handleConfirmClose} setModalSize={this.setModalSize}/>
      </Modal>;
    return <>
      <button onClick={this.handleShow} disabled={(this.props.disabled) ? true : false}>Редактировать</button>
    {(this.state.showModal) ? modal : null}
    </>;
  }
}

class ClientType extends React.Component {
  constructor(props) {
    super(props);
    this.handleChange = this.handleChange.bind(this);
  }
  handleChange(e) {
    this.props.onInputChangeSearchParams('clientType',e.target.value);
  }
  render() {
    return <>Тип клиента: <select value={this.props.value} onChange={this.handleChange}>
          <option value="ALL">Все</option>
          <option value="DOPOFFICE">ВСП</option>
          <option value="SELFSERVICE">УС</option>
        </select>
        </>;
  }
}

class ClientsAllGrid extends React.Component {
  constructor(props) {
    super(props);
  }
  render() {
    let fields = {
        clientCode:{
          name:'Код'
          , width:70
        }
      , clientName:{
          name:'Наименование клиента'
          , width:120
        }
      , clientTypeDescription:{
          name:'Тип клиента'
          , width:70
          , fieldFilter:function(value) {
              return <select value={value}>
              <option value="Все">Все</option>
              <option value="ВСП">ВСП</option>
              <option value="УС">УС</option>
              </select>;
          }
          , style:function(item) {
              return {textAlign:'center'}
            }
          , filter: 'Все'
        }
      , address: {
          name:'Адрес'
          , width:150
        }
      , closeDate: {
          name:'Дата закрытия'
          , width:85
          , type:'date'
          , style:function(item) {
              return {textAlign:'center'}
            }
        }}
    return <>
    <Grid fields={fields} items={this.props.items} defaultSort={{code:'clientName',direction:'asc'}} setClear={this.props.setClearFilter} setLoad={this.props.setLoad} onCheckedItems={this.props.onCheckedItems}/>
    </>;
  }
}

class ClientsSelfServiceGrid extends React.Component {
  constructor(props) {
    super(props);
  }
  render() {
    let fields = {
      clientCode:{
        name:'Код'
        , width:70
      }
      , clientName:{
        name:'Наименование клиента'
        , width:120
      }
      , clientTypeDescription:{
        name:'Тип клиента'
        , width:70
        , fieldFilter:function(value) {
          return <select value={value}>
            <option value="Все">Все</option>
            <option value="ВСП">ВСП</option>
            <option value="УС">УС</option>
          </select>;
        }
        , style:function(item) {
          return {textAlign:'center'}
        }
        , filter: 'Все'
      }
      , atmTypeDescription:{
        name:'Тип банкомата'
        , width:80
        , fieldFilter:function(value) {
          return <select value={value}>
            <option value="Все">Все</option>
            <option value="Банкомат">Банкомат</option>
            <option value="Терминал">Терминал</option>
          </select>;
        }
        , style:function(item) {
          return {textAlign:'center'}
        }
        , filter: 'Все'
      }
      , address: {
        name:'Адрес'
        , width:150
      }
      , closeDate: {
        name:'Дата закрытия'
        , width:85
        , type:'date'
        , style:function(item) {
          return {textAlign:'center'}
        }
      }}
    return <>
      <Grid fields={fields} items={this.props.items} defaultSort={{code:'clientName',direction:'asc'}} setClear={this.props.setClearFilter} setLoad={this.props.setLoad} onCheckedItems={this.props.onCheckedItems}/>
    </>;
  }
}

export class Clients extends React.Component {
  constructor(props) {
    super(props);
    this.handleInputChangeSearchParams = this.handleInputChangeSearchParams.bind(this);
    this.handleClearFilter = this.handleClearFilter.bind(this);
    this.setClearFilter = this.setClearFilter.bind(this);
    this.handleSearch = this.handleSearch.bind(this);
    this.handleRefresh = this.handleRefresh.bind(this);
    this.setRefresh = this.setRefresh.bind(this);
    this.setLoad = this.setLoad.bind(this);
    this.handleCheckedItems = this.handleCheckedItems.bind(this);
    this.handleDisabledButton = this.handleDisabledButton.bind(this);
    let searchParams = {
      clientType: 'ALL'
    };
    this.state = {
      disabledEditClientButton:true
      , searchParams: searchParams
      , clientCodeEdit: null
    }
  }
  handleInputChangeSearchParams(name, value) {
    let searchParams = this.state.searchParams;
    searchParams[name]=value;
    this.setState({searchParams:searchParams});
    this.state.onLoad(null);
  }
  setLoad(func) {
    this.setState({onLoad:func});
  }
  setRefresh(func) {
    this.setState({onRefresh:func});
  }
  handleRefresh() {
    this.state.onRefresh();
  }
  handleSearch(items) {
    this.state.onLoad(items);
  }
  setClearFilter(func) {
    this.setState({onClearFilter:func});
  }
  handleClearFilter() {
    this.state.onClearFilter();
  }
  handleDisabledButton(checkedItems) {
    let disabledEditClientButton;
    if (!checkedItems) {
      disabledEditClientButton = true;
    } else if (checkedItems.length>=2) {
      disabledEditClientButton = true;
    } else if (checkedItems.length==1) {
      disabledEditClientButton = false;
    }
    this.setState({
      disabledEditClientButton:disabledEditClientButton
    });
  }
  handleCheckedItems(checkedItems) {
    this.handleDisabledButton(checkedItems);
    if (this.props.onCheckedItems) this.props.onCheckedItems(checkedItems);
    if (checkedItems) this.setState({clientCodeEdit: (checkedItems.length==1 ? checkedItems[0].clientCode : null)});
  }
  render() {
    return <div data-display={this.props.dataDisplay}>
        {(this.props.choiceMode) ? null : <TitleForm name={"Клиенты"}/>}
        <ButtonBar>
          <ClearFilter onClearFilter={this.handleClearFilter}/>
          <Search onSearch={this.handleSearch} setRefresh={this.setRefresh} searchParams={this.state.searchParams}/>
          {(this.props.choiceMode) ? null : <CreateClientButton onRefresh={this.handleRefresh}/>}
          {(this.props.choiceMode) ? null : <EditClientButton clientCode={this.state.clientCodeEdit} disabled={this.state.disabledEditClientButton} onRefresh={this.handleRefresh}/>}
        </ButtonBar>
        <ButtonBar>
          <ClientType onInputChangeSearchParams={this.handleInputChangeSearchParams} value={this.state.searchParams.clientType}/>
        </ButtonBar>
        <div className="Content">
          {(this.state.searchParams.clientType != 'SELFSERVICE') ? <ClientsAllGrid setClearFilter={this.setClearFilter} setLoad={this.setLoad} onCheckedItems={this.handleCheckedItems}/> : null}
          {(this.state.searchParams.clientType == 'SELFSERVICE') ? <ClientsSelfServiceGrid setClearFilter={this.setClearFilter} setLoad={this.setLoad} onCheckedItems={this.handleCheckedItems}/> : null}
        </div>
    </div>;
  }
}