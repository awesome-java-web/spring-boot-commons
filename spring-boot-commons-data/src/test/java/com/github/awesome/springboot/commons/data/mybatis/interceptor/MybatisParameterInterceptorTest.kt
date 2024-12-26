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
    fun testForUserDefinedObject() {
        val session = sqlSessionFactory.openSession()
        val mapper = session.getMapper(MybatisTestMapper::class.java)

        var user = MybatisTestEntityUser(
            id = 1,
            name = "Alice",
            age = 30,
            email = "alice@gmail.com",
            city = "New York",
            country = "USA"
        )

        mapper.insertUser(user)
        user = mapper.selectUser(1)
        user.age = 18
        mapper.updateUser(user)
        mapper.deleteUser(1)

        // 测试从带有 JOIN 关键字的 SQL 语句中解析表名，以及解析表名时应该忽略转义符
        mapper.selectUserNameOfScoreGreaterThan50()

        session.commit()
        session.close()
    }

}
