package search.analyzers;

import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.KVPair;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.ISet;
import search.models.Webpage;

import java.net.URI;

/**
 * This class is responsible for computing the 'page rank' of all available webpages.
 * If a webpage has many different links to it, it should have a higher page rank.
 * See the spec for more details.
 */
public class PageRankAnalyzer {
    private IDictionary<URI, Double> pageRanks;
    
    /**
     * Computes a graph representing the internet and computes the page rank of all
     * available webpages.
     *
     * @param webpages  A set of all webpages we have parsed.
     * @param decay     Represents the "decay" factor when computing page rank (see spec).
     * @param epsilon   When the difference in page ranks is less then or equal to this number,
     *                  stop iterating.
     * @param limit     The maximum number of iterations we spend computing page rank. This value
     *                  is meant as a safety valve to prevent us from infinite looping in case our
     *                  page rank never converges.
     */
    public PageRankAnalyzer(ISet<Webpage> webpages, double decay, double epsilon, int limit) {
        // Implementation note: We have commented these method calls out so your
        // search engine doesn't immediately crash when you try running it for the
        // first time.
        //
        // You should uncomment these lines when you're ready to begin working
        // on this class.

        // Step 1: Make a graph representing the 'internet'
        IDictionary<URI, ISet<URI>> graph = this.makeGraph(webpages);

        // Step 2: Use this graph to compute the page rank for each webpage
        this.pageRanks = this.makePageRanks(graph, decay, limit, epsilon);

        // Note: we don't store the graph as a field: once we've computed the
        // page ranks, we no longer need it!
    }

    /**
     * This method converts a set of webpages into an unweighted, directed graph,
     * in adjacency list form.
     *
     * You may assume that each webpage can be uniquely identified by its URI.
     *
     * Note that a webpage may contain links to other webpages that are *not*
     * included within set of webpages you were given. You should omit these
     * links from your graph: we want the final graph we build to be
     * entirely "self-contained".
     */
    private IDictionary<URI, ISet<URI>> makeGraph(ISet<Webpage> webpages) {
        IDictionary<URI, ISet<URI>> graph = new ChainedHashDictionary<>();
        ISet<URI> uris = new ChainedHashSet<>();
        for (Webpage wp : webpages) {
            uris.add(wp.getUri());
        }
        for (Webpage wp : webpages) {
            ISet<URI> tempSet = new ChainedHashSet<>();
            for (URI pageUri : wp.getLinks()) {
                if ((!pageUri.equals(wp.getUri())) && (!tempSet.contains(pageUri))
                        && (uris.contains(pageUri))) {
                    tempSet.add(pageUri);
                }
            }
            graph.put(wp.getUri(), tempSet);
        }
        return graph;
    }

    /**
     * Computes the page ranks for all webpages in the graph.
     *
     * Precondition: assumes 'this.graphs' has previously been initialized.
     *
     * @param decay     Represents the "decay" factor when computing page rank (see spec).
     * @param epsilon   When the difference in page ranks is less then or equal to this number,
     *                  stop iterating.
     * @param limit     The maximum number of iterations we spend computing page rank. This value
     *                  is meant as a safety valve to prevent us from infinite looping in case our
     *                  page rank never converges.
     */
    private IDictionary<URI, Double> makePageRanks(IDictionary<URI, ISet<URI>> graph,
                                                   double decay,
                                                   int limit,
                                                   double epsilon) {
        // Step 1: The initialize step should go here
        pageRanks = new ChainedHashDictionary<>();
        IDictionary<URI, Double> newPageRanks = new ChainedHashDictionary<>();
        for (KVPair<URI, ISet<URI>> pair : graph) {
            this.pageRanks.put(pair.getKey(), 1.0/((double) graph.size()));
            newPageRanks.put(pair.getKey(), (1.0 - decay)/((double) graph.size()));
        }
        for (int i = 0; i < limit; i++) {
            // Step 2: The update step should go here
            
            // For each URI in the graph
            for (KVPair<URI, ISet<URI>> pair : graph) {
                // For each URI link
                for (URI pageUri : pair.getValue()) {
                    // Add to newPageRanks at pageUri, 
                    // current newPageRanks value + decay * (old pageRanks value / # of unique links)
                    newPageRanks.put(pageUri, newPageRanks.get(pageUri)
                          + decay * (pageRanks.get(pair.getKey()) / ((double) pair.getValue().size())));
                }
                
                // if no links in page, add to all pages (old pageRanks value / total URIs)
                if (pair.getValue().isEmpty()) { 
                    for (KVPair<URI, Double> pair2 : newPageRanks) {
                        newPageRanks.put(pair2.getKey(), pair2.getValue()
                                + decay * (pageRanks.get(pair.getKey()) / ((double) pageRanks.size())));
                    }
                }
            }
            
            // Step 3: the convergence step should go here.
            int j = 0;
            for (KVPair<URI, Double> pair : newPageRanks) {
                if (!(pair.getValue() < (pageRanks.get(pair.getKey()) + epsilon)) 
                        || !(pair.getValue() > (pageRanks.get(pair.getKey()) - epsilon))){
                    break;
                }
                j++;
            }
            if (j == pageRanks.size()) {
                break;
            }
            for (KVPair<URI, Double> pair : newPageRanks) {
                pageRanks.put(pair.getKey(), pair.getValue());
                newPageRanks.put(pair.getKey(), (1.0 - decay)/((double) graph.size())); // (1-d)/N
            }
            // Return early if we've converged.
        }
        return newPageRanks;
    }

    /**
     * Returns the page rank of the given URI.
     *
     * Precondition: the given uri must have been one of the uris within the list of
     *               webpages given to the constructor.
     */
    public double computePageRank(URI pageUri) {
        // Implementation note: this method should be very simple: just one line!
        return pageRanks.get(pageUri);
    }
}
