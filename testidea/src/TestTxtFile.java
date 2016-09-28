import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangzhonghua on 2016/9/5.
 */
public class TestTxtFile {

    public static void main(String[] args) {
        String findIdFilePath = "D:\\update_funcid.txt";
        String codeFilePath = "D:\\update_code.txt";

        try {
            FileReader funcIdFr = null;

            funcIdFr = new FileReader(findIdFilePath);
            BufferedReader funcIdBf = new BufferedReader(funcIdFr);

            String funcid = null;

            List<String> funcIds = new ArrayList<String>();
            while ((funcid=funcIdBf.readLine())!=null){
//                System.out.println(funcid);
                funcIds.add(funcid);
            }
            funcIdBf.close();
            funcIdFr.close();

            FileReader codeFr = null;

            codeFr = new FileReader(codeFilePath);
            BufferedReader codeBf = new BufferedReader(codeFr);

            String code = null;

            List<String> codes = new ArrayList<String>();
            while ((code=codeBf.readLine())!=null){
//                System.out.println(code);
                codes.add(code);
            }
            codeFr.close();
            codeBf.close();

            for(int i=0; i<funcIds.size(); i++) {
                String sql = "update b2b_function set code = '" + codes.get(i) + "' where func_id = " + funcIds.get(i) + ";";
//                System.out.println(funcIds.get(i) + " | " + codes.get(i));
                System.out.println(sql);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
