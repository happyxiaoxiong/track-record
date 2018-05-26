package cn.cnic.trackrecord.common.handler;

import cn.cnic.trackrecord.common.date.IntDate;
import cn.cnic.trackrecord.common.date.LongDate;
import cn.cnic.trackrecord.common.date.ShortDate;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * IntDate和数据库的int类型互转
 * @param <E>
 */
@MappedTypes(value = { LongDate.class, ShortDate.class })
public class IntDateTypeHandler<E extends IntDate> extends BaseTypeHandler<E> {
    private final Class<E> type;

    public IntDateTypeHandler(Class<E> type) {
        this.type = type;
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, E e, JdbcType jdbcType) throws SQLException {
        preparedStatement.setInt(i, e.getValue());
    }

    @Override
    public E getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        int value = resultSet.getInt(columnName);
        return get(value);
    }

    @Override
    public E getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
        int value = resultSet.getInt(columnIndex);
        return get(value);
    }

    @Override
    public E getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
        int value = callableStatement.getInt(columnIndex);
        return get(value);
    }

    private E get(int value) {
        E e = null;
        if (value >= 0) {
            try {
                e = type.newInstance();
                e.setValue(value);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return e;
    }
}
