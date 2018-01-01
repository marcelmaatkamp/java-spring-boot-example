import java.io.IOException;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SocksProxy {

  static String PROXY_HOST="127.0.0.1";
  static int    PROXY_PORT=9050;

  static String HOST_NAME = "nodepki";
  static String HOST_IP = "172.23.0.3";
  static int    HOST_PORT = 5000;

  static String HOST_NAME_PORT = "http://"+HOST_NAME+":"+HOST_PORT;
  static String HOST_IP_PORT = "http://"+HOST_IP+":"+HOST_PORT;

  public SocksProxy() { }

  public void isReachable(String hostname) throws UnknownHostException,IOException { 
    InetAddress address = InetAddress.getByName(hostname);
    boolean reachable = address.isReachable(5000);
    System.out.println(String.format("isReachable(%s): %b", hostname, reachable));
  }

  public void throughProxy(String proxyHost, int proxyPort, String urlString) throws MalformedURLException,IOException { 
    SocketAddress addr = new InetSocketAddress(proxyHost, proxyPort);
    Proxy proxy = new Proxy(Proxy.Type.SOCKS, addr);
    URL url = new URL(urlString);
    URLConnection conn = url.openConnection(proxy);
    System.out.println(String.format("throughProxy(%s,%d): host(%s): Got "+conn.getContentLength() + " bytes...",proxyHost,proxyPort,urlString));
  }

  public void direct(String urlString) throws MalformedURLException,IOException {
    URL url = new URL(urlString);
    URLConnection conn = url.openConnection();
    System.out.println(String.format("direct: host(%s): Got "+conn.getContentLength() + " bytes...",urlString));
  }  

  public static void main(String[] args) {
    SocksProxy socksProxy = new SocksProxy();

    try {
      socksProxy.isReachable(HOST_NAME);
    } catch(Exception e) {
      System.out.println("Exception: "+e);
    }

    try {
      socksProxy.isReachable(HOST_IP);
    } catch(Exception e) {
      System.out.println("Exception: "+e);
    }

    try {
      socksProxy.direct(HOST_NAME_PORT); 
    } catch(Exception e) { 
      System.out.println("Exception: "+e);
    }

    try {
      socksProxy.direct(HOST_IP_PORT);
    } catch(Exception e) {
      System.out.println("Exception: "+e);
    }

    try { 
      socksProxy.throughProxy(PROXY_HOST,PROXY_PORT,HOST_NAME_PORT); 
    } catch(Exception e) { 
      System.out.println("Exception: "+e);
    }

    try { 
      socksProxy.throughProxy(PROXY_HOST,PROXY_PORT,HOST_IP_PORT); 
    } catch(Exception e) { 
      System.out.println("Exception: "+e);
    }

  }
}
