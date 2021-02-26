import React from 'react';

export class Home extends React.Component {
  render() {
    return (<div data-display={this.props.dataDisplay}>
        Это демо web-приложения с использованием:
        <ul>
            <li>React, Node.js, JavaScript, Ajax</li>
            <li>Servlet, MVC, SpringMVC</li>
            <li>JDBC, MySQL</li>
            <li>SOAP, SpringWS</li>
            <li>MQ, SpringMQ, ActiveMQ</li>
            <li>JasperReports, iReports</li>
        </ul>
        Включает следующие возможности:
        <ul>
            <li>Аутентификация</li>
            <li>Аудит</li>
            <li>Разграничение доступа с применением ролей и пермишенов</li>
            <li>Использование настроек</li>
            <li>Просмотр списка клиентов</li>
            <li>Создание клиента</li>
            <li>Редактирование клиента</li>
            <li>Просмотр списка заявок</li>
            <li>Создание заявки</li>
            <li>Отмена заявки</li>
            <li>Ведение аудита и истории заявки, просмотр подробной информации</li>
            <li>Формирование отчетов по заявкам</li>
            <li>Ведение интеграционного лога по mq и ws интеграциям</li>
            <li>Выполнение отложенной обработки заявки</li>
            <li>Выполнение периодического задания по заявкам</li>
        </ul>
    </div>);
  }
}
