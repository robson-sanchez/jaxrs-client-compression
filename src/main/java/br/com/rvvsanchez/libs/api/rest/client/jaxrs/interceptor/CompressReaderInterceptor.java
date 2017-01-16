package br.com.rvvsanchez.libs.api.rest.client.jaxrs.interceptor;

import static br.com.rvvsanchez.libs.api.rest.client.jaxrs.interceptor.CompressConstants.GZIP_ENCODING;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.GZIPInputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;

/**
 * Reader interceptor responsible to deal with the encoded content.
 * 
 * @author robson-sanchez
 */
public class CompressReaderInterceptor implements ClientRequestFilter, ReaderInterceptor {

  /**
   * Includes the required HTTP parameters.
   */
  @Override
  public void filter(ClientRequestContext requestContext) throws IOException {
    final MultivaluedMap<String, Object> requestHeaders = requestContext.getHeaders();
    requestHeaders.add(HttpHeaders.ACCEPT_ENCODING, GZIP_ENCODING);
  }
  
  /**
   * Changes the input stream to support the compression using gzip.
   */
  @Override
  public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException,
      WebApplicationException {
    final MultivaluedMap<String, String> headers = context.getHeaders();
    final List<String> contentEncoding = headers.get(HttpHeaders.CONTENT_ENCODING);

    boolean responseEncoded = isEncoded(contentEncoding);

    if (responseEncoded) {
      final InputStream originalInputStream = context.getInputStream();
      context.setInputStream(new GZIPInputStream(originalInputStream));
    }

    return context.proceed();
  }

  private boolean isEncoded(List<String> headers) {
    boolean encoded = false;

    if (headers != null) {
      for (String encoding : headers) {
        if (encoding.contains(GZIP_ENCODING)) {
          encoded = true;
          break;
        }
      }
    }

    return encoded;
  }

}
