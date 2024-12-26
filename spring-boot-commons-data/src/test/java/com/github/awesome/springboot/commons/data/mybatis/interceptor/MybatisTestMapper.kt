package com.github.awesome.springboot.commons.data.mybatis.interceptor

interface MybatisTestMapper {

    fun createTable()

    fun insertUser(user: MybatisTestEntityUser): Int

    fun selectUser(id: Long): MybatisTestEntityUser

    fun updateUser(user: MybatisTestEntityUser): Int

    fun deleteUser(id: Long): Int

    fun selectUserNameOfScoreGreaterThan50(): String

}
