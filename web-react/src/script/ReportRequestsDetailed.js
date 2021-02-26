import React from 'react';

import {ButtonBar} from './ElementsWindow.js';
import { ClientCode } from './Client.js';
import {ChoiceObject} from './ElementsWindow.js';
import {FromCreateDate} from './ReportRequestsConsolidated.js';
import {ToCreateDate} from './ReportRequestsConsolidated.js';
import {Modal} from './Modal.js';
import {Error} from './Error.js';

export class ReportRequestsDetailed extends React.Component {
  constructor(props) {
    super(props);
    this.handleInputChange = this.handleInputChange.bind(this);
    this.handleComplete = this.handleComplete.bind(this);
    props.setComplete(this.handleComplete);
    this.handleChoice = this.handleChoice.bind(this);
    let generateReport = {
        fromCreateDate: ''
        , toCreateDate: ''
        , clientCode: ''
    }
    this.state = {
      modalError: null
      , generateReport: generateReport
    };
  }
  handleInputChange(name, value) {
    let generateReport = this.state.generateReport;
    generateReport[name]=value;
    this.setState({generateReport:generateReport});
  }
  handleComplete(func) {
    let text_error = null;
    if (!this.state.generateReport.fromCreateDate) text_error = "Введите дату создания заявки 'С'.";
    else if (!this.state.generateReport.toCreateDate) text_error = "Введите дату создания заявки 'По'.";
    else if (!this.state.generateReport.clientCode) text_error = "Введите код клиента.";
    if (text_error) {
      this.setState({modalError:(<Modal title={"Ошибка"} onOK={()=>{this.setState({modalError:null});}}>
          <Error errorMessage={text_error}/>
        </Modal>)});
      console.log(`ReportRequestsDetailed handleComplete false`);
    } return func({
      reportRequestsDetailed: {
        fromCreateDate: this.state.generateReport.fromCreateDate
        , toCreateDate: this.state.generateReport.toCreateDate
        , clientCode: this.state.generateReport.clientCode
      }
    });
  }
  handleChoice(items) {
    let clientCode = (items) ? items[0].clientCode : "";
    let generateReport = this.state.generateReport;
    generateReport.clientCode=clientCode;
    this.setState({generateReport:generateReport});
  }
  render() {
    let generateReport = this.state.generateReport;
    return <><div>
        <ButtonBar>
          <FromCreateDate onChange={this.handleInputChange} value={generateReport.fromCreateDate}/>
          <ToCreateDate onChange={this.handleInputChange} value={generateReport.toCreateDate}/>
        </ButtonBar>
        <ButtonBar>
          <>Код объекта: <ClientCode onChange={this.handleInputChange} value={generateReport.clientCode}/></>
          <ChoiceObject onOK={this.handleChoice}/>
        </ButtonBar>
    </div>
    {this.state.modalError}
    </>;
  }
}