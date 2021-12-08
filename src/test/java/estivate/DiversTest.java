package estivate;

import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import estivate.annotations.Text;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DiversTest extends EstivateTest {

    @Test
    public void first() throws IOException {

        InputStream document = read("/divers/u2.html");

        ResultFirst result = mapper.map(document, ResultFirst.class);

        Assertions.assertNotNull(result);

        Assertions.assertEquals("Name 1", result.getValue1());
        Assertions.assertEquals("Name 1", result.getValue2());

        log.info(result.toString());
    }

    @Test
    public void last() throws IOException {

        InputStream document = read("/divers/u2.html");

        ResultLast result = mapper.map(document, ResultLast.class);

        Assertions.assertNotNull(result);

        Assertions.assertEquals("Name 3", result.getValue1());
        Assertions.assertEquals("Name 3", result.getValue2());

        log.info(result.toString());
    }

    @Test
    public void failure() throws IOException {
        Assertions.assertThrows(IllegalArgumentException.class, new Executable() {
            public void execute() throws Throwable {
                InputStream document = read("/divers/u2.html");

                ResultFail result = mapper.map(document, ResultFail.class);
            }
        });
    }

    @Data
    public static class ResultFirst {

        @Text(select = ".someClass", first = true)
        public String value1;

        public String value2;

        @Text(select = ".someClass", first = true)
        public void setValue2(String value2) {
            this.value2 = value2;
        }

    }

    @Data
    public static class ResultLast {

        @Text(select = ".someClass", last = true)
        public String value1;

        public String value2;

        @Text(select = ".someClass", last = true)
        public void setValue2(String value2) {
            this.value2 = value2;
        }

    }

    @Data
    public static class ResultFail {

        @Text(select = ".someClass", first = true, last = true)
        public String value1;

        public String value2;

        @Text(select = ".someClass", first = true, last = true)
        public void setValue2(String value2) {
            this.value2 = value2;
        }

    }

}
