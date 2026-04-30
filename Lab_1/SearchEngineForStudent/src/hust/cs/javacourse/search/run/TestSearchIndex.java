package hust.cs.javacourse.search.run;

import hust.cs.javacourse.search.index.impl.Term;
import hust.cs.javacourse.search.query.AbstractHit;
import hust.cs.javacourse.search.query.AbstractIndexSearcher;
import hust.cs.javacourse.search.query.impl.IndexSearcher;
import hust.cs.javacourse.search.query.impl.SimpleSorter;
import hust.cs.javacourse.search.util.Config;

/**
 * 测试搜索
 */
public class TestSearchIndex {
    /**
     * 搜索程序入口
     *
     * @param args ：命令行参数
     */
    public static void main(String[] args) {
        AbstractIndexSearcher searcher = new IndexSearcher();
        searcher.open(Config.INDEX_DIR + "index.dat");

        AbstractHit[] hits = searcher.search(new Term("aaa"), new SimpleSorter());
        System.out.println("Single term query: aaa");
        for (AbstractHit hit : hits) {
            System.out.println(hit);
        }

        System.out.println("Two-term AND query: aaa AND fff");
        AbstractHit[] andHits = searcher.search(
                new Term("aaa"),
                new Term("fff"),
                new SimpleSorter(),
                AbstractIndexSearcher.LogicalCombination.AND
        );
        for (AbstractHit hit : andHits) {
            System.out.println(hit);
        }
    }
}
