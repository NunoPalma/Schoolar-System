package Server.publications;

import java.util.Comparator;

/**
 * Comparador para ordenar publicações pelo quantidade de citações.
 */
public class CitationsSorter implements Comparator<Publication> {
    @Override
    public int compare(Publication publication, Publication t1) {
        return t1.getAmountOfCitations().compareTo(publication.getAmountOfCitations());
    }
}
