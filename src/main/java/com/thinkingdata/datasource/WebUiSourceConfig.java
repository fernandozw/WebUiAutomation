package com.thinkingdata.datasource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2022/07/21 17:31
 */

@Configuration
@MapperScan(basePackages = "com.thinkingdata.webui.daoUi", sqlSessionFactoryRef = "webUiSqlSessionFactory")
public class WebUiSourceConfig {
    // 将这个对象放入Spring容器中
    @Bean(name = "webUiDataSource")
    // 表示这个数据源是默认数据源
    @Primary
    // 读取application.yml中的配置参数映射成为一个对象
    // prefix表示参数的前缀
    @ConfigurationProperties(prefix = "spring.datasource.webui")
    public DataSource getDateSourceWebUi() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "webUiSqlSessionFactory")
    // 表示这个数据源是默认数据源
    @Primary
    // @Qualifier表示查找Spring容器中名字为webUiDataSource的对象
    public SqlSessionFactory webUiSqlSessionFactory(@Qualifier("webUiDataSource") DataSource datasource)
            throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(datasource);
        bean.setMapperLocations(
                // 设置mybatis的xml所在位置
                new PathMatchingResourcePatternResolver().getResources("classpath:mapping/webUiMapping/*.xml"));
        return bean.getObject();
    }

    @Bean("webUiSqlSessionTemplate")
    // 表示这个数据源是默认数据源
    @Primary
    public SqlSessionTemplate webUiSqlSessionTemplate(
            @Qualifier("webUiSqlSessionFactory") SqlSessionFactory sessionFactory) {
        return new SqlSessionTemplate(sessionFactory);
    }

}
