package estivate.core.eval.lang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import estivate.core.ast.QueryAST;
import estivate.core.ast.lang.TableQueryAST;
import estivate.core.eval.EstivateEvaluator.EvalContext;
import estivate.core.eval.EstivateEvaluatorException;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TableQueryEvaluator extends SelectQueryEvaluator {

    public static final TableQueryEvaluator INSTANCE = new TableQueryEvaluator();

    @Override
    public void evalQuery(EvalContext context, QueryAST query) {
        if (query instanceof TableQueryAST) {
            TableQueryAST ast = (TableQueryAST) query;

            super.evalQuery(context, ast);

            Elements tableRootElement = context.getQueryResult();

            // here set table select datas and column indexation result

            Elements headRows = tableRootElement.select("thead tr");

            TableIndex tableIndex = new TableIndex(headRows);

            // index table header row
            int currRow = 0;
            // index table header cols
            int numberOfCols = numberOfCols(headRows);
            int[] rowCarries = new int[numberOfCols];
            int[] colCarries = new int[numberOfCols];
            for (Element row : headRows) {
                Iterator<Element> headers = row.select("th,td").iterator();
                for (int currCol = 0; currCol < numberOfCols;) {
                    int rowCarry = rowCarries[currCol];
                    int colCarry = colCarries[currCol];

                    if (rowCarry == 1) {
                        colCarries[currCol] = 1;
                    }
                    if (rowCarry >= 2) {
                        rowCarries[currCol]--;
                        currCol += colCarry;
                        continue;
                    }

                    if (!headers.hasNext()) {
                        log.error("Missing 'th' element, html was {}", headRows);
                        throw new EstivateEvaluatorException(context,
                                "Mal formed HTML, an other table header 'th' is missing");
                    }

                    Element header = headers.next();

                    int rowSpan = readIntAttr(header, "rowspan", 1);
                    int colSpan = readIntAttr(header, "colspan", 1);

                    tableIndex.put(currRow, header, IntRange.of(currCol, currCol + colSpan - 1));

                    rowCarries[currCol] = rowSpan;
                    colCarries[currCol] = colSpan;

                    currCol += colSpan;
                }
                currRow++;
            }

            context.setTableIndex(tableIndex);

            // select table data rows
            Elements dataRows = tableRootElement.select(ast.getRowSelector());

            context.setQueryResult(dataRows);
        }

    }

    /**
     * 
     * @param headRows
     * @return Number of cells of one line counting on colspan
     */
    private static int numberOfCols(Elements headRows) {
        int size = 0;
        Elements select = headRows.first().select("th,td");
        for (Element element : select) {
            size += readIntAttr(element, "colspan", 1);
        }
        return size;
    }

    public static int readIntAttr(Element node, String attrName, int defaut) {
        if (node.hasAttr(attrName)) {
            return Integer.parseInt(node.attr(attrName));
        }
        return defaut;
    }

    public static Elements findTdsInRange(Elements tds, IntRange range) {
        Elements results = new Elements();
        int currCol = 0;
        for (Element td : tds) {
            int colspan = readIntAttr(td, "colspan", 1);
            if (range.include(currCol)) {
                results.add(td);
            }
            currCol += colspan;
        }
        return results;
    }

    /**
     * <p>
     * Index of column of a table
     * <p>
     * IntRange if for the first and the last column index in case of colspan.
     */
    public static class TableIndex {

        private static final String COL_SEP = "/";

        Elements headerRows;

        // <Tex() Path, IntRange>
        @Getter
        Map<String, IntRange> textColMap = new HashMap<String, IntRange>();

        // <Row, Text() Path, IntRange>
        Map<Integer, Map<String, IntRange>> rowColMap = new HashMap<Integer, Map<String, IntRange>>();

        // <havingExpr, IntRange>
        Map<String, IntRange> evalCache = new HashMap<String, IntRange>();

        public TableIndex(Elements headRows) {
            headerRows = headRows;
        }

        protected void put(int row, Element header, IntRange range) {

            String col = header.text().replace(COL_SEP, "\\" + COL_SEP);

            Set<String> classNames = header.classNames();

            log.debug("put {} {} {}", col, row, range);
            Map<String, IntRange> colRangeMap = rowColMap.get(row);
            if (colRangeMap == null) {
                colRangeMap = new HashMap<String, IntRange>();
                rowColMap.put(row, colRangeMap);
            }
            textColMap.put(col, range);
            if (row == 0) {
                colRangeMap.put(col, range);
            } else {
                // find parent cell
                Map<String, IntRange> map2 = rowColMap.get(row - 1);
                for (Entry<String, IntRange> lastRowEntry : map2.entrySet()) {
                    if (lastRowEntry.getValue().include(range.begin)) {
                        colRangeMap.put(lastRowEntry.getKey() + COL_SEP + col, range);
                        textColMap.put(lastRowEntry.getKey() + COL_SEP + col, range);
                    }
                }
            }
        }

        public IntRange eval(String havingExpr) {
            IntRange intRange = evalCache.get(havingExpr);
            if (intRange == null) {
                intRange = evalWithoutCache(havingExpr);
                evalCache.put(havingExpr, intRange);
            }
            return intRange;
        }

        private IntRange evalWithoutCache(String havingExpr) {
            List<IntRange> results = new ArrayList<IntRange>();

            // index table header cols
            int numberOfCols = numberOfCols(headerRows);
            for (Element row : headerRows) {
                Iterator<Element> headers = row.select("th,td").iterator();
                for (int currCol = 0; currCol < numberOfCols;) {
                    if (!headers.hasNext()) {
                        log.error("Missing 'th' element, html was {}", headerRows);
                        throw new EstivateEvaluatorException("Mal formed HTML, an other table header 'th' is missing");
                    }
                    Element header = headers.next();

                    int colSpan = readIntAttr(header, "colspan", 1);

                    if (header.is(havingExpr) || header.is(":has(" + havingExpr + ")")) {
                        results.add(IntRange.of(currCol, currCol + colSpan - 1));
                    }

                    currCol += colSpan;
                }
            }
            if (results.isEmpty()) {
                log.error("having expr '{}' match nothing, was {}", havingExpr, headerRows);
                throw new EstivateEvaluatorException("having expr match nothing, was " + havingExpr);
            }
            if (results.size() >= 2) {
                log.error("having expr '{}' match {} columns, only one is expected, was {}", //
                        havingExpr, results.size(), headerRows);
                throw new EstivateEvaluatorException(
                        "having expr match " + results.size() + " columns, only one is expected, was " + havingExpr);
            }
            return results.get(0);
        }
    }

    @Data
    public static class IntRange {
        int begin;
        int end;

        private IntRange(int var1, int var2) {
            this.begin = var1;
            this.end = var2;
        }

        public static IntRange of(int a, int b) {
            return new IntRange(a, b);
        }

        @Override
        public String toString() {
            return this.begin + ".." + this.end;
        }

        public boolean include(int current) {
            return begin <= current && end >= current;
        }
    }
}
