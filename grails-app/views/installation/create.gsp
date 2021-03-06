

<%@ page import="au.org.emii.aatams.Installation" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'installation.label', default: 'Installation')}" />
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
            <g:hasErrors bean="${installationInstance}">
            <div class="errors">
                <g:renderErrors bean="${installationInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label class="compulsory" for="name"><g:message code="installation.name.label" default="Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: installationInstance, field: 'name', 'errors')}">
                                    <g:textField name="name" value="${installationInstance?.name}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label class="compulsory" for="configuration"><g:message code="installation.configuration.label" default="Configuration" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: installationInstance, field: 'configuration', 'errors')}">
                                    <g:select name="configuration.id" class="remember" from="${au.org.emii.aatams.InstallationConfiguration.list()}" optionKey="id" value="${installationInstance?.configuration?.id}"  />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label class="compulsory" for="project"><g:message code="installation.project.label" default="Project" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: installationInstance, field: 'project', 'errors')}">
                                    <g:select name="project.id" class="remember" from="${candidateProjects}" optionKey="id" value="${installationInstance?.project?.id}"  />

                                </td>
                            </tr>
                        
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
