package estivate.parser;

import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import estivate.EstivateTest;
import estivate.annotations.Attr;
import estivate.annotations.Select;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ParserASTTest extends EstivateTest {


	@Test
	public void parse2() {

		Document document = readDocument("/attr/u2.html");

		Result1 result = mapper.map(document, Result1.class);

		List<SubResult> subs = result.getSubs();
		for (SubResult subResult : subs) {
			
		}
		
		List<String> roleList = result.getRoleList();
		for (String string : roleList) {
			
		}
		
		log.info(result.toString());

	}

	@Data
	public static class Result1 {

		@Select("div")
		public List<SubResult> subs;
		
		@Select("div")
		@Attr("role")
		public String roles1;

		public String roles2;

		@Select("div")
		@Attr("role")
		public void setRole(Document doc1, String roles, List<String> roleList, Document doc2, Elements elt) {
			this.roles2 = roles;
		}

		@Select("div")
		@Attr("role")
		public List<String> roleList;

	}

	@Data
	public static class SubResult {
		
		@Attr("role")
		public String roles;
		
		@Attr("role")
		public List<String> roleList;
	}
}
