import React from 'react';

import {ButtonBar} from './ElementsWindow.js';
import {TitleForm} from './ElementsWindow.js';
import {ReportRequestsConsolidated} from './ReportRequestsConsolidated.js';
import {ReportRequestsDetailed} from './ReportRequestsDetailed.js';
import {permissions} from "./App.js";
import {HttpRequestCRUD} from './System.js';
import {url} from './System.js';
import {formatTime} from './System.js';
import {downloadReport} from './System.js';
import {Modal} from "./Modal";
import {Error} from "./Error";

class Popup extends React.Component {
  constructor(props) {
    super(props);
    this.handleInBackground = this.handleInBackground.bind(this);
    this.handleLoad = this.handleLoad.bind(this);
    this.handleShow = this.handleShow.bind(this);
    this.props.setShowPopup(this.handleShow);
    this.state = {
      isShow:false
      , isGenerated:true
      , timer:'00:00'
      , status:'Идет формирование'
      , modalError: null
    }
  }
  handleShow(reportId) {
    let json = JSON.stringify({
      reportId: reportId
    });
    let req = new HttpRequestCRUD();
    req.setFetch(url + '/checkReport', json);
    let request = null;
    this.setState({
      isShow:false
      , isGenerated:true
      , timer:'00:00'
      , status:'Идет формирование'
      , modalError: null
      , start_dt:new Date().getTime()
      , reportId: reportId});
    function funcTimerRepeat() {
      let cur_dt = new Date().getTime();
      this.setState({timer: formatTime(cur_dt - this.state.start_dt)});
    }
    let timerRepeat = funcTimerRepeat.bind(this);
    this.timer = setInterval(
        timerRepeat
        , 1000
    );
    function funcCheckTimer() {
      request = req.fetchJSON();
      request.then(
          () => {
            if (req.getStatus()) {
              if (req.getData().status == 'FINISH') {
                this.setState({
                  isGenerated:false
                  , status: 'Сформирован'
                });
                clearTimeout(this.check_timer);
                clearTimeout(this.timer);
              } else {
                // this.timer = setTimeout(repeat,3000);
                // alert('new:'+this.timer);
              }
            } else {
              clearTimeout(this.check_timer);
              clearTimeout(this.timer);
              let text_error = req.getErrorMessage();
              let stackTrace = req.getStackTrace();
              this.setState({
                isGenerated:null
                , status: 'Ошибка'
                , modalError:(<Modal title={"Ошибка"} onOK={()=>{this.setState({modalError:null});}}>
                  <Error errorMessage={text_error} stackTrace={stackTrace}/>
                </Modal>)
              });
            }
          }
      );
    }
    let checkTimer = funcCheckTimer.bind(this);
    this.check_timer = setInterval(
        checkTimer
        , 3000
    );
    this.setState({isShow:true});
  }
  handleInBackground() {
    clearTimeout(this.check_timer);
    clearTimeout(this.timer);
    this.setState({isShow:false});
  }
  handleLoad() {
    function funcError(text_error,stackTrace) {
      this.setState({
        status: 'Ошибка'
        , modalError:(<Modal title={"Ошибка"} onOK={()=>{this.setState({modalError:null});}}>
          <Error errorMessage={text_error} stackTrace={stackTrace}/>
        </Modal>)
      });
    }
    let error = funcError.bind(this);
    downloadReport(this.state.reportId);
    this.setState({isShow:true});
  }
  render() {
    return <>{(this.state.isShow) ? (<div className="BackgroundPopup">
      <div className="Popup">
        <div className="ButtonHeaderPopup"><button title="Закрыть" onClick={this.handleInBackground}>×</button></div>
        <div className="ContainerPopup">
        {(this.state.isGenerated) ? <button onClick={this.handleInBackground}>В фоне</button> : null}
        {(!this.state.isGenerated && this.state.isGenerated!=null) ? <button onClick={this.handleLoad}>Загрузить</button> : null}
        </div>
        <div className="StatusPopup"><span>{this.state.status}</span><br/><span>{this.state.timer}</span></div>
      </div>
    </div>) : null}
      {this.state.modalError}
    </>;
  }
}

class Complete extends React.Component {
  constructor(props) {
    super(props);
    this.handleClick = this.handleClick.bind(this);
    this.setShowPopup = this.setShowPopup.bind(this);
    this.handleGenerateReport = this.handleGenerateReport.bind(this);
    this.state = {modalError: null}
  }
  handleGenerateReport(generateReport) {
    console.log(this.props.formatType);
    generateReport.formatType = this.props.formatType;
    let json = JSON.stringify(generateReport);
    let req = new HttpRequestCRUD();
    req.setFetch(url + '/generateReport', json);
    let request = req.fetchJSON();
    request.then(
        () => {
          if (req.getStatus()) {
            this.state.onShowPopup(req.getData().reportId);
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
  handleClick() {
    let handleGenerateReport = this.handleGenerateReport;
    this.props.onComplete(handleGenerateReport);
  }
  setShowPopup(func) {
    this.setState({onShowPopup:func});
  }
  render() {
    return <><button onClick={this.handleClick}>Сформировать</button>
    <Popup setShowPopup={this.setShowPopup}/>
      {this.state.modalError}
    </>;
  }
}

class ReportType extends React.Component {
  constructor(props) {
    super(props);
    this.handleChange = this.handleChange.bind(this);
  }
  handleChange(e) {
    this.props.onChangeReportType(e.target.value);
  }
  render() {
    return <>Отчет: <select onChange={this.handleChange} value={this.props.reportType}>
        <option value="ReportRequestsDetailed">Отчет по заявкам (детальный)</option>
        <option value="ReportRequestsConsolidated">Отчет по заявкам (сводный)</option>
        </select>
        </>;
  }
}

class FormatType extends React.Component {
  constructor(props) {
    super(props);
    this.handleChange = this.handleChange.bind(this);
  }
  handleChange(e) {
    this.props.onChangeFormatType(e.target.value);
  }
  render() {
    return <> Формат: <select onChange={this.handleChange} value={this.props.formatType}>
        <option value="XLS">XLS</option>
        <option value="XLSX">XLSX</option>
    </select>
        </>;
  }
}

export class Report extends React.Component {
  constructor(props) {
    super(props);
    this.state = {reportType : props.form, prevReportType: null, formatType:'XLS'};
    this.handleChangeReportType = this.handleChangeReportType.bind(this);
    this.handleChangeFormatType = this.handleChangeFormatType.bind(this);
    this.handleComplete = this.handleComplete.bind(this);
    this.setComplete = this.setComplete.bind(this);
  }
  handleChangeReportType(reportType) {
    this.setState({reportType: reportType});
  }
  handleChangeFormatType(formatType) {
    this.setState({formatType: formatType});
  }
  static getDerivedStateFromProps(props, state) {
    // alert('getDerivedStateFromProps: '+props.form+','+state.reportType);
    if (state.prevReportType == null) return {reportType : props.form, prevReportType: props.form};
    if (state.prevReportType != props.form) return {reportType : props.form, prevReportType: props.form};
    if (state.prevReportType != state.reportType) return {reportType : state.reportType, prevReportType: state.prevReportType};
    return null;
  }
  setComplete(func) {
    this.setState({onComplete:func});
  }
  handleComplete(func) {
    this.state.onComplete(func);
  }
  render() {
    // alert('render: '+this.state.prevReportType+','+this.state.reportType);
    return (<div data-display={this.props.dataDisplay}>
        <TitleForm name={"Формирование отчета"}/>
        <ButtonBar>
          <Complete onComplete={this.handleComplete} formatType={this.state.formatType}/>
        </ButtonBar>
        <ButtonBar>
          <ReportType reportType={this.state.reportType} onChangeReportType={this.handleChangeReportType}/>
          <FormatType formatType={this.state.formatType} onChangeFormatType={this.handleChangeFormatType}/>
        </ButtonBar>
        {(this.state.reportType == 'ReportRequestsDetailed') ? <ReportRequestsDetailed setComplete={this.setComplete}/> : null}
        {(this.state.reportType == 'ReportRequestsConsolidated') ?  <ReportRequestsConsolidated setComplete={this.setComplete}/> : null}
    </div>);
  }
}