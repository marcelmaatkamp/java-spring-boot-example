import org.junit.Test;

import junit.framework.Assert.*;

class XmlTest { 

    @Test
    void testXml() { 
        String xml = "<xml>kiekeboe</xml>";
        Assert.assertNotNull(xml);
    }

}