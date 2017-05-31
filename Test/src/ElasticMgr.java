import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

/**
 * ELASTICSEARCH 管理
 * @author yi
 * @date 2017年5月23日
 */
public class ElasticMgr {
	private static ElasticMgr instance = null;
	
	private static final int PORT = 9300;
	private static final String IP = "172.21.4.56";
	
	private static final String INDEXNAME = "zw-ansj";
	
	
	
	private TransportClient client = null;
	
	
	public static synchronized ElasticMgr getInstance() {
		if (instance == null) {
			instance = new ElasticMgr();
		}
		return instance;
	}
	
	/**
	 * 获取客户端连接
	 * @return
	 * @throws UnknownHostException
	 */
	public TransportClient getClient() throws UnknownHostException {
		
		if (client == null) {
			client = new PreBuiltTransportClient(Settings.EMPTY)
					.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(IP), PORT));
		}
		return client;
	}
	
	public SearchResponse searchByType(String type, String field, String searchword) throws UnknownHostException {
		SearchResponse sr = getClient().prepareSearch(INDEXNAME)
				 .setQuery(QueryBuilders.matchQuery(field, searchword)).setTypes(type).setSize(50).get();
		
		
		return sr;
	}
	
	public void testFunctionScore() {
//       QueryBuilders.functionScoreQuery(queryBuilder, function)
    }
	
	/**
	 * 搜索得到一个词的所有结果，封装成对象
	 * @param searchword
	 * @return
	 * @throws UnknownHostException
	 */
	public Token search (String searchword) throws UnknownHostException {
		long t1 = System.currentTimeMillis();
		
		SearchResponse sr_subjectcaption = searchByType(ElasticConst.TYPE_SUBJECT, "caption", searchword);
		SearchResponse sr_dimcaption = searchByType(ElasticConst.TYPE_DIM, "caption", searchword);
		SearchResponse sr_subject = searchByType(ElasticConst.TYPE_SUBJECTXML, "caption", searchword);
		SearchResponse sr_dimitem = searchByType(ElasticConst.TYPE_DIMITEM, "caption", searchword);
		SearchResponse sr_subject_field = searchByType(ElasticConst.TYPE_SUBJECTFIELD, "caption", searchword);
		
		System.out.println("searchword: " + searchword);
		System.out.println("hits subjectxml: " + sr_subject.getHits().getTotalHits() + ", max score: " + sr_subject.getHits().getMaxScore());
		System.out.println("hits dimitem: " + sr_dimitem.getHits().getTotalHits() + ", max score: " + sr_dimitem.getHits().getMaxScore());
		System.out.println("hits subjectfield: " + sr_subject_field.getHits().getTotalHits() + ", max score: " + sr_subject_field.getHits().getMaxScore());
		long t2 = System.currentTimeMillis();
		System.out.println("Query time: " + (t2 - t1));
		
		Token token = new Token(searchword,sr_subjectcaption, sr_dimcaption, sr_dimitem, sr_subject_field, sr_subject);
		return token;
	}
	
	public void close() {
		if (client != null) {
			client.close();
		}
	}
	
	
}
