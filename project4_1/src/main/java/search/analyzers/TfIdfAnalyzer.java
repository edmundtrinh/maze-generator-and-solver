package search.analyzers;

import datastructures.concrete.KVPair;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import datastructures.interfaces.ISet;
import search.models.Webpage;

import java.net.URI;

/**
 * This class is responsible for computing how "relevant" any given document is
 * to a given search query.
 *
 * See the spec for more details.
 */
public class TfIdfAnalyzer {
    // This field must contain the IDF score for every single word in all
    // the documents.
    private IDictionary<String, Double> idfScores;

    // This field must contain the TF-IDF vector for each webpage you were given
    // in the constructor.
    //
    // We will use each webpage's page URI as a unique key.
    private IDictionary<URI, IDictionary<String, Double>> documentTfIdfVectors;
    private IDictionary<URI, Double> documentNormVectors;
    // Feel free to add extra fields and helper methods.

    public TfIdfAnalyzer(ISet<Webpage> webpages) {
        // Implementation note: We have commented these method calls out so your
        // search engine doesn't immediately crash when you try running it for the
        // first time.
        //
        // You should uncomment these lines when you're ready to begin working
        // on this class.

        this.idfScores = this.computeIdfScores(webpages);
        this.documentTfIdfVectors = this.computeAllDocumentTfIdfVectors(webpages);
        this.documentNormVectors = new ChainedHashDictionary<>();
        for (KVPair<URI, IDictionary<String, Double>> pair : documentTfIdfVectors) {
            this.documentNormVectors.put(pair.getKey(), this.norm(pair.getValue()));
        }
    }

    // Note: this method, strictly speaking, doesn't need to exist. However,
    // we've included it so we can add some unit tests to help verify that your
    // constructor correctly initializes your fields.
    public IDictionary<URI, IDictionary<String, Double>> getDocumentTfIdfVectors() {
        return this.documentTfIdfVectors;
    }

    // Note: these private methods are suggestions or hints on how to structure your
    // code. However, since they're private, you're not obligated to implement exactly
    // these methods: feel free to change or modify these methods however you want. The
    // important thing is that your 'computeRelevance' method ultimately returns the
    // correct answer in an efficient manner.

    /**
     * Return a dictionary mapping every single unique word found
     * in every single document to their IDF score.
     */
    private IDictionary<String, Double> computeIdfScores(ISet<Webpage> pages) {
        IDictionary<String, Double> arrDict = new ChainedHashDictionary<>();
        for (Webpage wp : pages) {
            IDictionary<String, Double> tempDict = new ChainedHashDictionary<>();
            IList<String> words = wp.getWords();
            for (String str : words) {
                if (!tempDict.containsKey(str)) {
                    tempDict.put(str, 1.0);
                }
            }
            for (KVPair<String, Double> temp : tempDict) {
                if (!arrDict.containsKey(temp.getKey())) {
                    arrDict.put(temp.getKey(), 1.0);
                } else {
                    arrDict.put(temp.getKey(), arrDict.get(temp.getKey()) + 1.0);
                }
            }
        }  
        for (KVPair<String, Double> temp : arrDict) {
            arrDict.put(temp.getKey(), Math.log(((double) pages.size())/temp.getValue()));
        }
        return arrDict;
    }

    /**
     * Returns a dictionary mapping every unique word found in the given list
     * to their term frequency (TF) score.
     *
     * The input list represents the words contained within a single document.
     */
    private IDictionary<String, Double> computeTfScores(IList<String> words) {
        IDictionary<String, Double> arrDict = new ChainedHashDictionary<>();
        double totalWords = 0.0;
        for (String str : words) {
            totalWords++;
            if (!arrDict.containsKey(str)) {
                arrDict.put(str, 1.0);
            } else {
                arrDict.put(str, arrDict.get(str) + 1.0);
            }
        }
        for (KVPair<String, Double> temp : arrDict) {
            arrDict.put(temp.getKey(), temp.getValue()/totalWords);
        }
        return arrDict;
    }

    private IDictionary<String, Double> computeTfIdfVector(IList<String> words) {
        IDictionary<String, Double> tempArr = new ChainedHashDictionary<>();
        IDictionary<String, Double> temp = computeTfScores(words);
        for (KVPair<String, Double> tempPair : temp) {
            if (idfScores.containsKey(tempPair.getKey())) {
                tempArr.put(tempPair.getKey(), temp.get(tempPair.getKey()) * idfScores.get(tempPair.getKey()));
            } else {
                tempArr.put(tempPair.getKey(), 0.0);
            }
        }
        return tempArr;
    }
    
    /**
     * See spec for more details on what this method should do.
     */
    private IDictionary<URI, IDictionary<String, Double>> computeAllDocumentTfIdfVectors(ISet<Webpage> pages) {
        // Hint: this method should use the idfScores field and
        // call the computeTfScores(...) method.
        IDictionary<URI, IDictionary<String, Double>> returnArr = new ChainedHashDictionary<>();
        for (Webpage wp : pages) {
            URI tempURI = wp.getUri();
            returnArr.put(tempURI, computeTfIdfVector(wp.getWords()));
        }
        return returnArr;
    }

    /**
     * Returns the cosine similarity between the TF-IDF vector for the given query and the
     * URI's document.
     *
     * Precondition: the given uri must have been one of the uris within the list of
     *               webpages given to the constructor.
     */
    public Double computeRelevance(IList<String> query, URI pageUri) {
        // Note: The pseudocode we gave you is not very efficient. When implementing,
        // this method, you should:
        //
        // 1. Figure out what information can be precomputed in your constructor.
        //    Add a third field containing that information.
        //
        // 2. See if you can combine or merge one or more loops.
        
        IDictionary<String, Double> documentVector = this.documentTfIdfVectors.get(pageUri);
        IDictionary<String, Double> queryVector = computeTfIdfVector(query);

        double numerator = 0.0;
        for (String word : query) {
            double docWordScore = 0.0;
            double queryWordScore = 0.0;
            if (documentVector.containsKey(word)) {
                docWordScore = documentVector.get(word);
            }
            queryWordScore = queryVector.get(word);
            numerator += docWordScore * queryWordScore;
        }
        double denominator = documentNormVectors.get(pageUri) * norm(queryVector);

        if (denominator != 0) {
            return numerator / denominator;
        }
        return 0.0;
    }
    
    private double norm(IDictionary<String, Double> vector) {
        
        double output = 0.0;
        double score = 0.0;
        for (KVPair<String, Double> pair : vector) {
            score = pair.getValue();
            output += score * score;
        }
        return Math.sqrt(output);
    }
}
