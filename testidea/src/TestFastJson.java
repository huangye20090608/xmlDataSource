import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * Created by zhangzhonghua on 2016/1/20.
 */
public class TestFastJson {
    private static SerializerFeature[] emptyFeatures = {};
    private static SerializerFeature[] writeZeroFeatures = {
            SerializerFeature.WriteNullNumberAsZero
    };

    public static void main(String[] args) {
        System.out.println("hello");

        TestMojo mojo = new TestMojo();

        JSONObject jsonObj = new JSONObject();
        jsonObj.put("mojo", mojo);

        System.out.println(jsonObj.toJSONString());
        System.out.println(JSON.toJSONString(jsonObj, writeZeroFeatures));
    }
}
