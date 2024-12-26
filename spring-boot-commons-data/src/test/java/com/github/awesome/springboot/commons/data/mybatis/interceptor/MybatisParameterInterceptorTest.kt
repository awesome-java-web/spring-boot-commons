package com.github.awesome.springboot.commons.data.mybatis.interceptor

import org.apache.ibatis.io.Resources
import org.apache.ibatis.session.SqlSessionFactory
import org.apache.ibatis.session.SqlSessionFactoryBuilder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class MybatisParameterInterceptorTest {

    private lateinit var sqlSessionFactory: SqlSessionFactory

    @BeforeEach
    fun setUp() {
        val mybatisConfigFileInputStream = Resources.getResourceAsStream("mybatis-config.xml")
        sqlSessionFactory = SqlSessionFactoryBuilder().build(mybatisConfigFileInputStream)

        val mybatisParameterInterceptor = MybatisParameterInterceptor()
        mybatisParameterInterceptor.setProperties(Properties())
        mybatisParameterInterceptor.registerParameterFieldHandler(MybatisTestParameterFieldHandler())

        val mybatisResultSetInterceptor = MybatisResultSetInterceptor()
        mybatisResultSetInterceptor.setProperties(Properties())
        mybatisResultSetInterceptor.registerResultSetFieldHandler(MybatisTestResultSetFieldHandler())

        sqlSessionFactory.configuration.addInterceptor(mybatisParameterInterceptor)
        sqlSessionFactory.configuration.addInterceptor(mybatisResultSetInterceptor)

        val session = sqlSessionFactory.openSession()
        val mapper = session.getMapper(MybatisTestMapper::class.java)
        mapper.createTable()
        session.commit()
        session.close()
    }

    @Test
    fun testSqlStatement() {
        val session = sqlSessionFactory.openSession()
        val mapper = session.getMapper(MybatisTestMapper::class.java)
        val user = MybatisTestEntity(
            id = 1,
            name = "Alice",
            age = 30,
            email = "alice@gmail.com",
            city = "New York",
            country = "USA"
        )

        val insert = mapper.insertUser(user)
        println("insert: $insert")

        val select = mapper.selectUser(1)
        println("select: $select")

        select.age = 18
        val update = mapper.updateUser(user)
        println("update: $update")

        val delete = mapper.deleteUser(1)
        println("delete: $delete")

        session.commit()
        session.close()
    }

}
