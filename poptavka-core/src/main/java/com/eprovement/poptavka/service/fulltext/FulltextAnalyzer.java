package com.eprovement.poptavka.service.fulltext;

import org.apache.lucene.analysis.ASCIIFoldingFilter;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.KeywordMarkerFilter;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.ReusableAnalyzerBase;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.WordlistLoader;
import org.apache.lucene.analysis.cz.CzechStemFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collections;
import java.util.Set;

/**
 * Wrapper for {@link CzechAnalyzer} that add {@link ASCIIFoldingFilter}
 * and set the used Lucene Version to 3.1.
 * <p>
 * This class will  replaced by CzechAnalyzer itself after upgrade of Hibernate Search to use Lucene versio
 * 3.1 or greater - filter {@link ASCIIFoldingFilter} can be specified by {@link org.hibernate.annotations.FilterDef}
 * annotation and enabled in {@link HibernateFulltextSearchService#search(Class, String[], String)} method.
 * For more details see Hibernate Search documentaiton - chapter 5.3 Filters>
 * <a href="http://docs.jboss.org/hibernate/stable/search/reference/en-US/html_single/#query-filter">Filters</a>.
 *
 * <p>
 *     Implemenation of this class is almost completely copied from {@link org.apache.lucene.analysis.cz.CzechAnalyzer}
 * because no simple way how to add required {@link ASCIIFoldingFilter} has not been found out.
 *
 * @author Juraj Martinka
 *         Date: 21.5.11
 */
public final class FulltextAnalyzer extends ReusableAnalyzerBase {


    /**
     * Contains the stopwords used with the {@link org.apache.lucene.analysis.StopFilter}.
     */
    // TODO once loadStopWords is gone those member should be removed too in favor of StopwordAnalyzerBase
    private Set<?> stoptable;
    private final Version matchVersion;
    private final Set<?> stemExclusionTable;


    public FulltextAnalyzer() {
        this.matchVersion = Version.LUCENE_31;
        this.stoptable = CharArraySet.unmodifiableSet(CharArraySet.copy(matchVersion, Collections.EMPTY_SET));
        this.stemExclusionTable = CharArraySet.unmodifiableSet(CharArraySet.copy(matchVersion, Collections.EMPTY_SET));

    }


    /**
     * Loads stopwords hash from resource stream (file, database...).
     * @param   wordfile    File containing the wordlist
     * @param   encoding    Encoding used (win-1250, iso-8859-2, ...), null for default system encoding
     * @deprecated use {@link WordlistLoader#getWordSet(Reader, String) }
     *             and {@link #CzechAnalyzer(Version, Set)} instead
     */
    // TODO extend StopwordAnalyzerBase once this method is gone!
    @Deprecated
    public void loadStopWords(InputStream wordfile, String encoding) {
        setPreviousTokenStream(null); // force a new stopfilter to be created
        if (wordfile == null) {
            stoptable = Collections.emptySet();
            return;
        }
        try {
            // clear any previous table (if present)
            stoptable = Collections.emptySet();

            InputStreamReader isr;
            if (encoding == null) {
                isr = new InputStreamReader(wordfile);
            } else {
                isr = new InputStreamReader(wordfile, encoding);
            }

            stoptable = WordlistLoader.getWordSet(isr);
        } catch (IOException e) {
            // clear any previous table (if present)
            // TODO: throw IOException
            stoptable = Collections.emptySet();
        }
    }

    /**
     * Creates
     * {@link org.apache.lucene.analysis.ReusableAnalyzerBase.TokenStreamComponents}
     * used to tokenize all the text in the provided {@link Reader}.
     *
     * @return {@link org.apache.lucene.analysis.ReusableAnalyzerBase.TokenStreamComponents}
     *         built from a {@link org.apache.lucene.analysis.standard.StandardTokenizer} filtered with
     *         {@link org.apache.lucene.analysis.standard.StandardFilter},
     *         {@link org.apache.lucene.analysis.LowerCaseFilter},
     *         {@link org.apache.lucene.analysis.ASCIIFoldingFilter},
     *         {@link StopFilter}
     *         , and {@link org.apache.lucene.analysis.cz.CzechStemFilter} (only if version is >= LUCENE_31). If
     *         a version is >= LUCENE_31 and a stem exclusion set is provided via
     *         {@link #CzechAnalyzer(Version, Set, Set)} a
     *         {@link org.apache.lucene.analysis.KeywordMarkerFilter} is added before
     *         {@link org.apache.lucene.analysis.cz.CzechStemFilter}.
     */
    @Override
    protected TokenStreamComponents createComponents(String fieldName,
                                                     Reader reader) {
        final Tokenizer source = new StandardTokenizer(matchVersion, reader);
        TokenStream result = new StandardFilter(matchVersion, source);
        // case insensitive
        result = new LowerCaseFilter(matchVersion, result);
        result = new StopFilter(matchVersion, result, stoptable);
        // accent insensitive
        result = new ASCIIFoldingFilter(result);
        if (matchVersion.onOrAfter(Version.LUCENE_31)) {
            if (!this.stemExclusionTable.isEmpty()) {
                result = new KeywordMarkerFilter(result, stemExclusionTable);
            }
            result = new CzechStemFilter(result);
        }

        return new TokenStreamComponents(source, result);
    }

}
