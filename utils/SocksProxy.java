import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLConnection;

/**
 * Test proxy settings, connect to tcp://nodepki:5000,
 * print failure or succes in byte count returned.
 * 
 * Mac OSX (High Sierra):
 * 
 *  - Preferences, Network, Advanced, Proxy: Enable socks proxy '127.0.0.1:9050'
 *    This seems to be 'the' setting to influence socks proxy settings
 * 
 *  - Preferences, Java, Network Settings, Advanced: Enable socks proxy '127.0.0.1:9050'
 *    Seems not to influence connection settings for java processes
 * 
 */

public class SocksProxy {

  static String PROXY_HOST = "127.0.0.1";
  static int PROXY_PORT = 9050;

  static String HOST_NAME = "nodepki";
  static String HOST_IP = "172.23.0.3";
  static int HOST_PORT = 5000;

  static String HOST_NAME_PORT = "http://" + HOST_NAME + ":" + HOST_PORT;
  static String HOST_IP_PORT = "http://" + HOST_IP + ":" + HOST_PORT;

  public void throughProxy(String proxyHost, int proxyPort, String urlString)
      throws MalformedURLException, IOException {
    SocketAddress addr = new InetSocketAddress(proxyHost, proxyPort);
    Proxy proxy = new Proxy(Proxy.Type.SOCKS, addr);
    URL url = new URL(urlString);
    URLConnection conn = url.openConnection(proxy);
    System.out.println(String.format("throughProxy(%s,%d): host(%s): returned %d bytes", proxyHost, proxyPort,
        urlString, conn.getContentLength()));
  }

  public void direct(String urlString) 
    throws MalformedURLException, IOException {
    URL url = new URL(urlString);
    URLConnection conn = url.openConnection();
    System.out.println(String.format("direct: host(%s): returned %d bytes", urlString, conn.getContentLength()));
  }

  public static void main(String[] args) {
    SocksProxy socksProxy = new SocksProxy();

    try {
      socksProxy.direct(HOST_NAME_PORT);
    } catch (Exception e) {
      System.out.println("Exception: " + e);
    }

    try {
      socksProxy.throughProxy(PROXY_HOST, PROXY_PORT, HOST_NAME_PORT);
    } catch (Exception e) {
      System.out.println("Exception: " + e);
    }

    try {
      socksProxy.throughProxy(PROXY_HOST, PROXY_PORT, HOST_IP_PORT);
    } catch (Exception e) {
      System.out.println("Exception: " + e);
    }

  }
}
