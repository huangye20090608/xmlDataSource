import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangzhonghua on 2016/9/12.
 */
public class DataFunc {

    private String funcType;

    private List<String> params = new ArrayList<String>();

    public void addParam(String param) {
        this.params.add(param);
    }

    public String getFuncType() {
        return funcType;
    }

    public void setFuncType(String funcType) {
        this.funcType = funcType;
    }

    public List<String> getParams() {
        return params;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }
}
