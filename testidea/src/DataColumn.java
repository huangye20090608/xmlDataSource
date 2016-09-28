/**
 * Created by zhangzhonghua on 2016/9/12.
 */
public class DataColumn {

    private String name;

    private DataColumnType type;

    private String ref;

    private String refTable;

    private String refColumn;

    private String func;

    private DataFunc dataFunc;

    private String defaultValue;

    private boolean pk = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataColumnType getType() {
        return type;
    }

    public void setType(DataColumnType type) {
        this.type = type;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
        if(ref.indexOf("\\.") > 0) {
            String[] arr = ref.split("\\.");
            this.refTable = arr[0];
            this.refColumn = arr[1];
        }
    }

    public String getRefTable() {
        return refTable;
    }

    public void setRefTable(String refTable) {
        this.refTable = refTable;
    }

    public String getRefColumn() {
        return refColumn;
    }

    public void setRefColumn(String refColumn) {
        this.refColumn = refColumn;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getFunc() {
        return func;
    }

    public void setFunc(String func) {
        this.func = func;
        if(func.startsWith("$ctx")) {
            dataFunc = new DataFunc();
            dataFunc.setFuncType("ctx");
            int leftBracketsPos = func.indexOf("(");
            int rightBracketsPos = func.lastIndexOf(")");
            String paramsStr = func.substring(leftBracketsPos + 1, rightBracketsPos);
            String[] arr = paramsStr.split(",");
            for(String a : arr) {
                dataFunc.addParam(a.trim());
            }
        }else if(func.startsWith("$replace")) {
        	dataFunc = new DataFunc();
            dataFunc.setFuncType("replace");
            int leftBracketsPos = func.indexOf("(");
            int rightBracketsPos = func.lastIndexOf(")");
            String paramsStr = func.substring(leftBracketsPos + 1, rightBracketsPos);
            String[] arr = paramsStr.split(",");
            for(String a : arr) {
                dataFunc.addParam(a.trim());
            }
        }else if(func.startsWith("$nowdate")) {
        	dataFunc = new DataFunc();
            dataFunc.setFuncType("nowdate");
        }else if(func.startsWith("$nowtime")) {
        	dataFunc = new DataFunc();
            dataFunc.setFuncType("nowtime");
        }else if(func.startsWith("$nowonlytime")) {
        	dataFunc = new DataFunc();
            dataFunc.setFuncType("nowonlytime");
        }else if(func.equals("$null")) {
            dataFunc = new DataFunc();
            dataFunc.setFuncType("null");
        }
    }

    public DataFunc getDataFunc() {
        return dataFunc;
    }

    public void setDataFunc(DataFunc dataFunc) {
        this.dataFunc = dataFunc;
    }

    public String toXml(){
        String xml = "<column name=\"" + this.getName() + "\" type=\"" + this.getType() + "\" ";
        if(ref != null) {
            xml += "ref=\"" + this.getRef() + "\"";
        }
        xml += "></column>";
        return xml;
    }

    public boolean isPk() {
        return pk;
    }

    public void setPk(boolean pk) {
        this.pk = pk;
    }
}
