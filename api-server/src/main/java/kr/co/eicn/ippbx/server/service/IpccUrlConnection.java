package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.server.util.UrlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.stream.Collectors;

public class IpccUrlConnection {
	private static final Logger logger = LoggerFactory.getLogger(IpccUrlConnection.class);

	public static void execute(final String urlPath, String peer) {
		if(urlPath == null || urlPath.equals("") || peer == null || peer.equals("") || peer.equals("|")) {
			return;
		}
		URLConnection httpConnection;
		PrintWriter out_p = null;
		BufferedReader in = null;
		try {
			URL url = new URL(urlPath);
			httpConnection = url.openConnection();
			httpConnection.setDoOutput(true);
			httpConnection.setUseCaches(false);

			out_p = new PrintWriter(httpConnection.getOutputStream());

			out_p.print("members="+peer);
			out_p.flush();

			InputStream is = httpConnection.getInputStream();
			in = new BufferedReader(new InputStreamReader(is),8 * 1024);

			String line;
			while( (line = in.readLine()) != null ) {
				logger.info(line);
			}
		} catch(Exception e) {
			logger.error("IpccUrlConnection.execute ERROR[urlPath={}, peer={}, dateTime={}, error={}]",
					urlPath, peer, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), e.getMessage());
		} finally {
			if ( out_p != null ) try {out_p.close();} catch (Exception ignored){}
			if ( in != null ) try {in.close();} catch (Exception ignored){}
		}
	}

	public static String fetch(String fetchUrl, Map<String, Object> params) {
		URLConnection httpConnection;
		PrintWriter out_p = null;
		BufferedReader in = null;

		try {
			URL url = new URL(fetchUrl);

			httpConnection = url.openConnection();
			httpConnection.setDoOutput(true);
			httpConnection.setUseCaches(false);

			out_p = new PrintWriter(httpConnection.getOutputStream());

			out_p.print(UrlUtils.encodeQueryParams(params));
			out_p.flush();

			InputStream is = httpConnection.getInputStream();
			in = new BufferedReader(new InputStreamReader(is), 8 * 1024);

			return in.lines().collect(Collectors.joining(System.lineSeparator()));
		} catch (Exception e) {
			logger.error("IpccUrlConnection.fetch ERROR[urlPath={}, params=({}), dateTime={}, error={}]",
					fetchUrl, UrlUtils.encodeQueryParams(params), LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), e.getMessage());
		} finally {
			if (out_p != null) try { out_p.close(); } catch (Exception ignore) {}
			if (in != null) try { in.close(); } catch (Exception ignore) {}
		}
		return null;
	}
}
