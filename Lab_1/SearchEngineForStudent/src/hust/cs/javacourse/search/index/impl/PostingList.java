package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractPosting;
import hust.cs.javacourse.search.index.AbstractPostingList;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.List;

public class PostingList extends AbstractPostingList {

    @Override
    public void add(AbstractPosting posting) {
        if (posting == null) return;
        if (!this.list.contains(posting)) {
            this.list.add(posting);
        }
    }

    @Override
    public void add(List<AbstractPosting> postings) {
        if (postings == null) return;
        for (AbstractPosting posting : postings) {
            this.add(posting);
        }
    }

    @Override
    public String toString() {
        if (this.list == null) return "";
        String output = "";
        for (AbstractPosting posting : this.list) {
            output = output + " " + posting;
        }
        return output;
    }

    @Override
    public AbstractPosting get(int index) {
        if (index < 0 || index >= this.list.size()) return null;
        return this.list.get(index);
    }

    @Override
    public int indexOf(AbstractPosting posting) {
        if (posting == null) return -1;
        for (int i = 0; i < this.list.size(); i++) {
            if (this.list.get(i).equals(posting)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int indexOf(int docId) {
        for (int i = 0; i < this.list.size(); i++) {
            if (this.list.get(i).getDocId() == docId) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean contains(AbstractPosting posting) {
        return this.indexOf(posting) != -1;
    }

    @Override
    public void remove(int index) {
        if (index < 0 || index >= this.list.size()) return;
        this.list.remove(index);
    }

    @Override
    public void remove(AbstractPosting posting) {
        int index = this.indexOf(posting);
        if (index != -1) {
            this.list.remove(index);
        }
    }

    @Override
    public int size() {
        return this.list.size();
    }

    @Override
    public void clear() {
        this.list.clear();
    }

    @Override
    public boolean isEmpty() {
        return this.list.isEmpty();
    }

    @Override
    public void sort() {
        Collections.sort(this.list);
    }

    @Override
    public void writeObject(ObjectOutputStream out) {
        try {
            out.writeInt(this.list.size());
            for (AbstractPosting posting : this.list) {
                posting.writeObject(out);
            }
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException("write PostingList failed", e);
        }
    }
//        try {
//            out.writeInt(this.list.size());
//            for (AbstractPosting posting : this.list) {
//                posting.writeObject(out);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    @Override
    public void readObject(ObjectInputStream in) {
        try {
            int size = in.readInt();
            this.list.clear();
            for (int i = 0; i < size; i++) {
                Posting posting = new Posting();
                posting.readObject(in);
                this.list.add(posting);
            }
        } catch (IOException e) {
            throw new RuntimeException("read PostingList failed", e);
        }
//        try {
//            int size = in.readInt();
//            this.list.clear();
//            for (int i = 0; i < size; i++) {
//                Posting posting = new Posting();
//                posting.readObject(in);
//                this.list.add(posting);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    }
}