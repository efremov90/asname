import React from 'react';

import {ButtonBar} from './ElementsWindow.js';
import {Modal} from './Modal.js';
import {Error} from './Error.js';

export class FromCreateDate extends React.Component {
  constructor(props) {
    super(props);
    this.handleChange = this.handleChange.bind(this);
  }
  handleChange(e) {
    this.props.onChange(e.target.name, e.target.value);
  }
  render() {
    return <>Дата создания заявки с: <input name="fromCreateDate" required type="date" autoFocus onChange={this.handleChange} value={(this.props.value) ? this.props.value : ""}/></>;
  }
}

export class ToCreateDate extends React.Component {
  constructor(props) {
    super(props);
    this.handleChange = this.handleChange.bind(this);
  }
  handleChange(e) {
    this.props.onChange(e.target.name, e.target.value);
  }
  render() {
    return <> по: <input name="toCreateDate" required type="date" onChange={this.handleChange} value={(this.props.value) ? this.props.value : ""}/></>;
  }
}

export class  ReportRequestsConsolidated extends React.Component {
  constructor(props) {
    super(props);
    this.handleInputChange = this.handleInputChange.bind(this);
    this.handleComplete = this.handleComplete.bind(this);
    props.setComplete(this.handleComplete);
    let generateReport = {
        fromCreateDate: ''
        , toCreateDate: ''
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
    if (text_error) {
      this.setState({modalError:(<Modal title={"Ошибка"} onOK={()=>{this.setState({modalError:null});}}>
          <Error errorMessage={text_error}/>
        </Modal>)});
      console.log(`ReportRequestsConsolidated handleComplete false`);
    } else func({
      reportRequestsConsolidated: {
        fromCreateDate: this.state.generateReport.fromCreateDate
        , toCreateDate: this.state.generateReport.toCreateDate
      }
    });
  }
  render() {
    let generateReport = this.state.generateReport;
    return (<><div>
        <ButtonBar>
          <FromCreateDate onChange={this.handleInputChange} value={generateReport.fromCreateDate}/>
          <ToCreateDate onChange={this.handleInputChange} value={generateReport.toCreateDate}/>
        </ButtonBar>
    </div>
    {this.state.modalError}
    </>);
  }
}