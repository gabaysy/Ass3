package bgu.spl.net.srv.BGS;

import java.util.HashSet;
import java.util.Set;

public class FilteredWords {
    private static HashSet<String> toFilter = new HashSet<String>(){{
        add("fuck");
        add("ass");
        //HardCoded words
    }};

    public static boolean isWordFiltered(String word){
        return toFilter.contains(word);
    }
    public static Set<String> wordsToFilter(){
        return toFilter;
    }
}
