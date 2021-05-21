package jcip.ch6.future;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

class QuoteTask implements Callable<QuoteTask.TravelQuote> {

    private final TravelCompany company;
    private final TravelInfo travelInfo;

    QuoteTask(TravelCompany company, TravelInfo travelInfo) {
        this.company = company;
        this.travelInfo = travelInfo;
    }

    @Override public TravelQuote call() throws Exception {
        return company.solicitQuote(travelInfo);
    }

    static class TravelCompany {
        public TravelQuote solicitQuote(TravelInfo info) {
            return new TravelQuote();
        }
    }

    static class TravelInfo {}

    static class TravelQuote {}

    static ExecutorService exec = Executors.newFixedThreadPool(43);

    public List<TravelQuote> getRankedTravelQuotes(
        TravelInfo travelInfo, Set<TravelCompany> companies,
        Comparator<TravelQuote> ranking, long time, TimeUnit unit) throws InterruptedException {

        List<QuoteTask> tasks = new ArrayList<>();
        for (TravelCompany company : companies)
            tasks.add(new QuoteTask(company, travelInfo));

        List<Future<TravelQuote>> futures = exec.invokeAll(tasks, time, unit);

        List<TravelQuote> quotes = new ArrayList<>(tasks.size());
        Iterator<QuoteTask> taskIterator = tasks.iterator();

        for (Future<TravelQuote> f : futures) {
            QuoteTask task = taskIterator.next();
            try {
                quotes.add(f.get());
            } catch (ExecutionException e) {
                quotes.add(task.getFailureQuote(e.getCause()));
            } catch (CancellationException e) {
                quotes.add(task.getTimeoutQuote(e));
            }
        }
        quotes.sort(ranking);
        return quotes;
    }

    private TravelQuote getTimeoutQuote(CancellationException e) {
        return null;
    }

    private TravelQuote getFailureQuote(Throwable cause) {
        return null;
    }
}
