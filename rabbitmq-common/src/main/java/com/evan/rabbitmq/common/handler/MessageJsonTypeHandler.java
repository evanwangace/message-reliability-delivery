package com.evan.rabbitmq.common.handler;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.evan.rabbitmq.api.Message;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 消息json处理器
 *
 * @author evan
 * @date 2022-03-08
 */
public class MessageJsonTypeHandler extends BaseTypeHandler<Message> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Message parameter,
                                    JdbcType jdbcType) throws SQLException {
        ps.setString(i, JSONUtil.toJsonStr(parameter));
    }

    @Override
    public Message getNullableResult(ResultSet rs, String columnName)
            throws SQLException {
        String value = rs.getString(columnName);
        if (null != value && !StrUtil.isBlank(value)) {
            return JSONUtil.toBean(rs.getString(columnName), Message.class);
        }
        return null;
    }

    @Override
    public Message getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        if (null != value && !StrUtil.isBlank(value)) {
            return JSONUtil.toBean(rs.getString(columnIndex), Message.class);
        }
        return null;
    }

    @Override
    public Message getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        if (null != value && !StrUtil.isBlank(value)) {
            return JSONUtil.toBean(cs.getString(columnIndex), Message.class);
        }
        return null;
    }
}
