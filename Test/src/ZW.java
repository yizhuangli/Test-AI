import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.library.DicLibrary;
import org.json.JSONArray;
import org.json.JSONObject;
import org.nlpcn.commons.lang.tire.domain.Forest;
import org.nlpcn.commons.lang.tire.library.Library;

public class ZW {
	
	/** 
	 * 合法的词性，即需要进行搜索分析的词性
	 */
	private final static String[] validNatures = {"nr", "n", "xx", "zb"};
	
	public static void query(String searchword) throws Exception {
		List<Token> tokens = new ArrayList<Token>();
		Result r = AnsjMgr.getInstance().segment(searchword);
		List<Term> terms = r.getTerms();
		for (int i = 0; i < terms.size(); i++) {
			Term term = terms.get(i);
			String name = term.getName();
			String nature = term.getNatureStr();
			if (!Arrays.asList(validNatures).contains(nature))
				continue;
			Token token = ElasticMgr.getInstance().search(name);
			tokens.add(token);
		}
		
//		List<ZwResult> results = TokenProcesser.cartesianProduct(tokens);
		
		System.out.println(r.toString());
		
	}
	
	public static List<Token> getTokens(String searchword) throws Exception {
		List<Token> tokens = new ArrayList<Token>();
		Result r = AnsjMgr.getInstance().segment(searchword);
		List<Term> terms = r.getTerms();
		for (int i = 0; i < terms.size(); i++) {
			Term term = terms.get(i);
			String name = term.getName();
			String nature = term.getNatureStr();
			if (!Arrays.asList(validNatures).contains(nature))
				continue;
			Token token = ElasticMgr.getInstance().search(name);
			tokens.add(token);
		}
		return tokens;
	}
	
	public static JSONArray getAllTokenJson(String searchword) throws Exception {
		JSONArray result = new JSONArray();
		List<Token> tokens = getTokens(searchword);
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			JSONObject j = token.toJSON();
			result.put(j);
		}
		return result;
	}
	
	
	public static void main(String[] args) throws Exception {
		 
		 
//		 Forest f = Library.makeForest(ZW.class.getResourceAsStream("my.dic"));
		 Forest f1 = Library.makeForest(ZW.class.getResourceAsStream("my3.dic"));
//		 ArrayList<Forest> forests = new ArrayList<Forest>();
//		 forests.add(f1);
//		 forests.add(f);v
//		 f1=null;
//		 Forest ff = DicLibrary.get("dic_my");
//		 String str = "2015年第三季度欢迎使用ansj_seg,(ansj中文分词)在这里如果你遇到什么问题都可以联系我.我一定尽我所能.帮助大家.ansj_seg更快,更准,更自由!" ;
//		 System.out.println(DicAnalysis.parse(str, ff));
//		 TransportClient client = new PreBuiltTransportClient(Settings.EMPTY)
//				   .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("172.21.4.56"), 9300));
//		SearchResponse sr = client.prepareSearch("zw-ansj")
//				 .setQuery(QueryBuilders.matchQuery("caption", "易庄力")).setTypes("dimitem").get();
//				
//				client.close();
		String str = "2015年第三季度康续的请假天数";
		query(str);
		
		JSONArray r = getAllTokenJson(str);
		
		String s = "122";
	}
}
