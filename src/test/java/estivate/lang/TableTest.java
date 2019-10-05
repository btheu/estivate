package estivate.lang;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import estivate.EstivateTest;
import estivate.annotations.Cell;
import estivate.annotations.Table;
import estivate.annotations.Text;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TableTest extends EstivateTest {

    @Test
    public void table1() throws IOException {

        InputStream document = this.read("/table/u1.html");

        Result1 result = this.mapper.map(document, Result1.class);

        Assert.assertEquals("streetA", result.lines.get(0).getStreet());
        Assert.assertEquals("streetB", result.lines.get(1).getStreet());
        Assert.assertEquals("streetC", result.lines.get(2).getStreet());

        log.info(result.toString());
    }

    @Data
    public static class Result1 {

        @Table("#table1")
        public List<LineResult> lines;

        @Data
        public static class LineResult {
            @Cell(name = "Number Column")
            @Text(select = "span")
            public int number;

            @Cell(name = "Street Column")
            @Text(select = "span")
            public String street;

            @Cell(name = "Name.*", regex = true)
            @Text(select = "span.name")
            public String name;
            public String name2;

            @Cell(name = "Name.*", regex = true)
            @Text(select = "span.name")
            public void setName2(String name2) {
                this.name2 = name2;
            }
        }

    }

    @Test
    public void table1List() throws IOException {

        InputStream document = this.read("/table/u1.html");

        Collection<Result1List> resultList = this.mapper.mapToList(document, Result1List.class);

        log.info(resultList.toString());
    }

    @Data
    @Table("table")
    public static class Result1List {

        @Cell(name = "Number Column")
        @Text(select = "span")
        public int number;

        @Cell(name = "Street Column")
        @Text(select = "span")
        public String street;

        @Cell(name = "Name.*", regex = true)
        @Text(select = "span.name")
        public String name;
        public String name2;

        @Cell(name = "Name.*", regex = true)
        @Text(select = "span.name")
        public void setName2(String name2) {
            this.name2 = name2;
        }

    }

    @Test
    public void table2colspan() throws IOException {

        InputStream document = this.read("/table/u2.html");

        Result2 result = this.mapper.map(document, Result2.class);

        Assert.assertEquals("valueA", result.lines.get(0).getValue());
        Assert.assertEquals("valueB", result.lines.get(1).getValue());
        Assert.assertEquals("valueC", result.lines.get(2).getValue());

        log.info(result.toString());
    }

    @Data
    public static class Result2 {

        @Table("#table1")
        public List<LineResult> lines;

        @Data
        public static class LineResult {
            @Cell(name = "col 1")
            @Text(select = "span")
            public int number;

            @Cell(name = "col 2")
            @Text(select = "span")
            public String street;

            @Cell(name = "col 3")
            @Text(select = "span")
            public String value;

        }
    }

    @Test
    public void table3rowspan() throws IOException {

        InputStream document = this.read("/table/u3.html");

        Result3 result = this.mapper.map(document, Result3.class);

        Assert.assertEquals("valueA", result.lines.get(0).getValue());
        Assert.assertEquals("valueB", result.lines.get(1).getValue());
        Assert.assertEquals("valueC", result.lines.get(2).getValue());

        log.info(result.toString());
    }

    @Data
    public static class Result3 {

        @Table("#table1")
        public List<LineResult> lines;

        @Data
        public static class LineResult {
            @Cell(name = "col 1")
            @Text(select = "span")
            public int number;

            @Cell(name = "col 2")
            @Text(select = "span")
            public String street;

            @Cell(name = "col 7")
            @Text(select = "span")
            public String value;

        }
    }

    @Test
    public void table4colrowspan() throws IOException {

        InputStream document = this.read("/table/u4.html");

        Result4 result = this.mapper.map(document, Result4.class);

        Assert.assertEquals("valueA", result.lines.get(0).getValue());
        Assert.assertEquals("valueB", result.lines.get(1).getValue());
        Assert.assertEquals("valueC", result.lines.get(2).getValue());

        log.info(result.toString());
    }

    @Test
    public void table5biscolrowspan() throws IOException {

        InputStream document = this.read("/table/u5.html");

        Result4 result = this.mapper.map(document, Result4.class);

        Assert.assertEquals("valueA", result.lines.get(0).getValue());
        Assert.assertEquals("valueB", result.lines.get(1).getValue());
        Assert.assertEquals("valueC", result.lines.get(2).getValue());

        Assert.assertEquals("spanC", result.lines.get(2).getCol9());

        log.info(result.toString());
    }

    @Data
    public static class Result4 {

        @Table("#table1")
        public List<LineResult> lines;

        @Data
        public static class LineResult {
            @Cell(name = "col 1")
            @Text(select = "span")
            public int number;

            @Cell(name = "col 2")
            @Text(select = "span")
            public String street;

            @Cell(name = "col 9")
            @Text(select = "span")
            public String col9;

            @Cell(name = "col 10")
            @Text(select = "span")
            public String value;

        }
    }

}
