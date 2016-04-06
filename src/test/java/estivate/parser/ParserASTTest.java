package estivate.parser;

import org.jsoup.nodes.Document;
import org.junit.Test;

import estivate.EstivateTest;
import estivate.annotations.Attr;
import estivate.annotations.Select;
import estivate.annotations.Text;
import estivate.core.ast.EstivateAST;
import estivate.core.ast.parser.EstivateParser;
import estivate.core.eval.EstivateEvaluator;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ParserASTTest extends EstivateTest {

    @Test
    public void parse2() {
        
        Document document = readDocument("/attr/u2.html");
        
        Result1 result = mapper.map(document, Result1.class);
        
        log.info(result.toString());
        
    }
    
	@Test
	public void parse1() {

		Document document = readDocument("/attr/u2.html");

		EstivateAST ast = EstivateParser.parse(Result1.class);

		log.info(ast.toString());

		Result1 result = (Result1) EstivateEvaluator.eval(document, ast);
		
		log.info(ast.toString());

		//log.info(result.toString());

	}

	@Data
	public static class Result1 {

		@Select("#famousId")
		@Attr("value")
		public String valueOfFamous1;

		@Attr(select = "#famousId", value = "value")
		public String valueOfFamous2;

		@Select("#famousId")
		@Text
		public String valueOfFamous3;
		
		@Select("div")
		@Attr("role")
		public String tust;
	}

}
