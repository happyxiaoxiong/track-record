package cn.cnic.trackrecord.common.handler;

import cn.cnic.trackrecord.common.enumeration.Gender;
import cn.cnic.trackrecord.common.enumeration.Role;
import cn.cnic.trackrecord.common.enumeration.TrackFileState;
import cn.cnic.trackrecord.common.enumeration.ValuedEnum;
import cn.cnic.trackrecord.common.util.Objects;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 枚举类型和数据库int类型互转
 * @param <E>
 */
@MappedTypes(value = { Gender.class, Role.class, TrackFileState.class})
public class ValuedEnumTypeHandler<E extends Enum<E>> extends BaseTypeHandler<E> {
    private final Map<Integer, E> map = new HashMap<>();

    public ValuedEnumTypeHandler(Class<E> type) {
        if (Objects.isNull(type)) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        E[] enums = type.getEnumConstants();
        if (Objects.isNull(enums)) {
            throw new IllegalArgumentException(type.getSimpleName() + " does not represent an enum type.");
        }
        for (E e : enums) {
            ValuedEnum valuedEnum = (ValuedEnum) e;
            map.put(valuedEnum.getValue(), e);
        }
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, E e, JdbcType jdbcType) throws SQLException {
        ValuedEnum valuedEnum = (ValuedEnum) e;
        preparedStatement.setInt(i, valuedEnum.getValue());
    }

    @Override
    public E getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        int i = resultSet.getInt(columnName);
        if (resultSet.wasNull()) {
            return null;
        } else {
            return map.get(i);
        }
    }

    @Override
    public E getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
        int i = resultSet.getInt(columnIndex);
        if (resultSet.wasNull()) {
            return null;
        } else {
            return map.get(i);
        }
    }

    @Override
    public E getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
        int i = callableStatement.getInt(columnIndex);
        if (callableStatement.wasNull()) {
            return null;
        } else {
            return map.get(i);
        }
    }
}
