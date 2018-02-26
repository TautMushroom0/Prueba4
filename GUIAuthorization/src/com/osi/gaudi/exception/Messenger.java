package com.osi.gaudi.exception;

import com.osi.gaudi.session.Session;
import com.osi.gaudi.session.SessionContext;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Messenger implements Serializable {
	
	private static final long serialVersionUID = 5467118857334289099L;
	private final Logger log = LoggerFactory.getLogger(Messenger.class);
	private static Messenger instance;

	public static synchronized Messenger getInstance() {
		if (instance == null) {
			instance = new Messenger();
		}
		return instance;
	}

	public String getMsg(String resource, String key, Object... args) {
		Message msg = getMessage(resource, key, args);
		return msg.getMessage();
	}

	public Message getMessage(String resource, String key, Object... args) {
		Locale locale = getLocale();
		String msg = key;
		try {
			ResourceBundle bundle = ResourceBundle.getBundle(resource, locale);
			msg = bundle.getString(key);
		} catch (MissingResourceException mre) {
			this.log.warn(
					"Llave no encontrada {} para el recurso {}.properties",
					key, resource);
			if (args != null) {
				StringBuffer bargs = new StringBuffer();
				bargs.append(",");
				for (Object arg : args) {
					bargs.append(arg);
					bargs.append(",");
				}
				msg = msg + bargs;
			}
		}
		if (args != null) {
			MessageFormat format = new MessageFormat(msg);
			format.setLocale(locale);
			msg = format.format(args);
		}
		return new Message(key, msg);
	}

	public FailureMessage getFailureMessage(String resource, String key,
			Object... args) {
		Message failure = getMessage(resource, key, args);

		int i = 1;

		ArrayList<String> fixes = new ArrayList<String>();
		String fix;
		while ((fix = getFixMessage(resource, key, i)) != null) {
			// String fix;
			fixes.add(fix);
			i++;
		}
		return new FailureMessage(key, failure.getMessage(), fixes);
	}

	public void addMessage(Message msg) {
	}

	private Locale getLocale() {
		SessionContext ctx = SessionContext.getInstance();
		Session session = ctx.getSession(false);
		if (session != null) {
			return session.getLocale();
		}
		return Locale.getDefault();
	}

	private String getFixMessage(String resource, String key, int index) {
		Locale locale = getLocale();
		StringBuffer fixkey = new StringBuffer();
		fixkey.append(key);
		fixkey.append(".fix");
		fixkey.append(index);
		String sfixkey = fixkey.toString();

		String msg = null;
		try {
			ResourceBundle bundle = ResourceBundle.getBundle(resource, locale);
			msg = bundle.getString(sfixkey);
		} catch (MissingResourceException mre) {
			this.log.warn(
					"Llave no encontrada {} para el recurso {}.properties",
					sfixkey, resource);
		}
		return msg;
	}
}