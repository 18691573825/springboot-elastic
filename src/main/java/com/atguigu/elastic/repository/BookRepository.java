package com.atguigu.elastic.repository;

import com.atguigu.elastic.bean.Book;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;


public interface BookRepository extends ElasticsearchRepository<Book,Integer> {

   //带like差不多类似加了分词器，不加就是几个分词的and关系
   List<Book> findByBookNameLike(String bookName);
   List<Book> findByBookNameLike(String bookName, Pageable pageable);
}
