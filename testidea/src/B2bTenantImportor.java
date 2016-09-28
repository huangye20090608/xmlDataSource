import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * Created by zhangzhonghua on 2016/9/12.
 */
public class B2bTenantImportor {

    public static Long sourcePlatformId = 20160627141L;   //源租户ID
    public static Long targetPlatformId = 33848913488L;   //新租户ID

    public HashMap<String, DataTable> tableCache = new HashMap<String, DataTable>();
    public List<DataTable> dataTables = new ArrayList<DataTable>();

    public HashMap<String, HashMap<Long, String>> sourceDataIdCache = new HashMap<String, HashMap<Long, String>>();
    public HashMap<String, HashMap<String, Long>> newDataIdCache = new HashMap<String, HashMap<String, Long>>();

    private String xmlPath = "D:\\tableDef.xml";

    public static void main(String[] args) {
        B2bTenantImportor imp = new B2bTenantImportor();
        imp.loadTableDefs();

        Connection conn = null;
        try {
            conn = ConnectionUtils.openConnection();

            long time = System.currentTimeMillis();

            //imp.clearTestData(conn);

            HashMap<String, Object> context = new HashMap<String, Object>();
            context.put("platformId", targetPlatformId);
            context.put("env", "b2b");
            context.put("title", "测试商城");
            context.put("username", "testuser");
            context.put("email", "testuser@b2btest.com");
            context.put("password", "pppppppp");
            context.put("beginTime", new Date());
            context.put("endTime", new Date());
            context.put("mobile", "13581700339");

            imp.importAdmin(context, conn);
            imp.importMallDomainInfo(context, conn);
            imp.importPropertiesConfig(context, conn);
            for(DataTable dataTable : imp.dataTables) {
                imp.importDatas(dataTable, context, conn);
            }

            conn.commit();

            System.out.println("耗时 " + (System.currentTimeMillis() - time) + " ms.");
        } catch (SQLException e) {
            e.printStackTrace();
            ConnectionUtils.closeConnection(conn);
        } finally {
            ConnectionUtils.closeConnection(conn);
        }
    }

    private void clearTestData(Connection conn) throws SQLException {
        conn.setAutoCommit(false);
        Statement stmt = conn.createStatement();
        for(DataTable dataTable : this.dataTables) {

            String platformIdCol = dataTable.getPlatformIdCol();
            String deleteSql = "delete from " + dataTable.getName() + " where " + platformIdCol + " = " + targetPlatformId;
            System.out.println(deleteSql);
            stmt.executeUpdate(deleteSql);
        }
        stmt.executeUpdate("delete from properties_config where platform_id = " + targetPlatformId);
        stmt.executeUpdate("delete from mall_domain_info where platform_id = " + targetPlatformId);
    }

    private void importAdmin(HashMap<String, Object> context, Connection conn) throws SQLException {
        conn.setAutoCommit(false);

        String adminSql = "select * from user where platform_id = " + sourcePlatformId + " and type = 4";
        PreparedStatement adminPstmt = conn.prepareStatement(adminSql);

        String insAdminSql = "insert into user(platform_id, username, password, mobile, type, parent_id, email, nickname, created_time, updated_time, deleted, status, shop_id, paymentCode, security_level, oldPassword, auditor, audit_remark, quick_type, wx_openid, avatar_pic_src)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        ResultSet adminRs = adminPstmt.executeQuery();
        if(adminRs.next()) {
            Long oldUserId = adminRs.getLong(1);
            String rowIndex = UUID.randomUUID().toString();
            String cacheKey = "user.id";
            HashMap<Long, String> tableDataCache = sourceDataIdCache.get(cacheKey);
            if(tableDataCache == null) {
                tableDataCache = new HashMap<Long, String>();
                sourceDataIdCache.put(cacheKey, tableDataCache);
            }
            tableDataCache.put(oldUserId, rowIndex);

            Long platformId = (Long)context.get("platformId");
            String adminUsername = (String)context.get("username");
            String adminPassword = (String)context.get("password");
            String adminMobile = (String)context.get("mobile");
            Long type = 4L;
            String parentId = null;
            String email = (String)context.get("email");

            PreparedStatement insAdminPstmt = conn.prepareStatement(insAdminSql, Statement.RETURN_GENERATED_KEYS);
            insAdminPstmt.setObject(1, platformId);
            insAdminPstmt.setObject(2, adminUsername);
            insAdminPstmt.setObject(3, adminPassword);
            insAdminPstmt.setObject(4, adminMobile);
            insAdminPstmt.setObject(5, type);
            insAdminPstmt.setObject(6, parentId);
            insAdminPstmt.setObject(7, email);
            insAdminPstmt.setObject(8, adminUsername);
            insAdminPstmt.setObject(9, new Date());
            insAdminPstmt.setObject(10, new Date());
            insAdminPstmt.setObject(11, adminRs.getObject("deleted"));
            insAdminPstmt.setObject(12, adminRs.getObject("status"));
            insAdminPstmt.setObject(13, adminRs.getObject("shop_id"));
            insAdminPstmt.setObject(14, adminRs.getObject("paymentCode"));
            insAdminPstmt.setObject(15, adminRs.getObject("security_level"));
            insAdminPstmt.setObject(16, adminRs.getObject("oldPassword"));
            insAdminPstmt.setObject(17, adminRs.getObject("auditor"));
            insAdminPstmt.setObject(18, adminRs.getObject("audit_remark"));
            insAdminPstmt.setObject(19, adminRs.getObject("quick_type"));
            insAdminPstmt.setObject(20, adminRs.getObject("wx_openid"));
            insAdminPstmt.setObject(21, adminRs.getObject("avatar_pic_src"));

            insAdminPstmt.executeUpdate();
            ResultSet autoKeyRs = insAdminPstmt.getGeneratedKeys();
            if(autoKeyRs.next()) {
                Long newAdminId = autoKeyRs.getLong(1);

                HashMap<String, Long> targetTableDataCache = newDataIdCache.get(cacheKey);
                if(targetTableDataCache == null) {
                    targetTableDataCache = new HashMap<String, Long>();
                    newDataIdCache.put(cacheKey, targetTableDataCache);
                }
                targetTableDataCache.put(rowIndex, newAdminId);
            }
        }
    }

    private void importPropertiesConfig(HashMap<String, Object> context, Connection conn) throws SQLException{
        conn.setAutoCommit(false);

        String propTempSql = "select * from properties_template";
        PreparedStatement propTempPstmt = conn.prepareStatement(propTempSql);

        String insPropConfSql = "insert into properties_config(platform_id, group_id, property_name, property_value, property_status)values(?,?,?,?,?)";

        Long platformId = (Long)context.get("platformId");
        String title = (String)context.get("title");

        ResultSet propTempRs = propTempPstmt.executeQuery();
        while(propTempRs.next()) {
            PreparedStatement insAdminPstmt = conn.prepareStatement(insPropConfSql);
            insAdminPstmt.setObject(1, platformId);
            insAdminPstmt.setObject(2, propTempRs.getObject("group_id"));
            insAdminPstmt.setObject(3, propTempRs.getObject("property_name"));
            insAdminPstmt.setObject(4, propTempRs.getObject("property_value").toString().replaceAll("\\{#name#\\}", title).replaceAll("\\{#NAME#\\}", title).replaceAll("\\{#Name#\\}", title));
            insAdminPstmt.setObject(5, propTempRs.getObject("property_status"));

            insAdminPstmt.executeUpdate();
        }
    }

    private void importMallDomainInfo(HashMap<String, Object> context, Connection conn) throws SQLException {
        conn.setAutoCommit(false);

        String insMallDomainInfoSql = "insert into mall_domain_info(platform_id, domain, domain_type, domain_group_id, is_https, created, modified, description)values(?,?,?,1,0,now(),now(),'')";
        PreparedStatement insMallDomainInfoPstmt = conn.prepareStatement(insMallDomainInfoSql);
        insMallDomainInfoPstmt.setObject(1, context.get("platformId"));
        String mainDomain = "www." + context.get("mobile").toString() + "." + context.get("env").toString() + ".jcloudec.com";
        insMallDomainInfoPstmt.setObject(2, mainDomain);
        insMallDomainInfoPstmt.setObject(3, 1);
        insMallDomainInfoPstmt.executeUpdate();

        context.put("mainDomainUrl", "http://" + mainDomain);

        insMallDomainInfoPstmt = conn.prepareStatement(insMallDomainInfoSql);
        insMallDomainInfoPstmt.setObject(1, context.get("platformId"));
        insMallDomainInfoPstmt.setObject(2, "buyer." + context.get("mobile").toString() + "." + context.get("env").toString() + ".jcloudec.com");
        insMallDomainInfoPstmt.setObject(3, 2);
        insMallDomainInfoPstmt.executeUpdate();

        insMallDomainInfoPstmt = conn.prepareStatement(insMallDomainInfoSql);
        insMallDomainInfoPstmt.setObject(1, context.get("platformId"));
        insMallDomainInfoPstmt.setObject(2, "shop." + context.get("mobile").toString() + "." + context.get("env").toString() + ".jcloudec.com");
        insMallDomainInfoPstmt.setObject(3, 3);
        insMallDomainInfoPstmt.executeUpdate();

        insMallDomainInfoPstmt = conn.prepareStatement(insMallDomainInfoSql);
        insMallDomainInfoPstmt.setObject(1, context.get("platformId"));
        insMallDomainInfoPstmt.setObject(2, "platform." + context.get("mobile").toString() + "." + context.get("env").toString() + ".jcloudec.com");
        insMallDomainInfoPstmt.setObject(3, 4);
        insMallDomainInfoPstmt.executeUpdate();

        insMallDomainInfoPstmt = conn.prepareStatement(insMallDomainInfoSql);
        insMallDomainInfoPstmt.setObject(1, context.get("platformId"));
        insMallDomainInfoPstmt.setObject(2, "m." + context.get("mobile").toString() + "." + context.get("env").toString() + ".jcloudec.com");
        insMallDomainInfoPstmt.setObject(3, 8);
        insMallDomainInfoPstmt.executeUpdate();

        insMallDomainInfoPstmt = conn.prepareStatement(insMallDomainInfoSql);
        insMallDomainInfoPstmt.setObject(1, context.get("platformId"));
        insMallDomainInfoPstmt.setObject(2, "store." + context.get("mobile").toString() + "." + context.get("env").toString() + ".jcloudec.com");
        insMallDomainInfoPstmt.setObject(3, 9);
        insMallDomainInfoPstmt.executeUpdate();

        insMallDomainInfoPstmt = conn.prepareStatement(insMallDomainInfoSql);
        insMallDomainInfoPstmt.setObject(1, context.get("platformId"));
        insMallDomainInfoPstmt.setObject(2, "passport." + context.get("mobile").toString() + "." + context.get("env").toString() + ".jcloudec.com");
        insMallDomainInfoPstmt.setObject(3, 10);
        insMallDomainInfoPstmt.executeUpdate();
    }

    private void importDatas(DataTable dataTable, HashMap<String, Object> context, Connection conn) throws SQLException {
        conn.setAutoCommit(false);

        String selectSql = "select * from " + dataTable.getName() + " where " + dataTable.getPlatformIdCol() + " = " + sourcePlatformId;
        if(dataTable.getCondition() != null) {
            selectSql += " and " + dataTable.getCondition();
        }

        if(dataTable.getPkColumn() != null) {
            selectSql += " order by " + dataTable.getPkColumn().getName() + " asc";
        }
        PreparedStatement pstmt = conn.prepareStatement(selectSql);
        ResultSet rs = pstmt.executeQuery();
        while(rs.next()) {
            String rowIndex = UUID.randomUUID().toString();
            List params = new ArrayList();
            String insSql = "insert into " + dataTable.getName() + "(";
            int c = 0;
            for(DataColumn col : dataTable.getColumns()) {
                if(col.getFunc() != null && col.getFunc().equals("$auto")) {
                    continue;
                }
                if(c > 0) {
                    insSql += ", ";
                }
                String colName = col.getName();
                insSql += colName;
                c++;
            }
            insSql += ")VALUES(";
            c=0;
            String autoColName = null;
            for(DataColumn col : dataTable.getColumns()) {
                if(col.getFunc() != null && col.getFunc().equals("$auto")) {
                    String colName = col.getName();
                    autoColName = colName;
                    Long sourceId = rs.getLong(colName);
                    String cacheKey = dataTable.getName() + "." + colName;
                    HashMap<Long, String> tableDataCache = sourceDataIdCache.get(cacheKey);
                    if(tableDataCache == null) {
                        tableDataCache = new HashMap<Long, String>();
                        sourceDataIdCache.put(cacheKey, tableDataCache);
                    }
                    tableDataCache.put(sourceId, rowIndex);
                    continue;
                }
                if(c > 0) {
                    insSql += ", ";
                }
                insSql += "?";

                Object value = null;

                if(col.getDataFunc() != null && col.getDataFunc().getFuncType().equals("ctx")) {
                    String ctxAttr = col.getDataFunc().getParams().get(0);
                    value = context.get(ctxAttr);
                }else if(col.getDataFunc() != null && col.getDataFunc().getFuncType().equals("null")) {
                    value = null;
                }else if(col.getRef() != null){
                    String cacheKey = col.getRef().trim();
                    HashMap<Long, String> sourceTableDataCache = sourceDataIdCache.get(cacheKey);
                    if(sourceTableDataCache == null) {
                        if(col.getDefaultValue() != null) {
                            value = col.getDefaultValue();
                        }else{
                            throw new RuntimeException("Data Error. Ref ["+ cacheKey + "] cache not exists.");
                        }
                    }
                    HashMap<String, Long> newTableDataCache = newDataIdCache.get(cacheKey);
                    if(newTableDataCache == null) {
                        if(col.getDefaultValue() != null) {
                            value = col.getDefaultValue();
                        }else{
                            throw new RuntimeException("Data Error. Ref ["+ cacheKey + "] cache not exists.");
                        }
                    }
                    String colName = col.getName();
                    Long sourceId = rs.getLong(colName);
                    String rowColIndex = sourceTableDataCache.get(sourceId);
                    if(newTableDataCache == null && col.getDefaultValue() != null) {
                        value = Long.parseLong(col.getDefaultValue());
                    }else{
                        if(newTableDataCache.get(rowColIndex) == null && col.getDefaultValue() != null) {
                            value = Long.parseLong(col.getDefaultValue());
                        }else{
                            value = newTableDataCache.get(rowColIndex);
                        }
                    }
                }else if(col.getDataFunc() != null && col.getDataFunc().getFuncType().equals("replace")){
                    String colName = col.getName();
                    String srcStr = col.getDataFunc().getParams().get(0);
                    String tarSrc = col.getDataFunc().getParams().get(1);
                    if(tarSrc.startsWith("$ctx")) {
                        int leftBracketsPos = tarSrc.indexOf("(");
                        int rightBracketsPos = tarSrc.lastIndexOf(")");
                        String tarParam = tarSrc.substring(leftBracketsPos+1, rightBracketsPos);
                        tarSrc = (String)context.get(tarParam);
                    }
                    String src = rs.getString(colName);
                    if(src != null) {
                        value = src.replaceAll(srcStr, tarSrc);
                    }
                }else if(col.getDataFunc() != null && col.getDataFunc().getFuncType().equals("nowtime")){
                    value = new Date();
                }else{
                    String colName = col.getName();
                    value = rs.getObject(colName);
                }

                params.add(value);
                c++;
            }
            insSql += ");";

            System.out.println(insSql);

            PreparedStatement insPstmt = conn.prepareStatement(insSql, Statement.RETURN_GENERATED_KEYS);
            for(int j=1; j<=params.size(); j++) {
                insPstmt.setObject(j, params.get(j-1));
            }
            insPstmt.executeUpdate();
            ResultSet autoKeyRs = insPstmt.getGeneratedKeys();
            if(autoKeyRs.next()) {
                Long newId = autoKeyRs.getLong(1);
                String colName = autoColName;
                String cacheKey = dataTable.getName() + "." + colName;
                HashMap<String, Long> tableDataCache = newDataIdCache.get(cacheKey);
                if(tableDataCache == null) {
                    tableDataCache = new HashMap<String, Long>();
                    newDataIdCache.put(cacheKey, tableDataCache);
                }
                tableDataCache.put(rowIndex, newId);
            }
        }

        compensateData(conn);
    }

    private void compensateData(Connection conn) {
//
    }

    private void loadTableDefs() {
        try {
            SAXReader reader = new SAXReader();
            Document doc = reader.read(new File(xmlPath));
            Element rootElement = doc.getRootElement();
            Iterator iterator = rootElement.elementIterator();
            while(iterator.hasNext()) {
                Element tableElement = (Element)iterator.next();
                String tableName = tableElement.attribute("name").getText();
                DataTable dataTable = new DataTable();
                dataTable.setName(tableName);

                if(tableElement.attribute("platformIdCol") != null) {
                    dataTable.setPlatformIdCol(tableElement.attribute("platformIdCol").getText());
                }

                if(tableElement.attribute("condition") != null) {
                    dataTable.setCondition(tableElement.attribute("condition").getText());
                }

                List<Element> columns= tableElement.elements("column");
                for(Element colEle : columns) {
                    DataColumn dataColumn = new DataColumn();
                    dataColumn.setName(colEle.attribute("name").getText());
                    if(colEle.attribute("ref") != null) {
                        dataColumn.setRef(colEle.attribute("ref").getText());
                    }
                    if(colEle.attribute("func") != null) {
                        dataColumn.setFunc(colEle.attribute("func").getText());
                    }
                    if(colEle.attribute("defaultValue") != null) {
                        String defaultValue = colEle.attribute("defaultValue").getText();
                        dataColumn.setDefaultValue(defaultValue);
                    }
                    if(colEle.attribute("pk") != null) {
                        String pk = colEle.attribute("pk").getText();
                        if(pk != null && pk.equals("true")) {
                            dataColumn.setPk(true);
                        }
                        dataTable.setPkColumn(dataColumn);
                    }
                    dataTable.addColumn(dataColumn);
                }
                this.tableCache.put(tableName, dataTable);
                this.dataTables.add(dataTable);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

}
