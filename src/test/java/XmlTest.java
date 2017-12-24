import org.junit.Test;

import junit.framework.Assert.*;

public class XmlTest { 

    @Test
    public void testXml() { 
        String xml = "<xml>kiekeboe</xml>";
        Assert.assertNotNull(xml);
    }

}