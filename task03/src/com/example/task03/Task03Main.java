package com.example.task03;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Set;
import java.util.Locale;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeSet;
import java.util.Arrays;

public class Task03Main {

    public static void main(String[] args) throws IOException {

        List<Set<String>> anagrams = findAnagrams(new FileInputStream("task03/resources/singular.txt"), Charset.forName("windows-1251"));
        for (Set<String> anagram : anagrams) {
            System.out.println(anagram);
        }

    }

    public static List<Set<String>> findAnagrams(InputStream inputStream, Charset charset) {
        Map<String, Set<String>> groups = new HashMap<>();
        Set<String> uniqueWords = new HashSet<>();
        Locale localeRu = new Locale("ru");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, charset))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String word = line.trim().toLowerCase(localeRu);
                if (word.length() < 3) {
                    continue;
                }
                if (!word.matches("[а-яё]+")) {
                    continue;
                }
                if (!uniqueWords.add(word)) {
                    continue;
                }
                String key = sortedLetters(word);
                groups.computeIfAbsent(key, k -> new TreeSet<>()).add(word);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<Set<String>> result = new ArrayList<>();
        for (Set<String> set : groups.values()) {
            if (set.size() >= 2) {
                result.add(set);
            }
        }
        result.sort(Comparator.comparing(set -> set.iterator().next()));
        return result;
    }

    private static String sortedLetters(String word) {
        char[] chars = word.toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }
}
