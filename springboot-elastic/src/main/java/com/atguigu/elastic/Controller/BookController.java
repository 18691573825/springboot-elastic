package com.atguigu.elastic.Controller;

import com.atguigu.elastic.bean.Book;
import com.atguigu.elastic.repository.BookRepository;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BookController {
    @Autowired
    BookRepository bookRepository;

    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;

    /**
     * c创建索引
     * @return
     */
    @PostMapping("/index/create")
    public String createIndex(){
        elasticsearchTemplate.createIndex(Book.class);
        return "success";
    }

    /**
     * 添加文档
     * 修改文档一样，类似saveOrUpdate方法
     * @return
     */
    @PostMapping("/document/add")
    public String addDocument(){
        Book book = new Book();
        book.setId(1);
        book.setBookName("西游记");
        book.setAuthor("吴承恩11");
        bookRepository.save(book);
        return "success";
    }

    /**
     * 删除文档
     * @return
     */
    @PostMapping("/document/delete")
    public String deleteDocument(){
        bookRepository.deleteAll();
        return "success";
    }

    /**
     * id查询
     * @return
     */
    @GetMapping("/document/get")
    public Book findById(@RequestParam("id")Integer id){
        Book book = bookRepository.findOne(id);
        return book;
    }

    /**
     * bookName自定义查询
     * @return
     */
    @GetMapping("/document/find-by-name")
    public List<Book> findByName(@RequestParam("name")String name){
        List<Book> bookList = bookRepository.findByBookNameLike(name);
        return bookList;
    }

    /**
     * bookName自定义查询带分页
     * @return
     */
    @GetMapping("/document/page-by-name")
    public List<Book> findByNamePage(@RequestParam("name")String name){
        Pageable pageable=new PageRequest(0,2);
        List<Book> bookList = bookRepository.findByBookNameLike(name,pageable);
        return bookList;
    }

    /**
     * 测试本地方法  不是同时包含  或的关系
     * @return
     */
    @GetMapping("/document/page-by-name-test-native")
    public List<Book> findByNamePageTestNative(@RequestParam("name")String name){
        NativeSearchQuery nativeSearchQuery= new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.queryStringQuery("西游").defaultField("bookName"))
                .withPageable(new PageRequest(0,2)).build();
        List<Book> bookList = elasticsearchTemplate.queryForList(nativeSearchQuery, Book.class);
        return bookList;
    }
}
