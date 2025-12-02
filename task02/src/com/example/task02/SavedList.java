package com.example.task02;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.AbstractList;
import java.util.List;

public class SavedList<E extends Serializable> extends AbstractList<E> {

    private final File file;
    private final List<E> data = new ArrayList<>();

    public SavedList(File file) {
        this.file = file;
        load();
    }

    @Override
    public E get(int index) {
        return data.get(index);
    }

    @Override
    public E set(int index, E element) {
        E previous = data.set(index, element);
        save();
        return previous;
    }

    @Override
    public int size() {
        return data.size();
    }

    @Override
    public void add(int index, E element) {
        data.add(index, element);
        modCount++;
        save();
    }

    @Override
    public E remove(int index) {
        E removed = data.remove(index);
        modCount++;
        save();
        return removed;
    }

    @Override
    public void clear() {
        if (data.isEmpty()) {
            return;
        }
        data.clear();
        modCount++;
        save();
    }

    private void load() {
        if (!file.exists() || file.length() == 0) {
            return;
        }
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))) {
            @SuppressWarnings("unchecked")
            List<E> loaded = (List<E>) inputStream.readObject();
            data.addAll(loaded);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Cannot load list from file: " + file, e);
        }
    }

    private void save() {
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file))) {
            outputStream.writeObject(new ArrayList<>(data));
        } catch (IOException e) {
            throw new RuntimeException("Cannot save list to file: " + file, e);
        }
    }
}
