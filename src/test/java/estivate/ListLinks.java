package estivate;

import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;

import estivate.annotations.Attr;
import estivate.annotations.Select;
import estivate.annotations.TagName;
import estivate.annotations.Text;

public class ListLinks {

	public static void main(String[] args) throws IOException {

		// args = new String[] { "" };

		Validate.isTrue(args.length == 1, "usage: supply url to fetch");
		String url = args[0];
		print("Fetching %s...", url);

		Document document = Jsoup.connect(url).get();

		EstivateMapper2 estivate = new EstivateMapper2();

		Data data = estivate.map(document, Data.class);

		print("\nMedia: (%d)", data.medias.size());
		for (Media src : data.medias) {
			if (src.tagName.equals("img")) {
				print(" * %s: <%s> %dx%d (%s)", src.tagName, src.src, src.width, src.height, src.alt);

			} else {
				print(" * %s: <%s>", src.tagName, src.src);
			}
		}

		print("\nImports: (%d)", data.imports.size());
		for (Import link : data.imports) {
			print(" * %s <%s> (%s)", link.tagName, link.href, link.rel);
		}

		print("\nLinks: (%d)", data.links.size());
		for (Link link : data.links) {
			print(" * a: <%s>  (%s)", link.href, link.text);
		}

	}

	public static class Data {

		@Select("[src]")
		List<Media> medias;

		@Select("link[href]")
		List<Import> imports;

		@Select("a[href]")
		List<Link> links;

	}

	public static class Media {

		@TagName
		public String tagName;

		@Attr("abs:src")
		public String src;

		@Attr(select = "img", value = "width", optional = true)
		public int width;

		@Attr(select = "img", value = "height", optional = true)
		public int height;

		public String alt;

		@Attr("alt")
		public void setAlt(String alt) {
			this.alt = trim(alt, 20);
		}
	}

	public static class Import {

		@TagName
		public String tagName;

		@Attr("abs:href")
		public String href;

		@Attr("rel")
		public String rel;

	}

	public static class Link {

		@TagName
		public String tagName;

		@Attr("abs:href")
		public String href;

		public String text;

		@Text
		public void setAlt(String text) {
			this.text = trim(text, 35);
		}
	}

	private static void print(String msg, Object... args) {
		System.out.println(String.format(msg, args));
	}

	private static String trim(String s, int width) {
		if (s.length() > width)
			return s.substring(0, width - 1) + ".";
		else
			return s;
	}
}
