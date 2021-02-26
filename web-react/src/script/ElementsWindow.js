import React from 'react';

import {Modal} from './Modal.js';
import {Clients} from './Clients.js';

export class ButtonBar extends React.Component {
  constructor(props) {
    super(props);
  }
  render() {
    return <div className="ButtonBar">
        {this.props.children}
    </div>;
  }
}

export class TitleForm extends React.Component {
  constructor(props) {
    super(props);
  }
  render() {
    return <div className="TitleForm">{this.props.name}</div>;
  }
}

export class ChoiceObject extends React.Component {
  constructor(props) {
    super(props);
    this.state = {modal: null};
    this.handleShow = this.handleShow.bind(this);
    this.handleSetOK = this.handleSetOK.bind(this);
    this.handleOK = this.handleOK.bind(this);
    this.handleConfirmClose = this.handleConfirmClose.bind(this);
    this.handleCheckedItems = this.handleCheckedItems.bind(this);
    this.handleDisabledButton = this.handleDisabledButton.bind(this);
    this.state={showModal:false,disabledOKButton:true,checkedItems:null}
  }
  handleShow() {
    this.setState({showModal:true});
  }
  handleSetOK(func) {
    this.setState({onOK:func});
  }
  handleOK() {
    this.props.onOK(this.state.checkedItems);
    this.handleConfirmClose();
  }
  handleConfirmClose() {
    this.setState({showModal:false, disabledOKButton:true});
  }
  handleDisabledButton(checkedItems) {
    let disabledOKButton;
    if (!checkedItems) {
      disabledOKButton = true;
    } else if (checkedItems.length>=2) {
      disabledOKButton = true;
    } else if (checkedItems.length==1) {
      disabledOKButton = false;
    }
    this.setState({
      disabledOKButton:disabledOKButton
    });
  }
  handleCheckedItems(checkedItems) {
    this.handleDisabledButton(checkedItems);
    if (this.props.onCheckedItems) this.props.onCheckedItems(checkedItems);
    this.setState({checkedItems:checkedItems});
  }
  render() {
    let modal = <Modal title={"Выбор клиента"} onOK={this.handleOK} onCancel={this.handleConfirmClose} size={{width:550,height:350}} disabledOK={this.state.disabledOKButton}>
      <Clients choiceMode={true} onCheckedItems={this.handleCheckedItems}/>
      </Modal>;
    return <> <button className="Choice" title="Выбрать" onClick={this.handleShow}>...</button>
    {(this.state.showModal) ? modal : null}
    </>;
  }
}