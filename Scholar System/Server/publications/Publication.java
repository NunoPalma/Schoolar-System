package Server.publications;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Classe que representa uma publicação.
 */
public class Publication implements Serializable {
    private static final long serialVersionUID = 2L;

    private ArrayList<String> mAuthors;
    private String mTitle, mMagazine, mDOI;
    private Integer mVolume, mNumber, mStartingPage, mEndPage, mAmountOfQuotes, mPublishYear;


    public Publication(ArrayList<String> authors, Integer publishYear, String title, String magazine, Integer volume,
                       Integer number, Integer startingPage, Integer endPage, Integer amountOfQuotes, String DOI) {
        mAuthors = authors;
        mPublishYear = publishYear;
        mTitle = title;
        mMagazine = magazine;
        mVolume = volume;
        mNumber = number;
        mStartingPage = startingPage;
        mEndPage = endPage;
        mAmountOfQuotes = amountOfQuotes;
        mDOI = DOI;
    }

    public String getId() {
        return mDOI;
    }

    public Integer getPublishYear() {
        return mPublishYear;
    }

    public Integer getAmountOfCitations() {
        return mAmountOfQuotes;
    }

    public ArrayList<String> getAuthors() {
        return mAuthors;
    }

    public String getTitle() {
        return mTitle;
    }

    public Integer getAmountOfQuotes() {
        return mAmountOfQuotes;
    }

    @Override
    public String toString() {
        return "Publication{" +
                mAuthors + ", " +
                mTitle + ", " +
                mPublishYear + ", " +
                mMagazine + ", " +
                mVolume + ", " +
                mNumber + ", " +
                mStartingPage + "-" + mEndPage + ", " +
                mDOI +
                ", Times Cited=" + mAmountOfQuotes +
                '}';
    }
}
