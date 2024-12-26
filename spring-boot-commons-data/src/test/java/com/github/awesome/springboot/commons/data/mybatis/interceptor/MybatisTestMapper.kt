package com.github.awesome.springboot.commons.data.mybatis.interceptor

interface MybatisTestMapper {

    fun createTable()

    fun insertUser(user: MybatisTestEntity): Int

    fun selectUser(id: Long): MybatisTestEntity

    fun updateUser(user: MybatisTestEntity): Int

    fun deleteUser(id: Long): Int

}
