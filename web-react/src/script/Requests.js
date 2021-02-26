import React from 'react';

import {ButtonBar} from './ElementsWindow.js';
import {TitleForm} from './ElementsWindow.js';
import {formatDate} from './System.js';
import {Modal} from './Modal.js';
import {Error} from './Error.js';
import {Grid} from './Grid.js';
import {CreateRequest} from './CreateRequest.js';
import {CancelRequest} from './CancelRequest.js';
import {ViewRequest} from './ViewRequest.js';
import {HttpRequestCRUD} from './System.js';
import {url} from './System.js';
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
      fromCreateDate: this.props.searchParams.fromRequestDate,
      toCreateDate: this.props.searchParams.toRequestDate
    };
    let json = JSON.stringify(search);
    let req = new HttpRequestCRUD();
    req.setFetch(url + "/getRequests", json);
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

class ViewRequestButton extends React.Component {
  constructor(props) {
    super(props);
    this.state = {modal: null};
    this.handleShow = this.handleShow.bind(this);
    this.handleConfirmClose = this.handleConfirmClose.bind(this);
    this.setModalSize = this.setModalSize.bind(this);
    this.state={showModal:false,size:null}
  }
  handleShow() {
    this.setState({showModal:true});
  }
  handleConfirmClose() {
    this.setState({showModal: false});
  }
  setModalSize(width,height) {
    this.setState({size:{width:width,height:height}});
  }
  render() {
    let modal = <Modal title={"Просмотр заявки"} onCancel={this.handleConfirmClose} size={this.state.size}>
      <ViewRequest requestId={this.props.requestId} setModalSize={this.setModalSize}/>
      </Modal>;
    return <>
      {permissions.has("REQUESTS_VIEW_REQUEST") ? <button onClick={this.handleShow} disabled={(this.props.disabled) ? true : false}>Просмотр</button> : null}
    {(this.state.showModal) ? modal : null}
    </>;
  }
}

class CreateRequestButton extends React.Component {
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
    this.setState({showModal:false});
  }
  setModalSize(width,height) {
    this.setState({size:{width:width,height:height}});
  }
  render() {
    let modal = <Modal title={"Создание заявки"} onOK={this.handleOK} onCancel={this.handleCancel} size={this.state.size}>
      <CreateRequest setOK={this.handleSetOK} setCancel={this.handleSetCancel} onConfirmClose={this.handleConfirmClose} onRefresh={this.props.onRefresh} setModalSize={this.setModalSize}/>
      </Modal>;
    return <>
      {permissions.has("REQUESTS_CREATE") ? <button onClick={this.handleShow}>Создать</button> : null}
    {(this.state.showModal) ? modal : null}
    </>;
  }
}

class CancelRequestButton extends React.Component {
  constructor(props) {
    super(props);
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
    this.state.onOK(this.props.requestId);
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
    this.setState({size:{width:width,height:height}});
  }
  render() {
    let modal = <Modal title={"Отмена заявки"} onOK={this.handleOK} onCancel={this.handleCancel} size={this.state.size}>
      <CancelRequest setOK={this.handleSetOK} setCancel={this.handleSetCancel} onConfirmClose={this.handleConfirmClose} onRefresh={this.props.onRefresh} setModalSize={this.setModalSize}/>
      </Modal>;
    return <>
      {permissions.has("REQUESTS_CANCEL") ? <button onClick={this.handleShow} disabled={(this.props.disabled) ? true : false}>Отменить</button> : null}
    {(this.state.showModal) ? modal : null}
    </>;
  }
}

class FromRequestDate extends React.Component {
  constructor(props) {
    super(props);
    this.handleChange = this.handleChange.bind(this);
  }
  handleChange(e) {
    this.props.onInputChangeSearchParams('fromRequestDate',e.target.value);
  }
  render() {
    let curDate = Date.now();
    return <>Дата заявки с: <input type="date" autoFocus max={formatDate(curDate+1000 * 60 * 60 * 24 * 1)} value={this.props.value} onChange={this.handleChange}/></>;
  }
}

class ToRequestDate extends React.Component {
  constructor(props) {
    super(props);
    this.state = {value: (Date.now())};
    this.handleChange = this.handleChange.bind(this);
  }
  handleChange(e) {
    this.props.onInputChangeSearchParams('toRequestDate',e.target.value);
  }
  render() {
    let curDate = Date.now();
    return <> по: <input type="date" max={formatDate(curDate+1000 * 60 * 60 * 24 * 1)} value={this.props.value} onChange={this.handleChange}/></>;
  }
}

class RequestsGrid extends React.Component {
  constructor(props) {
    super(props);
    this.handleEditClient = this.handleEditClient.bind(this);
  }
  handleEditClient() {

  }
  render() {
    let fields = {
        createDate:{
          name:'Дата заявки'
          , width:85
          , type:'date'
          , style:function(item) {
              return {textAlign:'center'}
            }
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
              let backgroundColor = null;
              switch (item['clientType']) {
                case 'DOPOFFICE':
                  backgroundColor = '#C0FFC2';
                  break;
                case 'SELFSERVICE':
                  backgroundColor = '#FDFFBA';
                  break;
                default:
                  backgroundColor = null;
                  break;
              }
              return {textAlign:'center',backgroundColor:backgroundColor}
          }
          , filter: 'Все'
        }
      , comment:{
          name:'Комментарий'
          , width:140
        }
      , requestStatusDescription:{
          name:'Статус'
          , width:80
          , fieldFilter:function(value) {
              return <select value={value}>
              <option value="Все">Все</option>
              <option value="Создана">Создана</option>
              <option value="Отменена">Отменена</option>
              <option value="Закрыта">Закрыта</option>
              </select>;
          }
          , style:function(item) {
              return {textAlign:'center'}
            }
          , filter: 'Все'
        }
      , commentRequestStatus:{
          name:'Комментарий к статусу'
          , width:140
        }
      , lastDateTimeChangeRequestStatus:{
          name:'Время последнего изменения статуса'
          , width:130
          , type: 'dateTime'
          , style:function(item) {
              return {textAlign:'center'}
            }
        }
      , lastUserNameChangeRequestStatus:{
          name:'Последний пользователь, изменивший статус'
          , width:190
        }
      , createSystemName:{
          name:'Система-создатель'
          , width:75
        }
    }
    let itemStyle = function(item) {
        let backgroundColor = null;
        if (item['requestStatus']=='CANCELED') {
          backgroundColor='#FA4659';
        }
        return {backgroundColor:backgroundColor}
    }
    return <>
    <Grid fields={fields} items={this.props.items} itemStyle={itemStyle}  defaultSort={{code:'createDatetime',direction:'desc'}} setClear={this.props.setClearFilter} setLoad={this.props.setLoad} onCheckedItems={this.props.onCheckedItems}/>
    </>;
  }
}

export class Requests extends React.Component {
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
      fromRequestDate: formatDate(Date.now()-1000 * 60 * 60 * 24 * 31),
      toRequestDate: formatDate(Date.now())
    };
    this.state = {
      disabledCancelRequestButton:true
      , disabledViewRequestButton:true
      , searchParams: searchParams
      , requestIdChecked: null
    }
  }
  handleInputChangeSearchParams(name, value) {
    let searchParams = this.state.searchParams;
    searchParams[name]=value;
    this.setState({searchParams:searchParams});
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
    //alert('Requests handleDisabledButton');
    //alert(checkedItems[0].requestStatus);
    let disabledViewRequestButton;
    let disabledCancelRequestButton;
    if (!checkedItems) {
      disabledCancelRequestButton = true;
      disabledViewRequestButton = true;
    } else if (checkedItems.length>=2) {
      disabledCancelRequestButton = true;
      disabledViewRequestButton = true;
    } else if (checkedItems.length==1) {
      disabledViewRequestButton = false;
      if (checkedItems[0].requestStatus=='CREATED') disabledCancelRequestButton = false;
      else disabledCancelRequestButton = true;
    }
    this.setState({
      disabledCancelRequestButton:disabledCancelRequestButton
      , disabledViewRequestButton:disabledViewRequestButton
    });
  }
  handleCheckedItems(checkedItems) {
    this.handleDisabledButton(checkedItems);
    if (checkedItems) this.setState({requestIdChecked: (checkedItems.length==1 ? checkedItems[0].requestId : null)});
  }
  render() {
    return <div data-display={this.props.dataDisplay}>
        <TitleForm name={"Заявки"}/>
        <ButtonBar>
          <ClearFilter onClearFilter={this.handleClearFilter}/>
          <Search onSearch={this.handleSearch} setRefresh={this.setRefresh} searchParams={this.state.searchParams}/>
          <ViewRequestButton disabled={this.state.disabledViewRequestButton} requestId={this.state.requestIdChecked}/>
          <CreateRequestButton onRefresh={this.handleRefresh}/>
          <CancelRequestButton disabled={this.state.disabledCancelRequestButton} requestId={this.state.requestIdChecked} onRefresh={this.handleRefresh}/>
        </ButtonBar>
        <ButtonBar>
          <FromRequestDate onInputChangeSearchParams={this.handleInputChangeSearchParams} value={this.state.searchParams.fromRequestDate}/>
          <ToRequestDate onInputChangeSearchParams={this.handleInputChangeSearchParams} value={this.state.searchParams.toRequestDate}/>
        </ButtonBar>
        <div className="Content">
            <RequestsGrid setClearFilter={this.setClearFilter} setLoad={this.setLoad} onCheckedItems={this.handleCheckedItems}/>
        </div>
    </div>;
  }
}