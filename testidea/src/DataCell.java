/**
 * Created by zhangzhonghua on 2016/9/12.
 */
public class DataCell {
    private String name;

    private Object value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String toXml() {
        String xml = "<cell name=\""+ this.getName() + "\">";
        xml += this.getValue().toString().replaceAll("&", "&amp;");
        xml += "</cell>";
        return xml;
    }
}
