package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractDocument;
import hust.cs.javacourse.search.index.AbstractDocumentBuilder;
import hust.cs.javacourse.search.index.AbstractIndex;
import hust.cs.javacourse.search.index.AbstractIndexBuilder;
import hust.cs.javacourse.search.util.FileUtil;

import java.io.File;
import java.util.List;

public class IndexBuilder extends AbstractIndexBuilder {

    public IndexBuilder(AbstractDocumentBuilder docBuilder) {
        super(docBuilder);
    }

    @Override
    public AbstractIndex buildIndex(String rootDirectory) {
        AbstractIndex index = new Index();

        List<String> filePaths = FileUtil.list(rootDirectory);

        for(String filePath : filePaths) {
            File file = new File(filePath);

            int currentDocId = docId++;

            AbstractDocument doc = docBuilder.build(currentDocId, filePath, file);

            index.addDocument(doc);
        }

        index.optimize();

        return index;
    }

}
