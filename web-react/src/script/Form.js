import React from 'react';

import {Home} from './Home.js';
import {Requests} from './Requests.js';
import {Clients} from './Clients.js';
import {MyReports} from './MyReports.js';
import {Report} from './Report.js';

export class Form extends React.Component {
  constructor(props) {
    super(props);
  }
  render() {
    let form = this.props.form;
    let activMenus = this.props.activMenus;
    return <>
      {activMenus.has('Home') ? <Home dataDisplay={form == 'Home' ? 'block' : 'none'}/> : null}
      {activMenus.has('Requests') ? <Requests dataDisplay={form == 'Requests' ? 'block' : 'none'}/> : null }
      {activMenus.has('Clients') ? <Clients dataDisplay={form == 'Clients' ? 'block' : 'none'}/> : null}
      {activMenus.has('MyReports') ? <MyReports dataDisplay={form == 'MyReports' ? 'block' : 'none'}/> : null}
      {activMenus.has('ReportRequestsDetailed') || activMenus.has('ReportRequestsConsolidated') ? <Report form={this.props.form} onChangeReportType={this.props.onChangeReportType} dataDisplay={form == 'ReportRequestsDetailed' || form == 'ReportRequestsConsolidated' ? 'block' : 'none'}/>: null}
    </>;
  }
}