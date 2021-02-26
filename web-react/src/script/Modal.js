import React from 'react';
import ReactDOM from 'react-dom';

import {Client} from './Client.js';

const root = document.querySelector('#root');

class HeaderWindow extends React.Component {
  constructor(props) {
    super(props);
    this.handleMouseDown = this.handleMouseDown.bind(this);
  }
  handleMouseDown(e) {
    if (e.target == e.currentTarget) {
      e.preventDefault();

      let window = e.target.getBoundingClientRect();
      let top = window.top;
      let left = window.left;

      let clickX = e.clientX;
      let clickY = e.clientY;
      let omm = this.props.onMove;

      function handleMouseUp(e) {
        onMouseUp(e,omm);
      }

      document.addEventListener('mouseup', handleMouseUp);

      function onMouseUp(e,onMoveModal) {
        top = top + (e.clientY - clickY);
        left = left + (e.clientX - clickX);
        if (((e.clientY - clickY) != 0) || ((e.clientX - clickX) != 0)) onMoveModal(top, left);
        document.removeEventListener('mouseup', handleMouseUp);
      }
    }
  }
  render() {
    return (
      <div className="HeaderWindow" onMouseDown={this.handleMouseDown}>
        <TitleHeaderWindow title={this.props.title}/>
        <ButtonHeaderWindow onClose={this.props.onClose}/>
      </div>
    );
  }
}

class TitleHeaderWindow extends React.Component {
  constructor(props) {
    super(props);
  }
  render() {
    return <div className="TitleHeaderWindow">{this.props.title}</div>;
  }
}

class ButtonHeaderWindow extends React.Component {
  constructor(props) {
    super(props);
  }
  render() {
    return (
      <div className="ButtonHeaderWindow">
        <Close idModal={this.props.id} onClose={this.props.onClose}/>
      </div>
    );
  }
}

class Close extends React.Component {
  constructor(props) {
    super(props);
    this.handleClick = this.handleClick.bind(this);
  }
  handleClick() {
    this.props.onClose();
  }
  render() {
    return (
      <button className="Close" title="Закрыть" onClick={this.handleClick}>
        &#10006;
      </button>
    );
  }
}

class OK extends React.Component {
  constructor(props) {
    super(props);
    this.handleClick = this.handleClick.bind(this);
  }
  handleClick() {
    this.props.onOK();
  }
  render() {
    return <button onClick={this.handleClick} disabled={(this.props.disabledOK===undefined) ? false : ((this.props.disabledOK) ? true : false)}>OK</button>;
  }
}

class Cancel extends React.Component {
  constructor(props) {
    super(props);
    this.handleClick = this.handleClick.bind(this);
  }
  handleClick() {
    this.props.onCancel();
  }
  render() {
    return <button onClick={this.handleClick}>Отменить</button>;
  }
}

class ButtonBottomModal extends React.Component {
  constructor(props) {
    super(props);
  }
  render() {
    return (
      <div className="ButtonBottomModal ButtonBar">
      <div style={{ 'marginLeft': '15px'}}>
        <OK onOK={this.props.onOK} disabledOK={this.props.disabledOK}/>
        {!(this.props.onCancel === undefined) ? <Cancel onCancel={this.props.onCancel} idModal={this.props.idModal}/> : null}
      </div>
      </div>
    );
  }
}

class ContainerContent extends React.Component {
  constructor(props) {
    super(props);
  }
  render() {
    return <div className="ContainerModal" style={{width:this.props.width-20+'px', height:this.props.height-20+'px'}}>{this.props.children}</div>;
  }
}

class RightBottomResize extends React.Component {
  constructor(props) {
    super(props);
    this.handleMouseDown = this.handleMouseDown.bind(this);
  }
  handleMouseDown(e) {
    e.preventDefault();
    document.body.classList.add('NwseResize');

    let clickX = e.clientX;
    let clickY = e.clientY;

    let orm = this.props.onResize;

    function handleMouseUp(e) {
      onMouseUp(e,orm);
    }

    document.addEventListener('mouseup', handleMouseUp);

    function onMouseUp(e,onResizeModal) {
      document.body.classList.remove('NwseResize');
      onResizeModal(e.clientY - clickY, e.clientX - clickX);
      document.removeEventListener('mouseup', handleMouseUp);
    }
  }
  render() {
    return (
      <div className="RightBottomResize" onMouseDown={this.handleMouseDown}>
      </div>
    );
  }
}

export function position(type,current,parent) {
    let left;
    let top;
    let body = (parent==null) ? {height:(document.querySelector('.Footer').getBoundingClientRect().bottom-0), width:document.body.clientWidth, left: 0, top: 0} : parent;
    switch (type) {
      case 'center':
      default: {
        let p;
        if ((parent == null) || ((parent != null) &&(current.width > parent.width))) {
          p = body;
        } else {
          p = parent;
        }
        left = (p.width-current.width) > 0 ? Math.floor(((p.width-current.width)/2)+p.left) : 0;
        top = (p.height-current.height-150) > 0 ? Math.floor(((p.height-current.height-150)/2)+p.top) : 0;
      }
    }
    return {left:left,top:top};
}

export class Modal extends React.Component {
  constructor(props) {
    super(props);
    this.handleMove = this.handleMove.bind(this);
    this.handleResizeShift = this.handleResizeShift.bind(this);
    this.state = {id: new Date().getTime()}
    //alert('Modal constructor');
    this.el = document.createElement('div');
    this.el.setAttribute('data-id', this.state.id);
    this.el.setAttribute('data-type', 'Modal');
  }
  static getDerivedStateFromProps(props, state) {
    //alert('getDerivedStateFromProps:'+props.size+','+state.width);
    let condition = 0;
    //error show
    if (!props.size && !state.width) condition=1;
    //other show
    else if (props.size && !state.width) condition=2;
    //error resize
    else if (!props.size && state.width) condition=3;
    //other show
    else if (props.size && state.width) {
      //prevProps==curProps
      if (props.size.width==state.prevPropsWidth && props.size.height==state.prevPropsHeight) condition=3;
      //prevProps!=curProps
      else condition=4;
    }
    if (condition==1) {
      //alert('1');
      let size = {width:400,height:0};
      let pos = position('center'
      , {width:size.width,height:size.height}
      , null);
      return {
        width:size.width
        , prevPropsWidth:size.width
        , height:size.height
        , prevPropsHeight:size.height
        , top: pos.top
        , left: pos.left
      };
    }
    if (condition==2) {
      //alert('2');
      let size = props.size;
      let pos = position('center'
      , {width:size.width,height:size.height}
      , null);
      return {
        /*dataDisplay:true
        ,*/ width:size.width
        , prevPropsWidth:size.width
        , height:size.height
        , prevPropsHeight:size.height
        , top: pos.top
        , left: pos.left
      };
    }
    if (condition==3) {
      //alert('3');
      //alert(state.width+','+props.heightForPos+','+state.height+','+state.top+','+state.left);
      return {
        width:state.width
        , height:state.height
      };
    }
    if (condition==4) {
      //alert('4');
      let size = props.size;
      let pos = position('center'
      , {width:size.width,height:size.height}
      , null);
      return {
        width:size.width
        , prevPropsWidth:size.width
        , height:size.height
        , prevPropsHeight:size.height
        , top: pos.top
        , left: pos.left
      };
    }
    return null;
  }
  componentDidMount() {
    //alert('Modal componentDidMount');
    root.appendChild(this.el);
  }
  componentWillUnmount() {
    //alert('Modal componentWillUnmount');
    root.removeChild(this.el);
  }
  handleMove(top,left) {
    this.setState({top:top, left:left});
  }
  handleResizeShift(shiftHeight,shiftWidth) {
    let rect = document.querySelector('.Window[data-id="'+this.state.id+'"] .ContainerModal').getBoundingClientRect();
    let height = Math.floor(rect.bottom - rect.top);
    let width = rect.width;
    this.setState({
      height:((height+shiftHeight) <= 25 ? 25 : (height+shiftHeight)),
      width:((width+shiftWidth) <= 175 ? 175 : (width+shiftWidth))
    });
  }
  render() {
    //alert(Object.keys(this.props.children));
    //alert(this.props.children.type);
    //alert('modal:'+this.state.id+','+this.state.dataDisplay+','+this.state.width+','+this.state.height+','+this.state.size);
    let modal = (
      <div className="BackgroundWindow" /*data-display={(this.state.dataDisplay) ? 'block' : 'none'}*/>
        <div className="Window" data-id={this.state.id} style={{position:'fixed',top:this.state.top+'px',left:this.state.left+'px'}} >
          <HeaderWindow title={this.props.title} onMove={this.handleMove} onClose={!(this.props.onCancel === undefined) ? this.props.onCancel : this.props.onOK}/>
          <ContainerContent width={this.state.width} height={this.state.height}>{this.props.children}</ContainerContent>
          {(!(this.props.onOK === undefined)) ? <ButtonBottomModal onOK={!(this.props.onOK === undefined) ? this.props.onOK : ()=>{}} onCancel={this.props.onCancel} idModal={this.state.id} disabledOK={this.props.disabledOK}/> : null}
          <RightBottomResize onResize={this.handleResizeShift}/>
        </div>
      </div>
    );
    return ReactDOM.createPortal(modal,this.el);
  }
}
