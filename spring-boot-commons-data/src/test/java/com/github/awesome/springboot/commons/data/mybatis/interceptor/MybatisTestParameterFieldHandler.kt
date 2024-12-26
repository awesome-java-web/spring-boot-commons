package com.github.awesome.springboot.commons.data.mybatis.interceptor

class MybatisTestParameterFieldHandler : MybatisFieldHandler {

    override fun handle(tableName: String?, fieldName: String?, fieldValue: Any?): Any {
        return "Dear $fieldValue from MybatisTestParameterFieldHandler"
    }

    override fun targetTableFields(): MutableMap<String, MutableList<String>> {
        return mutableMapOf("user" to mutableListOf("name"))
    }

}
