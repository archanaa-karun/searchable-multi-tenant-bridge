Searchable - Multi Tenant Bridge (SMTB)
=======================================

There are many developers who struggle to overcome the problems that exist when the two essential Grails Plugins Searchable and Multi Tenant (Core) for an application come together. Here is the solution for it.

This Plugin is to overcome the ClassCastException that occurs while the two plugins Searchable [http://grails.org/plugin/searchable] and Multi-Tenant Plugin (Core) [http://grails.org/plugin/multi-tenant-core] co-exist together.

This particular exception occurs due to the dependent plugin of the Multi-Tenant Plugin (Core) called Falcone Util [http://grails.org/plugin/falcone-util].
This is caused due to the modification of the session in com.infusion.util.event.spring.InterceptableSessionFactoryPostProcessor. Due to this modification, the session (SessionFactoryImpl) needed by the Indexing mechanism (Compass) is modified as the session (InterceptableSessionFactory) and hence throws the Exception at the below point of code:


	public class DefaultHibernateEntityLifecycleInjector implements HibernateEntityLifecycleInjector {
    
    		public void injectLifecycle(SessionFactory sessionFactory, HibernateGpsDevice device) throws HibernateGpsDeviceException {

        		SessionFactoryImpl sessionFactoryImpl = (SessionFactoryImpl) sessionFactory;
        		.....
     		}
      		.....
	}


So what this plugin does is, it performs the indexing required for search by the Searchable Plugin first and then the session modification for Multi Tenant (Core) happens.

Due to this the we need to accomodate the below few few drawbacks with Multi-Tenant Plugin (Core) Plugin:

Refer http://multi-tenant.github.com/grails-multi-tenant-core/guide/4.%20Behind%20the%20scenes.html

Just install this Plugin and do the following modifications specific to Multi-Tenant (Core).

1. Dynamically injected Named Parameter ":tenantId" will not work
=================================================================
  
Earlier GORM functions were automatically injected with tenantId without explicitly specifying it. Now this will not happen. 

All GORM functions backed by a Criteria object (createCriteria, countBy, exists, find, get, list, listOrderBy, withCriteria) will have to be passed with the tenantId explicitly.
    
Workaround:
    
Instead of ‘:tenantId’ in queries, use ‘TenantUtils.getCurrentTenant()’.
    
E.g.: 

Before SMTB: 
    
    Domain.executeQuery("select d from Domain as d where d.tenantId = :tenantId")

After SMTB: 

    Domain.executeQuery("select d from Domain as d where d.tenantId = " + TenantUtils.getCurrentTenant())

The same holds good for executeUpdate() too.

2. TenantId has to be artificially injected in createCriteria and withCriteria
==============================================================================

Before SMTB: 
        
    def listDomain(params) { 

            def d = Domain.createCriteria()

	        def results = d.list(max: params.max, offset: params.offset)  {
		        
                order(params.sort, params.order)
	        }
	    }

After SMTB: 
        
    def listDomain(params) { 

            def d = Domain.createCriteria()

            def results = d.list(max: params.max, offset: params.offset)  {

                eq('tenantId',TenantUtils.getCurrentTenant())

		        order(params.sort, params.order)
	        }
	    }

3. All GORM function has to be artificially injected with TenantId with the help of NamedQueries
================================================================================================

For all the Domain classes which are annotated as "@MultiTenant" add namedQuery "domain":

    static namedQueries = { 
      domain {
		 eq 'tenantId', TenantUtils.getCurrentTenant()
	  }
	}

The name ‘domain’ is the name of the Named Query and can be named as anything.

Now the NamedQuery "domain" is added to each of the GORM function to retrieve the right records for the Tenant.

Before SMTB:
    
    Domain.list()
    Domain.get(id)
    Domain.countByTitleAndAuthor("The Sum of All Fears", "Tom Clancy")
    Domain.exists(id)
    Domain.find(String query)
    Domain.listOrderByAuthor()

After SMTB:
    
    Domain.domain.list()
    Domain.domain.get(id)
    Domain.domain.countByTitleAndAuthor("The Sum of All Fears", "Tom Clancy")
    Domain.domain.exists(id)
    Domain.domain.find(String query)
    Domain.domain.listOrderByAuthor()