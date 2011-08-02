import au.org.emii.aatams.*

import org.apache.shiro.SecurityUtils

/**
 * Generated by the Shiro plugin. This filters class protects all URLs
 * via access control by convention.
 */
class SecSecurityFilters 
{
    def permissionUtilsService
    
    def accessibleControllersRegexp = 
        "organisation|organisationProject|project|projectRole|person|" + \
        "installation|installationStation|receiver|species|tag|sensor|" + \
        "animalRelease|detection|receiverDeployment|receiverRecovery|" + \
        "receiverEvent|navigationMenu|receiverDownloadFile"
    
    def filters = 
    {
        //
        // Only Sys Admins can delete.
        //
        delete(controller:'*', action:'delete')
        {
            before = 
            {
                accessControl
                {
                    role("SysAdmin")
                }
            }
        }
        
        //
        // Some controllers are only accessible to Sys Admin (generally those
        // that aren't linked on the navigation menu.
        //
        nonAccessible(controller:accessibleControllersRegexp, action:'*', invert:true)
        {
            before = 
            {
                accessControl
                {
                    role("SysAdmin")
                }
            }
        }
        
        //
        // Anyone (including unauthenticated users) can list/index/show the 
        // accessible controllers.
        //
        listIndexShow(controller:accessibleControllersRegexp, action:'list|index|show')
        {
            before = 
            {
                return true
            }
        }
        
        // As above.
        nullAction(controller:accessibleControllersRegexp, action:null)
        {
            before = 
            {
                return true
            }
        }
        
        //
        // Authenticated users can create Organisations (although
        // they will have status of PENDING and will not be useable).
        //
        organisationCreate(controller:'organisation', action:'create|save')
        {
            before =
            {
                if (SecurityUtils.subject.isAuthenticated())
                {
                    return true
                }

                redirect(controller:'auth', action:'unauthorized')
            }
        }

        // Only Sys Admins can update organisations.
        organisationUpdate(controller:'organisation', action:'edit|update')
        {
            before = 
            {
                accessControl
                {
                    role("SysAdmin")
                }
            }
        }

        //
        // Authenticated users can create Projects (although
        // they will have status of PENDING and will not be useable).
        //
        projectCreate(controller:'project', action:'create|save')
        {
            before =
            {
                if (SecurityUtils.subject.isAuthenticated())
                {
                    return true
                }

                redirect(controller:'auth', action:'unauthorized')
            }
        }

        // Only PIs can edit existing projects.
        projectUpdate(controller:'project', action:'edit|update')
        {
            before =
            {
                // Non-authenticated users can't update.
                if (   SecurityUtils.subject.isAuthenticated()
                    && SecurityUtils.subject.isPermitted(permissionUtilsService.buildPrincipalInvestigatorPermission(params.id)))
                {
                    return true
                }
                
                redirect(controller:'auth', action:'unauthorized')
            }
        }
        
        // Any user can create (as with project and organisation).
        personCreate(controller:'person', action:'create|save')
        {
            before =
            {
                return true
            }
        }
        
        //
        // The following users can update people:
        //
        //  - a sys admin
        //  - a PI for the project that the operand user belongs to
        //  - a user updating their own record.
        //
        personUpdate(controller:'person', action:'edit|update')
        {
            before =
            {
                // Non-authenticated users can't update.
                if (!SecurityUtils.subject.isAuthenticated())
                {
                    redirect(controller:'auth', action:'unauthorized')
                }
                
                // The user whose record is being updated.
                SecUser operandUser = SecUser.get(params?.id)
                
                // The logged in user.
                SecUser principal = SecUser.findByUsername(SecurityUtils?.subject?.principal)

                // User can update own record.
                if (operandUser == principal)
                {
                    return true
                }
                
                // Sys admin can do anything.
                if (SecurityUtils.subject.hasRole("SysAdmin"))
                {
                    return true
                }

                // Check if principal is a PI on the operand users's projects.
                if (SecurityUtils.subject.isPermitted(permissionUtilsService.buildPrincipalInvestigatorPermission('*')))
                {
                    for (ProjectRole operandUserRole : operandUser.projectRoles)
                    {
                        Project project = operandUserRole?.project

                        // If the principal is a PI on this project, then they can edit.
                        if (SecurityUtils.subject.isPermitted(permissionUtilsService.buildPrincipalInvestigatorPermission(project?.id)))
                        {
                            return true
                        }
                    }
                }

                // Non of the update conditions were met if we got this far.
                redirect(controller:'auth', action:'unauthorized')
            }
        }
        
        //
        // Some controllers security is determined by whether or not the 
        // principal has write access to the associated project.
        //
        def projectAccessWriteControllers = 
            "installation|installationStation|tag|animalRelease|detection|" + \
            "projectRole|receiverDeployment|receiverDownloadFile|receiverRecovery|receiverEvent|" + \
            "organisationProject|sensor"
        
        projectAccessWrite(controller:projectAccessWriteControllers, 
                           action:'create|save|edit|update')
        {
            before =
            {
                println params
                
                def projectId = params.project?.id
                
                // Some views store project's ID in "projectId" variable.
                if (projectId == null)
                {
                    projectId = params.projectId
                }
                
                // Non-authenticated users can't update.
                if (   SecurityUtils.subject.isAuthenticated()
                    && SecurityUtils.subject.isPermitted(permissionUtilsService.buildProjectWritePermission(projectId)))
                {
                    return true
                }
                
                redirect(controller:'auth', action:'unauthorized')
            }
        }
        
        //
        // Only PIs can create receivers (a PI has the appropriate permission
        // assigned when they are made a PI).
        //
        receiverCreate(controller:'receiver', action:'create|save')
        {
            before =
            {
                // Non-authenticated users can't update.
                if (   SecurityUtils.subject.isAuthenticated()
                    && SecurityUtils.subject.isPermitted(permissionUtilsService.buildReceiverCreatePermission(params.id)))
                {
                    return true
                }
                
                redirect(controller:'auth', action:'unauthorized')
            }
        }
        
        //
        // The user who creates a receiver is the only one who can update that
        // same user (the appropriate permission is assigned to the user at
        // receiver creation time).
        //
        receiverUpdate(controller:'receiver', action:'edit|update')
        {
            before =
            {
                // Non-authenticated users can't update.
                if (   SecurityUtils.subject.isAuthenticated()
                    && SecurityUtils.subject.isPermitted(permissionUtilsService.buildReceiverUpdatePermission(params.id)))
                {
                    return true
                }
                
                redirect(controller:'auth', action:'unauthorized')
            }
        }
    }
}
