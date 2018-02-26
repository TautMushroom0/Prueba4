package com.osi.gaudi.cfg;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.osi.gaudi.exception.Failure;
import com.osi.gaudi.exception.FailureMessage;
import com.osi.gaudi.exception.Messenger;

public class Configurator implements Serializable {
	
	private static final long serialVersionUID = -1011348183988013314L;
	private final Logger log = LoggerFactory.getLogger(Configurator.class);
	private static Configurator instance;
	private PersistentManager pm;
	private Element element;
	private HashMap<String, CfgModule> modules;

	public static synchronized Configurator getInstance() {
		if (instance == null) {
			instance = new Configurator();
		}
		return instance;
	}

	private Configurator() {
		String varName = getVarName();

		String gaudiHome = System.getProperty(varName);
		if (gaudiHome == null) {
			gaudiHome = System.getenv(varName);
		}
		if (gaudiHome == null) {
			FailureMessage msg = Messenger.getInstance().getFailureMessage(
					"gaudi", "error.pathNotFound", new Object[0]);

			this.log.error(msg.getMessage());
			throw new Failure(msg.getCode(), msg.getMessage(), msg.getFixes());
		}
		String filePath = gaudiHome + "/gaudiCfg.xml";

		this.pm = new PersistentManager(filePath);
		this.element = this.pm.getRoot();
		this.modules = new HashMap<String, CfgModule>();

		loadModules(this.element);
	}

	public String getString(String module, String name, String defaultValue) {
		return get(module, name, defaultValue, false);
	}

	public String getStringSecure(String module, String name,
			String defaultValue) {
		return get(module, name, defaultValue, true);
	}

	public int getInt(String module, String name, int defaultValue) {
		int result = defaultValue;
		CfgModule cfgmodule = getModule(module);

		CfgItem value = cfgmodule.get(name);
		if (value != null) {
			try {
				result = Integer.parseInt(value.getValue());
			} catch (NumberFormatException e) {
				FailureMessage msg =

				Messenger.getInstance().getFailureMessage("gaudi",
						"error.format",
						new Object[] { "int", value.getValue() });

				this.log.error(msg.getMessage());
				throw new Failure(msg.getCode(), msg.getMessage(),
						msg.getFixes());
			}
		} else {
			set(cfgmodule, name, defaultValue);
		}
		return result;
	}

	public boolean getBool(String module, String name, boolean defaultValue) {
		boolean result = defaultValue;
		CfgModule cfgmodule = getModule(module);

		CfgItem value = cfgmodule.get(name);
		if (value != null) {
			try {
				result = Boolean.parseBoolean(value.getValue());
			} catch (NumberFormatException e) {
				FailureMessage msg =

				Messenger.getInstance().getFailureMessage("gaudi",
						"error.format",
						new Object[] { "bool", value.getValue() });

				this.log.error(msg.getMessage());
				throw new Failure(msg.getCode(), msg.getMessage(),
						msg.getFixes());
			}
		} else {
			set(cfgmodule, name, defaultValue);
		}
		return result;
	}

	public double getDouble(String module, String name, double defaultValue) {
		double result = defaultValue;
		CfgModule cfgmodule = getModule(module);

		CfgItem value = cfgmodule.get(name);
		if (value != null) {
			try {
				result = Double.parseDouble(value.getValue());
			} catch (NumberFormatException e) {
				FailureMessage msg =

				Messenger.getInstance().getFailureMessage("gaudi",
						"error.format",
						new Object[] { "double", value.getValue() });

				this.log.error(msg.getMessage());
				throw new Failure(msg.getCode(), msg.getMessage(),
						msg.getFixes());
			}
		} else {
			set(cfgmodule, name, defaultValue);
		}
		return result;
	}

	public List<String> getArray(String module, String name,
			List<String> defaultValue) {
		return getArray(module, name, defaultValue, false);
	}

	public List<String> getArraySecure(String module, String name,
			List<String> defaultValue) {
		return getArray(module, name, defaultValue, true);
	}

	public void set(String module, CfgItem item) {
		set(module, item, false);
	}

	public void setSecure(String module, CfgItem item) {
		set(module, item, true);
	}

	public Collection<CfgItem> getAll(String module) {
		CfgModule cfgmodule = (CfgModule) this.modules.get(module);
		if (cfgmodule != null) {
			return cfgmodule.getAll();
		}
		return new ArrayList<CfgItem>();
	}

	public Set<String> getModules() {
		return this.modules.keySet();
	}

	private void loadModules(Element cfglistEl) {
		Iterator<Element> itCfgEl = cfglistEl.getChildren().iterator();
		while (itCfgEl.hasNext()) {
			Element cfgEl = (Element) itCfgEl.next();
			String module = cfgEl.getAttribute("module").getValue();
			String source = cfgEl.getAttribute("source").getValue();

			this.modules.put(module, new CfgModule(source));
		}
	}

	private void set(CfgModule cfgmodule, String name, int value) {
		String descr = Messenger.getInstance().getMsg("gaudi",
				"msg.cfgDefaultDescription", new Object[0]);

		CfgItem item = new CfgItem(name, String.valueOf(value), descr, 0, null);
		cfgmodule.set(item);
	}

	private void set(CfgModule cfgmodule, String name, String value,
			boolean isSecure) {
		String descr = Messenger.getInstance().getMsg("gaudi",
				"msg.cfgDefaultDescription", new Object[0]);

		CfgItem item = new CfgItem(name, value, descr, 0, null);
		if (isSecure) {
			cfgmodule.setSecure(item);
		} else {
			cfgmodule.set(item);
		}
	}

	private void set(CfgModule cfgmodule, String name, boolean value) {
		String descr = Messenger.getInstance().getMsg("gaudi",
				"msg.cfgDefaultDescription", new Object[0]);

		CfgItem item = new CfgItem(name, String.valueOf(value), descr, 0, null);
		cfgmodule.set(item);
	}

	private void set(CfgModule cfgmodule, String name, double value) {
		String descr = Messenger.getInstance().getMsg("gaudi",
				"msg.cfgDefaultDescription", new Object[0]);

		CfgItem item = new CfgItem(name, String.valueOf(value), descr, 0, null);
		cfgmodule.set(item);
	}

	private void set(CfgModule cfgmodule, String name, List<String> value,
			boolean isSecure) {
		String descr = Messenger.getInstance().getMsg("gaudi",
				"msg.cfgDefaultDescription", new Object[0]);

		CfgItem item = new CfgItem(name, null, descr, 1, value);
		cfgmodule.set(item);
	}

	private String getVarName() {
		try {
			ResourceBundle bundle = ResourceBundle.getBundle("cfg");
			return bundle.getString("var.name");
		} catch (MissingResourceException mre) {
			FailureMessage msg = Messenger.getInstance().getFailureMessage(
					"gaudi", "error.varNotFound", new Object[0]);

			this.log.error(msg.getMessage());
			throw new Failure(msg.getCode(), msg.getMessage(), msg.getFixes());
		}
	}

	private CfgModule getModule(String module) {
		Element temp = this.pm.getRoot();
		if (temp != this.element) {
			this.element = temp;
			loadModules(this.element);
		}
		CfgModule cfgmodule = (CfgModule) this.modules.get(module);
		if (cfgmodule == null) {
			FailureMessage msg = Messenger.getInstance().getFailureMessage(
					"gaudi", "error.moduleNotFound", new Object[] { module });

			this.log.error(msg.getMessage());
			throw new Failure(msg.getCode(), msg.getMessage(), msg.getFixes());
		}
		return cfgmodule;
	}

	private String get(String module, String name, String defaultValue,
			boolean isSecure) {
		String result = defaultValue;
		CfgModule cfgmodule = getModule(module);

		CfgItem value = null;
		if (isSecure) {
			value = cfgmodule.getSecure(name);
		} else {
			value = cfgmodule.get(name);
		}
		if (value != null) {
			result = value.getValue();
		} else {
			set(cfgmodule, name, defaultValue, isSecure);
		}
		return result;
	}

	private void set(String module, CfgItem item, boolean isSecure) {
		CfgModule cfgmodule = getModule(module);
		if (isSecure) {
			cfgmodule.setSecure(item);
		} else {
			cfgmodule.set(item);
		}
	}

	private List<String> getArray(String module, String name,
			List<String> defaultValue, boolean isSecure) {
		List<String> result = defaultValue;
		CfgModule cfgmodule = getModule(module);

		CfgItem value = null;
		if (isSecure) {
			value = cfgmodule.getSecure(name);
		} else {
			value = cfgmodule.get(name);
		}
		if (value != null) {
			result = value.getValues();
		} else {
			set(cfgmodule, name, defaultValue, isSecure);
		}
		return result;
	}
}