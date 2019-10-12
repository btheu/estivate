package estivate.core.ast;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public abstract class NodeAST {

    protected List<QueryAST> queries = new ArrayList<QueryAST>();

    public void addQuery(QueryAST query) {
        queries.add(query);
    }
}
