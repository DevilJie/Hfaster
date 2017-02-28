package hsj.czy.mybatisframe.core;

import hsj.czy.mybatisframe.listener.ChBatisFrameConfigListener;

import org.springframework.core.io.Resource;

public class ChBatisSpringMySqlFactoryBean extends ChBatisSpringFactoryBean {

    private String table_scan;

    private char   separator;

    public ChBatisSpringMySqlFactoryBean(){
        this.dialect = "mysql";
        setMapperLocations(new Resource[] {});
    }

    public String getTable_scan() {
        return table_scan;
    }

    public void setTable_scan(String table_scan) {
        this.table_scan = table_scan;
    }

    public char getSeparator() {
        return separator;
    }

    public void setSeparator(char separator) {
        this.separator = separator;
        ChBatisFrameConfigListener.separator = separator;
        ChBatisFrameConfigListener.f = "`";
        if (this.table_scan != null) {
            String[] packages = table_scan.split(",");
            TableInit.initTable(packages);
        }
    }

}
