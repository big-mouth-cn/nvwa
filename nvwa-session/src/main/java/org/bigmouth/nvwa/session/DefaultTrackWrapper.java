package org.bigmouth.nvwa.session;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

public class DefaultTrackWrapper implements Trackable, Serializable {

    private static final long serialVersionUID = 6945914698624056262L;
    private volatile String trackId;

    public DefaultTrackWrapper() {
    }

    public DefaultTrackWrapper(String trackId) {
        this.trackId = trackId;
    }

    public DefaultTrackWrapper(Object object, String fieldName) {
		super();
		if (null == object)
			throw new NullPointerException("object");
		if (StringUtils.isBlank(fieldName))
			throw new IllegalArgumentException("fieldName is blank.");
		try {
			trackId = BeanUtils.getProperty(object, fieldName);
		} catch (IllegalAccessException e) {
			trackId = null;
		} catch (InvocationTargetException e) {
			trackId = null;
		} catch (NoSuchMethodException e) {
			trackId = null;
		}
	}

	@Override
	public String getTrackId() {
		return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }
}
