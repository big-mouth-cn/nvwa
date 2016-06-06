package org.bigmouth.nvwa.dpl.factory;

import java.lang.reflect.Method;
import java.util.List;

import org.bigmouth.nvwa.dpl.event.listener.LoggingPlugInListener;
import org.bigmouth.nvwa.dpl.event.listener.LoggingServiceListener;
import org.bigmouth.nvwa.dpl.event.listener.PlugInListener;
import org.bigmouth.nvwa.dpl.event.listener.ServiceListener;
import org.bigmouth.nvwa.dpl.hotswap.ClassFilter;
import org.bigmouth.nvwa.dpl.hotswap.PlugInClassLoader;
import org.bigmouth.nvwa.dpl.plugin.GenericPlugIn;
import org.bigmouth.nvwa.dpl.plugin.ImmutablePlugInConfig;
import org.bigmouth.nvwa.dpl.plugin.MutablePlugIn;
import org.bigmouth.nvwa.dpl.plugin.PlugIn;
import org.bigmouth.nvwa.dpl.plugin.PlugInConfig;
import org.bigmouth.nvwa.dpl.service.DefaultFunctionalService;
import org.bigmouth.nvwa.dpl.service.DefaultProceduralService;
import org.bigmouth.nvwa.dpl.service.ImmutableServiceConfig;
import org.bigmouth.nvwa.dpl.service.MethodServiceClosure;
import org.bigmouth.nvwa.dpl.service.MethodServiceFunctor;
import org.bigmouth.nvwa.dpl.service.Service;
import org.bigmouth.nvwa.dpl.service.ServiceClosure;
import org.bigmouth.nvwa.dpl.service.ServiceConfig;
import org.bigmouth.nvwa.dpl.service.ServiceFunctor;
import org.bigmouth.nvwa.dpl.status.ImmutableStatus;
import org.bigmouth.nvwa.dpl.status.Status;

import com.google.common.collect.Lists;

//TODO:还需要细化
public class AnnotationPlugInDiscover extends AbstractPlugInDiscover implements PlugInDiscover {

	public AnnotationPlugInDiscover() {
		super();
	}

	public AnnotationPlugInDiscover(ResourceSearchSupport factorySupport) {
		super(factorySupport);
	}

	@SuppressWarnings("unchecked")
	@Override
	public PlugIn discover(PlugInClassLoader classloader) {
		List<Class<?>> classes = getSearchSupport().searchClasses(classloader, new ClassFilter() {

			@Override
			public boolean accept(Class<?> clazz) {
				org.bigmouth.nvwa.dpl.factory.annotation.PlugIn plugInAnno = clazz
						.getAnnotation(org.bigmouth.nvwa.dpl.factory.annotation.PlugIn.class);
				return null != plugInAnno;
			}
		});

		PlugInConfig plugInConfig = null;
		Status plugInStatus = null;
		List plugInListeners = Lists.newArrayList(new LoggingPlugInListener());

		List<Service> services = Lists.newArrayList();

		for (Class<?> clazz : classes) {
			org.bigmouth.nvwa.dpl.factory.annotation.PlugIn plugInAnno = clazz
					.getAnnotation(org.bigmouth.nvwa.dpl.factory.annotation.PlugIn.class);
			String code = plugInAnno.code();
			String name = plugInAnno.name();
			String desc = plugInAnno.description();
			boolean isRunning = plugInAnno.isRunning();
			Class<? extends PlugInListener>[] listnerClasses = plugInAnno.listeners();

			if (null == plugInConfig) {
				plugInConfig = new ImmutablePlugInConfig(name, code, desc);
				plugInStatus = new ImmutableStatus(isRunning);
				for (Class<? extends PlugInListener> plClass : listnerClasses) {
					try {
						Object pl = plClass.newInstance();
						plugInListeners.add(pl);
					} catch (InstantiationException e) {
						throw new RuntimeException("createAnnotationPlugIn:", e);
					} catch (IllegalAccessException e) {
						throw new RuntimeException("createAnnotationPlugIn:", e);
					}
				}
			}

			// create target
			Object target = null;
			try {
				target = clazz.newInstance();
			} catch (InstantiationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			// else{
			// PlugInConfig _plugInConfig = new
			// ImmutablePlugInConfig(name,code,desc);
			// if(!plugInConfig.equals(_plugInConfig)){
			// //TODO:
			// throw new RuntimeException("Same PlugIn Config.");
			// }
			// }

			Method[] methods = clazz.getDeclaredMethods();
			for (Method m : methods) {
				org.bigmouth.nvwa.dpl.factory.annotation.Service serviceAnno = m
						.getAnnotation(org.bigmouth.nvwa.dpl.factory.annotation.Service.class);
				if (null == serviceAnno)
					continue;
				String serviceCode = serviceAnno.code();
				String serviceName = serviceAnno.name();
				String serviceDesc = serviceAnno.description();
				boolean serviceIsRunning = serviceAnno.isRunning();
				Class<? extends ServiceListener>[] serviceListnerClasses = serviceAnno.listeners();

				ServiceConfig serviceConfig = new ImmutableServiceConfig(serviceName, serviceCode,
						serviceDesc);
				Status serviceStatus = new ImmutableStatus(serviceIsRunning);
				List serviceListeners = Lists.newArrayList(new LoggingServiceListener());

				for (Class<? extends ServiceListener> slClass : serviceListnerClasses) {
					try {
						Object sl = slClass.newInstance();
						serviceListeners.add(sl);
					} catch (InstantiationException e) {
						throw new RuntimeException("createAnnotationPlugIn:", e);
					} catch (IllegalAccessException e) {
						throw new RuntimeException("createAnnotationPlugIn:", e);
					}
				}

				if (m.getReturnType() == Void.TYPE) {
					ServiceClosure closure = new MethodServiceClosure(target, m);
					Service service = new DefaultProceduralService(serviceConfig, serviceStatus,
							serviceListeners, closure);
					services.add(service);
				} else {
					ServiceFunctor functor = new MethodServiceFunctor(target, m);
					Service service = new DefaultFunctionalService(serviceConfig, serviceStatus,
							serviceListeners, functor);
					services.add(service);
				}
			}
		}

		if (0 == services.size())
			throw new RuntimeException("!NO! service.");
		MutablePlugIn plugIn = new GenericPlugIn(plugInConfig, plugInStatus, plugInListeners);
		plugIn.setServices(services);

		return plugIn;
	}
}
