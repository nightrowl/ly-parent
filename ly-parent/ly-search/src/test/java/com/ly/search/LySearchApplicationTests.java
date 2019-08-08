package com.ly.search;

import com.ly.common.vo.PageResult;
import com.ly.item.Spu;
import com.ly.search.client.GoodsClient;
import com.ly.search.pojo.Goods;
import com.ly.search.repository.GoodRepository;
import com.ly.search.service.SearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LySearchApplicationTests {

	@Autowired
	GoodRepository goodRepository;

	@Autowired
	ElasticsearchTemplate elasticsearchTemplate;

	@Autowired
	SearchService searchService;

	@Autowired
	GoodsClient goodsClient;

	@Test
	public void contextLoads() {
		elasticsearchTemplate.createIndex(Goods.class);
		elasticsearchTemplate.putMapping(Goods.class);
	}

	@Test
	public void loadData() throws Exception{

		Integer page = 1;
		Integer row = 100;
		int size = 1;

		ResponseEntity<List<Spu>> listResponseEntity = goodsClient.queryCount();
		List<Spu> body = listResponseEntity.getBody();
		Integer total = body.size();
		//分页查询数据
		for (int i = 0 ; i <size ; i++ ){
			PageResult<Spu> result = goodsClient.querySpuByPage(page, row, null, true);
			List<Spu> spus = result.getItems();
			total = total - row;
			if(total > 0){
				size ++ ;
				page ++ ;
			}
			//创建Goods集合
			List<Goods> goodsList = new ArrayList<>();
			//遍历spu
			for (Spu spu : spus){
				try {
					Goods goods = this.searchService.buildGoods(spu);
					goodsList.add(goods);
				} catch (IOException e) {
					break;
				}
			}
			goodRepository.saveAll(goodsList);
		}

	}

}
