package com.github.awesome.springboot.commons.data.mybatis.interceptor

class MybatisTestParameterFieldHandler : MybatisFieldHandler {

    override fun handle(tableName: String?, fieldName: String?, fieldValue: Any?): Any {
        println("MybatisTestParameterFieldHandler ==> $tableName.$fieldName = $fieldValue")
        return fieldValue!!
    }

    override fun targetTableFields(): Map<String, List<String>?> {
        return mutableMapOf("user" to listOf("name", "phone_number"))
    }

}
