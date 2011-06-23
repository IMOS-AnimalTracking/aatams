// Place your Spring DSL code here
beans = 
{
    configurer(org.springframework.beans.factory.config.CustomEditorConfigurer) 
    {
        propertyEditorRegistrars = [ref("registrar")]
    }
    
    registrar(au.org.emii.aatams.CustomPropertyEditorRegistrar)
}

