package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractTerm;

import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public class Term extends AbstractTerm {
    public Term() { super(); }
    public Term(String content) { super(content); }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Term)) return false;
        Term other = (Term) obj;
        return this.content != null && other.content != null && this.content.equals(other.content);
    }

    @Override
    public String toString() {
        return this.content;
    }

    @Override
    public String getContent() {
        return this.content;
    }

    @Override
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int compareTo(AbstractTerm o) {
        if (o == null) return 1;
        if (this.content == null && o.getContent() == null) return 0;
        if (this.content == null) return -1;
        if (o.getContent() == null) return 1;
        return this.content.compareTo(o.getContent());
    }

    @Override
    public void writeObject(java.io.ObjectOutputStream out) {
        try {
            out.writeObject(this.content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void readObject(java.io.ObjectInputStream in) {
        try {
            this.content = (String) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}