package br.com.rvvsanchez.libs.api.rest.client.jaxrs.interceptor;

import static br.com.rvvsanchez.libs.api.rest.client.jaxrs.interceptor.CompressConstants.GZIP_ENCODING;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

/**
 * Writer interceptor responsible to include the required parameters to compress the request using gzip.
 * 
 * This class also change the output stream to support the compression.
 * 
 * @author robson-sanchez
 */
public class CompressWriterInterceptor implements ClientRequestFilter, WriterInterceptor {

  /**
   * Includes the required HTTP parameters.
   */
  @Override
  public void filter(ClientRequestContext requestContext) throws IOException {
    final MultivaluedMap<String, Object> requestHeaders = requestContext.getHeaders();
    requestHeaders.add(HttpHeaders.CONTENT_ENCODING, GZIP_ENCODING);
  }

  /**
   * Changes the output stream to support the compression using gzip.
   */
  @Override
  public void aroundWriteTo(WriterInterceptorContext context) throws IOException,
      WebApplicationException {
    final OutputStream outputStream = context.getOutputStream();
    context.setOutputStream(new GZIPOutputStream(outputStream));
    context.proceed();
  }

}
