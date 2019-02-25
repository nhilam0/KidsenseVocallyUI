package kidsense.kadho.com.kidsense_offline_demo.view;

import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.util.Span;

import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.postag.POSModel;


import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.namefind.NameFinderME;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.HashSet;
import java.util.Arrays;
import java.util.ArrayList;

import android.content.Context;

public class Filter {

    //private static final String PERSON_MODEL_PATH = "en-ner-person.bin";

    //private static final String NAME_MODEL_PATH = "en-ner-names-uppercase.bin";
    //private static final String NAME_MODEL_PATH = "en-ner-names-lowercase.bin";
    private static final String NAME_MODEL_PATH = "en-ner-first-upper.bin";

    //private static final String LOCATION_MODEL_PATH = "en-ner-location.bin";

    //note used atm
    private static final String POS_MODEL_PATH = "en-pos-maxent.bin";


    private static final String PROFANITY_PATH = "profanity.txt";

    private static Filter filter;

    //private static NameFinderME personModel;

    //private static NameFinderME locationModel;

    private static NameFinderME nameModel;


    private static HashSet<String> profanityList = new HashSet<>();

    private static HashSet<String> emailDomains = new HashSet<>(Arrays.asList(".com", ".org", ".edu",".net"));


    private static WordToNumber wtn;

    private Filter() {}

    private static Context myContext;

    public static Filter getFilter(Context c) {
        if(filter == null) {
            filter = new Filter();
            wtn = new WordToNumber();
            myContext = c;

            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(myContext.getAssets().open(PROFANITY_PATH)));
                String currentLine;
                while ((currentLine = bufferedReader.readLine()) != null) {
                    profanityList.add(currentLine);
                }
            }catch(IOException e) {
                e.printStackTrace();
            }

            //generateModels();
        }

        return filter;
    }

    public String filterText(String text) {

        text = wtn.replaceNums(text);
        String[] tokens = createTokens(text);

        String censored;

        censored = filterNumbers(text);
        censored = filterProfanity(censored);
        //censored = filterEmail(censored);

        //censored = genericEntityFilter(censored, tokens, nameModel);
        //censored = genericEntityFilter(text, tokens, personModel);
        //censored = genericEntityFilter(censored, tokens, locationModel);

        return censored;
    }

    //Generate streams on construction so not continuously re-generated when calling filtertext
    private static void generateModels() {
        //personModel = genericFinderModel(PERSON_MODEL_PATH);
        nameModel = genericFinderModel(NAME_MODEL_PATH);
        //locationModel = genericFinderModel(LOCATION_MODEL_PATH);
    }

    //Create file input stream for given model path
    private static InputStream createStream(String path) {
        InputStream stream = null;
        try {
            //stream = new FileInputStream(path);
            stream = myContext.getAssets().open(path);

            return stream;
        }catch(FileNotFoundException e) {
            e.printStackTrace();
        }catch(IOException e) {
            e.printStackTrace();
        }finally {
            myContext.getAssets().close();
        }

        return stream;
    }

    //Create token array using token model
    private static String[] createTokens(String text) {
        String[] tokens;

        SimpleTokenizer model = SimpleTokenizer.INSTANCE;

        tokens = model.tokenize(text);

        return tokens;
    }


    //Creates a NameFinder model for a given modelpath
    private static NameFinderME genericFinderModel(String modelPath) {
        NameFinderME finder = null;
        InputStream is = null;
        try {
            is = createStream(modelPath);
            if(is != null) {
                TokenNameFinderModel model = new TokenNameFinderModel(is);

                return new NameFinderME(model);
            }

            throw new IOException();

        }catch(IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (is != null)
                    is.close();

            }catch(IOException e) {
                e.printStackTrace();
            }
        }
        return finder;
    }

    //Filters entities based on a supplied finder model
    private static String genericEntityFilter(String text, String[] tokens, NameFinderME finder) {

        Span[] nameSpans = finder.find(tokens);

        String[] spans = Span.spansToStrings(nameSpans, tokens);

        String censored = text;
        for(int i = 0; i < spans.length; i++) {
            censored = censored.replace(spans[i], "*****");
        }

        return censored;
    }

    private static String filterNumbers(String text) {

        text = text.replaceAll("[0-9]{9}", "*****");

        return text.replaceAll("[0-9]{10}", "*****");
    }

    //Not used ATM
    private static String[] findPOS(String[] tokens) {
        String[] tags = null;
        try {
            POSModel model = new POSModel(createStream(POS_MODEL_PATH));

            POSTaggerME tagger = new POSTaggerME(model);

            tags = tagger.tag(tokens);

            return tags;
        }catch(IOException e) {
            e.printStackTrace();
        }

        return tags;
    }


    //Randys Code
    private static String filterProfanity(String text) {
        String [] split = text.split(" ");
        String temp = "";
        for (String word: split) {
            if (profanityList.contains(word.toLowerCase())) {
                word = "***";
            }
            temp += word + " ";
        }
        return temp;
    }

    private static String filterEmail(String text) {
        String [] split = text.split(" ");
        String temp = "";
        //EMAIL AS TEXT INPUT
        /*
        for (String word: split) {
            if (word.contains("@")) {
                for (String email: emailDomains) {
                    if (word.contains(email)) {
                        word = "***";
                    }
                }
            }
            temp += word + " ";
        }
        */
        //ASR INPUT (username identified as one word)
        ArrayList<String> sentence = new ArrayList<>();
        for (int i = 0; i < split.length; i ++) {
            if (split[i].equals("dot") && split[i-2].equals("at")) {
                sentence.set(i-3, "*****");
                sentence.set(i-2, "*****");
                sentence.set(i-1, "*****");
                sentence.add("*****");
                sentence.add("*****");
                i++;
            }
            else {
                sentence.add(split[i]);
            }
        }
        for (String word: sentence) {
            temp += word + " ";
        }
        return temp;
    }
}
