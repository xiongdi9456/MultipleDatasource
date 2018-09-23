package com.sheldon.multipledatasource.config;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.config.ConfigTools;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: Sheldon
 * @Date: 2018/09/23 21:57
 */

@Configuration
@MapperScan(basePackages = MasterDBConfig.PACKAGE,sqlSessionFactoryRef = "masterSqlSessionFactory")
public class MasterDBConfig {

    ///////////////////////////////////////////////////////
    ///////////
    ///////////主数据库配置
    ///////////
    ///////////////////////////////////////////////////////
    //master dao 所在的包
    public static final String PACKAGE = "com.sheldon.multipledatasource.dao.master";
    //mapper所在目录
    private static final String MAPPER_LOCATION = "classpath:mapper/master/*.xml";
    @Value("${master.datasource.driver-class-name}")
    private String driverClassName;
    @Value("${master.datasource.url}")
    private String dbUrl;
    @Value("${master.datasource.username}")
    private String username;
    @Value("${master.datasource.password}")
    private String password;
    @Value("${master.datasource.publickey}")
    private String dbPublicKey;

    ///////////////////////////////////////////////////////
    ///////////
    ///////////共通部分
    ///////////
    ///////////////////////////////////////////////////////
    @Value("${spring.datasource.druid.initial-size:#{null}}")
    private Integer initialSize;
    @Value("${spring.datasource.druid.min-idle:#{null}}")
    private Integer minIdle;
    @Value("${spring.datasource.druid.max-active:#{null}}")
    private Integer maxActive;
    @Value("${spring.datasource.druid.max-wait:#{null}}")
    private Integer maxWait;
    @Value("${spring.datasource.druid.time-between-eviction-runs-millis:#{null}}")
    private Integer timeBetweenEvictionRunsMillis;
    @Value("${spring.datasource.druid.min-evictable-idle-time-millis:#{null}}")
    private Integer minEvictableIdleTimeMillis;
    @Value("${spring.datasource.druid.validation-query:#{null}}")
    private String validationQuery;
    @Value("${spring.datasource.druid.test-while-idle:#{null}}")
    private Boolean testWhileIdle;
    @Value("${spring.datasource.druid.test-on-borrow:#{null}}")
    private Boolean testOnBorrow;
    @Value("${spring.datasource.druid.test-on-return:#{null}}")
    private Boolean testOnReturn;
    @Value("${spring.datasource.druid.pool-prepared-statements:#{null}}")
    private Boolean poolPreparedStatements;
    @Value("${spring.datasource.druid.max-pool-prepared-statement-per-connection-size:#{null}}")
    private Integer maxPoolPreparedStatementPerConnectionSize;
    @Value("${spring.datasource.druid.filters:#{null}}")
    private String filters;
    @Value("${spring.datasource.druid.connection-properties:#{null}}")
    private String connectionProperties;

    @Bean(name = "masterDataSource")
    @Primary
    public DataSource masterDataSource(){

        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(this.dbUrl);
        datasource.setUsername(username);
        try {
            datasource.setPassword(ConfigTools.decrypt(dbPublicKey,password));
        }catch (Exception e){
            datasource.setPassword(password);
        }
        datasource.setDriverClassName(driverClassName);
        //configuration
        if(initialSize != null) {
            datasource.setInitialSize(initialSize);
        }
        if(minIdle != null) {
            datasource.setMinIdle(minIdle);
        }
        if(maxActive != null) {
            datasource.setMaxActive(maxActive);
        }
        if(maxWait != null) {
            datasource.setMaxWait(maxWait);
        }
        if(timeBetweenEvictionRunsMillis != null) {
            datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        }
        if(minEvictableIdleTimeMillis != null) {
            datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        }
        if(validationQuery!=null) {
            datasource.setValidationQuery(validationQuery);
        }
        if(testWhileIdle != null) {
            datasource.setTestWhileIdle(testWhileIdle);
        }
        if(testOnBorrow != null) {
            datasource.setTestOnBorrow(testOnBorrow);
        }
        if(testOnReturn != null) {
            datasource.setTestOnReturn(testOnReturn);
        }
        if(poolPreparedStatements != null) {
            datasource.setPoolPreparedStatements(poolPreparedStatements);
        }
        if(maxPoolPreparedStatementPerConnectionSize != null) {
            datasource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
        }
        if(connectionProperties != null) {
            datasource.setConnectionProperties(connectionProperties);
        }
        List<Filter> filters = new ArrayList<>();
        filters.add(statFilter());
        filters.add(wallFilter());
        datasource.setProxyFilters(filters);

        return datasource;
    }

    @Bean
    public ServletRegistrationBean druidServlet() {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");

        //控制台管理用户，加入下面2行 进入druid后台就需要登录
        servletRegistrationBean.addInitParameter("loginUsername", "admin");
        servletRegistrationBean.addInitParameter("loginPassword", "admin");
        return servletRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        filterRegistrationBean.addInitParameter("profileEnable", "true");
        return filterRegistrationBean;
    }

    @Bean
    public StatFilter statFilter(){
        StatFilter statFilter = new StatFilter();
        statFilter.setLogSlowSql(true); //slowSqlMillis用来配置SQL慢的标准，执行时间超过slowSqlMillis的就是慢。
        statFilter.setMergeSql(true); //SQL合并配置
        statFilter.setSlowSqlMillis(1000);//slowSqlMillis的缺省值为3000，也就是3秒。
        return statFilter;
    }

    @Bean
    public WallFilter wallFilter(){
        WallFilter wallFilter = new WallFilter();
        //允许执行多条SQL
        WallConfig config = new WallConfig();
        config.setMultiStatementAllow(true);
        wallFilter.setConfig(config);
        return wallFilter;
    }

    //数据源事务管理器
    @Bean(name="masterDataSourceTransactionManager")
    @Primary
    public DataSourceTransactionManager masterDataSourceTransactionManager(){
        return new DataSourceTransactionManager(masterDataSource());
    }

    //创建Session
    @Bean(name="masterSqlSessionFactory")
    @Primary
    public SqlSessionFactory masterSqlSessionFactory(@Qualifier("masterDataSource") DataSource masterDataSource) throws Exception{
        final SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(masterDataSource);
        Resource[] mapperLocations = new PathMatchingResourcePatternResolver().getResources(MasterDBConfig.MAPPER_LOCATION);
        sqlSessionFactoryBean.setMapperLocations(mapperLocations);
        return sqlSessionFactoryBean.getObject();
    }
}
