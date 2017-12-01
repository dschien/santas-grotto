package ac.uk.bristol.cs.testutil;

import com.github.springtestdbunit.dataset.DataSetLoader;
import com.github.springtestdbunit.dataset.FlatXmlDataSetLoader;
import com.github.springtestdbunit.dataset.ReplacementDataSetLoader;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.Map;

/**
 * Created by csxds on 27/11/2017.
 */
public class OffsetDateTimeReplacementDataSetLoader extends ReplacementDataSetLoader {

    public static final Map<String, Object> DEFAULT_OBJECT_REPLACEMENTS = Collections.singletonMap("[offsetdatetime]", OffsetDateTime.parse("2017-12-03T10:15:30+01:00"));

    public OffsetDateTimeReplacementDataSetLoader() {
        this(new FlatXmlDataSetLoader());
    }

    public OffsetDateTimeReplacementDataSetLoader(DataSetLoader dataSetLoader) {
        super(dataSetLoader, DEFAULT_OBJECT_REPLACEMENTS);
    }

//    private ReplacementDataSet createReplacementDataSet(FlatXmlDataSet dataSet) {
//        ReplacementDataSet replacementDataSet = new ReplacementDataSet(dataSet);
//
//        replacementDataSet.addReplacementObject("[offsetdatetime]", OffsetDateTime.parse("2017-12-03T10:15:30+01:00"));
//
//        return replacementDataSet;
//    }
}
