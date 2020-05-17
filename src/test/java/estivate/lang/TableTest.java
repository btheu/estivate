package estivate.lang;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import estivate.EstivateTest;
import estivate.annotations.Column;
import estivate.annotations.Table;
import estivate.annotations.Text;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@FixMethodOrder(MethodSorters.JVM)
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
            @Column(name = "Number Column")
            @Text(select = "span")
            public int number;

            @Column(name = "Street Column")
            @Text(select = "span")
            public String street;

            @Column(name = "unknow", optional = false)
            @Text(select = "span", optional = true)
            public String unknow;

            @Column(name = "Name.*", regex = true)
            @Text(select = "span.name")
            public String name;
            public String name2;

            @Column(name = "Name.*", regex = true)
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

        @Column(name = "Number Column")
        @Text(select = "span")
        public int number;

        @Column(name = "Street Column")
        @Text(select = "span")
        public String street;

        @Column(name = "Name.*", regex = true)
        @Text(select = "span.name")
        public String name;
        public String name2;

        @Column(name = "Name.*", regex = true)
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
            @Column(name = "col 1")
            @Text(select = "span")
            public int number;

            @Column(name = "col 2")
            @Text(select = "span")
            public String street;

            @Column(name = "col 3")
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
            @Column(name = "col 1")
            @Text(select = "span")
            public int number;

            @Column(name = "col 2")
            @Text(select = "span")
            public String street;

            @Column(name = "col 7")
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
            @Column(name = "col 1")
            @Text(select = "span")
            public int number;

            @Column(name = "col 2")
            @Text(select = "span")
            public String street;

            @Column(name = "col 9")
            @Text(select = "span")
            public String col9;

            @Column(name = "col 10")
            @Text(select = "span")
            public String value;

        }
    }

    @Test
    public void table7samecolname() throws IOException {

        InputStream document = this.read("/table/u7.html");

        Result7 result = this.mapper.map(document, Result7.class);

        Assert.assertEquals("name1A", result.lines.get(0).getName());
        Assert.assertEquals("name1B", result.lines.get(1).getName());

        Assert.assertEquals("value246A", result.lines.get(0).getValue246());
        Assert.assertEquals("value257A", result.lines.get(0).getValue257());
        Assert.assertEquals("value346A", result.lines.get(0).getValue346());
        Assert.assertEquals("value357A", result.lines.get(0).getValue357());

        Assert.assertEquals("value356A value357A", result.lines.get(0).getValue35());

        Assert.assertEquals("value246B", result.lines.get(1).getValue246());
        Assert.assertEquals("value257B", result.lines.get(1).getValue257());
        Assert.assertEquals("value346B", result.lines.get(1).getValue346());
        Assert.assertEquals("value357B", result.lines.get(1).getValue357());

        Assert.assertEquals("value356B value357B", result.lines.get(1).getValue35());

        log.info(result.toString());
    }

    @Data
    public static class Result7 {

        @Table("#table1")
        public List<LineResult> lines;

        @Data
        public static class LineResult {
            @Column(name = "col\\/1")
            @Text(select = "span")
            public String name;

            @Column(name = "col.1", regex = true)
            @Text(select = "span")
            public String name2;

            @Column(name = "col.2/col 4/col 6", regex = true)
            @Text(select = "span")
            public String value246;

            @Column(name = "col 2/col 5/col 7")
            @Text(select = "span")
            public String value257;

            @Column(name = "col 3/col 4/col 6")
            @Text(select = "span")
            public String value346;

            @Column(name = "col 3/col 5/col 7")
            @Text(select = "span")
            public String value357;

            @Column(name = "col 3/col 5")
            @Text(select = "span")
            public String value35;

        }
    }

    @Test
    public void table8emptyHeader() throws IOException {

        InputStream document = this.read("/table/u8.html");

        Result8 result = this.mapper.map(document, Result8.class);

        Assert.assertEquals("name1A", result.lines.get(0).getName());
        Assert.assertEquals("name1B", result.lines.get(1).getName());

        Assert.assertEquals("value4A", result.lines.get(0).getValue4());
        Assert.assertEquals("value4B", result.lines.get(1).getValue4());

        log.info(result.toString());
    }

    @Data
    public static class Result8 {

        @Table("#table1")
        public List<LineResult> lines;

        @Data
        public static class LineResult {
            @Column(name = "col 1")
            @Text(select = "span")
            public String name;

            @Column(name = "col 4")
            @Text(select = "span")
            public String value4;

        }
    }

    @Test
    public void table9ThAndTdHeader() throws IOException {

        InputStream document = this.read("/table/u9.html");

        Result9 result = this.mapper.map(document, Result9.class);

        Assert.assertEquals("name1A", result.lines.get(0).getName());
        Assert.assertEquals("name1B", result.lines.get(1).getName());

        Assert.assertEquals("value4A", result.lines.get(0).getValue4());
        Assert.assertEquals("value4B", result.lines.get(1).getValue4());

        log.info(result.toString());
    }

    @Data
    public static class Result9 {

        @Table("#table1")
        public List<LineResult> lines;

        @Data
        public static class LineResult {
            @Column(name = "col 1")
            @Text(select = "span")
            public String name;

            @Column(name = "col 4")
            @Text(select = "span")
            public String value4;

        }
    }

    @Test
    public void table10CssClassHeader() throws IOException {

        InputStream document = this.read("/table/u10.html");

        Result10 result = this.mapper.map(document, Result10.class);

        Assert.assertEquals("name1A", result.lines.get(0).getName());
        Assert.assertEquals("name1B", result.lines.get(1).getName());

        Assert.assertEquals("value4A", result.lines.get(0).getValue4());
        Assert.assertEquals("value4B", result.lines.get(1).getValue4());

        log.info(result.toString());
    }

    @Data
    public static class Result10 {

        @Table("#table1")
        public List<LineResult> lines;

        @Data
        public static class LineResult {
            @Column(thClass = "col1")
            @Text(select = "span")
            public String name;

            @Column(thClass = "col4")
            @Text(select = "span")
            public String value4;

        }
    }

    @Test
    public void table11ThHaving() throws IOException {

        InputStream document = this.read("/table/u11.html");

        Result11 result = this.mapper.map(document, Result11.class);

        Assert.assertEquals("name1A", result.lines.get(0).getName());
        Assert.assertEquals("name1B", result.lines.get(1).getName());

        Assert.assertEquals("value4A", result.lines.get(0).getValue4());
        Assert.assertEquals("value4B", result.lines.get(1).getValue4());

        log.info(result.toString());
    }

    @Data
    public static class Result11 {

        @Table("#table1")
        public List<LineResult> lines;

        @Data
        public static class LineResult {
            @Column(thClass = "col1")
            @Text(select = "span")
            public String name;

            @Column(thHaving = "img[alt*=alt4]")
            @Text(select = "span")
            public String value4;

        }
    }
}
