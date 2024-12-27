package com.github.awesome.springboot.commons.data.mybatis.interceptor

interface MybatisTestMapper {

    fun createTable()

    fun insertUser(user: MybatisTestEntityUser): Int

    fun selectUser(id: Long): MybatisTestEntityUser

    fun updateUser(user: MybatisTestEntityUser): Int

    fun deleteUser(id: Long): Int

    fun selectUserNameOfScoreGreaterThan50(): String

    fun insertSimpleMap(userInfo: Map<String, Any>): Int

    fun insertEntityValueMap(userInfo: Map<String, MybatisTestEntityUser>): Int

    fun insertEntityListValueMap(userInfo: Map<String, List<MybatisTestEntityUser>>): Int

    fun insertMapList(userInfoList: List<Map<String, Any>>): Int

}
