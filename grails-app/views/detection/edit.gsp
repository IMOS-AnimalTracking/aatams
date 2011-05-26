

<%@ page import="au.org.emii.aatams.Detection" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'detection.label', default: 'Detection')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${detectionInstance}">
            <div class="errors">
                <g:renderErrors bean="${detectionInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${detectionInstance?.id}" />
                <g:hiddenField name="version" value="${detectionInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="receiver"><g:message code="detection.receiver.label" default="Receiver" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: detectionInstance, field: 'receiver', 'errors')}">
                                    <g:select name="receiver.id" from="${au.org.emii.aatams.Receiver.list()}" optionKey="id" value="${detectionInstance?.receiver?.id}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="tag"><g:message code="detection.tag.label" default="Tag" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: detectionInstance, field: 'tag', 'errors')}">
                                    <g:select name="tag.id" from="${au.org.emii.aatams.Tag.list()}" optionKey="id" value="${detectionInstance?.tag?.id}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="timestamp"><g:message code="detection.timestamp.label" default="Timestamp" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: detectionInstance, field: 'timestamp', 'errors')}">
                                    <g:datePicker name="timestamp" precision="day" value="${detectionInstance?.timestamp}"  />
                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>