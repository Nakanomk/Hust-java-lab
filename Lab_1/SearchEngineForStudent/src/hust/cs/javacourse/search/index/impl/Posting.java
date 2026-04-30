package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractPosting;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Posting extends AbstractPosting {
    public Posting() {

    }

    public Posting(int docId, int freq, List<Integer> positions) {
        this.docId = docId;
        this.freq = freq;
        this.positions = positions;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(!(obj instanceof AbstractPosting)) return false;

        AbstractPosting other = (AbstractPosting) obj;
        if (this.docId != other.getDocId()) return false;
        if (this.freq != other.getFreq()) return false;

        List<Integer> p1 = this.positions;
        List<Integer> p2 = other.getPositions();

        if (p1 == null || p2 == null) return p1 == p2;
        if (p1.size() != p2.size()) return false;

        List<Integer> s1 = new ArrayList<>(p1);
        List<Integer> s2 = new ArrayList<>(p2);
        Collections.sort(s1);
        Collections.sort(s2);

        return s1.equals(s2);
    }

    @Override
    public String toString() {
        return "Posting docId=" + this.docId + ", freq=" + this.freq + ", positions=" + positions;
    }

    @Override
    public int getDocId() {
        return this.docId;
    }

    @Override
    public void setDocId(int docId) {
        this.docId = docId;
    }

    @Override
    public int getFreq() {
        return this.freq;
    }

    @Override
    public void setFreq(int freq) {
        this.freq = freq;
    }

    @Override
    public List<Integer> getPositions() {
        return positions;
    }

    @Override
    public void setPositions(List<Integer> positions) {
        this.positions = positions;
    }

    @Override
    public int compareTo(AbstractPosting o) {
        return this.docId - o.getDocId();
    }

    @Override
    public void sort() {
        Collections.sort(this.positions);
    }

    @Override
    public void writeObject(ObjectOutputStream out) {
        try {
            out.writeInt(this.docId);
            out.writeInt(this.freq);
            out.writeInt(this.positions.size());
            for (Integer pos : this.positions) {
                out.writeInt(pos);
            }
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException("write Posting failed", e);
        }
    }
//        try {
//            out.writeInt(this.docId);
//            out.writeInt(this.freq);
//            out.writeInt(this.positions.size());
//            for (Integer pos : this.positions) {
//                out.writeInt(pos);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    @Override
    public void readObject(ObjectInputStream in) {
        try {
            this.docId = in.readInt();
            this.freq = in.readInt();
            int size = in.readInt();
            this.positions = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                this.positions.add(in.readInt());
            }
        } catch (IOException e) {
            throw new RuntimeException("read Posting failed", e);
        }
//        try {
//            this.docId = in.readInt();
//            this.freq = in.readInt();
//            int size = in.readInt();
//
//            this.positions = new ArrayList<>();
//            for (int i = 0; i < size; i++) {
//                this.positions.add(in.readInt());
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    }
}