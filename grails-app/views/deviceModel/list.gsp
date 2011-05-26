
<%@ page import="au.org.emii.aatams.DeviceModel" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'deviceModel.label', default: 'DeviceModel')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                            <g:sortableColumn property="id" title="${message(code: 'deviceModel.id.label', default: 'Id')}" />
                        
                            <th><g:message code="deviceModel.manufacturer.label" default="Manufacturer" /></th>
                        
                            <g:sortableColumn property="modelName" title="${message(code: 'deviceModel.modelName.label', default: 'Model Name')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${deviceModelInstanceList}" status="i" var="deviceModelInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${deviceModelInstance.id}">${fieldValue(bean: deviceModelInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: deviceModelInstance, field: "manufacturer")}</td>
                        
                            <td>${fieldValue(bean: deviceModelInstance, field: "modelName")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${deviceModelInstanceTotal}" />
            </div>
        </div>
    </body>
</html>