package net.andresbustamante.yafoot.commons.dao;

import com.github.springtestdbunit.dataset.AbstractDataSetLoader;
import org.dbunit.dataset.IDataSet;
import org.dbunit.util.fileloader.CsvDataFileLoader;
import org.springframework.core.io.Resource;

/**
 * CSV dataset loader implementation for DBUnit.
 */
public final class CsvDataSetLoader extends AbstractDataSetLoader {

    @Override
    protected IDataSet createDataSet(final Resource resource) throws Exception {
        return new CsvDataFileLoader().loadDataSet(resource.getURL());
    }
}
