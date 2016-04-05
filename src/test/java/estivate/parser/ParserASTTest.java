package estivate.parser;

import org.junit.Test;

import estivate.annotations.Attr;
import estivate.annotations.Select;
import estivate.annotations.ast.EstivateAST;
import estivate.annotations.ast.ParserAST;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ParserASTTest {

	@Test
	public void parse1() {

		EstivateAST ast = ParserAST.parse(Result1.class);

		log.info(ast.toString());

	}

	public static class Result1 {

		@Select("#famousId")
		@Attr("value")
		String valueOfFamous;
	}

}
