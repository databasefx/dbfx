package cn.navigational.dbfx.kit;


import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.select.*;

import java.util.HashMap;
import java.util.Map;

public class SqlParseHelper {
    /**
     * Determine whether the current SQL statement is a select statement
     *
     * @param sql Target sql
     * @return If target sql is select statement return true otherwise return false
     * @throws JSQLParserException If target sql is error sql {@link JSQLParserException} have throw
     */
    public static boolean selectStatement(String sql) throws JSQLParserException {
        var stmt = CCJSqlParserUtil.parse(sql);
        return stmt instanceof Select;
    }

    public static Map<String, Expression> getColumns(String sql) throws JSQLParserException {
        Map<String, Expression> map = new HashMap<>();
        Select stmt = (Select) CCJSqlParserUtil.parse(sql);
        for (SelectItem item : ((PlainSelect) stmt.getSelectBody()).getSelectItems()) {
            item.accept(new SelectItemVisitorAdapter() {
                @Override
                public void visit(SelectExpressionItem item) {
                    map.put(item.getAlias().getName(), item.getExpression());
                }
            });
        }
        return map;
    }
}
