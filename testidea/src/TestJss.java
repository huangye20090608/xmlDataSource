import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by zhangzhonghua on 2016/1/22.
 */
public class TestJss {

    public static void main(String[] args) {
        String path = "http://192.168.150.52/fw.jd.com/0cc467e0b08817ea443a0f347c29b48c20160121140127787.pfx?Expires=1611036087&AccessKey=CGZmtRcoyB7pzdPu&Signature=u0NV1jNIMOfyLEUq1HOOfeKH%2BBM%3D";

        try {
            InputStream in = new
                    URL(path).openStream();
            byte[] bytes = IOUtils.toByteArray(in);
            FileUtils.writeByteArrayToFile(new File("d:/test.pfx"), bytes);
            IOUtils.closeQuietly(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
