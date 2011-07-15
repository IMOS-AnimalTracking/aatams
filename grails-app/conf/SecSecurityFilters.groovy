import au.org.emii.aatams.*

import org.apache.shiro.SecurityUtils

/**
 * Generated by the Shiro plugin. This filters class protects all URLs
 * via access control by convention.
 */
class SecSecurityFilters 
{
    def permissionUtilsService
    
    //
    // Any controllers not specified here are only accessible to 
    // sys admin.
    //
    // The value in each map entry is the parameter relating to project ID
    // (for those entities controller by project membership/access).
    //
    //
    def accessibleControllers = 
        ["organisation":"",
         "organisationProject":"project.id",
         "project":"id",
         "projectRole":"project.id",
         "person":"",
         "installation":"project.id",
         "installationStation":"project.id",
         "receiver":"project.id",
         "species":"",
         "tag":"project.id",
         "sensor":"project.id",
         "animalRelease":"project.id",
         "detection":"project.id",
         "receiverDeployment":"project.id",
         "receiverRecovery":"project.id"]
    
    //
    // Anyone can execute the following actions (even if not
    // authenticated).
    //
    // Note that some entities may be filtered after action executes if not 
    // authenticated, e.g. embargoed data, scrambled station locations.
    //
    def publicActions =
        ["index",
         "list",
         "show",
         "lookupByName"]
    
    //
    // Only users with write permission on a specific project can perform these actions.
    //
    def projectWriteActions =
//        ["create",
//         "save",
//         "edit",
//         "update"]
        ["save",
         "edit",
         "update"]
    
    def projectWriteAnyActions =
        ["create"]
    
    //
    // Only sys admin role can perform these actions.
    //
    def deleteActions = 
        ["delete"]
                        
    def filters = 
    {
        all(uri: "/**") 
        {
            before = 
            {
                // Ignore direct views (e.g. the default main index page).
                if (!controllerName) return true

                // Anyone should be able to see the navigation menu.
                if (controllerName == "navigationMenu")
                {
                    return true
                }
                
                if (!accessibleControllers.containsKey(controllerName))
                {
                    accessControl
                    {
                        role("SysAdmin")
                    }
                }
                else
                {
                    // null action is equivalent to action of "index"/"list"
                    if (publicActions.contains(actionName) || (actionName == null))
                    {
                        return true
                    }
                    
                    //
                    //  Only some users have "WRITE" access to a project (and
                    //  all its associated data).
                    //
                    if (projectWriteActions.contains(actionName))
                    {
                        def projectId = getProjectId(params, controllerName)
                        
                        if (SecurityUtils.subject.isPermitted(permissionUtilsService.buildProjectWritePermission(projectId)))
                        {
                            return true
                        }
                        else
                        {
                            log.warn("Not permitted, user: " + SecurityUtils.subject
                                     + ", controller: " + controllerName
                                     + ", action: " + actionName
                                     + ", project ID: " + String.valueOf(projectId))
                            System.out.println("Not permitted, user: " + SecurityUtils.subject
                                     + ", controller: " + controllerName
                                     + ", action: " + actionName
                                     + ", project ID: " + String.valueOf(projectId))
                        }
                    }

                    if (projectWriteAnyActions.contains(actionName))
                    {
                        if (SecurityUtils.subject.isPermitted(permissionUtilsService.buildProjectWriteAnyPermission()))
                        {
                            return true
                        }
                        else
                        {
                            log.warn("Not permitted, user: " + SecurityUtils.subject
                                     + ", controller: " + controllerName
                                     + ", action: " + actionName)
                            System.out.println("Not permitted, user: " + SecurityUtils.subject
                                     + ", controller: " + controllerName
                                     + ", action: " + actionName)
                        }
                    }
                    
                    if (deleteActions.contains(actionName))
                    {
                        accessControl
                        {
                            role("SysAdmin")
                        }
                    }

                    // Access control by convention.
                    accessControl()
                }
            }
        }
    }
    
    /**
     * Returns the project ID (which can be stored in different parameters,
     * depending on the controller).
     */
    def getProjectId(params, controllerName)
    {
        // Special case for detection.
        if (controllerName == "detection")
        {
            def projectId = params[accessibleControllers[controllerName]]
            if (projectId != null)
            {
                return projectId
            }
            else
            {
                return ReceiverDeployment.get(params?.receiverDeployment?.id)?.receiver?.project?.id
            }
        }
        
        return params[accessibleControllers[controllerName]]
    }
}