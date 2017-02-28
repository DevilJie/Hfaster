package hsj.czy.mybatisframe.core;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;

public class ChBatisSpringFactoryBean extends SqlSessionFactoryBean {

    protected String dialect;

    public ChBatisSpringFactoryBean(){
    }

    @Override
    public void setMapperLocations(Resource[] mapperLocations) {
        String baseSqlMap = "";
        // default use mysql database
        if (dialect == null) {
            baseSqlMap = "/hsj/czy/mybatisframe/mapper/sqlmap/mysql/base.sqlmap.xml";
        } else {
            if (dialect.equals("mysql")) {
                baseSqlMap = "/hsj/czy/mybatisframe/mapper/sqlmap/mysql/base.sqlmap.xml";
            } else if (dialect.equals("oracle")) {
                baseSqlMap = "/hsj/czy/mybatisframe/mapper/sqlmap/oracle/base.sqlmap.xml";
            } else {
                baseSqlMap = "/hsj/czy/mybatisframe/mapper/sqlmap/mysql/base.sqlmap.xml";
            }
        }
        Resource r = new InputStreamResource(ChBatisSpringFactoryBean.class.getResourceAsStream(baseSqlMap));
        Resource[] mappers = new Resource[mapperLocations.length + 1];
        mappers[0] = r;
        int i = 1;
        for (Resource resource : mapperLocations) {
            mappers[i] = resource;
            i++;
        }
        super.setMapperLocations(mappers);
    }

    @Override
    public void setConfigLocation(Resource configLocation) {
        // TODO Auto-generated method stub
        super.setConfigLocation(configLocation);
    }
}
