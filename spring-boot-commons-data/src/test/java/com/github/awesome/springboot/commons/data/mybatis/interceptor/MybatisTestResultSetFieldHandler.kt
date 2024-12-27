package com.github.awesome.springboot.commons.data.mybatis.interceptor;

import com.github.awesome.springboot.commons.base.Chars

class MybatisTestResultSetFieldHandler : MybatisFieldHandler {

    override fun handle(tableName: String?, fieldName: String?, fieldValue: Any?): Any {
        println("MybatisTestResultSetFieldHandler ==> $tableName.$fieldName = $fieldValue")
        return fieldValue!!
    }

    override fun targetTableFields(): Map<String, List<String>?> {
        return mutableMapOf(Chars.WILDCARD.stringValue() to listOf("name", "phone_number"))
    }

}
