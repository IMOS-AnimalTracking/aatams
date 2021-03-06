
<%@ page import="au.org.emii.aatams.Person" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'person.label', default: 'Person')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <shiro:hasPermission permission="personWriteAny">
              <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
            </shiro:hasPermission>
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
                            <td valign="top" class="name"><g:message code="person.name.label" default="Name" /></td>

                            <td valign="top" class="value">${fieldValue(bean: personInstance, field: "name")}</td>

                        </tr>

                        <shiro:hasRole name="SysAdmin">
                          <tr class="prop">
                              <td valign="top" class="name"><g:message code="secUser.username.label" default="Username" /></td>

                              <td valign="top" class="value">${fieldValue(bean: personInstance, field: "username")}</td>

                          </tr>
                        </shiro:hasRole>

                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="person.organisation.label" default="Organisation" /></td>

                            <td valign="top" class="value">
                              <g:link controller="organisation" action="show" id="${personInstance?.organisation?.id}">${personInstance?.organisation?.encodeAsHTML()}</g:link>
                            </td>

                        </tr>

                        <shiro:user>
                          <tr class="prop">
                              <td valign="top" class="name"><g:message code="person.phoneNumber.label" default="Phone Number" /></td>

                              <td valign="top" class="value">${fieldValue(bean: personInstance, field: "phoneNumber")}</td>

                          </tr>

                          <tr class="prop">
                              <td valign="top" class="name"><g:message code="person.emailAddress.label" default="Email Address" /></td>

                              <td valign="top" class="value">${fieldValue(bean: personInstance, field: "emailAddress")}</td>

                          </tr>
                        </shiro:user>

                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="person.projectRoles.label" default="Projects" /></td>

                            <td valign="top" style="text-align: left;" class="value">
                                <ul>
                                <g:each in="${personInstance.projectRoles?.project}" var="p">
                                  <li><g:link controller="project" action="show" id="${p.id}">${p?.encodeAsHTML()}</g:link></li>
                                </g:each>
                                </ul>
                            </td>

                        </tr>

                        <shiro:hasRole name="SysAdmin">
                          <tr class="prop">
                              <td valign="top" class="name"><g:message code="person.status.label" default="Status" /></td>

                              <td valign="top" class="value">${personInstance?.status?.encodeAsHTML()}</td>

                          </tr>
                        </shiro:hasRole>

                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="person.defaultTimeZone.label" default="Default Time Zone" /></td>

                            <td valign="top" class="value">${personInstance?.defaultTimeZone?.encodeAsHTML()}</td>

                        </tr>

                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <g:hiddenField name="id" value="${personInstance?.id}" />

                    <!-- This button should only be shown if permitted -->
<%--                    <shiro:hasPermission permission="${personInstance?.id}:write">--%>
                      <g:if test="${canEdit}">
                        <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
                      </g:if>
<%--                    </shiro:hasPermission>--%>

                    <shiro:hasRole name="SysAdmin">
                      <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                    </shiro:hasRole>
                </g:form>
            </div>
        </div>
    </body>
</html>
