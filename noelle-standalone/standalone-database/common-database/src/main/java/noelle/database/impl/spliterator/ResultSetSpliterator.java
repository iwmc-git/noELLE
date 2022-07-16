package noelle.database.impl.spliterator;

import noelle.database.functions.SqlFunction;
import noelle.database.utils.Wrap;

import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ResultSetSpliterator<T> extends Spliterators.AbstractSpliterator<T> {
    private final SqlFunction<ResultSet, T> mapping;
    private final ResultSet resultSet;

    public static <T> @NotNull Stream<T> stream(SqlFunction<ResultSet, T> mapping, ResultSet resultSet) {
        return StreamSupport.stream(new ResultSetSpliterator<>(mapping, resultSet), false).onClose(() -> Wrap.execute(resultSet::close));
    }

    private ResultSetSpliterator(SqlFunction<ResultSet, T> mapping, ResultSet resultSet) {
        super(Long.MAX_VALUE, Spliterator.ORDERED);

        this.resultSet = resultSet;
        this.mapping = mapping;
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> consumer) {
        return Wrap.get(() -> {
            var hasNext = false;

            if (hasNext == resultSet.next()) {
                consumer.accept(mapping.apply(resultSet));
            }

            return hasNext;
        });
    }
}
