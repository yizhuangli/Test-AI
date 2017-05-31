import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 分词后，每个词对应的实体对象
 * @author yi
 * @date 2017年5月26日
 */
public class Token {
	
	private String searchword;
	
	private SearchResponse subjectcaption;
	
	private SearchResponse dimcaption;

	private SearchResponse dimitem;
	
	private SearchResponse subjectfield;
	
	private SearchResponse subjectxml;
	
	private List<SearchHit> results = new ArrayList<SearchHit>();

	
	

	public Token(String searchword, SearchResponse subjectcaption, SearchResponse dimcaption, SearchResponse dimitem,
			SearchResponse subjectfield, SearchResponse subjectxml) {
		super();
		this.searchword = searchword;
		this.subjectcaption = subjectcaption;
		this.dimcaption = dimcaption;
		this.dimitem = dimitem;
		this.subjectfield = subjectfield;
		this.subjectxml = subjectxml;
		initResult();
//		try {
////			toJSON();
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}


	/**
	 * 将searchResponse中的结果都合并到一起
	 */
	private void initResult() {
		SearchHit[] dimitem_hits = dimitem.getHits().getHits();
		for (int i = 0; i < dimitem_hits.length; i++) {
			results.add(dimitem_hits[i]);
		}
		
		SearchHit[] field_hits = subjectfield.getHits().getHits();
		for (int i = 0; i < field_hits.length; i++) {
			results.add(dimitem_hits[i]);
		}
		
	}

	public List<SearchHit> getResults() {
		return results;
	}
	
	public String getTokenword() {
		return searchword;
	}
	
	public JSONObject toJSON() throws JSONException {
		JSONObject result = new JSONObject();
		result.put("key", this.searchword);
		
		JSONArray resultArr = getAllJsonResult();
		result.put("result", resultArr);
		
		return result;
		
	}


	private JSONArray getAllJsonResult() throws JSONException {
		String type = null;
		type = "dimitem";
		JSONArray resultArr = new JSONArray();
		JSONObject dimcaption = getResultByType("dimcaption");
		JSONObject subjectcaption = getResultByType("subjectcaption");
		JSONObject subjectfield = getResultByType("subjectfield");
		JSONObject dimitem = getResultByType("dimitem");
		
		resultArr.put(dimcaption).put(subjectcaption).put(subjectfield).put(dimitem);
		
		return resultArr;
	}


	private JSONObject getResultByType(String type) throws JSONException {
		SearchResponse sr = null;
		JSONObject results = new JSONObject();
		if ("dimitem".equalsIgnoreCase(type)) {
			sr = dimitem;
			results.put("type", "dimitem");
		} else if ("subjectfield".equalsIgnoreCase(type)) {
			sr = subjectfield;
			results.put("type", "subjectfield");
		} else if ("dimcaption".equalsIgnoreCase(type)) {
			sr = dimcaption;
			results.put("type", "dimcaption");
		} else if ("subjectcaption".equalsIgnoreCase(type)) {
			sr = subjectcaption;
			results.put("type", "subjectcaption");
		}
		
		long total = sr.getHits().getTotalHits();
		double maxscore = sr.getHits().getMaxScore();
		if (Double.isNaN(maxscore)) {
			maxscore = -9999;
		}
		results.put("maxscore", maxscore);
		results.put("total", total);
		
		JSONArray resultArr = new JSONArray();
		if (total == 0) {
			//TODO
		} else {
			
			for (int i = 0; i < sr.getHits().getHits().length; i++) {
				SearchHit hit = sr.getHits().getHits()[i];
				String source = hit.getSourceAsString();
				JSONObject result = new JSONObject(source);
				float score = hit.getScore();
				result.put("score", score);
				resultArr.put(result);
			}
		}
		results.put("results", resultArr);
		return results;
		
	}

	
	
	
	
}
