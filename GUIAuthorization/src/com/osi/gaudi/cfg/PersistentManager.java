package com.osi.gaudi.cfg;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.osi.gaudi.exception.Failure;
import com.osi.gaudi.exception.FailureMessage;
import com.osi.gaudi.exception.Messenger;

public class PersistentManager implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8103530407102558689L;
	private Document doc;
	private Element el;
	private File file;
	private long lastModified;
	private final Logger log = LoggerFactory.getLogger(PersistentManager.class);

	public PersistentManager(String fileName) {
		this.file = new File(fileName);
		this.lastModified = this.file.lastModified();
		loadFile(this.file);
	}

	public void save() {
		Format format = Format.getPrettyFormat();
		format.setOmitEncoding(true);
		format.setOmitDeclaration(true);
		XMLOutputter out = new XMLOutputter(format);
		try {
			out.output(this.doc, new FileOutputStream(this.file));
		} catch (IOException e) {
			FailureMessage msg = Messenger.getInstance().getFailureMessage(
					"gaudi", "error.fileSave", new Object[] { this.file });

			this.log.error(msg.getMessage(), e);
			throw new Failure(msg.getCode(), msg.getMessage(), msg.getFixes(),
					e);
		}
	}

	public Element getRoot() {
		long currModified = this.file.lastModified();
		if (currModified > this.lastModified) {
			loadFile(this.file);
			this.lastModified = currModified;
		}
		return this.el;
	}

	private void loadFile(File file) {
		SAXBuilder builder = new SAXBuilder(false);
		try {
			this.doc = builder.build(file);
			this.el = this.doc.getRootElement();
		} catch (IOException ioe) {
			FailureMessage msg = Messenger.getInstance().getFailureMessage(
					"gaudi", "error.ioFile", new Object[] { file.getName() });

			this.log.error(msg.getMessage(), ioe);
			throw new Failure(msg.getCode(), msg.getMessage(), msg.getFixes(),
					ioe);
		} catch (JDOMException jdome) {
			FailureMessage msg = Messenger.getInstance()
					.getFailureMessage("gaudi", "error.fileParse",
							new Object[] { file.getName() });

			this.log.error(msg.getMessage(), jdome);
			throw new Failure(msg.getCode(), msg.getMessage(), msg.getFixes(),
					jdome);
		}
	}
}