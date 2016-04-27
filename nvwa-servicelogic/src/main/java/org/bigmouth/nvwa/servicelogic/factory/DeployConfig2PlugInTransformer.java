package org.bigmouth.nvwa.servicelogic.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang.StringUtils;
import org.bigmouth.nvwa.dpl.event.listener.PlugInListener;
import org.bigmouth.nvwa.dpl.event.listener.ServiceListener;
import org.bigmouth.nvwa.dpl.factory.annotation.ThreadModel;
import org.bigmouth.nvwa.dpl.plugin.ImmutablePlugInConfig;
import org.bigmouth.nvwa.dpl.plugin.MutablePlugIn;
import org.bigmouth.nvwa.dpl.plugin.PlugIn;
import org.bigmouth.nvwa.dpl.plugin.PlugInConfig;
import org.bigmouth.nvwa.dpl.plugin.SpringSytlePlugIn;
import org.bigmouth.nvwa.dpl.service.DefaultProceduralService;
import org.bigmouth.nvwa.dpl.service.ImmutableServiceConfig;
import org.bigmouth.nvwa.dpl.service.MethodServiceClosure;
import org.bigmouth.nvwa.dpl.service.Service;
import org.bigmouth.nvwa.dpl.service.ServiceClosure;
import org.bigmouth.nvwa.dpl.service.ServiceConfig;
import org.bigmouth.nvwa.dpl.status.ImmutableStatus;
import org.bigmouth.nvwa.dpl.status.PlugInStatusListener;
import org.bigmouth.nvwa.dpl.status.ServiceStatusListener;
import org.bigmouth.nvwa.dpl.status.Status;
import org.bigmouth.nvwa.dpl.status.StatusHolder;
import org.bigmouth.nvwa.servicelogic.codec.ContentDecoder;
import org.bigmouth.nvwa.servicelogic.codec.ContentEncoder;
import org.bigmouth.nvwa.servicelogic.factory.annotation.TransactionService;
import org.bigmouth.nvwa.servicelogic.handler.TransactionExecutor;
import org.bigmouth.nvwa.servicelogic.interceptor.ContentDecodeInterceptor;
import org.bigmouth.nvwa.servicelogic.interceptor.Interceptor;
import org.bigmouth.nvwa.utils.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class DeployConfig2PlugInTransformer implements
		Transformer<List<ServiceDeployConfig>, PlugIn> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(DeployConfig2PlugInTransformer.class);

	private static final String THREAD_MODEL_TYPE_CACHED = "cached";
	private static final String THREAD_MODEL_TYPE_FIXED = "fixed";
	private static final String THREAD_MODEL_TYPE_SINGLE = "single";

	@Override
	public PlugIn transform(List<ServiceDeployConfig> configs) {
		if (null == configs || 0 == configs.size())
			throw new IllegalArgumentException("configs is blank.");

		PlugInCfgMetadata plugInCfg = parseDeployConfigs(configs);

		if (!plugInCfg.existsPlugInCfg()) {
			throw new RuntimeException("Can not found @PlugIn annotation on TransactionHandler.");
		}
		if (!plugInCfg.existsServiceCfg()) {
			throw new RuntimeException(
					"Can not found any @TransactionService annotation on TransactionHandler.");
		}

		return createPlugIn(plugInCfg);
	}

	@SuppressWarnings("unchecked")
	private MutablePlugIn createPlugIn(PlugInCfgMetadata plugInCfg) {
		plugInCfg.getPlugInAnno().code();

		String code = plugInCfg.getPlugInAnno().code();
		String name = plugInCfg.getPlugInAnno().name();
		String desc = plugInCfg.getPlugInAnno().description();
		boolean isRunning = plugInCfg.getPlugInAnno().isRunning();
		Class<? extends PlugInListener>[] listnerClasses = plugInCfg.getPlugInAnno().listeners();

		PlugInConfig plugInConfig = createPlugInConfig(code, name, desc);
		Status plugInStatus = createStatus(isRunning);
		List plugInListeners = createPlugInListeners(plugInCfg.getDeployConfig(), listnerClasses);

		MutablePlugIn plugIn = createPlugIn(plugInCfg.getApplicationContext(), plugInConfig,
				plugInStatus, plugInListeners);

		// List<Service> services = Lists.newArrayList();

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
			List serviceListeners = createServiceListeners(serviceCfg.getDeployConfig(),
					serviceListnerClasses);

			Method handleMethod = serviceCfg.getMethod();
			if (null == handleMethod)
				throw new RuntimeException("Can not found any handle method.");

			// get request and response class
			Class<?>[] paramsClass = handleMethod.getParameterTypes();
			final Class<?> requestClass = paramsClass[0];
			final Class<?> responseClass = paramsClass[1];

			// create
			// interceptor(ContentDecodeInterceptor,TransactionInterceptor,SendbackInterceptor)
			Interceptor interceptorChain = createInterceptorChain(serviceCfg.getDeployConfig(),
					serviceCfg.getDeployConfig().getContentEncoder(), serviceCfg.getDeployConfig()
							.getContentDecoder(), requestClass, responseClass);

			// create service
			Service service = createService(threadModel, serviceCfg.getDeployConfig(),
					serviceConfig, serviceStatus, serviceListeners, interceptorChain, plugInConfig);

			// plugIn.addEventListener(listener)
			plugIn.addService(service);
		}
		return plugIn;
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

	private ExecutorService getServiceExecutorService(ThreadModel threadModel,
			ServiceDeployConfig config, ServiceConfig serviceConfig, PlugInConfig plugInConfig) {
		ExecutorService ret = config.getServiceExecutorService();
		if (null != ret)
			return ret;
		return createExecutorService(threadModel, serviceConfig, plugInConfig);
	}

	@SuppressWarnings("unchecked")
	private Service createService(ThreadModel threadModel, ServiceDeployConfig config,
			ServiceConfig serviceConfig, Status serviceStatus, List serviceListeners,
			Interceptor interceptorChain, PlugInConfig plugInConfig) {

		ExecutorService executorService = getServiceExecutorService(threadModel, config,
				serviceConfig, plugInConfig);

		ServiceClosure serviceClosure = new MethodServiceClosure(interceptorChain, "intercept");
		Service service = new DefaultProceduralService(serviceConfig, serviceStatus,
				serviceListeners, executorService, serviceClosure);
		return service;
	}

	@SuppressWarnings("unchecked")
	protected Interceptor createInterceptorChain(final ServiceDeployConfig config,
			ContentEncoder contentEncoder, ContentDecoder contentDecoder,
			final Class<?> requestClass, final Class<?> responseClass) {
		Interceptor sendbackInterceptor = null;

		//TODO:先暂时HOLD
//		Interceptor sendbackInterceptor = new SendbackInterceptor(contentEncoder);

		//TODO:暂时不可用，没有注入sessionHolder
		Interceptor transactionInterceptor = null;
//		Interceptor transactionInterceptor = new TransactionInterceptor(sendbackInterceptor) {
//
//			@Override
//			public TransactionHandler createTransactionHandler() {
//				return (TransactionHandler) config.getTransactionHandler();
//			}
//		};

		// TODO:
		Interceptor contentDecodeInterceptor = new ContentDecodeInterceptor(transactionInterceptor,
				null) {

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

	private boolean isServiceStatusListener(Class<? extends ServiceListener> slClass) {
		return ServiceStatusListener.class.isAssignableFrom(slClass);
	}

	@SuppressWarnings("unchecked")
	protected List createServiceListeners(final ServiceDeployConfig config,
			Class<? extends ServiceListener>[] serviceListnerClasses) {
		List serviceListeners = Lists.newArrayList();
		for (Class<? extends ServiceListener> slClass : serviceListnerClasses) {
			Object sl = null;
			// if status listener
			if (isServiceStatusListener(slClass)) {
				if (null == config.getStatusHolder()) {
					// no statusHolder
					if (LOGGER.isDebugEnabled())
						LOGGER.debug("StatusHolder is null,ignore ServiceStatusListener.");
					break;
				}
				sl = createServiceStatusListener(config, slClass);
			} else {
				sl = createServiceListener(slClass);
			}
			serviceListeners.add(sl);
		}
		return serviceListeners;
	}

	private Object createServiceListener(Class<? extends ServiceListener> slClass) {
		Object sl = null;
		try {
			sl = slClass.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException("createAnnotationPlugIn:", e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("createAnnotationPlugIn:", e);
		}
		return sl;
	}

	private Object createServiceStatusListener(final ServiceDeployConfig config,
			Class<? extends ServiceListener> slClass) {
		Object pl = null;
		try {
			Constructor<? extends ServiceListener> cons = slClass
					.getConstructor(new Class<?>[] { StatusHolder.class });
			pl = cons.newInstance(config.getStatusHolder());
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
		return pl;
	}

	private ServiceConfig createServiceConfig(String serviceCode, String serviceName,
			String serviceDesc) {
		ServiceConfig serviceConfig = new ImmutableServiceConfig(serviceName, serviceCode,
				serviceDesc);
		return serviceConfig;
	}

	@SuppressWarnings("unchecked")
	private MutablePlugIn createPlugIn(ApplicationContext applicationContext,
			PlugInConfig plugInConfig, Status plugInStatus, List plugInListeners) {
		SpringSytlePlugIn plugIn = new SpringSytlePlugIn(plugInConfig, plugInStatus,
				plugInListeners);
		plugIn.setApplicationContext(applicationContext);
		return plugIn;
	}

	@SuppressWarnings("unchecked")
	protected List createPlugInListeners(final ServiceDeployConfig config,
			Class<? extends PlugInListener>[] listnerClasses) {
		List plugInListeners = Lists.newArrayList();
		for (Class<? extends PlugInListener> plClass : listnerClasses) {
			Object pl = null;
			// if status listener
			if (isPlugInStatusListener(plClass)) {
				if (null == config.getStatusHolder()) {
					// no statusHolder
					if (LOGGER.isDebugEnabled())
						LOGGER.debug("StatusHolder is null,ignore PlugInStatusListener.");
					break;
				}
				pl = createPlugInStatusListener(config, plClass, pl);

			} else {
				pl = createPlugInListener(plClass, pl);
			}
			plugInListeners.add(pl);
		}
		return plugInListeners;
	}

	private boolean isPlugInStatusListener(Class<? extends PlugInListener> plClass) {
		return PlugInStatusListener.class.isAssignableFrom(plClass);
	}

	private Object createPlugInStatusListener(final ServiceDeployConfig config,
			Class<? extends PlugInListener> plClass, Object pl) {
		try {
			Constructor<? extends PlugInListener> cons = plClass
					.getConstructor(new Class<?>[] { StatusHolder.class });
			pl = cons.newInstance(config.getStatusHolder());
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
		return pl;
	}

	private Object createPlugInListener(Class<? extends PlugInListener> plClass, Object pl) {
		try {
			pl = plClass.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException("createAnnotationPlugIn:", e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("createAnnotationPlugIn:", e);
		}
		return pl;
	}

	private ImmutablePlugInConfig createPlugInConfig(String code, String name, String desc) {
		return new ImmutablePlugInConfig(name, code, desc);
	}

	private Status createStatus(boolean serviceIsRunning) {
		Status serviceStatus = new ImmutableStatus(serviceIsRunning);
		return serviceStatus;
	}

	private PlugInCfgMetadata parseDeployConfigs(List<ServiceDeployConfig> configs) {
		PlugInCfgMetadata plugInCfg = new PlugInCfgMetadata();
		for (final ServiceDeployConfig config : configs) {
			TransactionExecutor handler = config.getTransactionHandler();
			org.bigmouth.nvwa.dpl.factory.annotation.PlugIn plugInAnno = getPlugInAnno(handler);
			if (null != plugInAnno) {
				if (null != plugInCfg.getPlugInAnno()) {
					throw new TooManyPlugInAnnotationException(
							"Can only be one @PlugIn annotation on TransactionHandler.");
				} else {
					plugInCfg.setPlugInAnno(plugInAnno, config);
				}
			}

			// services
			TransactionService serviceAnno = getServiceAnno(handler);
			if (null == serviceAnno)
				continue;
			Method handleMethod = extractHandleMethod(handler);
			if (null == handleMethod) {
				throw new RuntimeException("Can not found any handle method in "
						+ config.getTransactionHandler());
			}

			ServiceCfgMetadata serviceCfg = new ServiceCfgMetadata(serviceAnno, handleMethod,
					config);
			plugInCfg.addServiceCfgMetadata(serviceCfg);
		}
		return plugInCfg;
	}

	private Method extractHandleMethod(TransactionExecutor handler) {
		Method[] methods = handler.getClass().getMethods();
		for (Method method : methods) {
			int paramsCount = method.getParameterTypes().length;
			Class<?> returnType = method.getReturnType();
			if (isTransactionHandlerMethod(method, paramsCount, returnType)) {
				return method;
			}
		}
		return null;
	}

	private boolean isTransactionHandlerMethod(Method method, int paramsCount, Class<?> returnType) {
		return "handle".equals(method.getName()) && 2 == paramsCount && Void.TYPE == returnType;
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

	private static final class PlugInCfgMetadata {
		private org.bigmouth.nvwa.dpl.factory.annotation.PlugIn plugInAnno;
		private ServiceDeployConfig deployConfig;
		private ApplicationContext context;

		private final Map<ServiceCfgMetadata, Object> serviceCfgMetadatas = Maps.newHashMap();

		public org.bigmouth.nvwa.dpl.factory.annotation.PlugIn getPlugInAnno() {
			return plugInAnno;
		}

		public void setPlugInAnno(
				org.bigmouth.nvwa.dpl.factory.annotation.PlugIn plugInAnno,
				ServiceDeployConfig deployConfig) {
			if (null == plugInAnno)
				throw new NullPointerException("plugInAnno");
			if (StringUtils.isBlank(plugInAnno.code()))
				throw new IllegalArgumentException("PlugIn Code is blank.");
			if (null == deployConfig)
				throw new NullPointerException("deployConfig");
			this.plugInAnno = plugInAnno;
			this.deployConfig = deployConfig;
			this.context = deployConfig.getApplicationContext();
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

		public ServiceDeployConfig getDeployConfig() {
			return deployConfig;
		}

		public boolean existsServiceCfg() {
			return serviceCfgMetadatas.size() > 0;
		}

		public boolean existsPlugInCfg() {
			return null != plugInAnno;
		}
	}

	private static final class ServiceCfgMetadata {
		private final TransactionService serviceAnno;
		private final Method method;
		private final ServiceDeployConfig deployConfig;

		ServiceCfgMetadata(TransactionService serviceAnno, Method method,
				ServiceDeployConfig deployConfig) {
			if (null == serviceAnno)
				throw new NullPointerException("serviceAnno");
			if (StringUtils.isBlank(serviceAnno.code()))
				throw new RuntimeException("Service Code is blank.");
			if (null == method)
				throw new NullPointerException("method");
			if (null == deployConfig)
				throw new NullPointerException("deployConfig");
			this.serviceAnno = serviceAnno;
			this.method = method;
			this.deployConfig = deployConfig;
		}

		public ServiceDeployConfig getDeployConfig() {
			return deployConfig;
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
