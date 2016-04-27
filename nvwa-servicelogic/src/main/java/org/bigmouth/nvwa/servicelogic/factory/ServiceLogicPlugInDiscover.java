package org.bigmouth.nvwa.servicelogic.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang.StringUtils;
import org.bigmouth.nvwa.dpl.event.ExecutionFailedEvent;
import org.bigmouth.nvwa.dpl.event.listener.PlugInListener;
import org.bigmouth.nvwa.dpl.event.listener.PlugInListenerAdapter;
import org.bigmouth.nvwa.dpl.event.listener.ReviseContextClassLoaderPlugInListener;
import org.bigmouth.nvwa.dpl.event.listener.ReviseContextClassLoaderServiceListener;
import org.bigmouth.nvwa.dpl.event.listener.RuntimeObjectServiceListener;
import org.bigmouth.nvwa.dpl.event.listener.ServiceListener;
import org.bigmouth.nvwa.dpl.event.listener.ServiceListenerAdapter;
import org.bigmouth.nvwa.dpl.factory.AbstractPlugInDiscover;
import org.bigmouth.nvwa.dpl.factory.PlugInDiscover;
import org.bigmouth.nvwa.dpl.factory.annotation.ThreadModel;
import org.bigmouth.nvwa.dpl.hotswap.PlugInClassLoader;
import org.bigmouth.nvwa.dpl.hotswap.ResourceFilter;
import org.bigmouth.nvwa.dpl.plugin.ImmutablePlugInConfig;
import org.bigmouth.nvwa.dpl.plugin.MutablePlugIn;
import org.bigmouth.nvwa.dpl.plugin.PlugIn;
import org.bigmouth.nvwa.dpl.plugin.PlugInConfig;
import org.bigmouth.nvwa.dpl.plugin.SpringSytlePlugIn;
import org.bigmouth.nvwa.dpl.service.DefaultProceduralService;
import org.bigmouth.nvwa.dpl.service.ImmutableServiceConfig;
import org.bigmouth.nvwa.dpl.service.MethodServiceClosure;
import org.bigmouth.nvwa.dpl.service.MutableService;
import org.bigmouth.nvwa.dpl.service.Service;
import org.bigmouth.nvwa.dpl.service.ServiceClosure;
import org.bigmouth.nvwa.dpl.service.ServiceConfig;
import org.bigmouth.nvwa.dpl.status.ImmutableStatus;
import org.bigmouth.nvwa.dpl.status.PlugInStatusListener;
import org.bigmouth.nvwa.dpl.status.ServiceStatusListener;
import org.bigmouth.nvwa.dpl.status.Status;
import org.bigmouth.nvwa.dpl.status.StatusHolder;
import org.bigmouth.nvwa.dpl.status.StatusHolderFactory;
import org.bigmouth.nvwa.dpl.status.StatusHolderSingleton;
import org.bigmouth.nvwa.log.MultiRecordSupport;
import org.bigmouth.nvwa.log.rdb.AnnoRecordControllerFactory;
import org.bigmouth.nvwa.log.rdb.LogAppender;
import org.bigmouth.nvwa.log.rdb.PlugInManageRecordControllerFactory;
import org.bigmouth.nvwa.log.rdb.RecordControllerFactory;
import org.bigmouth.nvwa.log.rdb.RecordDispatcher;
import org.bigmouth.nvwa.log.rdb.jmx.LogAppenderMBean;
import org.bigmouth.nvwa.log.rdb.jmx.LogMBeanExport;
import org.bigmouth.nvwa.log.rdb.jmx.MBeanRegisterRecordControllerFactory;
import org.bigmouth.nvwa.servicelogic.codec.CodecSelector;
import org.bigmouth.nvwa.servicelogic.factory.annotation.TransactionService;
import org.bigmouth.nvwa.servicelogic.handler.TransactionExecutor;
import org.bigmouth.nvwa.servicelogic.handler.TransactionHandler;
import org.bigmouth.nvwa.servicelogic.interceptor.ContentDecodeInterceptor;
import org.bigmouth.nvwa.servicelogic.interceptor.Interceptor;
import org.bigmouth.nvwa.servicelogic.interceptor.InvocationInjectInterceptor;
import org.bigmouth.nvwa.servicelogic.interceptor.RequestValidateInterceptor;
import org.bigmouth.nvwa.servicelogic.interceptor.SendbackInterceptor;
import org.bigmouth.nvwa.servicelogic.interceptor.SessionAware;
import org.bigmouth.nvwa.servicelogic.interceptor.SessionInjectInterceptor;
import org.bigmouth.nvwa.servicelogic.interceptor.TransactionInterceptor;
import org.bigmouth.nvwa.servicelogic.listener.InternelExceptionListener;
import org.bigmouth.nvwa.servicelogic.log.RecordStatusInterceptor;
import org.bigmouth.nvwa.servicelogic.statistics.ExtendedServiceStatistics;
import org.bigmouth.nvwa.servicelogic.statistics.ServiceLogicMBeanExport;
import org.bigmouth.nvwa.session.SessionHolder;
import org.bigmouth.nvwa.session.annotation.SessionSupport;
import org.bigmouth.nvwa.utils.ReflectUtils;
import org.bigmouth.nvwa.utils.ReflectUtils.MethodFilter;
import org.bigmouth.nvwa.validate.factory.AnnotationValidatorFactory;
import org.bigmouth.nvwa.validate.factory.PreGeneratedValidatorFactory;
import org.bigmouth.nvwa.validate.factory.ValidatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

//TODO:Separation of duties
public class ServiceLogicPlugInDiscover extends AbstractPlugInDiscover implements PlugInDiscover,
		ApplicationContextAware {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceLogicPlugInDiscover.class);
	private static final String THREAD_MODEL_TYPE_CACHED = "cached";
	private static final String THREAD_MODEL_TYPE_FIXED = "fixed";
	private static final String THREAD_MODEL_TYPE_SINGLE = "single";

	private ApplicationContext applicationContext;

	// TODO:simple version
	private boolean isSpringContextFile(String name) {
		return name.endsWith(".xml");
	}

	@Override
	public PlugIn discover(PlugInClassLoader classloader) {
		if (null == classloader)
			throw new NullPointerException("classloader");

		List<String> contextFiles = findPlugInContextFiles(classloader);
		if (null == contextFiles || 0 == contextFiles.size())
			throw new PlugInDiscoverException("Can not found any Spring application context file.");

		AbstractApplicationContext plugInContext = createPlugInContext(contextFiles);
		try {
			String[] handlerBeanNames = findHandlerBeanNames(plugInContext);
			if (null == handlerBeanNames || 0 == handlerBeanNames.length)
				throw new PlugInDiscoverException(
						"Can not found TransactionHandler bean definition in PlugIn Spring applicationContext file.");

			PlugInCfgMetadata plugInCfgMetadata = createPlugInCfgMetadata(handlerBeanNames,
					plugInContext);

			return createPlugIn(plugInCfgMetadata);
		} catch (RuntimeException e) {
			plugInContext.destroy();
			throw e;
		}
	}

	private String[] findHandlerBeanNames(ApplicationContext plugInContext) {
		String[] handlerBeanNames = plugInContext.getBeanNamesForType(TransactionExecutor.class);
		return handlerBeanNames;
	}

	private AbstractApplicationContext createPlugInContext(List<String> contextFiles) {
		try {
			AbstractApplicationContext plugInContext = new ClassPathXmlApplicationContext(
					contextFiles.toArray(new String[0]), applicationContext);
			return plugInContext;
		} catch (Exception e) {
			throw new IllegalSpringContextConfigException("createPlugInContext:", e);
		}
	}

	private List<String> findPlugInContextFiles(PlugInClassLoader classloader) {
		List<String> contextFiles = getSearchSupport().searchResources(classloader,
				new ResourceFilter() {

					@Override
					public boolean accept(String name) {
						return isSpringContextFile(name);
					}
				}, false);
		return contextFiles;
	}

	// TODO:
	protected CodecSelector getCodecSelector(ApplicationContext applicationContext) {
		// contentDecoderSelector
		return (CodecSelector) applicationContext.getBean("codecSelector");
	}

	private PlugInCfgMetadata createPlugInCfgMetadata(String[] handlerBeanNames,
			ApplicationContext plugInContext) {

		PlugInCfgMetadata plugInCfgMetadata = new PlugInCfgMetadata();
		for (String beanName : handlerBeanNames) {
			if (isSingleton(plugInContext, beanName)) {
				throw new PlugInDiscoverException("TransactionHandler[" + beanName
						+ "] is singleton.");
			}

			TransactionExecutor handler = getTransactionHandler(beanName, plugInContext);
			if (null == handler)
				throw new PlugInDiscoverException("Can not get handerBean for plugInContext.");
			org.bigmouth.nvwa.dpl.factory.annotation.PlugIn plugInAnno = getPlugInAnno(handler);
			if (null != plugInAnno) {
				if (null != plugInCfgMetadata.getPlugInAnno()) {
					throw new TooManyPlugInAnnotationException(
							"Can only be one @PlugIn annotation on TransactionHandler.");
				} else {
					plugInCfgMetadata.setPlugInAnno(plugInAnno, plugInContext);
				}
			}

			// services
			TransactionService serviceAnno = getServiceAnno(handler);
			if (null == serviceAnno)
				continue;
			Method handleMethod = extractHandleMethod(handler);
			if (null == handleMethod) {
				throw new PlugInDiscoverException("Can not found any handle method in " + handler);
			}

			// session support
			SessionSupport sessionSupport = handler.getClass().getAnnotation(SessionSupport.class);
			if ((handler instanceof SessionAware) && (null == sessionSupport)) {
				throw new PlugInDiscoverException(
						"Service is not support session,but implements SessionAware,service implementation class:"
								+ handler);
			}

			ServiceCfgMetadata serviceCfg = new ServiceCfgMetadata(serviceAnno, handleMethod,
					plugInContext, beanName, null != sessionSupport);
			plugInCfgMetadata.addServiceCfgMetadata(serviceCfg);
		}

		if (null == plugInCfgMetadata.getPlugInAnno())
			throw new PlugInDiscoverException("Can not found any @PlugIn annotation.");

		// ready status holder
		final StatusHolder statusHolder = getStatusHolder();
		plugInCfgMetadata.setStatusHolder(statusHolder);

		// ready codec
		final CodecSelector codecSelector = getCodecSelector(applicationContext);
		plugInCfgMetadata.setCodecSelector(codecSelector);

		// ready log record
		Object logJdbcTemplate = plugInCfgMetadata.getApplicationContext().getBean("logJdbcTemplate");
		if (null != logJdbcTemplate && (logJdbcTemplate instanceof JdbcTemplate)) {
		    JdbcTemplate sjt = (JdbcTemplate) logJdbcTemplate;
		    String plugInCode = plugInCfgMetadata.getPlugInAnno().code();
		    String logAppendThreadName = "Rdb-LogAppender-" + plugInCode + "-thread";
		    final LogAppender logAppender = new LogAppender(sjt, logAppendThreadName);
		    
		    final LogMBeanExport logMbeanExport = new LogMBeanExport();
		    logMbeanExport.registerLogAppenderMBean(new LogAppenderMBean(logAppender), plugInCode);
		    
		    plugInCfgMetadata.addListener(new PlugInListenerAdapter() {
		        
		        @Override
		        public void init() {
		            logAppender.init();
		        }
		        
		        @Override
		        public void destroy() {
		            logMbeanExport.destroy();
		            logAppender.destroy();
		        }
		    });
		    RecordControllerFactory recordControllerFactory = new MBeanRegisterRecordControllerFactory(
		            logMbeanExport, new AnnoRecordControllerFactory(logAppender), plugInCode);
		    plugInCfgMetadata.setRecordControllerFactory(recordControllerFactory);
		    String recordDispatcherThreadName = "RecordDispatcher-" + plugInCode + "-thread";
		    plugInCfgMetadata.setRecordDispatcherThreadName(recordDispatcherThreadName);
		}

		// ready session
		SessionHolder sessionHolder = getSessionHolder("sessionHolder", plugInContext);
		if (null == sessionHolder) {
			throw new PlugInDiscoverException("sessionHolder bean is null.");
		}
		plugInCfgMetadata.setSessionHolder(sessionHolder);

		// check constraint
		if (!plugInCfgMetadata.existsPlugInCfg()) {
			throw new PlugInDiscoverException(
					"Can not found any @PlugIn annotation on TransactionHandler.");
		}
		if (!plugInCfgMetadata.existsServiceCfg()) {
			throw new PlugInDiscoverException(
					"Can not found any @TransactionService annotation on TransactionHandler.");
		}

		return plugInCfgMetadata;
	}

	private SessionHolder getSessionHolder(String sessionHolderBeanName,
			ApplicationContext applicationContext) {
		return (SessionHolder) applicationContext.getBean(sessionHolderBeanName);
	}

	private boolean isTransactionHandlerMethod(Method method) {
		Class<?>[] parameterTypes = method.getParameterTypes();
		Class<?> returnType = method.getReturnType();

		return "handle".equals(method.getName()) && 2 == parameterTypes.length
				&& parameterTypes[0] != Object.class && parameterTypes[1] != Object.class
				&& Void.TYPE == returnType;
	}

	private Method extractHandleMethod(TransactionExecutor handler) {
		List<Method> methods = ReflectUtils.findMethods(handler.getClass(), new MethodFilter() {

			@Override
			public boolean accept(final Method method) {
				return isTransactionHandlerMethod(method);
			}
		});

		// TODO:if methods.size() >= 1?
		if (methods.size() > 0)
			return methods.get(0);
		return null;
	}

	private TransactionService getServiceAnno(TransactionExecutor handler) {
		TransactionService serviceAnno = handler.getClass().getAnnotation(TransactionService.class);
		return serviceAnno;
	}

	private org.bigmouth.nvwa.dpl.factory.annotation.PlugIn getPlugInAnno(
			TransactionExecutor handler) {
		org.bigmouth.nvwa.dpl.factory.annotation.PlugIn plugInAnno = handler.getClass()
				.getAnnotation(org.bigmouth.nvwa.dpl.factory.annotation.PlugIn.class);
		return plugInAnno;
	}

	private TransactionExecutor getTransactionHandler(String handlerBeanName,
			ApplicationContext applicationContext) {
		return (TransactionExecutor) applicationContext.getBean(handlerBeanName);
	}

	private MutablePlugIn createPlugIn(PlugInCfgMetadata plugInCfg) {
		plugInCfg.getPlugInAnno().code();

		String code = plugInCfg.getPlugInAnno().code();
		String name = plugInCfg.getPlugInAnno().name();
		String desc = plugInCfg.getPlugInAnno().description();
		boolean isRunning = plugInCfg.getPlugInAnno().isRunning();
		Class<? extends PlugInListener>[] listnerClasses = plugInCfg.getPlugInAnno().listeners();

		PlugInConfig plugInConfig = createPlugInConfig(code, name, desc);
		Status plugInStatus = createStatus(isRunning);

		MutablePlugIn plugIn = createPlugIn(plugInCfg.getApplicationContext(), plugInConfig,
				plugInStatus);

		// plugin listeners
		List<PlugInListener> plugInListeners = createPlugInListeners(plugInCfg.getListeners(),
				plugIn, plugInCfg.getStatusHolder(), listnerClasses);
		plugIn.addEventListeners(plugInListeners);

		// record dispatcher
		RecordControllerFactory recordControllerFactory = plugInCfg.getRecordControllerFactory();
		String recordDispatcherThreadName = plugInCfg.getRecordDispatcherThreadName();
		RecordControllerFactory plugInManageRecordControllerFactory = new PlugInManageRecordControllerFactory(
				plugIn, recordControllerFactory);

		final RecordDispatcher recordDispatcher = new RecordDispatcher(
				plugInManageRecordControllerFactory, recordDispatcherThreadName);
		plugIn.addEventListener(new PlugInListenerAdapter() {

			@Override
			public void init() {
				recordDispatcher.init();
			}

			@Override
			public void destroy() {
				recordDispatcher.destroy();
			}
		});

		// validator factory
		ValidatorFactory validatorFactory = new PreGeneratedValidatorFactory(
				new AnnotationValidatorFactory(), plugInCfg.getServiceRequestClasses());

		// Dpl MBean Server
		final ServiceLogicMBeanExport mbeanExport = new ServiceLogicMBeanExport();
		plugIn.addEventListener(new PlugInListenerAdapter() {

			@Override
			public void destroy() {
				mbeanExport.destroy();
			}
		});

		for (ServiceCfgMetadata serviceCfg : plugInCfg.getServiceConfigMetadatas()) {
			TransactionService serviceAnno = serviceCfg.getServiceAnno();

			String serviceCode = serviceAnno.code();
			String serviceName = serviceAnno.name();
			String serviceDesc = serviceAnno.description();
			boolean serviceIsRunning = serviceAnno.isRunning();
			ThreadModel threadModel = serviceAnno.threadModel();

			Class<? extends ServiceListener>[] serviceListnerClasses = serviceAnno.listeners();

			ServiceConfig serviceConfig = createServiceConfig(serviceCode, serviceName, serviceDesc);
			Status serviceStatus = createStatus(serviceIsRunning);
			List<ServiceListener> serviceListeners = createServiceListeners(plugIn,
					plugInCfg.getStatusHolder(), serviceListnerClasses);

			Method handleMethod = serviceCfg.getMethod();
			if (null == handleMethod)
				throw new RuntimeException("Can not found any handle method.");

			// get request and response class
			Class<?>[] paramsClass = handleMethod.getParameterTypes();
			final Class<?> requestClass = paramsClass[0];
			final Class<?> responseClass = paramsClass[1];

			// create
			// interceptor(ContentDecodeInterceptor,TransactionInterceptor,SendbackInterceptor)
			Interceptor interceptorChain = createInterceptorChain(plugInCfg, serviceCfg,
					requestClass, responseClass, recordDispatcher, validatorFactory);

			// create service
			Service service = createService(threadModel, serviceConfig, serviceStatus,
					serviceListeners, interceptorChain, plugInConfig);

			//
			final ExtendedServiceStatistics ess = new ExtendedServiceStatistics(plugIn, service);
			ess.init();
			((MutableService) service).setAttachment(ess);
			((MutableService) service).addEventListener(new ServiceListenerAdapter() {

				@Override
				public void destroy() {
					ess.destroy();
				}

				@Override
				public void onExecuteFailed(ExecutionFailedEvent event) {
					// ignore logging exception.
				}
			});

			// export service mbean
			mbeanExport.registerServiceMBean(plugIn, service);

			// plugIn.addEventListener(listener)
			plugIn.addService(service);
		}

		// export plugin mbean
		mbeanExport.registerPlugInMBean(plugIn);

		return plugIn;
	}

	protected Interceptor createInterceptorChain(final PlugInCfgMetadata plugInCfg,
			final ServiceCfgMetadata serviceCfg, final Class<?> requestClass,
			final Class<?> responseClass, RecordDispatcher recordDispatcher,
			ValidatorFactory validatorFactory) {

		Interceptor recordStatusInterceptor = new RecordStatusInterceptor(new MultiRecordSupport(
				recordDispatcher));
		Interceptor sendbackInterceptor = new SendbackInterceptor(recordStatusInterceptor,
				plugInCfg.getCodecSelector());

		List<Interceptor> transactionChildInterceptors = Lists.newArrayList();
		transactionChildInterceptors.add(new InvocationInjectInterceptor());
		if (serviceCfg.isSessionSupport()) {
			transactionChildInterceptors.add(new SessionInjectInterceptor(plugInCfg
					.getSessionHolder()));
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		Interceptor transactionInterceptor = new TransactionInterceptor(sendbackInterceptor,
				transactionChildInterceptors) {

			@Override
			public TransactionHandler createTransactionHandler() {
				return (TransactionHandler) serviceCfg.createTransactionHandler();
			}
		};

		Interceptor paramsValidateInterceptor = new RequestValidateInterceptor(
				transactionInterceptor, validatorFactory);

		Interceptor contentDecodeInterceptor = new ContentDecodeInterceptor(
				paramsValidateInterceptor, plugInCfg.getCodecSelector()) {

			@Override
			public Object createResponseModel() {
				try {
					return responseClass.newInstance();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					throw new RuntimeException("createResponseModel:");
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					throw new RuntimeException("createResponseModel:");
				}
			}

			@Override
			public Class<?> getRequestTemplate() {
				return requestClass;
			}
		};
		return contentDecodeInterceptor;
	}

	private ExecutorService getServiceExecutorService(ThreadModel threadModel,
			ServiceConfig serviceConfig, PlugInConfig plugInConfig) {
		return createExecutorService(threadModel, serviceConfig, plugInConfig);
	}

	private ExecutorService createExecutorService(ThreadModel threadModel,
			ServiceConfig serviceConfig, PlugInConfig plugInConfig) {
		final String type = threadModel.type();
		String threadNamePrefix = null;

		if (StringUtils.isBlank(threadModel.threadNameFlag())) {
			threadNamePrefix = plugInConfig.getKey() + "-" + serviceConfig.getKey() + "-thread-";
		} else {
			threadNamePrefix = threadModel.threadNameFlag();
		}

		final int threadCount = threadModel.threadCount();
		if (threadCount <= 0)
			throw new RuntimeException("threadCount:" + threadCount);
		final boolean isDaemon = threadModel.isDaemon();
		final int priority = threadModel.priority();

		if (null == type)
			return null;

		ThreadFactory threadFactory = new CustomThreadFactory(threadNamePrefix, isDaemon, priority);

		if (THREAD_MODEL_TYPE_CACHED.equals(type)) {
			return Executors.newCachedThreadPool(threadFactory);
		} else if (THREAD_MODEL_TYPE_FIXED.equals(type)) {
			return Executors.newFixedThreadPool(threadCount, threadFactory);
		} else if (THREAD_MODEL_TYPE_SINGLE.equals(type)) {
			return Executors.newSingleThreadExecutor(threadFactory);
		} else {
			throw new RuntimeException("Unkown ThreadModel.type:" + type);
		}
	}

	private Service createService(ThreadModel threadModel, ServiceConfig serviceConfig,
			Status serviceStatus, List<ServiceListener> serviceListeners,
			Interceptor interceptorChain, PlugInConfig plugInConfig) {

		ExecutorService executorService = getServiceExecutorService(threadModel, serviceConfig,
				plugInConfig);

		ServiceClosure serviceClosure = new MethodServiceClosure(interceptorChain, "intercept");
		Service service = new DefaultProceduralService(serviceConfig, serviceStatus,
				serviceListeners, executorService, serviceClosure);
		return service;
	}

	protected List<ServiceListener> createServiceListeners(PlugIn plugIn,
			final StatusHolder statusHolder,
			Class<? extends ServiceListener>[] serviceListnerClasses) {
		List<ServiceListener> serviceListeners = Lists.newArrayList();

		serviceListeners.addAll(createDefaultServiceListeners(plugIn));

		for (Class<? extends ServiceListener> slClass : serviceListnerClasses) {
			ServiceListener sl = null;
			// if status listener
			if (isServiceStatusListener(slClass)) {
				if (null == statusHolder) {
					// no statusHolder
					if (LOGGER.isDebugEnabled())
						LOGGER.debug("StatusHolder is null,ignore ServiceStatusListener.");
					break;
				}
				sl = createServiceStatusListener(statusHolder, slClass);
			} else {
				sl = createServiceListener(plugIn, slClass);
			}
			serviceListeners.add(sl);
		}
		return serviceListeners;
	}

	protected List<ServiceListener> createDefaultServiceListeners(PlugIn plugIn) {
		List<ServiceListener> serviceListeners = Lists.newArrayList();

		// for replier session count recycle when service execute occur
		// exception.
		ServiceListener internelExceptionListener = createServiceListener(plugIn,
				InternelExceptionListener.class);
		serviceListeners.add(internelExceptionListener);

		// for runtime object
		ServiceListener runtimeObjectServiceListener = createServiceListener(plugIn,
				RuntimeObjectServiceListener.class);
		serviceListeners.add(runtimeObjectServiceListener);

		return serviceListeners;
	}

	private ServiceListener createServiceListener(PlugIn plugIn,
			Class<? extends ServiceListener> slClass) {
		ServiceListener sl = null;
		try {
			sl = slClass.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException("createServiceListener:", e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("createServiceListener:", e);
		}

		return wrapReviseContextClassLoaderServiceListener(plugIn, sl);
	}

	private ServiceListener wrapReviseContextClassLoaderServiceListener(PlugIn plugIn,
			ServiceListener sl) {
		return new ReviseContextClassLoaderServiceListener(plugIn, sl);
	}

	private ServiceListener createServiceStatusListener(final StatusHolder statusHolder,
			Class<? extends ServiceListener> slClass) {
		try {
			Constructor<? extends ServiceListener> cons = slClass
					.getConstructor(new Class<?>[] { StatusHolder.class });
			return cons.newInstance(statusHolder);
		} catch (SecurityException e) {
			throw new RuntimeException("createAnnotationPlugIn:", e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException("createAnnotationPlugIn:", e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("createAnnotationPlugIn:", e);
		} catch (InstantiationException e) {
			throw new RuntimeException("createAnnotationPlugIn:", e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("createAnnotationPlugIn:", e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException("createAnnotationPlugIn:", e);
		}
	}

	private boolean isServiceStatusListener(Class<? extends ServiceListener> slClass) {
		return ServiceStatusListener.class.isAssignableFrom(slClass);
	}

	private ServiceConfig createServiceConfig(String serviceCode, String serviceName,
			String serviceDesc) {
		ServiceConfig serviceConfig = new ImmutableServiceConfig(serviceName, serviceCode,
				serviceDesc);
		return serviceConfig;
	}

	private MutablePlugIn createPlugIn(ApplicationContext applicationContext,
			PlugInConfig plugInConfig, Status plugInStatus) {
		SpringSytlePlugIn plugIn = new SpringSytlePlugIn(plugInConfig, plugInStatus);
		plugIn.setApplicationContext(applicationContext);
		return plugIn;
	}

	protected List<PlugInListener> createPlugInListeners(List<PlugInListener> listeners,
			PlugIn plugIn, final StatusHolder statusHolder,
			Class<? extends PlugInListener>[] listnerClasses) {
		List<PlugInListener> ret = Lists.newArrayList();
		for (PlugInListener listener : listeners) {
			ret.add(wrapReviseContextClassLoaderPlugInListener(plugIn, listener));
		}
		for (Class<? extends PlugInListener> plClass : listnerClasses) {
			PlugInListener pl = null;
			// if status listener
			if (isPlugInStatusListener(plClass)) {
				if (null == statusHolder) {
					// no statusHolder
					if (LOGGER.isDebugEnabled())
						LOGGER.debug("StatusHolder is null,ignore PlugInStatusListener.");
					break;
				}
				pl = createPlugInStatusListener(statusHolder, plClass);
			} else {
				pl = createPlugInListener(plugIn, plClass);
			}
			ret.add(pl);
		}
		return ret;
	}

	private PlugInListener createPlugInListener(PlugIn plugIn,
			Class<? extends PlugInListener> plClass) {
		PlugInListener pl = null;
		try {
			pl = plClass.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException("createAnnotationPlugIn:", e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("createAnnotationPlugIn:", e);
		}

		return wrapReviseContextClassLoaderPlugInListener(plugIn, pl);
	}

	private PlugInListener wrapReviseContextClassLoaderPlugInListener(PlugIn plugIn,
			PlugInListener pl) {
		return new ReviseContextClassLoaderPlugInListener(plugIn, pl);
	}

	private PlugInListener createPlugInStatusListener(final StatusHolder statusHolder,
			Class<? extends PlugInListener> plClass) {
		try {
			Constructor<? extends PlugInListener> cons = plClass
					.getConstructor(new Class<?>[] { StatusHolder.class });
			return cons.newInstance(statusHolder);
		} catch (SecurityException e) {
			throw new RuntimeException("createAnnotationPlugIn:", e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException("createAnnotationPlugIn:", e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("createAnnotationPlugIn:", e);
		} catch (InstantiationException e) {
			throw new RuntimeException("createAnnotationPlugIn:", e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("createAnnotationPlugIn:", e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException("createAnnotationPlugIn:", e);
		}
	}

	private boolean isPlugInStatusListener(Class<? extends PlugInListener> plClass) {
		return PlugInStatusListener.class.isAssignableFrom(plClass);
	}

	private Status createStatus(boolean serviceIsRunning) {
		Status serviceStatus = new ImmutableStatus(serviceIsRunning);
		return serviceStatus;
	}

	private ImmutablePlugInConfig createPlugInConfig(String code, String name, String desc) {
		return new ImmutablePlugInConfig(name, code, desc);
	}

	private StatusHolder getStatusHolder() {
		return getStatusHolderFactory().create();
	}

	protected StatusHolderFactory getStatusHolderFactory() {
		return new StatusHolderSingleton();
	}

	private boolean isSingleton(ApplicationContext context, String beanName) {
		return context.getBean(beanName) == context.getBean(beanName);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	// TODO:extract this class later.
	private static class CustomThreadFactory implements ThreadFactory {

		private final AtomicInteger threadIdx = new AtomicInteger(1);
		private final String threadNamePrefix;
		private final boolean isDaemon;
		private int priority;

		public CustomThreadFactory(String threadNamePrefix, boolean isDaemon, int priority) {
			this.threadNamePrefix = threadNamePrefix;
			this.isDaemon = isDaemon;
			this.priority = priority;
		}

		public Thread newThread(Runnable r) {
			Thread t = new Thread(r, threadNamePrefix + this.threadIdx.getAndIncrement());
			t.setDaemon(isDaemon);
			t.setPriority(priority);

			return t;
		}
	}

	private static final class PlugInCfgMetadata {
		private org.bigmouth.nvwa.dpl.factory.annotation.PlugIn plugInAnno;
		private ApplicationContext context;
		private StatusHolder statusHolder;
		private CodecSelector codecSelector;
		private SessionHolder sessionHolder;

		private RecordControllerFactory recordControllerFactory;
		private String recordDispatcherThreadName;

		private final List<PlugInListener> listeners = Lists.newArrayList();

		private final Map<ServiceCfgMetadata, Object> serviceCfgMetadatas = Maps.newHashMap();

		public void addListener(PlugInListener listener) {
			if (null == listener)
				throw new NullPointerException("listener");
			listeners.add(listener);
		}

		public List<PlugInListener> getListeners() {
			return listeners;
		}

		public org.bigmouth.nvwa.dpl.factory.annotation.PlugIn getPlugInAnno() {
			return plugInAnno;
		}

		public void setPlugInAnno(
				org.bigmouth.nvwa.dpl.factory.annotation.PlugIn plugInAnno,
				ApplicationContext context) {
			if (null == plugInAnno)
				throw new NullPointerException("plugInAnno");
			if (StringUtils.isBlank(plugInAnno.code()))
				throw new IllegalArgumentException("PlugIn Code is blank.");
			if (null == context)
				throw new NullPointerException("context");

			this.plugInAnno = plugInAnno;
			this.context = context;
		}

		public String getRecordDispatcherThreadName() {
			return recordDispatcherThreadName;
		}

		public void setRecordDispatcherThreadName(String recordDispatcherThreadName) {
			if (StringUtils.isBlank(recordDispatcherThreadName))
				throw new IllegalArgumentException("recordDispatcherThreadName is blank.");
			this.recordDispatcherThreadName = recordDispatcherThreadName;
		}

		public void setStatusHolder(StatusHolder statusHolder) {
			if (null == statusHolder)
				throw new NullPointerException("statusHolder");
			this.statusHolder = statusHolder;
		}

		public void setCodecSelector(CodecSelector codecSelector) {
			if (null == codecSelector)
				throw new NullPointerException("codecSelector");
			this.codecSelector = codecSelector;
		}

		public RecordControllerFactory getRecordControllerFactory() {
			return recordControllerFactory;
		}

		public void setRecordControllerFactory(RecordControllerFactory recordControllerFactory) {
			if (null == recordControllerFactory)
				throw new NullPointerException("recordControllerFactory");
			this.recordControllerFactory = recordControllerFactory;
		}

		public ApplicationContext getApplicationContext() {
			return context;
		}

		public void addServiceCfgMetadata(ServiceCfgMetadata serviceCfgMetadata) {
			if (serviceCfgMetadatas.containsKey(serviceCfgMetadata))
				throw new RuntimeException("Service code define repeated,code["
						+ serviceCfgMetadata.getServiceAnno().code() + "].");
			serviceCfgMetadatas.put(serviceCfgMetadata, new Object());
		}

		public List<ServiceCfgMetadata> getServiceConfigMetadatas() {
			return new ArrayList<ServiceCfgMetadata>(serviceCfgMetadatas.keySet());
		}

		public boolean existsServiceCfg() {
			return serviceCfgMetadatas.size() > 0;
		}

		public boolean existsPlugInCfg() {
			return null != plugInAnno;
		}

		public StatusHolder getStatusHolder() {
			return statusHolder;
		}

		public CodecSelector getCodecSelector() {
			return codecSelector;
		}

		public List<Class<?>> getServiceRequestClasses() {
			List<Class<?>> ret = Lists.newArrayList();
			for (Entry<ServiceCfgMetadata, Object> e : serviceCfgMetadatas.entrySet()) {
				ServiceCfgMetadata scm = e.getKey();
				ret.add(scm.getMethod().getParameterTypes()[0]);
			}
			return ret;
		}

		protected SessionHolder getSessionHolder() {
			return sessionHolder;
		}

		protected void setSessionHolder(SessionHolder sessionHolder) {
			this.sessionHolder = sessionHolder;
		}
	}

	private static final class ServiceCfgMetadata {
		private final TransactionService serviceAnno;
		private final Method method;
		private final ApplicationContext plugInContext;
		private final String handlerBeanName;
		private final boolean isSessionSupport;

		ServiceCfgMetadata(TransactionService serviceAnno, Method method,
				ApplicationContext plugInContext, String handlerBeanName, boolean isSessionSupport) {
			if (null == serviceAnno)
				throw new NullPointerException("serviceAnno");
			if (StringUtils.isBlank(serviceAnno.code()))
				throw new RuntimeException("Service Code is blank.");
			if (null == method)
				throw new NullPointerException("deployConfig");
			if (null == plugInContext)
				throw new NullPointerException("plugInContext");
			if (StringUtils.isBlank(handlerBeanName))
				throw new IllegalArgumentException("handlerBeanName is blank.");
			this.serviceAnno = serviceAnno;
			this.method = method;
			this.plugInContext = plugInContext;
			this.handlerBeanName = handlerBeanName;
			this.isSessionSupport = isSessionSupport;
		}

		public boolean isSessionSupport() {
			return isSessionSupport;
		}

		public TransactionExecutor createTransactionHandler() {
			return (TransactionExecutor) this.plugInContext.getBean(handlerBeanName);
		}

		public TransactionService getServiceAnno() {
			return serviceAnno;
		}

		public Method getMethod() {
			return method;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((serviceAnno.code() == null) ? 0 : serviceAnno.code().hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ServiceCfgMetadata other = (ServiceCfgMetadata) obj;
			if (serviceAnno.code() == null) {
				if (other.serviceAnno.code() != null)
					return false;
			} else if (!serviceAnno.code().equals(other.serviceAnno.code()))
				return false;
			return true;
		}
	}
}
