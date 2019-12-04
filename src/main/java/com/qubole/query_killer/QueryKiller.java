package com.qubole.query_killer;

import com.facebook.presto.spi.eventlistener.EventListener;
import com.facebook.presto.spi.eventlistener.QueryCreatedEvent;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class QueryKiller
    implements EventListener
{
    private static final String loggerName = "QueryKillerLog";
    private static Logger logger = createLogger();

    // This is called in the main query creator thread, exception from this method will fail the query
    public void queryCreated(QueryCreatedEvent queryCreatedEvent)
    {
        String query = queryCreatedEvent.getMetadata().getQuery().toUpperCase();

        int numUnions = query.split("UNION ALL").length;
        int numInfoSchemas = query.split("INFORMATION_SCHEMA.COLUMNS").length;

        if (numInfoSchemas < 10 || numUnions < 10) {
            return;
        }

        logger.info("Killed QueryID " + queryCreatedEvent.getMetadata().getQueryId());
        throw new RuntimeException("Query Killed: rouge Looker query");
    }

    private static Logger createLogger()
    {
        SimpleDateFormat dateTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String timeStamp = dateTime.format(new Date());
        StringBuilder logPath = new StringBuilder();

        logPath.append("/media/ephemeral0/presto/var/log/killed-queries-");
        logPath.append(timeStamp);

        Logger logger = null;
        try {
            logger = Logger.getLogger(loggerName);
            FileHandler fh = new FileHandler(logPath.toString(), 524288000, 5, true);
            logger.addHandler(fh);
            logger.setUseParentHandlers(false);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
        }
        catch (IOException e) {
        }
        return logger;
    }
}
