import React from 'react';

import {ButtonBar} from './ElementsWindow.js';
import {TitleForm} from './ElementsWindow.js';
import {formatDate, HttpRequestCRUD, url} from './System.js';
import {Grid} from './Grid.js';

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
    this.state = {modalError: null};
  }
  handleClick() {
      let search = {
          fromRunDate: this.props.searchParams.fromReportDate,
          toRunDate: this.props.searchParams.toReportDate
      };
      let json = JSON.stringify(search);
      let req = new HttpRequestCRUD();
      req.setFetch(url + "/getReports", json);
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

class DownloadButton extends React.Component {
  render() {
    return <button disabled={(this.props.disabled) ? true : false}>Скачать</button>;
  }
}

class FromReportDate extends React.Component {
  constructor(props) {
    super(props);
    this.handleChange = this.handleChange.bind(this);
  }
  handleChange(e) {
    this.props.onInputChangeSearchParams('fromReportDate',e.target.value);
  }
  render() {
    let curDate = Date.now();
    return <>Дата отчета с: <input type="date" autoFocus max={formatDate(curDate+1000 * 60 * 60 * 24 * 1)} value={this.props.value} onChange={this.handleChange}/></>;
  }
}

class ToReportDate extends React.Component {
  constructor(props) {
    super(props);
    this.handleChange = this.handleChange.bind(this);
  }
  handleChange(e) {
    this.props.onInputChangeSearchParams('toReportDate',e.target.value);
  }
  render() {
    let curDate = Date.now();
    return <> по: <input type="date" max={formatDate(curDate+1000 * 60 * 60 * 24 * 1)} value={this.props.value} onChange={this.handleChange}/></>;
  }
}

class ReportsGrid extends React.Component {
  constructor(props) {
    super(props);
    this.handleEditClient = this.handleEditClient.bind(this);
  }
  handleEditClient() {

  }
  render() {
    let fields = {
        runDatetime:{
          name:'Дата и время запуска'
          , width:100
          , style:function(item) {
              return {textAlign:'center'}
            }
          , type: 'dateTime'
        }
      , reportName:{
          name:'Наименование отчета'
          , width:140
          , fieldFilter:function(value) {
              return <select value={value}>
              <option value="Все">Все</option>
              <option value="Отчет по заявкам (детальный)">Отчет по заявкам (детальный)</option>
              <option value="Отчет по заявкам (сводный)">Отчет по заявкам (сводный)</option>
              </select>;
          }
        }
      , status:{
          name:'Статус'
          , width:140
          , style:function(item) {
              return {textAlign:'center'}
            }
          , fieldFilter:function(value) {
              return <select value={value}>
              <option value="Все">Все</option>
              <option value="Формируется">Формируется</option>
              <option value="Сформирован">Сформирован</option>
              </select>;
          }
        }
      , fromPeriodDate:{
          name:'Период С'
          , width:85
          , type: 'date'
          , style:function(item) {
              return {textAlign:'center'}
            }
        }
      , toPeriodDate:{
          name:'Период По'
          , width:85
          , type: 'date'
          , style:function(item) {
              return {textAlign:'center'}
            }
        }
      , parameters:{
          name:'Параметры'
          , width:330
        }
      , format:{
          name:'Формат'
          , width:50
          , style:function(item) {
              return {textAlign:'center'}
            }
        }
    }
    return <>
    <Grid fields={fields} items={this.props.items} defaultSort={{code:'runDatetime',direction:'desc'}} setClear={this.props.setClearFilter} setLoad={this.props.setLoad} onCheckedItems={this.props.onCheckedItems}/>
    </>;
  }
}

export class MyReports extends React.Component {
  constructor(props) {
    super(props);
    this.handleInputChangeSearchParams = this.handleInputChangeSearchParams.bind(this);
    this.handleClearFilter = this.handleClearFilter.bind(this);
    this.setClearFilter = this.setClearFilter.bind(this);
    this.handleSearch = this.handleSearch.bind(this);
    this.setLoad = this.setLoad.bind(this);
    this.handleCheckedItems = this.handleCheckedItems.bind(this);
    this.handleDisabledButton = this.handleDisabledButton.bind(this);
    let searchParams = {
        fromReportDate: formatDate(Date.now()-1000 * 60 * 60 * 24 * 31),
        toReportDate: formatDate(Date.now())
    };
    this.state = {disabledDownloadButton:true, searchParams: searchParams}
  }
  handleInputChangeSearchParams(name, value) {
      let searchParams = this.state.searchParams;
      searchParams[name]=value;
      this.setState({searchParams:searchParams});
  }
  setLoad(func) {
    this.setState({onLoad:func});
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
    let disabledDownloadButton;
    if (!checkedItems) {
      disabledDownloadButton = true;
    } else if (checkedItems.length>=2) {
      disabledDownloadButton = true;
    } else if (checkedItems.length==1) {
      disabledDownloadButton = false;
    }
    this.setState({
      disabledDownloadButton:disabledDownloadButton
    });
  }
  handleCheckedItems(checkedItems) {
    this.handleDisabledButton(checkedItems);
  }
  render() {
    return <div data-display={this.props.dataDisplay}>
        <TitleForm name={"Мои отчеты"}/>
        <ButtonBar>
          <ClearFilter onClearFilter={this.handleClearFilter}/>
          <Search onSearch={this.handleSearch} searchParams={this.state.searchParams}/>
          <DownloadButton disabled={this.state.disabledDownloadButton}/>
        </ButtonBar>
        <ButtonBar>
          <FromReportDate onInputChangeSearchParams={this.handleInputChangeSearchParams} value={this.state.searchParams.fromReportDate}/>
          <ToReportDate onInputChangeSearchParams={this.handleInputChangeSearchParams} value={this.state.searchParams.toReportDate}/>
        </ButtonBar>
        <div className="Content">
            <ReportsGrid setClearFilter={this.setClearFilter} setLoad={this.setLoad} onCheckedItems={this.handleCheckedItems}/>
        </div>
    </div>;
  }
}