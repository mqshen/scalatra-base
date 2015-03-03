package com.ynet

import java.io.{File, IOException}
import java.util
import java.util.Properties
import javax.persistence.EntityManagerFactory
import javax.sql.DataSource

import org.apache.commons.dbcp.BasicDataSource
import org.apache.commons.io.FileUtils
import org.apache.commons.lang.StringUtils
import org.hibernate.ejb.HibernatePersistence
import org.springframework.beans.factory.config.PropertiesFactoryBean
import org.springframework.context.{ApplicationContext, ApplicationContextAware}
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.{Resource, ClassPathResource}
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.vendor.{HibernateJpaDialect, Database, HibernateJpaVendorAdapter}
import org.springframework.orm.jpa.{JpaTransactionManager, JpaDialect, JpaVendorAdapter, LocalContainerEntityManagerFactoryBean}
import org.springframework.transaction.PlatformTransactionManager


object DatabaseConfig {
  val JDBC_POOL_MIN_SIZE = "5"

  /**
   * 默认连接池大小
   */
  val JDBC_POOL_MAX_SIZE = "20"

  /**
   * 默认连接池超时时间
   */
  val JDBC_POOL_TIMEOUT = "1800"

  /**
   * 默认statement大小
   */
  val JDBC_POOL_MAX_STATEMENTS = "50"


  val CurrentDatabase = Database.MYSQL
}

@Configuration
@EnableJpaRepositories(basePackages = Array("com.ynet.repositories"))
class DatabaseConfig extends ApplicationContextAware {
  import DatabaseConfig._

  var applicationContext: ApplicationContext = null

  override def setApplicationContext(applicationContext: ApplicationContext): Unit = this.applicationContext = applicationContext

  def initConfigProperties (properties: Properties) = {
    setPropertyDefaultValue(properties, "jdbc.pool.min_size", JDBC_POOL_MIN_SIZE)
    setPropertyDefaultValue(properties, "jdbc.pool.max_size", JDBC_POOL_MAX_SIZE)
    setPropertyDefaultValue(properties, "jdbc.pool.timeout", JDBC_POOL_TIMEOUT)
    setPropertyDefaultValue(properties, "jdbc.pool.max_statements", JDBC_POOL_MAX_STATEMENTS)
  }

  def setPropertyDefaultValue (properties: Properties, key: String, defaultValue: String) {
    val value: String = properties.getProperty(key)
    if (StringUtils.isBlank(value)) {
      properties.setProperty(key, defaultValue)
    }
  }

  @Bean
  def configProperties: Properties = {
    val factoryBean: PropertiesFactoryBean = new PropertiesFactoryBean
    factoryBean.setLocations(new ClassPathResource("config/environment/develop.properties"),
      new ClassPathResource("config/system.properties"))
    factoryBean.afterPropertiesSet
    val properties: Properties = factoryBean.getObject
    initConfigProperties(properties)
    properties
  }

  @Bean(destroyMethod = "close")
  def dataSource: DataSource = {
    val dataSource: BasicDataSource = new BasicDataSource
    dataSource.setDriverClassName(configProperties.getProperty("jdbc.driver"))
    dataSource.setUrl(configProperties.getProperty("jdbc.url"))
    dataSource.setUsername(configProperties.getProperty("jdbc.username"))
    dataSource.setPassword(configProperties.getProperty("jdbc.password"))
    dataSource
  }

  @Bean
  def entityManagerFactory: EntityManagerFactory = {
    val factory: LocalContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean
    val jpaProperties: Properties = new Properties
    jpaProperties.setProperty("hibernate.format_sql", "true")
    jpaProperties.setProperty("hibernate.max_fetch_depth", "2")
    jpaProperties.setProperty("hibernate.c3p0.min_size", configProperties.getProperty("jdbc.pool.min_size"))
    jpaProperties.setProperty("hibernate.c3p0.max_size", configProperties.getProperty("jdbc.pool.max_size"))
    jpaProperties.setProperty("hibernate.c3p0.timeout", configProperties.getProperty("jdbc.pool.timeout"))
    jpaProperties.setProperty("hibernate.c3p0.max_statements", configProperties.getProperty("jdbc.pool.max_statements"))
    jpaProperties.setProperty("hibernate.search.default.indexBase", configProperties.getProperty("system.path.index"))
    jpaProperties.setProperty("hibernate.hbm2ddl.auto", configProperties.getProperty("hibernate.hbm2ddl.auto"))
    jpaProperties.setProperty("hibernate.ejb.naming_strategy", "org.hibernate.cfg.ImprovedNamingStrategy")
    jpaProperties.setProperty("hibernate.cache.region.factory_class", "org.hibernate.cache.ehcache.EhCacheRegionFactory")
    factory.setDataSource(dataSource)
    factory.setPackagesToScan("com.ynet.domain")
    factory.setPersistenceProvider(new HibernatePersistence)
    factory.setJpaVendorAdapter(vendorAdapter)
    factory.setJpaDialect(jpaDialect)
    factory.setJpaProperties(jpaProperties)
    factory.afterPropertiesSet
    factory.getObject
  }

  @Bean
  def vendorAdapter:JpaVendorAdapter =  {
    val vendorAdapter: HibernateJpaVendorAdapter = new HibernateJpaVendorAdapter
    vendorAdapter.setShowSql(true)
    vendorAdapter.setGenerateDdl(true)
    vendorAdapter.setDatabase(CurrentDatabase)
    vendorAdapter
  }

  @Bean
  def jpaDialect: JpaDialect = {
    new HibernateJpaDialect
  }

  @Bean
  def transactionManager:PlatformTransactionManager = {
    val txManager: JpaTransactionManager = new JpaTransactionManager
    txManager.setEntityManagerFactory(entityManagerFactory)
    txManager
  }

}