import React from 'react';

import {Modal} from './Modal.js';
import {Error} from './Error.js';
import {HttpRequestCRUD} from './System.js';
import {url} from './System.js';

class Comment extends React.Component {
  constructor(props) {
    super(props);
    this.handleChange = this.handleChange.bind(this);
  }
  handleChange(e) {
    this.props.onChange(e.target.name, e.target.value);
  }
  render() {
    return (
      <>
        <textarea
          name="comment"
          rows="3"
          maxLength="150"
          style={{width:'250px'}}
          onChange={this.handleChange}
          value={this.props.value}
        ></textarea>
      </>
    );
  }
}

export class CancelRequest extends React.Component {
  constructor(props) {
    super(props);
    this.handleInputChange = this.handleInputChange.bind(this);
    this.handleOK = this.handleOK.bind(this);
    props.setOK(this.handleOK);
    this.handleCancel = this.handleCancel.bind(this);
    props.setCancel(this.handleCancel);
    this.state = {modalError: null, isChange:false, comment:"Отсутствуют ресурсы"};
    this.props.setModalSize(370,0);
  }
  handleInputChange(name, value) {
    this.setState({[name]: value});
  }
  handleOK(requestId) {
    let text_error = null;
    if (!this.state.comment) text_error = "Введите комментарий.";
    if (text_error) this.setState({modalError:(<Modal title={"Ошибка"} onOK={()=>{this.setState({modalError:null});}}>
      <Error errorMessage={text_error}/>
      </Modal>)});
    else {
      let cancelRequest = {
        requestId: requestId,
        comment: this.state.comment
      }
      let json = JSON.stringify(cancelRequest);
      let req = new HttpRequestCRUD();
      req.setFetch(url + '/cancelRequest', json);
      let request = req.fetchJSON();
      request.then(
          () => {
            if (req.getStatus()) {
              this.props.onConfirmClose();
              this.props.onRefresh();
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
  }
  handleCancel() {
    this.props.onConfirmClose();
  }
  render() {
    return (
      <>
       <table>
          <tr>
            <td style={{['textAlign']:'right'}}>Комментарий:</td>
            <td>
              <Comment onChange={this.handleInputChange} value={this.state.comment}/>
            </td>
          </tr>
        </table>
        {this.state.modalError}
      </>
    );
  }
}