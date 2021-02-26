import React from 'react';

export class ErrorMessage extends React.Component {
  constructor(props) {
    super(props);
    this.handleChange = this.handleChange.bind(this);
  }
  handleChange(e) {
    this.props.onChange(e.target.name, e.target.value);
  }
  render() {
    return (
      <div>
        {this.props.errorMessage}
      </div>
    );
  }
}

class ShowStackTrace extends React.Component {
  constructor(props) {
    super(props);
    this.handleClick = this.handleClick.bind(this);
  }
  handleClick() {
    this.props.onShowStackTrace();
  }
  render() {
    return <div onClick={this.handleClick} style={{color:'blue',cursor: 'pointer',display:'inline'}}>Стектрейс</div>;
  }
}

class CopyStackTrace extends React.Component {
  constructor(props) {
    super(props);
    this.handleClick = this.handleClick.bind(this);
  }
  handleClick() {
    let stackTrace = this.props.stackTrace;
    document.addEventListener('copy', function(e) {
      e.clipboardData.setData('text', stackTrace);
      e.preventDefault();
    });
    document.execCommand('copy');
  }
  render() {
    return <div onClick={this.handleClick} style={{color:'blue',cursor: 'pointer', display:'inline', marginLeft:'10px'}}>Копировать в буфер</div>;
  }
}

export class StackTrace extends React.Component {
  constructor(props) {
    super(props);
    this.handleChange = this.handleChange.bind(this);
  }
  handleChange(e) {
    this.props.onChange(e.target.name, e.target.value);
  }
  render() {
    return (
      <div style={{borderTop: '1px solid #ddd', 'max-height':'100px'}} data-display={this.props.isShowStackTrace ? 'block' : 'none'}>
        {this.props.stackTrace}
      </div>
    );
  }
}

export class Error extends React.Component {
  constructor(props) {
    super(props);
    this.state = { isShowStackTrace: false };
    this.handleShowStackTrace = this.handleShowStackTrace.bind(this);
  }
  handleShowStackTrace() {
    this.setState({ isShowStackTrace: !this.state.isShowStackTrace });
  }
  //!(this.props.stackTrace === undefined)
  render() {
    return (
      <>
        <ErrorMessage errorMessage={this.props.errorMessage}/>
        {(this.props.stackTrace) ? <>
        <div>
        <ShowStackTrace onShowStackTrace={this.handleShowStackTrace}/>
        <CopyStackTrace stackTrace={this.props.stackTrace}/>
        </div>
        <StackTrace isShowStackTrace={this.state.isShowStackTrace} stackTrace={this.props.stackTrace}/>
        </> 
        : null}
      </>
    );
  }
}