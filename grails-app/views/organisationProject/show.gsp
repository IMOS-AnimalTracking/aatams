
<%@ page import="au.org.emii.aatams.OrganisationProject" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'organisationProject.label', default: 'OrganisationProject')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.show.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="dialog">
                <table>
                    <tbody>

                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="organisationProject.organisation.label" default="Organisation" /></td>

                            <td valign="top" class="value"><g:link controller="organisation" action="show" id="${organisationProjectInstance?.organisation?.id}">${organisationProjectInstance?.organisation?.encodeAsHTML()}</g:link></td>

                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="organisationProject.project.label" default="Project" /></td>

                            <td valign="top" class="value"><g:link controller="project" action="show" id="${organisationProjectInstance?.project?.id}">${organisationProjectInstance?.project?.encodeAsHTML()}</g:link></td>

                        </tr>

                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <g:hiddenField name="id" value="${organisationProjectInstance?.id}" />
                    <g:hiddenField name="projectId" value="${organisationProjectInstance?.project?.id}" />
                    <shiro:hasPermission permission="project:${organisationProjectInstance?.project?.id}:edit">
                      <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
                      <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                    </shiro:hasPermission>
                </g:form>
            </div>
        </div>
    </body>
</html>
