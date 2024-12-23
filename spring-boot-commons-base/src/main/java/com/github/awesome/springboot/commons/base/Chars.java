package com.github.awesome.springboot.commons.base;

/**
 * 表示在各种环境中使用的常见特殊字符的枚举类。
 * <p>
 * 该枚举类定义了一组特殊字符，每个字符与其对应的字符值和字符串表示形式相关联。它可以在需要特定特殊字符的场景中使用，例如字符串处理或字符验证。
 * </p>
 * <p>
 * 每个枚举常量有两个属性：
 * <ul>
 *   <li><b>charValue</b>: 字符符号的字符表示。</li>
 *   <li><b>stringValue</b>: 字符符号的字符串表示。</li>
 * </ul>
 * <p>
 * 使用示例：
 * <pre>
 *     Chars atSymbol = Chars.AT;
 *     System.out.println(atSymbol.charValue());   // 输出 '@'
 *     System.out.println(atSymbol.stringValue()); // 输出 "@"
 * </pre>
 *
 * @author <a href="https://github.com/codeboyzhou">codeboyzhou</a>
 * @since 1.1.0
 */
public enum Chars {

    /**
     * "@" 符号，常用于电子邮件地址和社交媒体账号
     */
    AT('@'),

    /**
     * "&amp;" 符号，常用作逻辑与运算符或用于 URL 中
     */
    AND('&'),

    /**
     * "." 符号，常用于文件扩展名或小数点分隔符
     */
    DOT('.'),

    /**
     * "-" 符号，用于连接词或作为数学中的减号
     */
    DASH('-'),

    /**
     * "," 符号，用于列表或数据值的分隔
     */
    COMMA(','),

    /**
     * "=" 符号，常用于赋值操作或比较操作
     */
    EQUAL('='),

    /**
     * "_" 符号，常用于变量名中或作为 URL 中的分隔符
     */
    UNDERLINE('_'),

    /**
     * "%" 符号，通常用于表示百分比或作为特殊运算符
     */
    PERCENT('%'),

    /**
     * "*" 符号，用于乘法运算、通配符操作或作为一般符号
     */
    WILDCARD('*'),

    /**
     * 空格字符
     */
    WHITESPACE(' '),

    ;

    /**
     * 枚举常量的字符表示
     */
    private final char charValue;

    /**
     * 枚举常量的字符串表示
     */
    private final String stringValue;

    /**
     * 构造方法，用于初始化枚举常量的字符值
     *
     * @param charValue 表示枚举常量的字符值
     */
    Chars(char charValue) {
        this.charValue = charValue;
        this.stringValue = String.valueOf(charValue);
    }

    /**
     * 返回枚举常量的字符表示
     *
     * @return 字符值
     */
    public char charValue() {
        return charValue;
    }

    /**
     * 返回枚举常量的字符串表示
     *
     * @return 字符的字符串值
     */
    public String stringValue() {
        return stringValue;
    }

}
