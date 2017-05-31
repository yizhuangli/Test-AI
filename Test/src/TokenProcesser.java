import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.search.SearchHit;

/**
 * token的处理类，包括进行token间的组合
 * @author yi
 * @date 2017年5月26日
 */
public class TokenProcesser {
	
	public static List<ZwResult> getResult(List<Token> tokens) {
		List<ZwResult> zwresults = cartesianProduct(tokens);
		
		
		
		return zwresults;
	}

	/**
	 * 进行笛卡尔积运算
	 * @param tokens 
	 */
	public static List<ZwResult> cartesianProduct(List<Token> tokens) {
		List<String> inputList = getInputList(tokens);
		List<String> resultList = new ArrayList<String>();
		loop(inputList, resultList, 0, new String[inputList.size()]);
		
		List<ZwResult> zwresults = new ArrayList<ZwResult>();
		if (inputList.size() == 0) {
			return zwresults;
		}
		Map<String, Token> tokenmap = getTokenMap(tokens);
		
		for (int i = 0; i < resultList.size(); i++) {
			String[] array = resultList.get(i).split("##");
			
			ZwResult zwresult = new ZwResult();
			for (int j = 0; j < array.length; j++) {
				String[] key_array = array[j].split("#");
				int index = Integer.valueOf(key_array[1]);
				String tokenword = key_array[0];
				Token token = tokenmap.get(tokenword);
				
				SearchHit hit = token.getResults().get(index);
				
				zwresult.add(tokenword, hit);
			}
			zwresults.add(zwresult);
			
		}
		
		
		return zwresults;
	}
	
	/**
	 * 将tokens数组转成一种新的格式，例如： [人员#0##人员#1, 报销#0##报销1]<br>
	 * 格式说明 ：先将tokens转成字符串形式的数组，其中0,1,2..表示在该token中的结果的下标<br>
	 * 这个输入结果会用于之后的递归调用生成词语间的组合
	 * 
	 * @param tokens
	 * @return
	 */
	private static List<String> getInputList(List<Token> tokens) {
		List<String> inputList = new ArrayList<String>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = (Token) tokens.get(i); 
			if (token.getResults().size()== 0) {
				continue;
			}
			StringBuffer buffer = new StringBuffer();
			for (int j = 0; j < token.getResults().size(); j++) {
				buffer.append(token.getTokenword() + "#" + j);
				if (j != token.getResults().size() - 1) {
					buffer.append("##");
				}
			}
			inputList.add(buffer.toString());
			
		}
		return inputList;
	}
	
	/**
	 * 词性组合的递归算法
	 * @param inputList
	 * @param resultList
	 * @param index
	 * @param arr
	 */
	private static void loop(List<String> inputList, List<String> resultList, int index, String[] arr) {
		if (index == inputList.size()) {
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < arr.length; i++) {
				buffer.append(arr[i]);
				if (i != arr.length - 1) {
					buffer.append("##");
				}
			}
			resultList.add(buffer.toString());
			return;
		}
		
		//inputList.get(index).split("##");
		for (String s : inputList.get(index).split("##")) {
			arr[index] = s;
			loop(inputList, resultList, index + 1, arr);
		}
	}
	
	/**
	 * 根据词语数组生成一个map，便于生成组合时查找
	 * @param tokens
	 * @return
	 */
	private static Map<String, Token> getTokenMap(List<Token> tokens) {
		Map<String, Token> tokenmap = new HashMap<String, Token>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = (Token) tokens.get(i);
			String tokenword = token.getTokenword();
			tokenmap.put(tokenword, token);
		}
		return tokenmap;
	}
	
	
}
