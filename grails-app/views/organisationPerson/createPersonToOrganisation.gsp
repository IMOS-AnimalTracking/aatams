

<%@ page import="au.org.emii.aatams.OrganisationPerson" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'organisationPerson.label', default: 'OrganisationPerson')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
        </div>
        <div class="body">
            <h1>Add Person to Organisation</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${organisationPersonInstance}">
            <div class="errors">
                <g:renderErrors bean="${organisationPersonInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="savePersonToOrganisation" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="organisation"><g:message code="organisationPerson.organisation.label" default="Organisation" /></label>
                                </td>
                                <td>${organisationPersonInstance.organisation}</td>
                                  <g:hiddenField name="organisation.id" value="${organisationPersonInstance?.organisation?.id}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="person"><g:message code="organisationPerson.person.label" default="Person" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: organisationPersonInstance, field: 'person', 'errors')}">
                                    <g:select name="person.id" from="${au.org.emii.aatams.Person.list()}" optionKey="id" value="${organisationPersonInstance?.person?.id}"  />

                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:submitButton name="savePersonToOrganisation" class="savePersonToOrganisation" value="${message(code: 'default.button.add.label', default: 'Add')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
