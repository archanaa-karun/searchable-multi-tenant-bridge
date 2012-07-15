package grails.plugin.searchablemultitenantbridge.index;

import grails.plugin.searchable.internal.compass.CompassGpsUtils;

import org.compass.gps.CompassGps;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.Ordered;

public class IndexWithCompass implements BeanFactoryPostProcessor, Ordered	{
	
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException	{
		CompassGps compassGps = (CompassGps)beanFactory.getBean("compassGps");
		CompassGpsUtils.index(compassGps, null);
	}

	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return 0;
	}
}