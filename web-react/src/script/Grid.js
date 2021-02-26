import React from 'react';

import {dateTimeJSONToView} from './System.js';

export function convertValue(value,type) {
  if (!value) return "";
  switch (type) {
      case 'date':
        return dateTimeJSONToView(value,'dd');
      case 'dateTime':
        return dateTimeJSONToView(value,'ss');
      default:
        return value; 
  }
}

function isChildOfParent(child,parent) {
  let curElm = child;
  while (curElm.parentNode) {
    if (curElm==parent) return true; 
    curElm = curElm.parentNode;
    if (curElm.nodeType=='BODY') return false;
  }
  return false;
}

class FieldFilterGrid extends React.Component {
  constructor(props) {
    super(props);
    this.handleChange = this.handleChange.bind(this);
  }
  handleChange(e) {
    this.props.onChangeFilter(this.props.code,e.target.value);
  }
  render() {
    let code = this.props.code;
    let field = this.props.field;
    let valuesFilter = this.props.valuesFilter;
   let input = (!field.fieldFilter) ? <input type="text" value={valuesFilter.get(code).value}/>
    : field.fieldFilter(valuesFilter.get(code).value);
    return <td data-field={code} onChange={this.handleChange}>
      {input}
    </td>
  }
}

export class FilterGrid extends React.Component {
  constructor(props) {
    super(props);
  }
  render() {
    let fields = this.props.fields;
    let keys = Object.keys(fields);
    //alert(this.props.valuesFilter.size);
    let fieldsFilter = keys.map(key=>{
      return <FieldFilterGrid code={key} field={fields[key]} valuesFilter={this.props.valuesFilter} onChangeFilter={this.props.onChangeFilter}/>
    })
    return <tr className="FilterGrid">
      {fieldsFilter}
    </tr>;
  }
}

class FieldHeaderGrid extends React.Component {
  constructor(props) {
    super(props);
    this.handleClick = this.handleClick.bind(this);
    this.handleMouseDown = this.handleMouseDown.bind(this);
    this.handleResize = this.handleResize.bind(this);
    this.state={width:this.props.field.width};
  }
  handleClick() {
    this.props.onSort(this.props.code);
  }
  handleResize(shiftWidth) {
    //alert(this.state.width+','+shiftWidth);
    this.setState({width:(this.state.width+shiftWidth)});
  }
  handleMouseDown(event) {

    event.preventDefault();
    document.body.classList.add('ColResize');

    let hr = this.handleResize;
    let clickX = event.clientX;

    function handleMouseUp(e) {
      onMouseUp(e,hr);
    }

    document.addEventListener('mouseup', handleMouseUp);

    function onMouseUp(e,onResize) {
      document.body.classList.remove('ColResize');
      onResize(e.clientX - clickX);
      document.removeEventListener('mouseup', handleMouseUp);
    }
  }
  render() {
    let code = this.props.code;
    let field = this.props.field;
    let sort = this.props.sort;
    let width = this.state.width;
    //alert(width);
    return <td className="Field" data-field={code}
              data-sort-direction={((code==sort.code) && sort.direction) ? sort.direction : null}
              style={{['min-width']:width+'px', ['max-width']:width+'px'}}>
              <td className="Name" onClick={this.handleClick}>{field.name}</td>
              <td className="Resize" onMouseDown={this.handleMouseDown}/>
            </td>;
  }
}

export class HeaderGrid extends React.Component {
  constructor(props) {
    super(props);
  }
  render() {
    let fields = this.props.fields;
    let keys = Object.keys(fields);
    let fieldsHeader = keys.map(key=>{
      return <FieldHeaderGrid code={key} field={fields[key]} sort={this.props.sort} onSort={this.props.onSort}/>;
    });
    fieldsHeader.push(<td data-field="LastColumn" style={{width: '100%', backgroundColor: '#fff', border: 'none', cursor: 'auto'}}></td>);
    return <tr className="HeaderGrid">
      {fieldsHeader}
    </tr>;
  }
}

class Field extends React.Component {
  constructor(props) {
    super(props);
  }
  render() {
    let code = this.props.code;
    let field = this.props.field;
    let item = this.props.item;
    let value = convertValue(item[code],field.type);

    let style = {backgroundColor:null,textAlign:null}
    if (field.style) {
      let getStyle = field.style(this.props.item);
      if (getStyle.backgroundColor) style.backgroundColor=getStyle.backgroundColor;
      if (getStyle.textAlign) style.textAlign=getStyle.textAlign;
    }
    if (this.props.isChecked) style.backgroundColor=null;
    return <td style={style} data-field={code}>{value}</td>;
  }
}

class Item extends React.Component {
  constructor(props) {
    super(props);
    this.handleClick = this.handleClick.bind(this);
  }
  handleClick(e) {
    this.props.onCheckedItem(this.props.id,this.props.item,e.ctrlKey);
  }
  render() {
    let item = this.props.item;
    let fields = this.props.fields;
    let keys = Object.keys(fields);
    let checkedItems = this.props.checkedItems;
    let isCurrent = (this.props.curItem == this.props.id) ? true : false;
    let fieldsItem = keys.map(key=>{
      return <Field key={key} field={fields[key]} code={key} item={item} isChecked={checkedItems.has(this.props.id)}/>;
    });

    let style = {backgroundColor:null}
    if (this.props.itemStyle) {
      let getStyle = this.props.itemStyle(this.props.item);
      if (getStyle.backgroundColor) style.backgroundColor=getStyle.backgroundColor;
    }
    if (!style.backgroundColor)
      if (this.props.isEven) style.backgroundColor='#f2f2f2';
    if (checkedItems.has(this.props.id)) style.backgroundColor=null;

    return <tr data-checked={checkedItems.has(this.props.id)} data-checked-current={isCurrent} style={{backgroundColor:style.backgroundColor}} onClick={this.handleClick}>{fieldsItem}</tr>;
  }
}

export class BodyGrid extends React.Component {
  constructor(props) {
    super(props);
  }
  render() {
    let index = 0;
    let items = (this.props.items) ? this.props.items.map(item=>{
      index=index+1;
      return <Item id={index} fields={this.props.fields} item={item} isEven={(index % 2 == 0)} itemStyle={this.props.itemStyle} checkedItems={this.props.checkedItems} curItem={this.props.curItem} onCheckedItem={this.props.onCheckedItem}/>;
    }) : null;
    // alert('Grid BodyGrid: '+items);
    return <tbody>{items}</tbody>;
  }
}

export class Grid extends React.Component {
  constructor(props) {
    super(props);
    this.filter = this.filter.bind(this);
    this.handleLoad = this.handleLoad.bind(this);
    this.handleChangeFilter = this.handleChangeFilter.bind(this);
    this.handleSort = this.handleSort.bind(this);
    this.handleClear = this.handleClear.bind(this);
    this.handleCheckedItem = this.handleCheckedItem.bind(this);
    this.handleClick = this.handleClick.bind(this);
    this.handleKeyUpItem = this.handleKeyUpItem.bind(this);
    this.getDefaultFilter = this.getDefaultFilter.bind(this);

    if (this.props.setClear) this.props.setClear(this.handleClear);
    if (this.props.setLoad) this.props.setLoad(this.handleLoad);

    let defaultSort = (this.props.defaultSort) ? this.props.defaultSort : {sort:{
        code:null
        , direction:null
      }};
    let valuesFilter = this.getDefaultFilter();
    this.state = {
      items: ((this.props.items) ? this.props.items : null)
      , sort:{
        code:defaultSort.code
        , direction:defaultSort.direction
      }
      , valuesFilter:valuesFilter
      , checkedItems: new Map()
      , isFocus: false
      , curItem: null
    };
  }
  getDefaultFilter() {
    let fields = this.props.fields;
    let valuesFilter = new Map();
    let keys = Object.keys(fields);
    keys.forEach((code,i,arr)=>{
      valuesFilter.set(code
        , { value:((fields[code].filter) ? fields[code].filter : "")
        , type: ((fields[code].type ) ? fields[code].type : null)
        });
    });
    return valuesFilter;
  }
  handleLoad(items) {
    // alert('Grid handleLoad: '+items.length);
    this.setState({
      items: items
      , checkedItems: new Map()
      , curItem: null
    });
    if (this.props.onCheckedItems) this.props.onCheckedItems(null);
  }
  handleClear() {
    let defaultSort = this.props.defaultSort;
    let valuesFilter = this.getDefaultFilter();
    this.setState({
      sort:{
        code:defaultSort.code
        , direction:defaultSort.direction
      }
      , valuesFilter:valuesFilter
      , checkedItems: new Map()
      , curItem: null
    });
    if (this.props.onCheckedItems) this.props.onCheckedItems(null);
  }
  handleChangeFilter(code,value) {
    let valuesFilter = this.state.valuesFilter;
    let property = valuesFilter.get(code);
    property.value = value;
    valuesFilter.set(code,property);
    this.setState({
      valuesFilter:valuesFilter
      , checkedItems: new Map()
    });
    if (this.props.onCheckedItems) this.props.onCheckedItems(null);
  }
  filter(items,values,fields) {
    //где values = Map([code;{value,type}])
    let keys = Object.keys(fields);
    if (!items) return null;
    let filterItems = items.filter(item => {
        let result = true;
        for (let i=0; i<keys.length; i++) {
          let stringSearch = values.get(keys[i]).value ? values.get(keys[i]).value : null;
          let value = convertValue(item[keys[i]], values.get(keys[i]).type);
          if (stringSearch && stringSearch!='Все') {
            if (!(value.toUpperCase().indexOf(stringSearch.toUpperCase()) > -1)) {
              result = false;
              //break;
            }
          }
        }
        return result;
      });
      return filterItems;
  }
  sort(items, field, direction) {
    let x, y, shouldSwitch, switch_i;
    let switching = true;
    let sortItems = items;
    if (!items) return null;
    /*Цикл, который будет продолжаться до тех пор, пока
     никакой перестановки не сделано:*/
        while (switching) {
            //начать с того, что никакой перестановки не сделано:
            switching = false;
            for (let i = 0; i < sortItems.length - 1; i++) {
              //alert(i);
                switch_i = i;
                shouldSwitch = false;
                x = (typeof(sortItems[i][field])=="number") ? sortItems[i][field] : ((sortItems[i][field]) ? sortItems[i][field].toLowerCase() : "");
                y = (typeof(sortItems[i][field])=="number") ? sortItems[i][field] : ((sortItems[i+1][field]) ? sortItems[i+1][field].toLowerCase() : "");
                //alert(i+' '+x+' '+y);
                if (direction == 'asc') {
                    if (x > y) {
                        shouldSwitch = true;
                        break;
                    }
                } else if (direction == 'desc') {
                    if (x < y) {
                        shouldSwitch = true;
                        break;
                    }
                }
            }
            //если нужно менять местами
            if (shouldSwitch) {
                /*Меняем элементы местами и помечаем, что была перестановка*/
                let item_i = sortItems[switch_i];
                sortItems[switch_i]=sortItems[switch_i+1];
                sortItems[switch_i+1]=item_i;
                switching = true;
            }
        }
    return sortItems;
  }
  handleSort(code) {
    let direction = (this.state.sort.code == code) ? ((this.state.sort.direction == 'asc') ? 'desc' : 'asc') : 'asc';
    this.setState({
      sort:{code:code,direction:direction}
      , checkedItems: new Map()
      , curItem:null
    });
    if (this.props.onCheckedItems) this.props.onCheckedItems(null);
  }
  getCheckedItemsToArray(checkedItems) {
    //alert('Grid getCheckedItemsToArray');
    if (!checkedItems) return null;
    let result = [];
    let iterator = checkedItems[Symbol.iterator]();
    let elm = iterator.next();
    while (!elm.done) {
      result.push(elm.value[1]);
      elm = iterator.next();     
    }
    return result;
  }
  handleCheckedItem(id,item,isCtrl) {
    let checkedItems = this.state.checkedItems;
    if (isCtrl && checkedItems.has(id)) {
      checkedItems.delete(id);
    } else if (isCtrl && !checkedItems.has(id)) {
      checkedItems.set(id,item);
    } else if (!isCtrl) {
      checkedItems.clear();
      checkedItems.set(id,item);
    } 
    this.setState({
      checkedItems:checkedItems
      , curItem:id
    });
    //alert('Grid handleCheckedItem:'+checkedItems);
    //alert(this.props.onCheckedItems);
    if (this.props.onCheckedItems) this.props.onCheckedItems(this.getCheckedItemsToArray(checkedItems));
  }
  handleClick(e) {
    if (!this.state.isFocus) {
      let grid = e.currentTarget;

      let bodyClick = onBodyClick.bind(this);
      function handleBodyClick(e) {
        bodyClick(e,grid);
      }

      let keyUpItem = this.handleKeyUpItem;

      document.addEventListener('click', handleBodyClick);
      document.addEventListener('keyup', keyUpItem);
      function onBodyClick(e,grid) {
        if (!isChildOfParent(e.target,grid)) {
          this.setState({isFocus:false});
          document.removeEventListener('click', handleBodyClick);
          document.removeEventListener('keyup', keyUpItem);
        }
      }

      this.setState({isFocus:true});
    }
  }
  handleKeyUpItem(e) {
    let keyUpTrue = false;
    let curItem=this.state.curItem;
    //вверх
    if (e.keyCode == 40) {
      curItem = curItem + 1;
      keyUpTrue = true;
    //вниз
    } else if (e.keyCode == 38) {
      curItem = curItem - 1;
      keyUpTrue = true;
    }
    if (keyUpTrue &&
      curItem >= 1 &&
      curItem <= this.countItems) {
    let fields = this.props.fields;
    let items = this.filter(this.state.items,this.state.valuesFilter,fields);
    let sort = this.state.sort;
    items = this.sort(items, sort.code, sort.direction);
    //alert(curItem+','+items[curItem-1].requestStatus);
    this.countItems = (items) ? items.length : 0;
    this.setState({
      checkedItems:new Map().set(curItem,items[curItem-1])
      , curItem:curItem
    });
        if (this.props.onCheckedItems) this.props.onCheckedItems([items[curItem-1]]);
    }
  }
  render() {

    let fields = this.props.fields;

    // alert('Grid render 1: '+this.state.items);

    let items = this.filter((this.props.items ? this.props.items : this.state.items),this.state.valuesFilter,fields);
    let sort = this.state.sort;
    items = this.sort(items, sort.code, sort.direction);
    this.countItems = (items) ? items.length : 0;

    // alert('Grid render 2: '+items);

    return <table className="Grid" onBlur={this.handleBlur} onClick={this.handleClick}>
      <thead>
        <FilterGrid fields={this.props.fields} valuesFilter={this.state.valuesFilter} onChangeFilter={this.handleChangeFilter}/>
        <HeaderGrid fields={fields} sort={this.state.sort} onSort={this.handleSort}/>
      </thead>
      <BodyGrid fields={fields} items={items} itemStyle={this.props.itemStyle} checkedItems={this.state.checkedItems} curItem={this.state.curItem} onCheckedItem={this.handleCheckedItem}/>
    </table>;
  }
}