package cn.com.xlmx.reduce.service.db;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.pagehelper.PageHelper;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author luxiaohang
 * @Title:
 * @Description:Druid配置的DataSource类
 * 该类被Spring容器进行管理，实现了接口Environment，并重写方法setEnvironment  可以在工程启动时加载
 * 并获取到配置文件中的信息。
 * @date 2019/04/27/14:18
 **/
@Configuration
@MapperScan(basePackages = DataSourceMaster.PACKAGE, sqlSessionFactoryRef = "masterSqlSessionFactory")
public class DataSourceMaster implements EnvironmentAware {

    Logger log = LoggerFactory.getLogger(DataSourceMaster.class);
    static final String PACKAGE = "cn.com.xlmx.reduce.service.db.mapper";
    static final String MAPPER_LOCATION = "classpath:cn/com/xlmx/reduce/service/db/mapper/*.xml";


    private String url;
    private String driverClassName;
    private String userName;
    private String passWord;
    private String initialSize;
    private String minIdle;
    private String maxWait;
    private String maxActive;
    private String minEvictableIdleTimeMillis;
    private String dialect;

    @Override
    public void setEnvironment(Environment environment) {
        log.info("加载配置文件信息");
        this.url = environment.getProperty("spring.datasource.url");
        this.driverClassName = environment.getProperty("spring.datasource.driver-class-name");
        this.userName = environment.getProperty("spring.datasource.username");
        this.passWord = environment.getProperty("spring.datasource.password");
        this.initialSize = environment.getProperty("spring.datasource.initialSize");
        this.minIdle = environment.getProperty("spring.datasource.minIdle");
        this.maxWait = environment.getProperty("spring.datasource.maxWait");
        this.maxActive = environment.getProperty("spring.datasource.maxActive");
        this.minEvictableIdleTimeMillis = environment.getProperty("spring.datasource.minEvictableIdleTimeMillis");
        this.dialect = environment.getProperty("spring.datasource.dialect");
    }

    @Bean(name = "masterDataSource")
    @Primary
    public DataSource dataSource(){
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(url);
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUsername(userName);
        dataSource.setPassword(passWord);
        dataSource.setInitialSize(Integer.parseInt(initialSize));
        dataSource.setMinIdle(Integer.parseInt(minIdle));
        dataSource.setMaxWait(Integer.parseInt(maxWait));
        dataSource.setMaxActive(Integer.parseInt(maxActive));
        dataSource.setMinEvictableIdleTimeMillis(Long.parseLong(minEvictableIdleTimeMillis));
        try{
            dataSource.setFilters("stat,wall");
            log.info("加载数据源完成。。。");
        }catch(SQLException e){
            e.printStackTrace();
        }

        return dataSource;
    }


    @Bean(name = "masterSqlSessionFactory")
    @Primary
    public SqlSessionFactory clusterSqlSessionFactory(@Qualifier("masterDataSource") DataSource masterDataSource)
    throws Exception{
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(masterDataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(DataSourceMaster.MAPPER_LOCATION));

        /**
         *  分页查询
         **/
        PageHelper pageHelper = new PageHelper();
        Properties p = new Properties();
        p.setProperty("offsetAsPageNum","true");
        p.setProperty("rowBoundsWithCount","true");
        p.setProperty("reasonable","true");
        p.setProperty("dialect",dialect);
        pageHelper.setProperties(p);

        //添加分页
        sessionFactory.setPlugins(new Interceptor[] {pageHelper});
        return sessionFactory.getObject();

    }
}
