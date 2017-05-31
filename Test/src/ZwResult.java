import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.search.SearchHit;

/**
 * 智问中间态结果<br>
 * 一条记录对应的中间态结果，记录了指标维度信息
 * 
 * @author yi
 * @date 2017年5月25日
 */
public class ZwResult {
	
	private Map<String, SearchHit> resultMap = new HashMap<String, SearchHit>();

	public void add(String key, SearchHit hit) {
		resultMap.put(key, hit);
	}

}
