package flat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemStreamWriter;

public final class ItemStreamHelper {

    private ItemStreamHelper() {
    }

    // tag::read[]
    public static <T> List<T> read(ItemStreamReader<T> reader)
            throws IOException {
        List<T> items = new ArrayList<>();
        try {
            T item;
            reader.open(new ExecutionContext()); // provide dummy ExecutionContext
            while ((item = reader.read()) != null) { // null signals end of stream
                items.add(item);
            }
        } catch (Exception ex) { // interface may throw any exception
            throw new IOException("Unable to read form item stream.", ex);
        } finally {
            reader.close(); // can't use try-with-resource here
        }
        return items;
    }
    // end::read[]

    // tag::write[]
    public static <T> void write(ItemStreamWriter<T> writer,
            List<? extends T> items) throws IOException {
        try {
            writer.open(new ExecutionContext()); // provide dummy ExecutionContext
            writer.write(items);
        } catch (Exception ex) {
            throw new IOException("Unable to write to item stream.", ex);
        } finally {
            writer.close(); // can't use try-with-resource here
        }
    }
    // end::write[]
}
