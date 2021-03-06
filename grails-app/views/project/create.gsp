

<%@ page import="au.org.emii.aatams.Project" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'project.label', default: 'Project')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${createProjectCmd}">
            <div class="errors">
                <g:renderErrors bean="${createProjectCmd}" as="list" />
            </div>
            </g:hasErrors>
            <g:hasErrors bean="${projectInstance}">
            <div class="errors">
                <g:renderErrors bean="${projectInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" >
                <div class="dialog">
                    <table>
                        <tbody>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label class="compulsory" for="name"><g:message code="project.name.label" default="Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: createProjectCmd, field: 'name', 'errors')}">
                                    <g:textField name="name" value="${createProjectCmd?.name}" />

                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label class="compulsory" for="organisation"><g:message code="project.organisation.label" default="Organisation" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: createProjectCmd, field: 'organisation', 'errors')}">
                                    <g:select name="organisation.id" class="remember" from="${au.org.emii.aatams.Organisation.listActive()}" optionKey="id" value="${createProjectCmd?.organisation?.id}"  />

                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label class="compulsory" for="person"><g:message code="project.organisation.label" default="Project Investigator" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: createPersonCmd, field: 'person', 'errors')}">
                                    <g:select name="person.id" class="remember" from="${au.org.emii.aatams.Person.list()}" optionKey="id" value="${createProjectCmd?.person?.id}"  />

                                </td>
                            </tr>

                            <shiro:hasRole name="SysAdmin">
                                <tr class="prop">
                                    <td valign="top" class="name">
                                        <label for="isProtected"><g:message code="project.isProtected.label" default="Protected" /></label>
                                    </td>
                                    <td valign="top" class="value ${hasErrors(bean: projectInstance, field: 'isProtected', 'errors')}">
                                        <g:checkBox name="isProtected" value="${projectInstance?.isProtected}" checked="${projectInstance?.isProtected}" />
                                    </td>
                                </tr>
                            </shiro:hasRole>
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
