package kidsense.kadho.com.kidsense_offline_demo.view;

import kidsense.kadho.com.kidsense_offline_demo.R;
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


import android.app.Activity;
import android.content.res.AssetManager;
import android.view.View;


public class Filter {

    //private static final String PERSON_MODEL_PATH = "en-ner-person.bin";


    //private static final String NAME_MODEL_PATH = "en-ner-names-25k.bin";
    private static final String NAME_MODEL_PATH = "en-ner-new-names-20k.bin";


    //private static final String LOCATION_MODEL_PATH = "en-ner-location.bin";

    //note used atm
    private static final String POS_MODEL_PATH = "en-pos-maxent.bin";


    private static final String PROFANITY_PATH = "profanity.txt";

    private static Filter filter;

    //private static NameFinderME personModel;

    //private static NameFinderME locationModel;

    private static NameFinderME nameModel;


    private static HashSet<String> profanityList = new HashSet<>();

    private static HashSet<String> addressDomains = new HashSet<>(Arrays.asList("alley", "avenue", "backroad",
            "boulevard", "crescent", "court", "drive", "lane", "street", "place", "road", "route", "way"));

    private static HashSet<String> addressIdentifiers = new HashSet<>(Arrays.asList(
            "my address is", "i live at", "send it to", "take me to", "the directions to"));


    private static WordToNumber wtn;

    private Filter() {}

    private static Activity myActivity;


    public static Filter getFilter(Activity a) {
    //public static Filter getFilter(Activity c) {
        if(filter == null) {
            filter = new Filter();
            wtn = new WordToNumber();
            myActivity = a;

            fillProfanityList();

            generateModels();
        }

        return filter;
    }

    public String filterText(String text) {

        text = wtn.replaceNums(text);

        String censored;

        String[] splits = text.split(" ");

        text = "";

        for(String s : splits) {
            if(s.length() > 1) {
                text += s.substring(0, 1).toUpperCase() + s.substring(1) + " ";
            }else {
                text += s.toUpperCase() + " ";
            }
        }

        String[] tokens = createTokens(text);

        //censored = genericEntityFilter(text, tokens, personModel);
        censored = genericEntityFilter(text, tokens, nameModel);

        censored = filterNumbers(censored);
        censored = filterProfanity(censored);
        censored = filterEmail(censored);
        //censored = filterAddress(censored);

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
            stream = myActivity.getAssets().open(path);

            return stream;
        }catch(FileNotFoundException e) {
            e.printStackTrace();
        }catch(IOException e) {
            e.printStackTrace();
        }finally {
            myActivity.getAssets().close();
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
                word = "*****";
            }
            temp += word + " ";
        }
        return temp;
    }

    private static String filterEmail(String text) {
        String regex = "^[a-zA-Z]* at [a-zA-Z]* dot (com|edu|org|net)$";
        return text.replaceAll(regex, "*****");
    }

    private static void fillProfanityList() {
        try {
            //BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(myActivity.getAssets().open(PROFANITY_PATH)));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(myActivity.getAssets().open(PROFANITY_PATH)));

            String currentLine;
            while ((currentLine = bufferedReader.readLine()) != null) {
                profanityList.add(currentLine);
            }
        }catch(IOException e) {
            e.printStackTrace();
        }
    }
 
    private static String filterAddress(String text) {
        String temp = "";
        for (String identifier: addressIdentifiers) {
            if (text.toLowerCase().contains(identifier)) {
                String [] sentence = text.split(" ");
                int start = 0;
                for (int i = 0; i < sentence.length; i++) {
                    if (sentence[i].matches("[0-9]{1,5}")) {
                        sentence[i] = "**";
                        start = i;
                    }
                    else if (addressDomains.contains(sentence[i].toLowerCase())) {
                        sentence[i] = "**";
                        start = 0;
                    }
                    else if (start != 0) {
                        sentence[i] = "**";
                    }
                    temp += sentence[i] + " ";
                }
            }
        }
        return temp;
    }

}
