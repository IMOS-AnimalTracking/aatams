

<%@ page import="au.org.emii.aatams.ReceiverDownloadFile" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'receiverDownloadFile.label', default: 'ReceiverDownloadFile')}" />
        
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.upload.label" args="${['Receiver Events']}" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${receiverDownloadFileInstance}">
            <div class="errors">
                <g:renderErrors bean="${receiverDownloadFileInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:uploadForm action="save" >
                <div class="dialog">
                    <table>
                        <tbody>
                            <g:hiddenField name="type" value="${au.org.emii.aatams.ReceiverDownloadFileType.EVENTS_CSV.name()}" />
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label class="compulsory" for="path"><g:message code="receiverDownloadFile.path.label" default="Events file (CSV)" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: receiverDownloadFileInstance, field: 'path', 'errors')}">
                                    <input type="file" name="path" accept="*" />
                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
                </div>
            </g:uploadForm>
        </div>
    </body>
</html>
