package com.skillnet;

import java.io.InputStream;
import java.io.OutputStream;

import org.apache.camel.Exchange;
import org.apache.camel.spi.DataFormat;

public class BindyCsvDataFormat implements DataFormat {

	public BindyCsvDataFormat(String string) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void marshal(Exchange arg0, Object arg1, OutputStream arg2) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public Object unmarshal(Exchange arg0, InputStream arg1) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
