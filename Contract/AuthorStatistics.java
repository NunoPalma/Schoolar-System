package Contract;

import java.io.Serializable;

/**
 * Classe que representa as estatisticas de um dado autor.
 */
public class AuthorStatistics implements Serializable {
    private final Integer TOTAL_CITATIONS;
    private final Integer INDEX_H;
    private final Integer INDEX_I;

    public AuthorStatistics(Integer totalCitations, Integer h, Integer i) {
        TOTAL_CITATIONS = totalCitations;
        INDEX_H = h;
        INDEX_I = i;
    }

    @Override
    public String toString() {
        return "Total Citations=" + TOTAL_CITATIONS + "\n" +
                "H=" + INDEX_H + "\n" +
                "i10=" + INDEX_I + "\n";
    }
}
