package estivate.parser;

import org.junit.Test;

import estivate.annotations.Attr;
import estivate.annotations.Select;
import estivate.annotations.ast.EstivateAST;
import estivate.annotations.ast.EstivateParserAST;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ParserASTTest {

	@Test
	public void parse1() {

		EstivateAST ast = EstivateParserAST.parse(Result1.class);

		log.info(ast.toString());

	}

	@Data
	public static class Result1 {

		@Select("#famousId")
		@Attr("value")
		public String valueOfFamous;

		@Attr(select = "#famousId", value = "value")
		public String valueOfFamous2;
	}

}
