package Server.publications;

import java.util.Comparator;

/**
 * Comparador para ordenar publicações pelo ano de publicação.
 */
public class PublicationYearSorter implements Comparator<Publication> {
    @Override
    public int compare(Publication publication, Publication t1) {
        return t1.getPublishYear().compareTo(publication.getPublishYear());
    }
}
