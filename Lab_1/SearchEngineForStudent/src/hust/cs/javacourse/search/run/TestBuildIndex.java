package hust.cs.javacourse.search.run;

import hust.cs.javacourse.search.index.AbstractDocumentBuilder;
import hust.cs.javacourse.search.index.AbstractIndex;
import hust.cs.javacourse.search.index.AbstractIndexBuilder;
import hust.cs.javacourse.search.index.impl.DocumentBuilder;
import hust.cs.javacourse.search.index.impl.IndexBuilder;
import hust.cs.javacourse.search.util.Config;

import java.io.File;

/**
 * 测试索引构建
 */
public class TestBuildIndex {
    /**
     *  索引构建程序入口
     * @param args : 命令行参数
     */
    public static void main(String[] args){
        AbstractDocumentBuilder docBuilder = new DocumentBuilder();
        AbstractIndexBuilder indexBuilder = new IndexBuilder(docBuilder);
        AbstractIndex index = indexBuilder.buildIndex(Config.DOC_DIR);
        File indexFile = new File(Config.INDEX_DIR + "index.dat");
        index.save(indexFile);
        System.out.println("Index built and saved to: " + indexFile.getAbsolutePath());
        System.out.println(index);

    }
}
