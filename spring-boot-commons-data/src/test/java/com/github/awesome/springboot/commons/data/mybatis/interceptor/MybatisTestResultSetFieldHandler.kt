package com.github.awesome.springboot.commons.data.mybatis.interceptor;

class MybatisTestResultSetFieldHandler : MybatisFieldHandler {

    override fun handle(tableName: String?, fieldName: String?, fieldValue: Any?): Any {
        return "Dear $fieldValue from MybatisTestResultSetFieldHandler"
    }

    override fun targetTableFields(): MutableMap<String, MutableList<String>> {
        return mutableMapOf("user" to mutableListOf("name"))
    }

}
