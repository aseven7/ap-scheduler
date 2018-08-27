package com.tanuputra.apsch.logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

// TODO: Auto-generated Javadoc
/**
 * The Class ApListAppender.
 */
@Plugin(name = "ApListAppender", category = "Core", elementType = "appender", printObject = true)
public class ApListAppender extends AbstractAppender {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4967423614250129771L;
	
	/** The timestamp. */
	public static long timestamp = 0;
	
	/** The events. */
	public static List<LogEvent> events = new ArrayList<LogEvent>();

	/** The rw lock. */
	private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
	
	/** The read lock. */
	private final Lock readLock = rwLock.readLock();

	/**
	 * Instantiates a new ap list appender.
	 *
	 * @param name the name
	 * @param filter the filter
	 * @param layout the layout
	 * @param ignoreExceptions the ignore exceptions
	 */
	protected ApListAppender(String name, Filter filter, Layout<? extends Serializable> layout, final boolean ignoreExceptions) {
		super(name, filter, layout, ignoreExceptions);
	}

	/* (non-Javadoc)
	 * @see org.apache.logging.log4j.core.Appender#append(org.apache.logging.log4j.core.LogEvent)
	 */
	@Override
	public void append(LogEvent event) {
		readLock.lock();
		try {
			events.add(event);
			timestamp = event.getTimeMillis();
		} catch (Exception ex) {
			if (!ignoreExceptions()) {
				throw new AppenderLoggingException(ex);
			}
		} finally {
			readLock.unlock();
		}

	}

	/**
	 * Creates the appender.
	 *
	 * @param name the name
	 * @param layout the layout
	 * @param filter the filter
	 * @return the ap list appender
	 */
	@PluginFactory
	public static ApListAppender createAppender(@PluginAttribute("name") String name,
			@PluginElement("Layout") Layout<? extends Serializable> layout, @PluginElement("Filter") Filter filter) {
		if (name == null) {
			LOGGER.error("No name provided for ApHttpClientLogAppender");
			return null;
		}
		if (layout == null)
			layout = PatternLayout.createDefaultLayout();

		return new ApListAppender(name, filter, layout, true);
	}
}
