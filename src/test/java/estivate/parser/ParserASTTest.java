package estivate.parser;

import org.jsoup.nodes.Document;
import org.junit.Test;

import estivate.EstivateTest;
import estivate.annotations.Attr;
import estivate.annotations.Select;
import estivate.annotations.Text;
import estivate.annotations.ast.EstivateAST;
import estivate.annotations.ast.parser.EstivateParser;
import estivate.core.eval.EstivateEvaluater;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ParserASTTest extends EstivateTest {

	@Test
	public void parse1() {

		Document document = readDocument("/attr/u2.html");

		EstivateAST ast = EstivateParser.parse(Result1.class);

		log.info(ast.toString());

		Object result = EstivateEvaluater.eval(document, ast);
		
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
