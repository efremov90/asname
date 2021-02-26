import React from 'react';

import {Modal} from './Modal.js';
import {Error} from './Error.js';
import {Grid} from './Grid.js';
import {convertValue} from './Grid.js';
import {formatDate, dateTimeJSONToView, HttpRequestCRUD, url} from './System.js';

class Request extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
        request: {
            requestId:""
            , createDateTime:""
            , clientCode:""
            , comment:""
        }
    }
  }
  componentDidMount() {
      let dataReq = {
          requestId: this.props.requestId
      };

      let json = JSON.stringify(dataReq);
      //alert(json);
      let req = new HttpRequestCRUD();
      req.setFetch(url + "/getRequest", json);
      let request = req.fetchJSON();
      request.then(
          () => {
              if (req.getStatus()) {
                  let data = req.getData();
                  // alert('Requests Search handleClick: '+data.items.length);
                  this.setState({request: data.request});
              }
          }
      );
  }
  render() {
    let r = this.state.request;
    return <table>
        <tr>
            <td style={{['text-align']:'right'}}>Id заявки:</td>
            <td><input data-field="requestId" disabled value={r.requestId}/></td>
            <td style={{['text-align']:'right'}}>Дата и время создания:</td>
            <td><input data-field="createDatetime" disabled value={dateTimeJSONToView(r.createDatetime,'ss')}/></td>
        </tr>
        <tr>
            <td style={{['text-align']:'right'}}>Код объекта:</td>
            <td><input data-field="clientCode" disabled value={r.clientCode}/></td>
        </tr>
        <tr>
            <td>Комментарий:</td>
            <td><textarea data-field="comment" disabled rows="3" style={{width:'250px'}}
            value={r.comment}></textarea></td>
        </tr>
    </table>;
  }
}

class RequestHistoryGrid extends React.Component {
  constructor(props) {
    super(props);
    this.state={items:null}
  }
    componentDidMount() {
        let dataReq = {
            requestId: this.props.requestId
        };

        let json = JSON.stringify(dataReq);
        //alert(json);
        let req = new HttpRequestCRUD();
        req.setFetch(url + "/getRequestHistory", json);
        let request = req.fetchJSON();
        request.then(
            () => {
                if (req.getStatus()) {
                    let data = req.getData();
                    // alert('Requests Search handleClick: '+data.items.length);
                    this.setState({items: data.items});
                }
            }
        );
    }
  render() {
    let fields = {
        statusDescription:{
          name:'Статус'
          , width:80
          , style:function(item) {
              return {textAlign:'center'}
            }
        }
      , user:{
          name:'Пользователь'
          , width:190
        }
      , eventDateTime:{
          name:'Дата и время'
          , width:130
          , type: 'dateTime'
          , style:function(item) {
              return {textAlign:'center'}
            }
        }
      , comment:{
          name:'Комментарий'
          , width:140
        }
    }
    return <>
    <Grid fields={fields} items={this.state.items} defaultSort={{code:'eventDateTime',direction:'desc'}}/>
    </>;
  }
}

class RequestAuditGrid extends React.Component {
  constructor(props) {
    super(props);
    this.state={items:null}
  }
    componentDidMount() {
        let dataReq = {
            requestId: this.props.requestId
        };

        let json = JSON.stringify(dataReq);
        //alert(json);
        let req = new HttpRequestCRUD();
        req.setFetch(url + "/getRequestAudits", json);
        let request = req.fetchJSON();
        request.then(
            () => {
                if (req.getStatus()) {
                    let data = req.getData();
                    // alert('Requests Search handleClick: '+data.items.length);
                    this.setState({items: data.items});
                }
            }
        );
    }
  render() {
    let fields = {
        event:{
          name:'Событие'
          , width:100
          , style:function(item) {
              return {textAlign:'center'}
            }
        }
      , user:{
          name:'Пользователь'
          , width:190
        }
      , eventDateTime:{
          name:'Дата и время'
          , width:130
          , type: 'dateTime'
          , style:function(item) {
              return {textAlign:'center'}
            }
        }
      , description:{
          name:'Описание'
          , width:140
        }
      , content:{
          name:'Детализация'
          , width:200
        }
    }
    return <>
    <Grid fields={fields} items={this.state.items} defaultSort={{code:'eventDateTime',direction:'desc'}}/>
    </>;
  }
}

export class ViewRequest extends React.Component {
  constructor(props) {
    super(props);
    this.handleClickTab = this.handleClickTab.bind(this);
    this.state = {tabs:'request'}
    this.props.setModalSize(820,400);
  }
  handleClickTab(e) {
    if (e.target.id) this.setState({tabs:e.target.id});
  }
  render() {
    return <div>
        <table class="Tabs">
          <tr onClick={this.handleClickTab}>
            <td id="request" data-checked={(this.state.tabs == 'request') ? true : false}>Заявка</td>
            <td id="history" data-checked={(this.state.tabs == 'history') ? true : false}>История</td>
            <td id="audit" data-checked={(this.state.tabs == 'audit') ? true : false}>Аудит</td>
          </tr>
        </table>
        <div className="Content" style={{width:'100%',height:'100%', overflow:'auto'}}>
            {(this.state.tabs == 'request') ? <Request requestId={this.props.requestId}/> : null}
            {(this.state.tabs == 'history') ? <RequestHistoryGrid requestId={this.props.requestId}/> : null}
            {(this.state.tabs == 'audit') ? <RequestAuditGrid requestId={this.props.requestId}/> : null}
        </div>
    </div>;
  }
}