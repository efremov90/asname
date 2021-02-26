import React from 'react';

import {Form} from './Form.js';
import {permissions} from "./App.js";
import {Modal} from "./Modal";
import {Error} from "./Error";
import {HttpRequestCRUD, url} from "./System";

class LogoutBtn extends React.Component {
  constructor(props) {
    super(props);
    this.handleClick = this.handleClick.bind(this);
    // this.handleOK = this.handleOK.bind(this);
    this.state = {modalError: null};
  }
  handleOK() {
    let req = new HttpRequestCRUD();
    req.setURL(url + "/logout")
    req.setContentType("text/plain;charset=UTF-8");
    let request = req.fetchSimple();
    request.then(
        () => {
          if (req.getStatus()) {
            document.location.reload();
          } else {
            let text_error = req.getErrorMessage();
            this.setState({modalError:(<Modal title={"Ошибка"} onOK={()=>{this.setState({modalError:null});}}>
                <Error errorMessage={text_error}/>
              </Modal>)});
          }
        }
    );
  }
  handleClick() {
    this.setState({modalError:(<Modal title={"Предупреждение"} onOK={()=>{this.handleOK();}} onCancel={()=>{this.setState({modalError:null});}}>
        <Error errorMessage={"Вы действительно хотите выйти?"}/>
      </Modal>)});
  }
  render() {
    return (
        <>
      <li id="logout" onClick={this.handleClick}>
        Выход
      </li>
          {this.state.modalError}
        </>
    );
  }
}

class NodeLI extends React.Component {
  constructor(props) {
    super(props);
    let isChecked = props.idNodeClick == props.node.id ? true : false;
    this.state = { isChecked: isChecked };
    this.handleClick = this.handleClick.bind(this);
  }
  handleClick() {
    this.setState({ isChecked: true });
    this.props.onNodeLIClick(this.props.node.id);
    this.props.onClickMenu(this.props.node.id);
  }
  render() {
    let node = this.props.node;
    return (
      <li
        id={node.id}
        className={
          'NodeLI' +
          (this.state.isChecked && node.id == this.props.idNodeClick
            ? ' Checked'
            : '')
        }
        onClick={this.handleClick}
      >
        {node.name}
      </li>
    );
  }
}

class NodeUL extends React.Component {
  constructor(props) {
    super(props);
  }
  render() {
    let nodes = this.props.node.submode;
    let nodesLI = nodes.map((node) => {
      if ('submode' in node) return <NodeGroupLI node={node} />;
      else
        return ((node.isAvailable) ?
                <NodeLI
            key={node.id}
            node={node}
            idNodeClick={this.props.idNodeClick}
            onNodeLIClick={this.props.onNodeLIClick}
            onClickMenu={this.props.onClickMenu}
          /> : null
        );
    });
    return (
      <ul className={'NodeUL' + (this.props.isOpen ? ' Open' : '')}>
        {nodesLI}
      </ul>
    );
  }
}

class NodeGroupLI extends React.Component {
  constructor(props) {
    super(props);
    this.state = { isOpen: false };
    this.handleClick = this.handleClick.bind(this);
  }
  handleClick(e) {
    if (e.target == e.currentTarget) {
      this.setState({ isOpen: !this.state.isOpen });
    }
  }
  render() {
    let node = this.props.node;
    return (
      <li
        id={node.id}
        className={'NodeGroupLI' + (this.state.isOpen ? ' Open' : ' Close')}
        onClick={this.handleClick}
      >
        {node.name}
        {
          <NodeUL
            isOpen={this.state.isOpen}
            node={node}
            idNodeClick={this.props.idNodeClick}
            onNodeLIClick={this.props.onNodeLIClick}
            onClickMenu={this.props.onClickMenu}
          />
        }
      </li>
    );
  }
}

class Menu extends React.Component {
  constructor(props) {
    super(props);
    this.handleClick = this.handleClick.bind(this);
    this.handleNodeLIClick = this.handleNodeLIClick.bind(this);

    let menu = [
      { id: 'Home', name: 'Домашняя', isAvailable: true},
      { id: 'Requests', name: 'Заявки', permission: 'REQUESTS_VIEW' },
      {
        id: 'Directory',
        name: 'Справочники',
        submode: [{ id: 'Clients', name: 'Клиенты', permission: 'CLIENTS_VIEW' }],
      },
      {
        id: 'Reports',
        name: 'Отчеты',
        submode: [
          { id: 'MyReports', name: 'Мои отчеты' , permission: 'REPORTS_VIEW'},
          { id: 'ReportRequestsDetailed', name: 'Отчет по заявкам (детальный)', permission: 'REPORT_GENERATE_REQUESTS_DETAILED' },
          { id: 'ReportRequestsConsolidated', name: 'Отчет по заявкам (сводный)', permission: 'REPORT_GENERATE_REQUESTS_CONSOLIDATED'},
        ],
      },
    ];

    let stack = [];
    for (let i=0; i<menu.length; i++) stack.push(menu[i]);
    let last;
    let prevLast;
    while (stack.length!=0) {
      console.log('length:'+stack.length);
      last = stack[stack.length-1];
      if (('submode' in last) && (last.submode[0]!=prevLast)) {
        for (let i=0; i<last.submode.length; i++) stack.push(last.submode[i]);
        console.log('(\'submode\' in last) && (last.submode[0]!=prevLast): '+last.id+':'+last.isAvailable);
      }
      if (('submode' in last) && (last.submode[0]==prevLast)) {
        prevLast = stack.pop();
        prevLast.isAvailable = false;
        for (let i=0; i<prevLast.submode.length; i++) {
          if (prevLast.submode[i].isAvailable) {
            prevLast.isAvailable = true;
          }
        }
        console.log('(\'submode\' in last) && (last.submode[0]==prevLast): '+prevLast.id+':'+prevLast.isAvailable);
      }
      if (!('submode' in last)) {
        prevLast = stack.pop();
        if (permissions.has(prevLast.permission) || prevLast.isAvailable) {
          prevLast.isAvailable = true;
        } else {
          prevLast.isAvailable = false;
        }
        console.log(`!(\'submode\' in last): ${prevLast.id} ${prevLast.permission} ${prevLast.isAvailable}`);
      }
    }

    this.state = {idNodeClick: 'Home', width: props.width, menu: menu};
  }
  handleClick(e) {
    //Вариант через idNodeClick={this.state.idNodeClick}
    //if (e.target.classList.contains('NodeLI')) this.setState({idNodeClick: e.target.id});
  }
  handleNodeLIClick(idNodeClick) {
    //Вариант через onNodeLIClick={this.handleNodeLIClick}
    this.setState({ idNodeClick: idNodeClick });
  }
  // componentDidMount() {
  //   this.setState();
  // }
  render() {
    let nodesLI = this.state.menu.map((node) => {
      if ('submode' in node)
        return (((node.id=='Home') || node.isAvailable) ?
                <NodeGroupLI
            key={node.id}
            node={node}
            idNodeClick={this.state.idNodeClick}
            onNodeLIClick={this.handleNodeLIClick}
            onClickMenu={this.props.onClickMenu}
          /> : null
        );
      else
      {
        // alert(node.permission+','+permissions.has(node.permission));
        return (
            ((node.id=='Home') || node.isAvailable) ?
                <NodeLI
            key={node.id}
            node={node}
            idNodeClick={this.state.idNodeClick}
            onNodeLIClick={this.handleNodeLIClick}
            onClickMenu={this.props.onClickMenu}
          /> : null
        );}
    });
    return (
      <div className="MainForm Menu" style={{width: this.props.width}}>
        <ul className="MainForm Menu" key="sidebar" onClick={this.handleClick}>
          <LogoutBtn />
          {nodesLI}
        </ul>
      </div>
    );
  }
}

export class Splitter extends React.Component {
  constructor(props) {
    super(props);
    this.handleMouseDown = this.handleMouseDown.bind(this);
  }
  handleMouseDown(event) {

    event.preventDefault();
    document.body.classList.add('ColResize');

    let oms = this.props.onMoveSplitter;
    let clickX = event.clientX;

    function handleMouseUp(e) {
      onMouseUp(e,oms);
    }

    document.addEventListener('mouseup', handleMouseUp);

    function onMouseUp(e,onMoveSplitter) {
      document.body.classList.remove('ColResize');
      onMoveSplitter(e.clientX - clickX);
      document.removeEventListener('mouseup', handleMouseUp);
    }
  }
  render() {
    return <div className="Splitter" onMouseDown={this.handleMouseDown}/>;
  }
}

class Container extends React.Component {
  constructor(props) {
    super(props);
  }
  render() {
    return <div className="MainForm Container" style={{width: this.props.width}}>
        {this.props.children}
    </div>;
  }
}

export class MainForm extends React.Component {
  constructor(props) {
    super(props);
    this.state = {widthMenu: 175, widthContainer: (document.body.clientWidth - 175 - 15), form: 'Home', activMenus: new Set(['Home'])};
    this.handleMoveSplitter = this.handleMoveSplitter.bind(this);
    this.handleClickMenu = this.handleClickMenu.bind(this);
    this.handleResize = this.handleResize.bind(this);
    window.addEventListener('resize',this.handleResize);
  }
  handleMoveSplitter(shiftWidth) {
    this.setState({widthMenu: (this.state.widthMenu+shiftWidth),
    widthContainer: (document.body.clientWidth - (this.state.widthMenu+shiftWidth)-15)});
  }
  handleClickMenu(form) {
    this.setState({form: form, activMenus: this.state.activMenus.add(form)});
  }
  handleResize() {
    this.setState({widthContainer: (document.body.clientWidth - (this.state.widthMenu)-15)});
  }
  render() {
    return (
      <div>
        <div className="Header">AS Name</div>
        <div className="MainForm">
          <Menu width={this.state.widthMenu} idNodeClick={this.state.form} onClickMenu={this.handleClickMenu}/>
          <Splitter onMoveSplitter={this.handleMoveSplitter}/>
          <Container width={this.state.widthContainer}><Form form={this.state.form} activMenus={this.state.activMenus}/></Container>
        </div>
        <div className="Footer">EAA © (2020)</div>
      </div>
    );
  }
}
