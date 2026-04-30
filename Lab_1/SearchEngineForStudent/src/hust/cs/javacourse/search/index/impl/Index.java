package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractDocument;
import hust.cs.javacourse.search.index.AbstractIndex;
import hust.cs.javacourse.search.index.AbstractPosting;
import hust.cs.javacourse.search.index.AbstractPostingList;
import hust.cs.javacourse.search.index.AbstractTerm;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * AbstractIndex的具体实现类
 */
public class Index extends AbstractIndex {
    /**
     * 返回索引的字符串表示
     *
     * @return 索引的字符串表示
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<AbstractTerm, AbstractPostingList> entry : termToPostingListMapping.entrySet()) {
            sb.append(entry.getKey()).append(" -> ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }

    /**
     * 添加文档到索引，更新索引内部的HashMap
     *
     * @param document ：文档的AbstractDocument子类型表示
     */
    @Override
    public void addDocument(AbstractDocument document) {
        if (document == null) {
            return;
        }
        int docId = document.getDocId();
        docIdToDocPathMapping.put(docId, document.getDocPath());

        for (int i = 0; i < document.getTupleSize(); i++) {
            hust.cs.javacourse.search.index.AbstractTermTuple tuple = document.getTuple(i);
            AbstractTerm term = tuple.term;

            AbstractPostingList postingList = termToPostingListMapping.get(term);
            if (postingList == null) {
                postingList = new PostingList();
                termToPostingListMapping.put(term, postingList);
            }

            int postingIndex = postingList.indexOf(docId);
            if (postingIndex >= 0) {
                AbstractPosting posting = postingList.get(postingIndex);
                posting.setFreq(posting.getFreq() + 1);
                List<Integer> positions = posting.getPositions();
                positions.add(tuple.curPos);
                posting.setPositions(positions);
            } else {
                List<Integer> positions = new ArrayList<>();
                positions.add(tuple.curPos);
                postingList.add(new Posting(docId, 1, positions));
            }
        }
    }

    /**
     * <pre>
     * 从索引文件里加载已经构建好的索引.内部调用FileSerializable接口方法readObject即可
     * @param file ：索引文件
     * </pre>
     */
    @Override
    public void load(File file) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            readObject(in);
        } catch (IOException e) {
            throw new RuntimeException("Load index failed: " + file.getAbsolutePath(), e);
        }
    }

    /**
     * <pre>
     * 将在内存里构建好的索引写入到文件. 内部调用FileSerializable接口方法writeObject即可
     * @param file ：写入的目标索引文件
     * </pre>
     */
    @Override
    public void save(File file) {
        File parent = file.getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs()) {
            throw new RuntimeException("Create index directory failed: " + parent.getAbsolutePath());
        }
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            writeObject(out);
        } catch (IOException e) {
            throw new RuntimeException("Save index failed: " + file.getAbsolutePath(), e);
        }
    }

    /**
     * 返回指定单词的PostingList
     *
     * @param term : 指定的单词
     * @return ：指定单词的PostingList;如果索引字典没有该单词，则返回null
     */
    @Override
    public AbstractPostingList search(AbstractTerm term) {
        return termToPostingListMapping.get(term);
    }

    /**
     * 返回索引的字典.字典为索引里所有单词的并集
     *
     * @return ：索引中Term列表
     */
    @Override
    public Set<AbstractTerm> getDictionary() {
        return new TreeSet<>(termToPostingListMapping.keySet());
    }

    /**
     * <pre>
     * 对索引进行优化，包括：
     *      对索引里每个单词的PostingList按docId从小到大排序
     *      同时对每个Posting里的positions从小到大排序
     * 在内存中把索引构建完后执行该方法
     * </pre>
     */
    @Override
    public void optimize() {
        for (AbstractPostingList postingList : termToPostingListMapping.values()) {
            postingList.sort();
            for (int i = 0; i < postingList.size(); i++) {
                postingList.get(i).sort();
            }
        }
    }

    /**
     * 根据docId获得对应文档的完全路径名
     *
     * @param docId ：文档id
     * @return : 对应文档的完全路径名
     */
    @Override
    public String getDocName(int docId) {
        return docIdToDocPathMapping.get(docId);
    }

    /**
     * 写到二进制文件
     *
     * @param out :输出流对象
     */
    @Override
    public void writeObject(ObjectOutputStream out) {
        try {
            out.writeInt(docIdToDocPathMapping.size());
            for (Map.Entry<Integer, String> entry : docIdToDocPathMapping.entrySet()) {
                out.writeInt(entry.getKey());
                out.writeObject(entry.getValue());
            }

            out.writeInt(termToPostingListMapping.size());
            for (Map.Entry<AbstractTerm, AbstractPostingList> entry : termToPostingListMapping.entrySet()) {
                entry.getKey().writeObject(out);
                entry.getValue().writeObject(out);
            }
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException("Write index object failed.", e);
        }
    }

    /**
     * 从二进制文件读
     *
     * @param in ：输入流对象
     */
    @Override
    public void readObject(ObjectInputStream in) {
        try {
            docIdToDocPathMapping.clear();
            termToPostingListMapping.clear();

            int docCount = in.readInt();
            for (int i = 0; i < docCount; i++) {
                int docId = in.readInt();
                String docPath = (String) in.readObject();
                docIdToDocPathMapping.put(docId, docPath);
            }

            int termCount = in.readInt();
            for (int i = 0; i < termCount; i++) {
                Term term = new Term();
                term.readObject(in);
                PostingList postingList = new PostingList();
                postingList.readObject(in);
                termToPostingListMapping.put(term, postingList);
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Read index object failed.", e);
        }
    }
}
