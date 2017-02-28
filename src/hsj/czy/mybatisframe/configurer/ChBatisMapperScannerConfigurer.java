package hsj.czy.mybatisframe.configurer;

import org.mybatis.spring.mapper.MapperScannerConfigurer;

public class ChBatisMapperScannerConfigurer extends MapperScannerConfigurer {

    public static String dialect;

    public void setDialect(String dialect) {
        ChBatisMapperScannerConfigurer.dialect = dialect;
    }

    public ChBatisMapperScannerConfigurer(){
        setBasePackage();
    }

    public void setBasePackage() {
        String basePackage = "hsj.czy.mybatisframe.mapper";
        super.setBasePackage(basePackage);
    }
}
