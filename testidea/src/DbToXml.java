import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.sql.*;
import java.util.*;

/**
 * Created by zhangzhonghua on 2016/9/12.
 */
public class DbToXml {
    private static Long platformId = 20160627141L;

    public static void main(String[] args) {
        DbToXml dbToXml = new DbToXml();
        dbToXml.generateXml();

//        dbToXml.importToDb("aaa");
    }

    private void generateXml() {
        HashSet<String> validTables = new HashSet<String>();
//        validTables.add("base_flow");
//        validTables.add("base_sms_config");
//        validTables.add("delivery_range");
//        validTables.add("item");
//        validTables.add("item_brand");
//        validTables.add("item_category");
//        validTables.add("item_category_brand");
//        validTables.add("item_picture");
//        validTables.add("item_price");
//        validTables.add("item_sku");
//        validTables.add("item_sku_picture");
//        validTables.add("mall_domain_info");
//        validTables.add("mall_help_classify");
//        validTables.add("mall_help_document");
//        validTables.add("mall_info");
//        validTables.add("mall_mobile_instance_template");
//        validTables.add("mall_notice");
//        validTables.add("mall_tdk");
//        validTables.add("menu_resource");
//        validTables.add("platform_edition");
//        validTables.add("platform_func");
//        validTables.add("properties_config");
//        validTables.add("shop_brand");
//        validTables.add("shop_credit_audit");
//        validTables.add("shop_domain_mapper");
//        validTables.add("shop_fare");
//        validTables.add("shop_info");
//        validTables.add("shop_info_extends");
//        validTables.add("shop_info_for_query");
//        validTables.add("shop_member");
//        validTables.add("shop_platform_category");
//        validTables.add("shop_regedit_credit");
//        validTables.add("shop_user_credit_account_history");
//        validTables.add("trade_inventory");
//        validTables.add("trade_inventory_operation_log");
//        validTables.add("trade_payment_type");
//        validTables.add("trade_payment_type_attribute");
//        validTables.add("trade_sku_price");
        validTables.add("user");
//        validTables.add("user_audit");
//        validTables.add("user_contract");
        validTables.add("user_extends");
//        validTables.add("user_mall_resource");
//        validTables.add("user_permission_code");
//        validTables.add("user_visit_track");

        String url = "jdbc:mysql://192.168.149.57:3306/ecc_b2b_meta?characterEncoding=utf8&connectTimeout=1000&autoReconnect=true";
        String username = "root";
        String password = "1qaz2wsx";

        Connection conn = null;

        List<DataTable> tableList = new ArrayList<DataTable>();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = (Connection) DriverManager.getConnection(url, username, password);

            DatabaseMetaData dbMetaData = conn.getMetaData();
            String[]   types   =   {"TABLE"};
            ResultSet  tables   =   dbMetaData.getTables(null, null, null, types);

            while(tables.next()) {
                String tableName = (String)tables.getObject("TABLE_NAME");

                System.out.println(tableName);

                if(!validTables.contains(tableName)) {
                    continue;
                }

                DataTable dataTable = new DataTable();
                dataTable.setName(tableName);
                tableList.add(dataTable);

                ResultSet columns = conn.getMetaData().getColumns(null, "%", tableName, "%");
                while(columns.next()) {
                    DataColumn dataColumn = new DataColumn();
                    String columnName = (String)columns.getObject("COLUMN_NAME");
                    dataColumn.setName(columnName);
                    Integer columnType = (Integer)columns.getObject("DATA_TYPE");
                    if(columnType == 12) {
                        dataColumn.setType(DataColumnType.VARCHAR);
                    }else if(columnType == -5) {
                        dataColumn.setType(DataColumnType.BIGINT);
                    }else if(columnType == -6) {
                        dataColumn.setType(DataColumnType.TINYINT);
                    }else if(columnType == 91) {
                        dataColumn.setType(DataColumnType.DATE);
                    }

                    dataTable.addColumn(dataColumn);
                }

//                String sql = "select * from " + tableName + " where platform_id = " + platformId;
//                PreparedStatement pstmt = conn.prepareStatement(sql);
//                ResultSet recordRs = pstmt.executeQuery();
//                while(recordRs.next()) {
//                    DataRow row = new DataRow();
//                    String rowIndex = UUID.randomUUID().toString();
//                    row.setRowIndex(rowIndex);
//                    for(DataColumn column : dataTable.getColumns()) {
//                        Object value = recordRs.getObject(column.getName());
//                        DataCell cell = new DataCell();
//                        cell.setName(column.getName());
//                        if(value == null) {
//                            cell.setValue("[NULL]");
//                        }else {
//                            cell.setValue(value.toString());
//                        }
//                        row.addCell(cell);
//                    }
//                    dataTable.addRow(row);
//                }
            }

            System.out.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            System.out.println("<root>");
            for(int i=0; i<tableList.size(); i++) {
                DataTable dataTable = tableList.get(i);
                System.out.println(dataTable.toXml());
            }
            System.out.println("</root>");

            conn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void importToDb(String xml) {
        xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><root><table name=\"user\"><column name=\"id\" type=\"BIGINT\" ></column><column name=\"platform_id\" type=\"BIGINT\" ></column><column name=\"username\" type=\"VARCHAR\" ></column><column name=\"password\" type=\"VARCHAR\" ></column><column name=\"mobile\" type=\"VARCHAR\" ></column><column name=\"type\" type=\"null\" ></column><column name=\"parent_id\" type=\"BIGINT\" ></column><column name=\"email\" type=\"VARCHAR\" ></column><column name=\"nickname\" type=\"VARCHAR\" ></column><column name=\"created_time\" type=\"null\" ></column><column name=\"updated_time\" type=\"null\" ></column><column name=\"deleted\" type=\"null\" ></column><column name=\"status\" type=\"null\" ></column><column name=\"shop_id\" type=\"BIGINT\" ></column><column name=\"paymentCode\" type=\"VARCHAR\" ></column><column name=\"security_level\" type=\"null\" ></column><column name=\"oldPassword\" type=\"VARCHAR\" ></column><column name=\"auditor\" type=\"VARCHAR\" ></column><column name=\"audit_remark\" type=\"VARCHAR\" ></column><column name=\"quick_type\" type=\"null\" ></column><column name=\"wx_openid\" type=\"VARCHAR\" ></column><column name=\"avatar_pic_src\" type=\"VARCHAR\" ></column><rows><row><cell name=\"id\">1001669</cell><cell name=\"platform_id\">20160627141</cell><cell name=\"username\">wanggen033</cell><cell name=\"password\">c0b540134794156ebe6ed58cedd1ba60</cell><cell name=\"mobile\">13810000033</cell><cell name=\"type\">3</cell><cell name=\"parent_id\">[NULL]</cell><cell name=\"email\">wanggen033@jd.com</cell><cell name=\"nickname\">[NULL]</cell><cell name=\"created_time\">2016-06-27 10:13:43.0</cell><cell name=\"updated_time\">2016-06-27 14:34:33.0</cell><cell name=\"deleted\">1</cell><cell name=\"status\">90</cell><cell name=\"shop_id\">[NULL]</cell><cell name=\"paymentCode\">[NULL]</cell><cell name=\"security_level\">[NULL]</cell><cell name=\"oldPassword\">[NULL]</cell><cell name=\"auditor\"></cell><cell name=\"audit_remark\"></cell><cell name=\"quick_type\">1</cell><cell name=\"wx_openid\">[NULL]</cell><cell name=\"avatar_pic_src\">[NULL]</cell></row><row><cell name=\"id\">1001670</cell><cell name=\"platform_id\">20160627141</cell><cell name=\"username\">admin033@jd.com</cell><cell name=\"password\">c0b540134794156ebe6ed58cedd1ba60</cell><cell name=\"mobile\">13800000033</cell><cell name=\"type\">4</cell><cell name=\"parent_id\">[NULL]</cell><cell name=\"email\">admin033@jd.com</cell><cell name=\"nickname\">[NULL]</cell><cell name=\"created_time\">2016-06-27 14:18:27.0</cell><cell name=\"updated_time\">[NULL]</cell><cell name=\"deleted\">1</cell><cell name=\"status\">20</cell><cell name=\"shop_id\">[NULL]</cell><cell name=\"paymentCode\">[NULL]</cell><cell name=\"security_level\">[NULL]</cell><cell name=\"oldPassword\">[NULL]</cell><cell name=\"auditor\"></cell><cell name=\"audit_remark\"></cell><cell name=\"quick_type\">[NULL]</cell><cell name=\"wx_openid\">[NULL]</cell><cell name=\"avatar_pic_src\">[NULL]</cell></row><row><cell name=\"id\">1001671</cell><cell name=\"platform_id\">20160627141</cell><cell name=\"username\">wanggen033-x1x1</cell><cell name=\"password\">c0b540134794156ebe6ed58cedd1ba60</cell><cell name=\"mobile\">[NULL]</cell><cell name=\"type\">6</cell><cell name=\"parent_id\">[NULL]</cell><cell name=\"email\">[NULL]</cell><cell name=\"nickname\">[NULL]</cell><cell name=\"created_time\">2016-06-27 18:48:51.0</cell><cell name=\"updated_time\">2016-06-27 18:48:51.0</cell><cell name=\"deleted\">1</cell><cell name=\"status\">20</cell><cell name=\"shop_id\">[NULL]</cell><cell name=\"paymentCode\">[NULL]</cell><cell name=\"security_level\">[NULL]</cell><cell name=\"oldPassword\">[NULL]</cell><cell name=\"auditor\"></cell><cell name=\"audit_remark\"></cell><cell name=\"quick_type\">[NULL]</cell><cell name=\"wx_openid\">[NULL]</cell><cell name=\"avatar_pic_src\">[NULL]</cell></row><row><cell name=\"id\">1001693</cell><cell name=\"platform_id\">20160627141</cell><cell name=\"username\">wanggen034</cell><cell name=\"password\">c0b540134794156ebe6ed58cedd1ba60</cell><cell name=\"mobile\">13810000034</cell><cell name=\"type\">2</cell><cell name=\"parent_id\">[NULL]</cell><cell name=\"email\">wanggen034@jd.com</cell><cell name=\"nickname\">[NULL]</cell><cell name=\"created_time\">2016-08-04 17:12:28.0</cell><cell name=\"updated_time\">2016-08-04 17:17:38.0</cell><cell name=\"deleted\">1</cell><cell name=\"status\">50</cell><cell name=\"shop_id\">[NULL]</cell><cell name=\"paymentCode\">[NULL]</cell><cell name=\"security_level\">[NULL]</cell><cell name=\"oldPassword\">[NULL]</cell><cell name=\"auditor\"></cell><cell name=\"audit_remark\"></cell><cell name=\"quick_type\">1</cell><cell name=\"wx_openid\">[NULL]</cell><cell name=\"avatar_pic_src\">[NULL]</cell></row><row><cell name=\"id\">1001718</cell><cell name=\"platform_id\">20160627141</cell><cell name=\"username\">yuki</cell><cell name=\"password\">c0b540134794156ebe6ed58cedd1ba60</cell><cell name=\"mobile\">[NULL]</cell><cell name=\"type\">1</cell><cell name=\"parent_id\">[NULL]</cell><cell name=\"email\">liss2727@163.com</cell><cell name=\"nickname\">[NULL]</cell><cell name=\"created_time\">2016-09-06 15:53:10.0</cell><cell name=\"updated_time\">2016-09-06 15:53:10.0</cell><cell name=\"deleted\">1</cell><cell name=\"status\">10</cell><cell name=\"shop_id\">[NULL]</cell><cell name=\"paymentCode\">[NULL]</cell><cell name=\"security_level\">[NULL]</cell><cell name=\"oldPassword\">[NULL]</cell><cell name=\"auditor\"></cell><cell name=\"audit_remark\"></cell><cell name=\"quick_type\">1</cell><cell name=\"wx_openid\">[NULL]</cell><cell name=\"avatar_pic_src\">[NULL]</cell></row><row><cell name=\"id\">1001720</cell><cell name=\"platform_id\">20160627141</cell><cell name=\"username\">alion</cell><cell name=\"password\">c0b540134794156ebe6ed58cedd1ba60</cell><cell name=\"mobile\">13789876545</cell><cell name=\"type\">1</cell><cell name=\"parent_id\">[NULL]</cell><cell name=\"email\">33@33.com</cell><cell name=\"nickname\">[NULL]</cell><cell name=\"created_time\">2016-09-06 18:23:07.0</cell><cell name=\"updated_time\">2016-09-06 18:28:13.0</cell><cell name=\"deleted\">1</cell><cell name=\"status\">30</cell><cell name=\"shop_id\">[NULL]</cell><cell name=\"paymentCode\">[NULL]</cell><cell name=\"security_level\">[NULL]</cell><cell name=\"oldPassword\">[NULL]</cell><cell name=\"auditor\"></cell><cell name=\"audit_remark\"></cell><cell name=\"quick_type\">1</cell><cell name=\"wx_openid\">[NULL]</cell><cell name=\"avatar_pic_src\">[NULL]</cell></row></rows></table><table name=\"user_extends\"><column name=\"extend_id\" type=\"BIGINT\" ></column><column name=\"platform_id\" type=\"BIGINT\" ></column><column name=\"user_id\" type=\"BIGINT\" ></column><column name=\"company_name\" type=\"VARCHAR\" ></column><column name=\"artificial_person_name\" type=\"VARCHAR\" ></column><column name=\"registered_capital\" type=\"VARCHAR\" ></column><column name=\"business_licence_id\" type=\"VARCHAR\" ></column><column name=\"company_address\" type=\"VARCHAR\" ></column><column name=\"business_scope\" type=\"VARCHAR\" ></column><column name=\"artificial_person_pic_src\" type=\"VARCHAR\" ></column><column name=\"business_licence_pic_src\" type=\"VARCHAR\" ></column><column name=\"company_phone\" type=\"VARCHAR\" ></column><column name=\"organization_id\" type=\"VARCHAR\" ></column><column name=\"organization_pic_src\" type=\"VARCHAR\" ></column><column name=\"tax_man_id\" type=\"VARCHAR\" ></column><column name=\"taxpayer_type\" type=\"VARCHAR\" ></column><column name=\"taxpayer_code\" type=\"VARCHAR\" ></column><column name=\"tax_registration_certificate_pic_src\" type=\"VARCHAR\" ></column><column name=\"taxpayer_certificate_pic_src\" type=\"VARCHAR\" ></column><column name=\"bank_account_name\" type=\"VARCHAR\" ></column><column name=\"bank_account\" type=\"VARCHAR\" ></column><column name=\"bank_name\" type=\"VARCHAR\" ></column><column name=\"bank_branch_joint_line\" type=\"VARCHAR\" ></column><column name=\"bank_branch_is_located\" type=\"VARCHAR\" ></column><column name=\"bank_account_permits_pic_src\" type=\"VARCHAR\" ></column><column name=\"bank_account_type\" type=\"VARCHAR\" ></column><column name=\"bank_code\" type=\"VARCHAR\" ></column><column name=\"artificial_person_pic_back_src\" type=\"VARCHAR\" ></column><column name=\"business_licence_indate\" type=\"null\" ></column><column name=\"seller_frozen_account\" type=\"VARCHAR\" ></column><column name=\"seller_withdraws_cash_account\" type=\"VARCHAR\" ></column><column name=\"buyer_pays_account\" type=\"VARCHAR\" ></column><column name=\"buyer_financing_account\" type=\"VARCHAR\" ></column><column name=\"account_state\" type=\"VARCHAR\" ></column><column name=\"linkman\" type=\"VARCHAR\" ></column><column name=\"register_type\" type=\"TINYINT\" ></column><column name=\"avatar_pic_src\" type=\"VARCHAR\" ></column><rows><row><cell name=\"extend_id\">1001575</cell><cell name=\"platform_id\">20160627141</cell><cell name=\"user_id\">1001669</cell><cell name=\"company_name\">中华人民共和国</cell><cell name=\"artificial_person_name\">毛泽东</cell><cell name=\"registered_capital\">1</cell><cell name=\"business_licence_id\">999999999999999</cell><cell name=\"company_address\">北京市,市辖区,西城区</cell><cell name=\"business_scope\">国家</cell><cell name=\"artificial_person_pic_src\">http://192.168.150.52/fw.jd.com/a77c5860-703c-4b0e-94d8-3b46b2218c00.png</cell><cell name=\"business_licence_pic_src\">http://192.168.150.52/fw.jd.com/063ba805-f301-49cf-abd7-fed8aa431758.png</cell><cell name=\"company_phone\">010-99999999</cell><cell name=\"organization_id\">9999999999</cell><cell name=\"organization_pic_src\">http://192.168.150.52/fw.jd.com/3acec26e-dbe4-446b-ae79-82b4dafbaffe.png</cell><cell name=\"tax_man_id\">99999999999999999999</cell><cell name=\"taxpayer_type\">[NULL]</cell><cell name=\"taxpayer_code\">[NULL]</cell><cell name=\"tax_registration_certificate_pic_src\">http://192.168.150.52/fw.jd.com/ed95eaea-4794-4a30-8782-434bd4d94290.png</cell><cell name=\"taxpayer_certificate_pic_src\">http://192.168.150.52/fw.jd.com/8e620b8c-e6c9-4671-a042-e6a367fce764.png</cell><cell name=\"bank_account_name\">毛泽东</cell><cell name=\"bank_account\">999999999999999999999999999999</cell><cell name=\"bank_name\">中国银行中南海支行</cell><cell name=\"bank_branch_joint_line\">[NULL]</cell><cell name=\"bank_branch_is_located\">北京市,市辖区,西城区</cell><cell name=\"bank_account_permits_pic_src\">[NULL]</cell><cell name=\"bank_account_type\">C</cell><cell name=\"bank_code\">BOC</cell><cell name=\"artificial_person_pic_back_src\">http://192.168.150.52/fw.jd.com/437b2cbc-7ec7-442b-a721-3d6ee1a4adac.png</cell><cell name=\"business_licence_indate\">2016-06-27 00:00:00.0</cell><cell name=\"seller_frozen_account\">[NULL]</cell><cell name=\"seller_withdraws_cash_account\">[NULL]</cell><cell name=\"buyer_pays_account\">[NULL]</cell><cell name=\"buyer_financing_account\">[NULL]</cell><cell name=\"account_state\">[NULL]</cell><cell name=\"linkman\">毛泽东</cell><cell name=\"register_type\">1</cell><cell name=\"avatar_pic_src\">[NULL]</cell></row><row><cell name=\"extend_id\">1001591</cell><cell name=\"platform_id\">20160627141</cell><cell name=\"user_id\">1001693</cell><cell name=\"company_name\">你好</cell><cell name=\"artificial_person_name\">你好</cell><cell name=\"registered_capital\">1</cell><cell name=\"business_licence_id\">000</cell><cell name=\"company_address\">北京市,市辖区,东城区</cell><cell name=\"business_scope\">你好</cell><cell name=\"artificial_person_pic_src\">http://192.168.150.52/fw.jd.com/13a615ea-8676-4d85-9ed3-30e70fabf5fc.png</cell><cell name=\"business_licence_pic_src\">http://192.168.150.52/fw.jd.com/af6ab46b-0e3a-46cc-913a-0eac486a2716.png</cell><cell name=\"company_phone\">000-00000000</cell><cell name=\"organization_id\">[NULL]</cell><cell name=\"organization_pic_src\">[NULL]</cell><cell name=\"tax_man_id\">[NULL]</cell><cell name=\"taxpayer_type\">[NULL]</cell><cell name=\"taxpayer_code\">[NULL]</cell><cell name=\"tax_registration_certificate_pic_src\">[NULL]</cell><cell name=\"taxpayer_certificate_pic_src\">[NULL]</cell><cell name=\"bank_account_name\">你好</cell><cell name=\"bank_account\">000</cell><cell name=\"bank_name\">xx银行xx支行</cell><cell name=\"bank_branch_joint_line\">000000000000</cell><cell name=\"bank_branch_is_located\">北京市,市辖区,东城区</cell><cell name=\"bank_account_permits_pic_src\">http://192.168.150.52/fw.jd.com/3dc6785c-ed97-487c-9d3c-54b93c4a6354.png</cell><cell name=\"bank_account_type\">[NULL]</cell><cell name=\"bank_code\">[NULL]</cell><cell name=\"artificial_person_pic_back_src\">http://192.168.150.52/fw.jd.com/f16c7f63-202d-4362-835c-9641e28f548c.png</cell><cell name=\"business_licence_indate\">1970-01-01 08:00:00.0</cell><cell name=\"seller_frozen_account\">[NULL]</cell><cell name=\"seller_withdraws_cash_account\">[NULL]</cell><cell name=\"buyer_pays_account\">[NULL]</cell><cell name=\"buyer_financing_account\">[NULL]</cell><cell name=\"account_state\">[NULL]</cell><cell name=\"linkman\">你好</cell><cell name=\"register_type\">[NULL]</cell><cell name=\"avatar_pic_src\">[NULL]</cell></row><row><cell name=\"extend_id\">1001611</cell><cell name=\"platform_id\">20160627141</cell><cell name=\"user_id\">1001718</cell><cell name=\"company_name\">[NULL]</cell><cell name=\"artificial_person_name\">[NULL]</cell><cell name=\"registered_capital\">[NULL]</cell><cell name=\"business_licence_id\">[NULL]</cell><cell name=\"company_address\">[NULL]</cell><cell name=\"business_scope\">[NULL]</cell><cell name=\"artificial_person_pic_src\">[NULL]</cell><cell name=\"business_licence_pic_src\">[NULL]</cell><cell name=\"company_phone\">[NULL]</cell><cell name=\"organization_id\">[NULL]</cell><cell name=\"organization_pic_src\">[NULL]</cell><cell name=\"tax_man_id\">[NULL]</cell><cell name=\"taxpayer_type\">[NULL]</cell><cell name=\"taxpayer_code\">[NULL]</cell><cell name=\"tax_registration_certificate_pic_src\">[NULL]</cell><cell name=\"taxpayer_certificate_pic_src\">[NULL]</cell><cell name=\"bank_account_name\">[NULL]</cell><cell name=\"bank_account\">[NULL]</cell><cell name=\"bank_name\">[NULL]</cell><cell name=\"bank_branch_joint_line\">[NULL]</cell><cell name=\"bank_branch_is_located\">[NULL]</cell><cell name=\"bank_account_permits_pic_src\">[NULL]</cell><cell name=\"bank_account_type\">[NULL]</cell><cell name=\"bank_code\">[NULL]</cell><cell name=\"artificial_person_pic_back_src\">[NULL]</cell><cell name=\"business_licence_indate\">[NULL]</cell><cell name=\"seller_frozen_account\">[NULL]</cell><cell name=\"seller_withdraws_cash_account\">[NULL]</cell><cell name=\"buyer_pays_account\">[NULL]</cell><cell name=\"buyer_financing_account\">[NULL]</cell><cell name=\"account_state\">[NULL]</cell><cell name=\"linkman\">[NULL]</cell><cell name=\"register_type\">[NULL]</cell><cell name=\"avatar_pic_src\">[NULL]</cell></row><row><cell name=\"extend_id\">1001613</cell><cell name=\"platform_id\">20160627141</cell><cell name=\"user_id\">1001720</cell><cell name=\"company_name\">alion</cell><cell name=\"artificial_person_name\">1234</cell><cell name=\"registered_capital\">200000</cell><cell name=\"business_licence_id\">234123</cell><cell name=\"company_address\">北京市,市辖区,东城区</cell><cell name=\"business_scope\">423435</cell><cell name=\"artificial_person_pic_src\">http://192.168.150.52/fw.jd.com/e474b589-26d0-4bda-85c0-1a4a59709049.jpg</cell><cell name=\"business_licence_pic_src\">http://192.168.150.52/fw.jd.com/f23245de-941f-4a95-9af5-79442438d0ed.jpg</cell><cell name=\"company_phone\">010-34567543</cell><cell name=\"organization_id\">[NULL]</cell><cell name=\"organization_pic_src\">[NULL]</cell><cell name=\"tax_man_id\">[NULL]</cell><cell name=\"taxpayer_type\">[NULL]</cell><cell name=\"taxpayer_code\">[NULL]</cell><cell name=\"tax_registration_certificate_pic_src\">[NULL]</cell><cell name=\"taxpayer_certificate_pic_src\">[NULL]</cell><cell name=\"bank_account_name\">alion</cell><cell name=\"bank_account\">234532452345234</cell><cell name=\"bank_name\">alion银行alion支行</cell><cell name=\"bank_branch_joint_line\">243523452435</cell><cell name=\"bank_branch_is_located\">北京市,市辖区,西城区</cell><cell name=\"bank_account_permits_pic_src\">http://192.168.150.52/fw.jd.com/07bfff37-d5d1-42bf-a83a-ca10eb3338c2.jpg</cell><cell name=\"bank_account_type\">[NULL]</cell><cell name=\"bank_code\">[NULL]</cell><cell name=\"artificial_person_pic_back_src\">http://192.168.150.52/fw.jd.com/fdf7aab1-fc3a-4762-9b5d-2a22050f44a5.jpg</cell><cell name=\"business_licence_indate\">2018-11-08 00:00:00.0</cell><cell name=\"seller_frozen_account\">[NULL]</cell><cell name=\"seller_withdraws_cash_account\">[NULL]</cell><cell name=\"buyer_pays_account\">[NULL]</cell><cell name=\"buyer_financing_account\">[NULL]</cell><cell name=\"account_state\">[NULL]</cell><cell name=\"linkman\">235234</cell><cell name=\"register_type\">[NULL]</cell><cell name=\"avatar_pic_src\">[NULL]</cell></row></rows></table></root>";
        try {
            Document doc = DocumentHelper.parseText(xml);
            Element rootElement = doc.getRootElement();
            Iterator iterator = rootElement.elementIterator();
            while(iterator.hasNext()) {
                Element tableElement = (Element)iterator.next();
                String tableName = tableElement.attribute("name").getText();
                DataTable dataTable = new DataTable();
                dataTable.setName(tableName);
                Element rowsElement = tableElement.element("rows");
                Iterator rowsIte = rowsElement.elementIterator();
                while(rowsIte.hasNext()) {
                    DataRow row = new DataRow();
                    dataTable.addRow(row);
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    private void clearImportedData() {



    }
}
