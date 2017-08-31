package zw;
import org.ansj.domain.Result;
import org.ansj.library.DicLibrary;
import org.ansj.recognition.impl.StopRecognition;
import org.ansj.splitWord.analysis.DicAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.nlpcn.commons.lang.tire.domain.Forest;
import org.nlpcn.commons.lang.tire.library.Library;

/**
 * 分词工具ansj 管理
 * @author yi
 * @date 2017年5月23日
 */
public class AnsjMgr {
	private static AnsjMgr instance = null;
	
	
	
	public static synchronized AnsjMgr getInstance() {
		if (instance == null) {
			instance = new AnsjMgr();
		}
		return instance;
	}
	
	/**
	 * 分词
	 * @throws Exception 
	 */
	public Result segment(String input) throws Exception {
		//过滤指定词性的词
		StopRecognition filter = new StopRecognition();
		filter.insertStopNatures("uj", "null");
		
//		String key = "dic_mykey" ;
//		Forest f_bbq = Library.makeForest(ZW.class.getResourceAsStream("my3.dic"));
//		DicLibrary.put(key, key, f_bbq);
//		DicLibrary.insert(key, "请假天数", "我是词性", 1000);
//		DicLibrary.reload(key);
//		 Forest f_zb = Library.makeForest(ZW.class.getResourceAsStream("my3.dic"));
		
		Forest ff = DicLibrary.get("dic_my");
		Result result = DicAnalysis.parse(input, ff).recognition(filter);
		return result;
	}
	
	
	
	
}
