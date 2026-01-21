package com.goolbitg.api.v1.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.goolbitg.api.v1.repository.mappers.ChallengeRecordCustomMapper;

@Configuration
public class MybatisConfig {

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);

        factoryBean.setMapperLocations(
            new PathMatchingResourcePatternResolver() 
                .getResources("classpath:/mapper/**/*.xml")
        );
        factoryBean.setTypeAliasesPackage("com.goolbitg.api.v1.entity.custom");

        var configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        factoryBean.setConfiguration(configuration);

        return factoryBean.getObject();
    }

    @Bean SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean ChallengeRecordCustomMapper challengeRecordCustomMapper(SqlSessionTemplate sqlSessionTemplate) throws Exception {
        return sqlSessionTemplate.getMapper(ChallengeRecordCustomMapper.class);
    }

}
