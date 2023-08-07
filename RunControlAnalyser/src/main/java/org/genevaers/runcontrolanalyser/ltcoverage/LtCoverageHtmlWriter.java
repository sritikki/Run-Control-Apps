package org.genevaers.runcontrolanalyser.ltcoverage;

import static j2html.TagCreator.a;
import static j2html.TagCreator.body;
import static j2html.TagCreator.div;
import static j2html.TagCreator.each;
import static j2html.TagCreator.h1;
import static j2html.TagCreator.h2;
import static j2html.TagCreator.head;
import static j2html.TagCreator.header;
import static j2html.TagCreator.html;
import static j2html.TagCreator.join;
import static j2html.TagCreator.link;
import static j2html.TagCreator.meta;
import static j2html.TagCreator.script;
import static j2html.TagCreator.table;
import static j2html.TagCreator.tbody;
import static j2html.TagCreator.td;
import static j2html.TagCreator.th;
import static j2html.TagCreator.title;
import static j2html.TagCreator.tr;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.genevaers.utilities.TestEnvironment;

import j2html.tags.specialized.DivTag;
import j2html.tags.specialized.TableTag;
import j2html.tags.specialized.TdTag;
import j2html.tags.specialized.ThTag;
import j2html.tags.specialized.TrTag;

public class LtCoverageHtmlWriter {

	public enum HITS_STATE {
		NONE, SOME, ALL
	}

	private static final String HITS_STYLE = "text-align: right; border-style: solid; border-width: 1px;padding: 3;";

	private static final String TH_STYLE = "w3-green";
	
	private static final String POPUP = "w3-modal-content w3-animate-zoom";

	private static final String TABLE_STYLE = "w3-table w3-bordered w3-small";
	private static final String COVERED = "w3-pale-green";
	private static final String SOME = "w3-pale-yellow";
	private static final String NONE = "w3-pale-red";

	static String toggleScript = "function toggleDiv(divname) {" +
			"var ele = document.getElementById(divname);" +
			"if (ele.style.display == \"none\") {" +
			"ele.style.display = \"block\";" +
			"}" +
			"else {" +
			"ele.style.display = \"none\";" +
			"}" +
			"}";

	private static String SAFR_CSS;
	
	public static void main(String[] args) {
  
		System.out.println("Coverage Report Generator");
		File rootDir = Paths.get(TestEnvironment.get("LOCALROOT")).toFile();;
		LtCoverageHtmlWriter generator = new LtCoverageHtmlWriter();
 		//generator.writeCoverageHTML(rootDir, "LTCov");
	}

	private File accumulationFileName=null;

	//private CoverageFile coverageFile;

	public void setCSSDir(String cssDir){
		SAFR_CSS = cssDir;
	}
	
	public static void writeTo(Path output, Map<String, LtCoverageEntry> coverageMap) {
		FileWriter testHtml;
		try {

			testHtml = new FileWriter(output.toFile());
			testHtml.write(
					html(
							head(
									meta().withContent("text/html; charset=UTF-8"),
									title("Function Code Coverage"),
									link().withRel("stylesheet").withType("text/css").withHref("https://www.w3schools.com/w3css/4/w3.css"),
									link().withRel("stylesheet").withType("text/css").withHref("https://www.w3schools.com/lib/w3-colors-flat.css"),
									link().withRel("stylesheet").withType("text/css").withHref("https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css"),
									script(join(toggleScript)).withLang("Javascript")),
							body(
									div(
											div(
													h1("Function Code Coverage").withClass("w3-teal"),
													functionCodeTable(coverageMap)).withClass("w3-col l10 m12"),
											div().withClass("w3-col l2 m12")).withClass("w3-row")))
							.renderFormatted());
			testHtml.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static TableTag functionCodeTable(Map<String, LtCoverageEntry> coverageMap)
	{
		return table(
				tbody(
						tr(
								th("Code"),
								th("Description"),
								th("Hits"),
								th("Arguments")
						  ).withClass(TH_STYLE),
						  each(coverageMap.entrySet(), e -> getCodeRow(e))
			    )
			   ).withClass(TABLE_STYLE);
	}

	private static TrTag getCodeRow(Entry<String, LtCoverageEntry> codeHits) {
		return tr(
				td(codeHits.getKey()),
				td(codeHits.getValue().getDescription()),
				td(Integer.toString(codeHits.getValue().getHits())),
				getArgInfo(codeHits.getValue())
				)
					.withCondClass(getHitsState(codeHits.getValue()) == HITS_STATE.NONE, NONE)
					.withCondClass(getHitsState(codeHits.getValue()) == HITS_STATE.SOME, SOME)
					.withCondClass(getHitsState(codeHits.getValue()) == HITS_STATE.ALL, COVERED);
	}

	private static TdTag getArgInfo(LtCoverageEntry codeHits) {
		//Use expected items as an indicator of matrix
		//Single arg will have expected max @12
		int exp = codeHits.getExpectedItems();
		if (exp == 0) {
			return td("");
		}
		if (exp <= LtCoverageEntry.MAXTYPES) {
			return td(
						a("Show Types").withHref(getTypeHref(codeHits.getName())),
						getTypeHits((LTCoverageEntry1Arg)codeHits)
					);
		}
		else {
			return td(a("Show Type Matrix").withHref(getTypeHref(codeHits.getName())),
					  getTypesMatrix((LTCoverageEntry2Args)codeHits) //((LTCoverageEntry2Args)codeHits).getTypeMatrixIterator())
					  );
		}
	}

	private static DivTag getTypesMatrix(LTCoverageEntry2Args codeHits) {
		return div(
					div(
						header(
								h2(
								a(codeHits.getName() + " Type Hits").withHref(getTypeHref(codeHits.getName()))
								.withClass("w3-button")
								)
						).withClass("w3-green"),
						
						table(
						tbody(
								getMatrixHeader(codeHits),
								getMatrixRows(codeHits)
						)).withClass("w3-table-all w3-small")
					).withClass(POPUP).withStyle("width:1500px")
				).withId(codeHits.getName()).withStyle("display: none").withClass("w3-modal");
	}

	private static TrTag getMatrixRows(LTCoverageEntry2Args codeHits) {
		return tr(
					each(codeHits.getTypeMatrix().getTypeHitMatrix().entrySet(), es -> getMatrixRow(es))
				);
	}

	private static TrTag getMatrixHeader(LTCoverageEntry2Args codeHits) {
		Set<String> header = codeHits.getTypeMatrix().getTypeHitMatrix().keySet();
		return tr(
					getColumnHeader("AAType"),
					each(header, h -> getColumnHeader(h))
				);
	}

	private static ThTag getColumnHeader(String h) {
		return th(h).withClass("info");
	}

	private static TrTag getMatrixRow(Entry<String, TypeHitMap> es) {
		return tr(
				td(es.getKey()).withClass("info"),
				each(es.getValue().getTypeHits().entrySet(), r -> getTypesRow(r))
			);
	}

	private static TdTag getTypesRow(Entry<String, Integer> typeHits) {
		return td(Integer.toString(typeHits.getValue()))
				.withCondClass(typeHits.getValue() == 0, NONE)
				.withCondClass(typeHits.getValue() > 0, COVERED);
	}

	private static DivTag getTypeHits(LTCoverageEntry1Arg codeHits) {
		return div(
				div(
					header(
							h2(
							a(codeHits.getName() + " Type Hits").withHref(getTypeHref(codeHits.getName()))
							.withClass("w3-button")
							)
					).withClass("w3-green"),
					
					table(
					tbody(
							th("Type"),
							th("Hits").withClass("info"),
					        each(codeHits.getTypeHits().getTypeHits().entrySet(), i -> getTypeRow(i))
					)).withClass("w3-table-all w3-small")
				).withClass(POPUP)
			).withId(codeHits.getName()).withStyle("display: none").withClass("w3-modal");
	}


	private static String getTypeHref(String name) {
		return "javascript:toggleDiv(\"" + name + "\")";
	}

	private static TrTag getTypeRow(Entry<String, Integer> typeHits) {
		return tr(
				td(typeHits.getKey()).withClass("info"),
				td(Integer.toString(typeHits.getValue()))
				.withCondClass(typeHits.getValue() == 0, NONE)
				.withCondClass(typeHits.getValue() > 0, COVERED)
				);
	}

	public static HITS_STATE getHitsState(LtCoverageEntry codeHits) {
		// if args is 1 the check all relevant types have passed
		// Can we do this... will typehits have been set
		if(codeHits.getExpectedItems() == 0){
			return codeHits.getHits() > 0 ? HITS_STATE.ALL : HITS_STATE.NONE; 
		} else {
			if (codeHits.getHits() == 0)
			{
				return HITS_STATE.NONE;
			}
			else {
				return getItemsHits(codeHits) >= codeHits.getExpectedItems() ? HITS_STATE.ALL : HITS_STATE.SOME;
			}
		}
	}

	private static int getItemsHits(LtCoverageEntry codeHits) {
		int typeHits = 0;
		if(codeHits.getExpectedItems() <= LtCoverageEntry.MAXTYPES) {
			LTCoverageEntry1Arg f1 = (LTCoverageEntry1Arg)codeHits;
			Iterator<Entry<String, Integer>> thi = f1.getTypeHitsIterator();
			while(thi.hasNext()) {
				thi.next();
				typeHits++;
			}
		}
		return typeHits;
	}

	// public void accumulateTo(Path overallcoverage) {
	// 	if(overallcoverage != null) {
	// 		accumulationFileName = overallcoverage.toFile();
	// 	}
	// }

	// public void close() {
	// 	coverageFile.close();
	// }

}
