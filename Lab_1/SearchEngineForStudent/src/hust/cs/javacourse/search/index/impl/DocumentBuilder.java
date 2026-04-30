package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractDocument;
import hust.cs.javacourse.search.index.AbstractDocumentBuilder;
import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleStream;
import hust.cs.javacourse.search.parse.impl.LengthTermTupleFilter;
import hust.cs.javacourse.search.parse.impl.PatternTermTupleFilter;
import hust.cs.javacourse.search.parse.impl.StopWordTermTupleFilter;
import hust.cs.javacourse.search.parse.impl.TermTupleScanner;
import hust.cs.javacourse.search.util.Config;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class DocumentBuilder extends AbstractDocumentBuilder {

    @Override
    public AbstractDocument build(int docId, String docPath, AbstractTermTupleStream termTupleStream) {
        Document doc = new Document(docId, docPath);
        AbstractTermTuple tuple;
        while((tuple = termTupleStream.next()) != null) {
            doc.addTuple(tuple);
        }
        termTupleStream.close();
        return doc;
    }

    @Override
    public AbstractDocument build(int docId, String docPath, File file) {
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)
            );
            AbstractTermTupleStream stream = new TermTupleScanner(reader);
            stream = new PatternTermTupleFilter(stream);
            stream = new LengthTermTupleFilter(stream);
            stream = new StopWordTermTupleFilter(stream);
            return build(docId, docPath, stream);
        } catch (IOException e) {
            throw new RuntimeException("Build Document failed: " + file.getAbsolutePath(), e);
        }
    }
}
