import React from 'react';

export class NotFound extends React.Component {
    constructor(props) {
        super(props);
    }
    render() {
        return <>
            <div style={{fontSize:'20px',margin: '5px 5px'}}>Ресурс не найден.</div>
            </>;
    }
}