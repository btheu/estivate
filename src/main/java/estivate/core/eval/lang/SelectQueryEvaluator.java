package estivate.core.eval.lang;

import org.jsoup.select.Elements;

import estivate.core.ast.QueryAST;
import estivate.core.ast.lang.SelectQueryAST;
import estivate.core.eval.EstivateEvaluator.EvalContext;
import estivate.core.eval.EstivateEvaluator.QueryEvaluator;
import estivate.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SelectQueryEvaluator implements QueryEvaluator {

    public static final SelectQueryEvaluator INSTANCE = new SelectQueryEvaluator();

    public void evalQuery(EvalContext context, QueryAST query) {
        if (query instanceof SelectQueryAST) {
            SelectQueryAST ast = (SelectQueryAST) query;

            Elements queryResult = context.getQueryResult();

            String queryString = ast.getQueryString();

            if (!StringUtil.isBlank(queryString)) {

                log.debug("> eval Select Query [{}]", queryString);

                Elements select = context.getQueryResult().select(queryString);

                if (ast.isFirst()) {
                    queryResult = new Elements(select.first());
                } else if (ast.isLast()) {
                    queryResult = new Elements(select.last());
                } else if (ast.getIndex() > 0) {
                    if (ast.getIndex() > select.size()) {
                        log.warn("index (" + ast.getIndex() + ") is greater than select result size (" + select.size()
                                + "). Peeking the last.");
                        queryResult = new Elements(select.last());
                    } else {
                        queryResult = new Elements(select.get(ast.getIndex() - 1));
                    }
                } else if (ast.isUnique() && select.size() != 1) {
                    throw new RuntimeException("No unique element after query");
                } else {
                    queryResult = select;
                }

                log.debug("< eval Select Query [{}] : {}", queryString, queryResult);
            }

            context.setQueryResult(queryResult);

        }

    }

}
