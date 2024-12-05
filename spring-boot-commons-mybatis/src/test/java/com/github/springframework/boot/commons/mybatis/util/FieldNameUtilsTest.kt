package com.github.springframework.boot.commons.mybatis.util

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class FieldNameUtilsTest {

    @Test
    fun testNewInstance() {
        assertThrows<UnsupportedOperationException>(FieldNameUtils::class.java::newInstance)
    }

    @Test
    fun testDetermineTargetFieldNames_HitTableName() {
        val tableName = "test_table_name"
        val tableFields = mapOf(tableName to listOf("id", "name", "age"))
        val targetFieldNames = FieldNameUtils.determineTargetFieldNames(tableFields, tableName)
        assert(targetFieldNames.size == tableFields.values.flatten().size)
        assert(targetFieldNames.containsAll(tableFields.values.flatten()))
    }

    @Test
    fun testDetermineTargetFieldNames_NotHitTableName() {
        val tableName = "test_table_name"
        val tableFields = mapOf("other_table_name" to listOf("id", "name", "age"))
        val targetFieldNames = FieldNameUtils.determineTargetFieldNames(tableFields, tableName)
        assert(targetFieldNames.isEmpty())
    }

    @Test
    fun testUnderscoreToCamelCase_NullOrEmptyParameter() {
        assertThrows<IllegalArgumentException> { FieldNameUtils.underscoreToCamelCase(null) }
        assertThrows<IllegalArgumentException> { FieldNameUtils.underscoreToCamelCase("") }
    }

    @Test
    fun testUnderscoreToCamelCase_NormalParameter() {
        assert(FieldNameUtils.underscoreToCamelCase("test_table_name") == "testTableName")
        assert(FieldNameUtils.underscoreToCamelCase("test_table_name_id") == "testTableNameId")
        assert(FieldNameUtils.underscoreToCamelCase("test_table_name_id_name") == "testTableNameIdName")
        assert(FieldNameUtils.underscoreToCamelCase("test_table_name_id_name_a") == "testTableNameIdNameA")
    }

}
