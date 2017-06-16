package cn.jedisoft.jediframework.db;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.sql.Types;

/**
 * 记录 SQL 数据库字段和 JAVA 对象属性的匹配信息，加速 ORM 过程
 * 
 * @author lzm
 *
 */
public class FieldMetaData {

	private int index = -1;
	private String name = "";
	private int type = Types.NULL;
	private String javaName = "";
	private Type javaType = null;
	private Field javaField = null;
	private boolean matched = false;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getJavaName() {
		return javaName;
	}

	public void setJavaName(String javaName) {
		this.javaName = javaName;
	}

	public Type getJavaType() {
		return javaType;
	}

	public void setJavaType(Type javaType) {
		this.javaType = javaType;
	}

	public Field getJavaField() {
		return javaField;
	}

	public void setJavaField(Field javaField) {
		this.javaField = javaField;
	}

	public boolean isMatched() {
		return matched;
	}

	public void setMatched(boolean matched) {
		this.matched = matched;
	}

}
