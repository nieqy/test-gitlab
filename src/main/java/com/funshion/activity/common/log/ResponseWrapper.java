package com.funshion.activity.common.log;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ResponseWrapper extends HttpServletResponseWrapper {

	private ByteArrayOutputStream output;
	private FilterServletOutputStream filterOutput;

	private PrintWriter cachedWriter;
	private CharArrayWriter bufferedWriter;

	public ResponseWrapper(HttpServletResponse response) throws IOException {
		super(response);
		output = new ByteArrayOutputStream();
		bufferedWriter = new CharArrayWriter();
		cachedWriter = new PrintWriter(bufferedWriter);
	}

	public PrintWriter getWriter() throws IOException {
		return cachedWriter;
	}


	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		if (filterOutput == null) {
			filterOutput = new FilterServletOutputStream(output);
		}
		return filterOutput;
	}

	public byte[] getDataStream() {
		return output.toByteArray();
	}


}