package com.ly.search.repository;

import com.ly.search.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface GoodRepository extends ElasticsearchRepository<Goods,Long> {
}
