import org.apache.commons.logging.LogFactory
import org.codehaus.groovy.grails.commons.ConfigurationHolder

import grails.plugin.searchablemultitenantbridge.index.IndexWithCompass

class SearchableMultiTenantBridgeGrailsPlugin {
    // the plugin version
    def version = "0.1"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.3.7 > *"
    // the other plugins this plugin depends on
    def dependsOn = [searchable: "0.6.3",
					multiTenantCore: "1.0.3"]
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]

    // TODO Fill in these fields
    def author = "Archanaa K. Karun"
    def authorEmail = ""
    def title = "searchable-multi-tenant-bridge (SMTB)"
    def description = '''\\
This Plugin is to overcome the ClassCastException that occurs while the two plugins Searchable [http://grails.org/plugin/searchable] and Multi-Tenant Plugin (Core) [http://grails.org/plugin/multi-tenant-core] co-exist together.
'''

    // URL to the plugin's documentation
    def documentation = "https://github.com/archanaa-karun/searchable-multi-tenant-bridge"

    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional), this event occurs before 
    }

    def doWithSpring = {
		indexWithCompass(IndexWithCompass)
    }

    def doWithDynamicMethods = { ctx ->
        // TODO Implement registering dynamic methods to classes (optional)
    }

    def doWithApplicationContext = { applicationContext ->
        // TODO Implement post initialization spring config (optional)
    }

    def onChange = { event ->
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    def onConfigChange = { event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }
}
