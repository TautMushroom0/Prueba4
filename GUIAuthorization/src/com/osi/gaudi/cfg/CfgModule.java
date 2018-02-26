package com.osi.gaudi.cfg;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.osi.gaudi.exception.Failure;
import com.osi.gaudi.exception.FailureMessage;
import com.osi.gaudi.exception.Messenger;
import com.osi.gaudi.security.util.EEncryptingAlgorithm;
import com.osi.gaudi.security.util.EncryptingUtil;

public class CfgModule implements Serializable {

	private static final long serialVersionUID = 8692751018880815511L;
	private final Logger log = LoggerFactory.getLogger(CfgModule.class);
	private String file;
	private PersistentManager pm;
	private HashMap<String, CfgItem> cfgItems;
	private Element element;

	public CfgModule(String file) {
		this.file = file;
		this.pm = new PersistentManager(file);
		this.element = this.pm.getRoot();
		this.cfgItems = loadCfg(this.element);
	}

	public CfgItem get(String name) {
		Element temp = this.pm.getRoot();
		if (temp != this.element) {
			this.element = temp;
			this.cfgItems = loadCfg(this.element);
		}
		return (CfgItem) this.cfgItems.get(name);
	}

	public CfgItem getSecure(String name) {
		CfgItem item = get(name);

		return item == null ? null : decryptItem(item);
	}

	public void set(CfgItem item) {
		if (this.cfgItems.containsKey(item.getName())) {
			change(item);
		} else {
			add(item);
		}
		this.pm.save();
		this.cfgItems.put(item.getName(), item);
	}

	public void setSecure(CfgItem item) {
		CfgItem secureItem = encryptItem(item);
		set(secureItem);
	}

	public Collection<CfgItem> getAll() {
		return this.cfgItems.values();
	}

	private HashMap<String, CfgItem> loadCfg(Element cfgEl) {
		HashMap<String, CfgItem> result = new HashMap<String, CfgItem>();

		Iterator<Element> itItemEl = cfgEl.getChildren("item").iterator();
		while (itItemEl.hasNext()) {
			Element itemEl = (Element) itItemEl.next();
			boolean itemAncestor = itemEl.getChildren("value").size() > 0;

			String name = getAttributeValue(itemEl, "name", true);
			String descr = getAttributeValue(itemEl, "description", false);
			String value = getAttributeValue(itemEl, "value", !itemAncestor);
			ArrayList<String> valueList = null;
			if (itemAncestor) {
				Iterator<Element> itValueEl = itemEl.getChildren("value").iterator();
				valueList = new ArrayList<String>();
				while (itValueEl.hasNext()) {
					Element valueEl = (Element) itValueEl.next();
					valueList.add(valueEl.getText());
				}
			}
			CfgItem item = new CfgItem(name, value, descr,
					valueList == null ? 0 : 1, valueList);
			result.put(name, item);
		}
		return result;
	}

	private String getAttributeValue(Element itemEl, String name,
			boolean required) {
		String value = itemEl.getAttributeValue(name);
		if ((required) && ((value == null) || (value.isEmpty()))) {
			FailureMessage msg = Messenger.getInstance().getFailureMessage(
					"gaudi", "error.itemRequired",
					new Object[] { this.file, name });

			this.log.error(msg.getMessage());
			throw new Failure(msg.getCode(), msg.getMessage(), msg.getFixes());
		}
		return value;
	}

	private void add(CfgItem item) {
		Element rootEl = this.pm.getRoot();
		Element itemEl = new Element("item");
		itemEl.setAttribute("name", item.getName());
		itemEl.setAttribute("description", item.getDescription());
		if (item.getType() == 0) {
			itemEl.setAttribute("value", item.getValue());
		} else {
			Iterator<String> itValues = item.getValues().iterator();
			while (itValues.hasNext()) {
				Element valueEl = new Element("value");
				valueEl.setText((String) itValues.next());
				itemEl.addContent(valueEl);
			}
		}
		rootEl.addContent(itemEl);
	}

	private void change(CfgItem item) {
		Element rootEl = this.pm.getRoot();
		Iterator<Element> itItemEl = rootEl.getChildren("item").iterator();
		while (itItemEl.hasNext()) {
			Element itemEl = (Element) itItemEl.next();
			Attribute nameAttr = itemEl.getAttribute("name");
			if (nameAttr.getValue().equals(item.getName())) {
				itemEl.setAttribute("description", item.getDescription());
				if (item.getType() == 0) {
					itemEl.setAttribute("value", item.getValue());
				} else {
					itemEl.removeAttribute("value");
					itemEl.removeChildren("value");
					Iterator<String> itValues = item.getValues().iterator();
					while (itValues.hasNext()) {
						Element valueEl = new Element("value");
						valueEl.setText((String) itValues.next());
						itemEl.addContent(valueEl);
					}
				}
			}
		}
	}

	private CfgItem decryptItem(CfgItem item) {
		String secureValue = item.getValue();
		List<String> secureValues = item.getValues();
		if (item.getType() == 0) {
			secureValue = EncryptingUtil.decrypt(secureValue,
					EEncryptingAlgorithm.AES);
			if (secureValue == null) {
				FailureMessage msg = Messenger.getInstance().getFailureMessage(
						"gaudi", "error.decrypt",
						new Object[] { item.getName(), this.file });

				this.log.error(msg.getMessage());
				throw new Failure(msg.getCode(), msg.getMessage(),
						msg.getFixes());
			}
		} else {
			List<String> newSecureValues = new ArrayList<String>();
			for (String value : secureValues) {
				secureValue = EncryptingUtil.decrypt(value,
						EEncryptingAlgorithm.AES);
				if (secureValue == null) {
					FailureMessage msg = Messenger.getInstance()
							.getFailureMessage("gaudi", "error.decrypt",
									new Object[] { item.getName(), this.file });

					this.log.error(msg.getMessage());
					throw new Failure(msg.getCode(), msg.getMessage(),
							msg.getFixes());
				}
				newSecureValues.add(secureValue);
			}
			secureValues = newSecureValues;
		}
		CfgItem secureItem = new CfgItem(item.getName(), secureValue,
				item.getDescription(), item.getType(), secureValues);
		return secureItem;
	}

	private CfgItem encryptItem(CfgItem item) {
		String secureValue = item.getValue();
		List<String> secureValues = item.getValues();
		if (item.getType() == 0) {
			secureValue = EncryptingUtil.encrypt(secureValue,
					EEncryptingAlgorithm.AES);
			if (secureValue == null) {
				FailureMessage msg = Messenger.getInstance().getFailureMessage(
						"gaudi", "error.encrypt",
						new Object[] { item.getName(), this.file });

				this.log.error(msg.getMessage());
				throw new Failure(msg.getCode(), msg.getMessage(),
						msg.getFixes());
			}
		} else {
			List<String> newSecureValues = new ArrayList<String>();
			for (String value : secureValues) {
				secureValue = EncryptingUtil.encrypt(value,
						EEncryptingAlgorithm.AES);
				if (secureValue == null) {
					FailureMessage msg = Messenger.getInstance()
							.getFailureMessage("gaudi", "error.encrypt",
									new Object[] { item.getName(), this.file });

					this.log.error(msg.getMessage());
					throw new Failure(msg.getCode(), msg.getMessage(),
							msg.getFixes());
				}
				newSecureValues.add(secureValue);
			}
			secureValues = newSecureValues;
		}
		CfgItem secureItem = new CfgItem(item.getName(), secureValue,
				item.getDescription(), item.getType(), secureValues);
		return secureItem;
	}
}